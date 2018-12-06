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


  let JSONsendDupl = new Array()
  let JSONsendComp = new Array()

  let user_name = req.body.user_name
  let user_email = req.body.user_email
  let user_password = req.body.user_password

  console.log(user_name+ " " + user_email + " " + user_password)

  // DB에 회원 정보를 삽입하는 부분 (이메일이 중복되면 저장 X)
  let store_sql = "insert into vasy.user_info (user_name, user_email, user_password) select ?,?,? from dual where not exists (select user_email from vasy.user_info where user_email=?);"
  
  conn.query(store_sql, [user_name, user_email, user_password, user_email], function(err, results){
    if (err){
      console.log(err)
    }else{
      console.log("삽입완료 : " + user_name+ " " + user_email + " " + user_password)
      // results.affectedRows == 1 이면 새로운 회원의 정보가 DB에 저장된 것
      console.log(results.affectedRows)
     
      if (results.affectedRows == 1){
        // 이메일 중복되지 않아 DB에 저장이 된 것
        // 'SIGNIP_COMPLETE' 를 안드로이드에 전송 --> 로그인 페이지로 넘어가기

        JSONsendComp.push({ "status" : 'SIGNUP_COMPLETE' })

        console.log(JSONsendComp)
        res.send(JSONsendComp)
      } else {
        // 이메일이 중복되어서 DB에 저장이 되지 않은 것 
        // 'DUPL_EMAIL'을 안드로이드에 전송 --> 이메일 텍스트 부분 비워주기

        JSONsendDupl.push({"status" : 'DUPL_EMAIL'})

        console.log(JSONsendDupl)
        res.send(JSONsendDupl)
      }

    }
  })
});

router.post("/sign_in", function(req, res, next){
  console.log("@" + req.method + " " + req.url)

  let access_token
  let refresh_token
  let tokens = new Array()
  let LOGIN_PERMIT
  let LOGIN_FAIL_EMAIL = new Array()
  let LOGIN_FAIL_PASSWORD = new Array()

  // 안드로이드에서 보낸 이메일과 비밀번호 받기 (req.body)
  let user_email = req.body.user_email
  let user_password = req.body.user_password

  console.log(user_email + " " + user_password)

  // DB에서 일치여부 확인 : 이메일 먼저 확인해봄 --> 이메일이 일치하면 비밀번호 일치하는지 확인해봄
  let sql_email_match = "select user_password from vasy.user_info where user_email=?"
  let sql_store_refresh = ""

  conn.query(sql_email_match, [user_email], function(err, rows){
    if ( err ) {
      console.log(err)
    }
    else {
      if(rows == ""){
        // 이메일이 없으므로 회원 아님 --> 이메일 & 비번 위치 빈칸으로 만들기
        console.log("회원이 아닙니다. 회원가입 해주세요.")

        LOGIN_FAIL_EMAIL.push({ "status" : 'LOGIN_FAIL_EMAIL' })

        res.send(LOGIN_FAIL_EMAIL)
      }
      else {
        // 이메일 & 비번 모두 일치
        console.log(rows[0].user_password)
        if(rows[0].user_password == user_password){

          // DB에 일치하는 회원이 있으면 안드로이드에게 토큰을 발급해줌
          console.log("이메일 일치 & 비밀번호 일치")

          access_token = jwt.sign({ user_email }, secretObj.secret, { expiresIn: '5m' })
          refresh_token = jwt.sign({ user_email },  secretObj.secret, { expiresIn: '90d' })

          tokens.push({ "status" : 'LOGIN_PERMIT' })
          tokens.push({ "access_token" : access_token})
          tokens.push({ "refresh_token" : refresh_token})

          // refresh token 저장하는 것 추가하기 

          LOGIN_PERMIT = tokens

          res.send(LOGIN_PERMIT)
        }
        else {

          // 비밀번호 불일치 --> 비번 위치 빈칸으로 만들기
          console.log("비밀 번호 불일치")

          LOGIN_FAIL_PASSWORD.push({ "status" : 'LOGIN_FAIL_PASSWORD'})

          res.send(LOGIN_FAIL_PASSWORD)
        }
      }
    }
  })
})

// router.get('/calender', function(req, res, next){
//   console.log(req.headers.authorization)
// })

module.exports = router;
