// owner가 가지고 있는 가게에 대한 메뉴 등록/삭제 하는 핸들러
let express = require('express');
let router = express.Router();
let sql = require('../../mysql/db_sql')();

/* GET menu information */
router.get('/', function(req, res, next){
	console.log("---log start(OWN MENU:GET)---");
 	console.log("OWN MENU:GET -> shop_id : ", req.query.id);
	sql.getMenuDetail(req.query.id, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/ownMenu?id={shop_id}

/* POST add new Menu. / delete new Menu */
router.post('/del/', function(req, res, next){
	console.log("---log start(OWN MENU:POST)---");
	console.log("DELETE:POST -> shop_id : ", req.query.id);
	console.log("DELETE:POST -> name : ", req.query.name);

	sql.deleteShopMenu(req.query.id, req.query.name, function(err){
		if(err){
			console.error("DELETE MENU:GET FAILED: ", err);
			res.send([{"result":"ERROR"}]);
		}
		else{
			res.send([{"result":"OK"}]);
		}
	});
	
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/ownMenu/del?id={shop_id}&name={menu_name}

router.post('/add/', function(req, res, next){
	console.log("---log start(OWN MENU:POST)---");
 	console.log("ADD:POST -> shop_id : ", req.query.id);
	console.log("ADD:POST -> name : ", req.query.name);
	console.log("ADD:POST -> price : ", req.query.price);
	console.log("ADD:POST -> description : ", req.query.des);
	console.log("ADD:POST -> count : ", req.query.count);

	sql.addShopMenu(req.query.id, req.query.name, req.query.price, req.query.des, req.query.count, function(err){
		if(err){
			console.error("ADD MENU:GET FAILED: ",err);
			res.send([{"result":"ERROR"}]);
		}
		else{
		res.send([{"result":"OK"}]);
		}
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/ownMenu/add?id={shop_id}&name={menu_name}&price={price}&des={description}&count={number of menu}

module.exports = router;
