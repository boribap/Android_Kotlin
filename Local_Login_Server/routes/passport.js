const passport = require('passport');
const LocalStrategy = require('passport-local').Strategy;
const Users = require('./user');

module.exports = () => {
    
  // Strategy 성공 시 호출됨
  passport.serializeUser((user, done) => { 
    // 여기의 user가 deserializeUser의 첫 번째 매개변수로 이동
    done(null, user); 
  });
  
  // 매개변수 user는 serializeUser의 done의 인자 user를 받은 것
  passport.deserializeUser((user, done) => { 
    // 여기의 user가 req.user가 됨
    done(null, user); 
  });

  // local 전략을 세움
  passport.use(new LocalStrategy({ 
    usernameField: 'id',
    passwordField: 'pw',
    session: true, // 세션에 저장 여부
    passReqToCallback: false,
 }, (id, password, done) => {
    Users.findOne({ id: id }, (findError, user) => {
      // 서버 에러 처리
      if (findError) {
        return done(findError);
      } 

      // id 검사
      if (!user) {
        return done(null, false, { message: '존재하지 않는 아이디입니다' }); // 임의 에러 처리
      }
      
      // pw 검사
      return user.comparePassword(password, (passError, isMatch) => {
        // 검증 성공
        if (isMatch) {
            return done(null, user); 
        }
        return done(null, false, { message: '비밀번호가 틀렸습니다' }); // 임의 에러 처리
      });
    });
  }));
};