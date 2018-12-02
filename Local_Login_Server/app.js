var createError = require('http-errors');
var express = require('express');
var session = require('express-session');
var mySQLStore = require('express-mysql-session')(session)
var bodyParser = require('body-parser');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var passport = require('passport')

// router 연결
var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
var authRouther = require('./routes/auth')

// mysql DB와 연동
var mysql = require('mysql');
var db_config = require('./config/db_config.json');

var conn = mysql.createConnection({
  host      : db_config.host,
  user      : db_config.user,
  password  : db_config.password,
  database  : db_config.database
})

conn.connect()
var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(bodyParser.urlencoded({ extended: false}));

app.use(
  session({
    secret: db_config.session_key,
    resave: false,
    saveUninitialized: true,
    store: new mySQLStore({
      host: db_config.host,
      port: db_config.port,
      user: db_config.user,
      password: db_config.password,
      database: db_config.database
    })
  })
)
app.use(passport.initialize());
app.use(passport.session());


app.use(express.static(path.join(__dirname, 'public')));

// Router
app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/auth', authRouther);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// // error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});


// 포트 연결
app.listen(7260, function () {
  console.log("Connected 7260 port")
});

module.exports = app;