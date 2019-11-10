// 회원정보 확인 및 수정 핸들러
let express = require('express');
let router = express.Router();
let sql = require('../../mysql/db_sql')();

/* GET user's information */
router.get('/user/', function(req, res, next){
	console.log("---log start(MYPAGE:GET)---");
 	console.log("USER:GET -> id : ", req.query.id);
	sql.getClientUserDetail(req.query.id, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/mypage/user?id={id}

/* GET owner's information */
router.get('/owner/', function(req, res, next){
	console.log("---log start(MYPAGE:GET)---");
	console.log("OWNER:GET -> id : ", req.query.id);
	sql.getOwnerUserDetail(req.query.id, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/mypage/owner?id={id}

/* POST update user's new passwd */
router.post('/user/', function(req, res, next){
	console.log("---log start(MYPAGE:POST)---");
	console.log("USER:POST -> id : ", req.query.id);
	console.log("USER:POST -> new passwd : ", req.query.new);
	sql.updateClientUser(req.query.id, req.query.new, function(err){
		if(err){
			console.error("UPDATE:GET FAILED: ",err);
			res.send([{"result":"ERROR"}]);
		}
		else{
			res.send([{"result":"OK"}]);
		}
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/mypage/user?id={id}&new={new_passwd}

/* POST update owner's new passwd */
router.post('/owner/', function(req, res, next){
	console.log("---log start(MYPAGE:POST)---");
	console.log("OWNER:POST -> id : ", req.query.id);
	console.log("OWNER:POST -> new passwd : ", req.query.new);
	sql.updateOwnerUser(req.query.id, req.query.new, function(err){
		if(err){
			console.error("UPDATE:GET FAILED: ",err);
			res.send([{"result":"ERROR"}]);
		}
		else{
			res.send([{"result":"OK"}]);
		}
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/mypage/owner?id={id}&new={new_passwd}

module.exports = router;
