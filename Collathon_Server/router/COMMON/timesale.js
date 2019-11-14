// 타임세일하는 메뉴를 추가/삭제하는 기능을 하는 핸들러
let express = require('express');
let router = express.Router();
let sql = require('../../mysql/db_sql')();

/* GET timesale menu */
router.get('/', function(req, res, next){
	console.log("---log start(TIMESALE:GET)---");
 	console.log("TIMESALE:GET -> shop id : ", req.query.shop);	
	sql.getTimeSale(req.query.shop, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/timesale?shop={shop_id}

module.exports = router;
