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
}); // http://oreh.onyah.net:7777/ownShop?id={id}

module.exports = router;
