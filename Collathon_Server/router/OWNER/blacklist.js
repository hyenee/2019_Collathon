// 블랙리스트 출력/추가하는 기능을 하는 핸들러
let express = require('express');
let router = express.Router();
let sql = require('../../mysql/db_sql')();

/* GET blacklist user */
router.get('/all/', function(req, res, next){
	console.log("---log start(BLACKLIST:GET)---");
	sql.getBlackList(function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/blacklist/all

/* POST new blacklist user */
router.post('/add/', function(req, res, next){
	console.log("---log start(BLACKLIST:POST)---");
	console.log("BLACKLIST:POST -> user id : ", req.query.id);
	console.log("LOGIN:POST -> shop id : ", req.query.shop);
	console.log("LOGIN:POST -> comment : ", req.query.comment);
	sql.addBlackList(req.query.id, req.query.shop, req.query.comment, function(err){
		if(err){
			console.error("PUT BLACKLIST:GET FAILED: ",err);
			res.send([{"result":"ERROR"}]);
		}
		else{
			res.send([{"result":"OK"}]);
		}
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/blacklist/add?id={client_id}&shop={shop_id}&comment={comment}
module.exports = router;
