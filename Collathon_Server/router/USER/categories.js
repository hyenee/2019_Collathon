// 카테고리 별로 가게 정보 리스트 출력하는 핸들
let express = require('express');
let router = express.Router();
let sql = require('../../mysql/db_sql')();

/* GET shop list by categories */
router.get('/', function(req, res, next){
	console.log("---log start(CATEGORY:GET)---");
 	console.log("CATEGORY:GET -> category : ", req.query.category);
	sql.getCategoryShop(req.query.category,  function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/categories?category={category}

/* GET a shop info */
router.get('/shop/', function(req, res, next){
	console.log("---log start(SHOP:GET)---");
	console.log("DETAIL SHOP:GET -> shop id : ", req.query.id);
	sql.getShopDetail(req.query.id, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/categories/shop?id={shop_id}

/* GET a menu info about a certain shop */
router.get('/menu/', function(req, res, next){
	console.log("---log start(SHOP:GET)---");
	console.log("DETAIL MENU:GET -> shop id : ", req.query.id);
	sql.getMenuDetail(req.query.id, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/categories/menu?id={shop_id}

module.exports = router;
