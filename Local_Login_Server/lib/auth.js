module.exports = {
    isOwner: function(req, res){
        if(req.session.is_logined){
          return true;
        }else{
          return false;
        }
    },

    status: function(req, res){
        var authStatusUI = 'none';
        if(this.isOwner(req,res)){
          authStatusUI = req.session.nickName;
        }
      
        return authStatusUI
      }
}