// 타임세일하는 메뉴를 추가/삭제하는 기능을 하는 핸들러
let express = require('express');
let router = express.Router();
let sql = require('../../mysql/db_sql')();

/* Get Shop Stock Menu*/
router.get('/stock/', function(req, res, next){
	console.log("---log start(TIMESALE:GETSTOCK)---");
	console.log("TIMESALE:GETSTOCK -> shop id : ", req.query.shop);
	sql.getStockMenu(req.query.shop, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); //http://oreh.onyah.net:7080/timesale/stock?shop={shop_id}

/* GET timesale history */
router.get('/', function(req, res, next){
	console.log("---log start(TIMESALE:GET)---");
 	console.log("TIMESALE:GET -> shop id : ", req.query.shop);
	sql.getTimeSale(req.query.shop, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/timesale?shop={shop_id}

/* POST new timesale information */
router.post('/add/', function(req, res, next){
	console.log("---log start(TIMESALE:POST)---");
	console.log("NEW TIMESALE:POST -> shop id : ", req.query.shop);
	console.log("NEW TIMESALE:POST -> menu name : ", req.query.menu);
	console.log("NEW TIMESALE:POST -> sale price : ", req.query.price);
	console.log("NEW TIMESALE:POST -> sale time : ", req.query.saletime);
	sql.addTimeSale(req.query.shop, req.query.menu, req.query.price, req.query.saletime, function(err){
		if(err){
			console.error("ADD TIMESALE:GET FAILED: ",err);
			res.send([{"result":"ERROR"}]);
		}
		else{
			res.send([{"result":"OK"}]);
		}
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/timesale/add?shop={shop_id}&menu={menu_name}&price={menu_sale_price}&saletime={menu_saletime}

/* POST delete timesale information */
router.post('/delete/', function(req, res, next){
	console.log("---log start(TIMESALE:POST)---");
	console.log("DELETE TIMESALE:POST -> timesale registeration id : ", req.query.register);
	sql.deleteTimeSale(req.query.register, function(err){
		if(err){
			console.error("DELETE TIMESALE:GET FAILED: ",err);
			res.send([{"result":"ERROR"}]);
		}
		else{
			res.send([{"result":"OK"}]);
		}
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/timesale/delete?register={timesale_id}

module.exports = router;
