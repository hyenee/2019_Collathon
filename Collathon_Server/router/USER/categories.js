// 카테고리 별로 가게 정보 리스트 출력하는 핸들
let express = require('express');
let router = express.Router();
let sql = require('../../mysql/db_sql')();

/* GET home page. */
router.get('/', function(req, res, next){
	console.log("---log start(CATEGORY:GET)---");
 	console.log("CATEGORY:GET -> category : ", req.query.category);
	sql.getCategoryShop(req.query.category,  function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7777/categories?category={category}

module.exports = router;
