var express = require('express');
var passport = require('passport');
var LocalStrategy = require('passport-local').Strategy;
var router = express.Router();

var authData = require('../config/auth_data.json')


router.get('/login', function(req, res){
    res.render('loginform', { title: 'login' })
});

router.post('/login_process', function(req, res, next){
    var userInfo = req.body;
    var id = userInfo.userID;
    var pw = userInfo.userPW;

    if(id === authData.userID && pw === authData.userPW){
        // success
        req.session.is_logined = true;
        req.session.nickName = authData.nickName;
        res.redirect(`/`)
    }
    res.send('who?')
})

router.get('/logout', function(req, res){
    req.session.destroy(function(err){
        res.redirect(`/`)
    })
})

module.exports = router;