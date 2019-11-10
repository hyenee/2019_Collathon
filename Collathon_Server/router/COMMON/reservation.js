// 예약 정보 확인/추가/삭제 기능을 하는 핸들러
let express = require('express');
let router = express.Router();
let sql = require('../../mysql/db_sql')();

/* GET seat reservation information */
router.get('/table/', function(req, res, next){
	console.log("---log start(RESERVATION:GET)---");
 	console.log("TABLE:GET -> id : ", req.query.id);
	sql.getReservationTable(req.query.id, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/reservation/table?id={id}

/* GET reservation detailed information */
router.get('/detail/', function(req, res, next){
	console.log("---log start(RESERVATION:GET)---");
	console.log("DETAIL:GET -> id : ", req.query.id);
	sql.getReservationInfo(req.query.id, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/reservation/detail?id={id}

module.exports = router;
