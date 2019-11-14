// 유저가 즐겨찾기처럼 찜한 가게를 출력/추가/삭제해주는 핸들러
let express = require('express');
let router = express.Router();
let sql = require('../../mysql/db_sql')();

/* GET likeShop */
router.get('/', function(req, res, next){
	console.log("---log start(LIKESHOP:GET)---");
 	console.log("LIKESHOP:GET -> client id : ", req.query.id);
	sql.getLikeShop(req.query.id, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/like?id={client_id}

/* POST new Likeshop */
router.post('/add/', function(req, res, next){
	console.log("---log start(LIKESHOP:POST)---");
	console.log("LIKESHOP:POST -> client id : ", req.query.user);
	console.log("LIKESHOP:POST -> shop id : ", req.query.shop);
	sql.addLikeShop(req.query.shop, req.query.user, function(err){
		if(err){
			console.error("ADD LIKESHOP:GET FAILED: ",err);
			res.send([{"result":"ERROR"}]);
		}
		else{
			res.send([{"result":"OK"}]);
		}
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/like/add?shop={shop_id}&user={client_id}

/* POST delete likeShop */
router.post('/delete/', function(req, res, next){
	console.log("---log start(LIKESHOP:POST)---");
	console.log("LIKESHOP:POST -> client id : ", req.query.user);
	console.log("LIKESHOP:POST -> shop id: ", req.query.shop);
	sql.deleteLikeShop(req.query.shop, req.query.user, function(err){
		if(err){
			console.error("DELETE LIKESHOP:GET FAILED: ", err);
			res.send([{"result":"ERROR"}]);
		}
		else{
			res.send([{"result":"OK"}]);
		}
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/like/delete?shop={shop_id}&user={client_id}

module.exports = router;
