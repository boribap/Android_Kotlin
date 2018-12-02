var express = require('express');
var router = express.Router();
var jwt = require('jsonwebtoken');
var secretObj = require("../config/jwt");

router.get('/login', function(req, res, next){
    let token = jwt.sign({
        email : "bsww201@naver.com"
    },
    secretObj.secert,
    {
        expiresIn: '5m'
    })
});

