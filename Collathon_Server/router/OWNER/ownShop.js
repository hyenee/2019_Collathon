// owner가 가지고 있는 가게 정보 전달하는 핸들러
let express = require('express');
let router = express.Router();
let sql = require('../../mysql/db_sql')();

/* GET home page. */
router.get('/', function(req, res, next){
	console.log("---log start(OWN SHOP:GET)---");
 	console.log("OWN SHOP:GET -> id : ", req.query.id);
	sql.getOwnerShop(req.query.id, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/ownShop?id={owner_id}

/* POST new shop. */
router.post('/add/', function(req, res, next){
	console.log('---log start(OWN SHOP:POST)---');
	console.log("OWN SHOP:POST -> owner id : ", req.query.id);
	console.log("OWN SHOP:POST -> shop name : ", req.query.name);
	console.log("OWN SHOP:POST -> tel : ", req.query.tel);
	console.log("OWN SHOP:POST -> address : ", req.query.addr);
	console.log("OWN SHOP:POST -> category : ", req.query.category);
	console.log("OWN SHOP:POST -> table : ", req.query.table);
	sql.addOwnerShop(req.query.id, req.query.name, req.query.tel, req.query.addr, req.query.category, req.query.table, function(err){
		if(err){
			console.error("ADD SHOP:GET FAILED: ", err);
			res.send([{"result":"ERROR"}]);
		}
		else{
			res.send([{"result":"OK"}]);
		}
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/ownShop/add?id={owner_id}&name={shop_name}&tel={tel}&addr={address}&category={category}&table={Y/N}

/* POST new shopTable. */
router.post('/add/table/', function(req, res, next){
	console.log('---log start(OWN SHOP:POST)---');
	console.log("OWN SHOP:POST -> number : ", req.query.number);
	console.log("OWN SHOP:POST -> count : ", req.query.count);
	sql.addOwnerShopTable(req.query.number, req.query.count, function(err){
		if(err){
			console.error("ADD SHOPTABLE:GET FAILED: ", err);
			res.send([{"result":"ERROR"}]);
		}
		else{
			res.send([{"result":"OK"}]);
		}
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/ownShop/add/table?number={table_number}&count={table_count}

/* POST delete shop. */
router.post('/delete/', function(req, res, next){
	console.log('---log start(OWN SHOP:POST)---');
	console.log("OWN SHOP:POST -> shop id : ", req.query.shop);
	sql.deleteOwnerShop(req.query.shop, function(err){
		if(err){
			console.error("DELETE SHOP:GET FAILED: ", err);
			res.send([{"result":"ERROR"}]);
		}
		else{
			res.send([{"result":"OK"}]);
		}
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/ownShop/delete?shop={shop_id}

module.exports = router;
