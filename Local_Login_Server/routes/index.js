var express = require('express');
var router = express.Router();

// passport 할 때 사용했던 것
var auth = require('../lib/auth');

// mysql DB와 연동
var mysql = require('mysql');
var db_config = require('../config/db_config.json');

var conn = mysql.createConnection({
  host      : db_config.host,
  user      : db_config.user,
  password  : db_config.password,
  database  : db_config.database
})

conn.connect()

// jwt사용을 위해 사용하는 것 
var jwt = require('jsonwebtoken');
var secretObj = require("../config/jwt");


/* GET home page. */
router.get('/', function(req, res, next) {
  // 현재 누가 로그인되어 있는지 띄워주기
  authStatusUI = auth.status(req,res);
  res.render('index', { title: authStatusUI + ' Express' });
});


/* -------------------------------------------------------

Table : user_info

user_id         : auto increasing data <PK>
user_name       : 사용자가 등록한 이름
user_email      : 사용자가 등록한 이메일
user_password   : 사용자가 등록한 비밀번호 

---------------------------------------------------------*/


router.post('/sign_up', function(req, res, next){
  console.log("@" + req.method + " " + req.url)

  console.log(req.body)


  let JSONsendNo = {"token" : 'NO'}

  let user_name = req.body.user_name
  let user_email = req.body.user_email
  let user_password = req.body.user_password

  console.log(user_name+ " " + user_email + " " + user_password)

  // DB에 회원 정보를 삽입하는 부분 
  let store_sql = "insert into vasy.user_info (user_name, user_email, user_password) select ?,?,? from dual where not exists (select user_email from vasy.user_info where user_email=?);"
  //let store_sql = "INSERT INTO user_info (user_name, user_email, user_password) VALUES (?,?,?);"

  conn.query(store_sql, [user_name, user_email, user_password, user_email], function(err, results){
    if (err){
      console.log(err)
    }else{
      console.log("삽입완료 : " + user_name+ " " + user_email + " " + user_password)
      console.log(results.affectedRows)
      // results.affectedRows == 1 이면 새로운 회원의 정보가 DB에 저장된 것 --> 토큰 전송
      if (results.affectedRows == 1){
        // 토큰을 생성하여 안드로이드로 전송하는 부분

        
        let token = jwt.sign({
          user_email
        },
        secretObj.secret,
        {
          expiresIn: '5m'
        })

        let JSONsendToken = { "token" : token }
        console.log(JSONsendToken)

        res.send(JSONsendToken)
      } else {

        console.log(JSONsendNo)
        res.send(JSONsendNo)
      }

    }
  })
});

module.exports = router;
