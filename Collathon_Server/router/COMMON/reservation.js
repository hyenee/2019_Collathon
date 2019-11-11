// 예약 정보 확인/추가/삭제 기능을 하는 핸들러
let express = require('express');
let router = express.Router();
let sql = require('../../mysql/db_sql')();

/* GET user's seat reservation information */
router.get('/table/user/', function(req, res, next){
	console.log("---log start(RESERVATION:GET)---");
 	console.log("USER'S TABLE:GET -> user id : ", req.query.id);
	sql.getUserReservationTable(req.query.id, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/reservation/table/user?id={client_id}

/* GET reservation detailed information */
router.get('/detail/', function(req, res, next){
	console.log("---log start(RESERVATION:GET)---");
	console.log("DETAIL:GET -> user id : ", req.query.id);
	sql.getReservationInfo(req.query.id, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/reservation/detail?id={client_id}

/* GET remaining table information */
router.get('/table/remain/', function(req, res, next){
	console.log("---log start(RESERVATION:GET)---");
	console.log("TABLE:GET -> shop id : ", req.query.id);
	console.log("TABLE:GET -> reservation id : ", req.query.time);
	sql.getReservationTable(req.query.id, req.query.time, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/reservation/table/remain?id={shop_id}&time={reservation_time}

module.exports = router;
