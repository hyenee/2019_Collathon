// owner가 가지고 있는 가게에 대한 메뉴 등록/삭제 하는 핸들러
let express = require('express');
let router = express.Router();
let sql = require('../../mysql/db_sql')();

/* POST add new Menu. / delete new Menu */
router.post('/', function(req, res, next){
	console.log("---log start(OWN MENU:POST)---");
	console.log("OWN MENU:POST -> delete : ", req.query.del);
	if(req.query.del === "Y"){
		console.log("DELETE:POST -> shop_id : ", req.query.id);
		console.log("DELETE:POST -> name : ", req.query.name);

		sql.deleteShopMenu(req.query.id, req.query.name, function(err){
			if(err){
				console.error("DELETE MENU:GET FAILED: ", err);
			}
			else{
				res.send([{"result":"OK"}]);
			}
		});
	} // http://oreh.onyah.net:7777/ownMenu?del=Y&id={shop_id}&name={menu_name}
	else if(req.query.del === "N"){
 		console.log("ADD:POST -> shop_id : ", req.query.id);
		console.log("ADD:POST -> name : ", req.query.name);
		console.log("ADD:POST -> price : ", req.query.price);
		console.log("ADD:POST -> description : ", req.query.des);
		console.log("ADD:POST -> count : ", req.query.count);

		sql.addShopMenu(req.query.id, req.query.name, req.query.price, req.query.des, req.query.count, function(err){
		if(err){
			console.error("ADD MENU:GET FAILED: ",err);
		}
		else{
		res.send([{"result":"OK"}]);
		}
	});
	} // http://oreh.onyah.net:7777/ownMenu?del=N&id={shop_id}&name={menu_name}&price={price}&des={description}&count={number of menu}
	
	console.log("---log end---");
});

module.exports = router;