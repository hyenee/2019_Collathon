// login와 회원가입 핸들러
let express = require('express');
let router = express.Router();
let sql = require('../../mysql/db_sql')();

/* GET home page. */
router.get('/', function(req, res, next){
	console.log("---log start(LOGIN:GET)---");
 	console.log("LOGIN:GET -> id : ", req.query.id);
	console.log("LOGIN:GET -> passwd : ", req.query.passwd);
	sql.getClientUser(req.query.id, req.query.passwd, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7777/login?id={id}&passwd={passwd}

/* POST new User */
router.post('/', function(req, res, next){
	console.log("---log start(LOGIN:POST)---");
	console.log("LOGIN:POST -> id : ", req.query.id);
	console.log("LOGIN:POST -> passwd : ", req.query.passwd);
	console.log("LOGIN:POST -> name : ", req.query.name);
	console.log("LOGIN:POST -> phone : ", req.query.phone);
	console.log("LOGIN:POST -> email : ", req.query.email);
	sql.addClientUser(req.query.name, req.query.id, req.query.passwd, req.query.phone, req.query.email, function(err){
		if(err){
			console.error("SIGN UP:GET FAILD: ",err);
		}
		else{
			res.send([{"result":"OK"}]);
		}
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7777/login?id={id}&passwd={passwd}&name={name}&phone={phone}&email={email}

module.exports = router;
