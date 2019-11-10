// 블랙리스트 출력/추가하는 기능을 하는 핸들러
let express = require('express');
let router = express.Router();
let sql = require('../../mysql/db_sql')();

/* GET blacklist user */
router.get('/all', function(req, res, next){
	console.log("---log start(BLACKLIST:GET)---");
	sql.getBlackList(function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/blacklist/all

module.exports = router;
