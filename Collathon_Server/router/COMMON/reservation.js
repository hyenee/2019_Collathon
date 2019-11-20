// 예약 정보 확인/추가/삭제 기능을 하는 핸들러
let express = require('express');
let router = express.Router();
let sql = require('../../mysql/db_sql')();

var delete_info = null;

/* GET user's reservation id that client_id */
router.get('/getDelete/', function(req, res, next){
	console.log("---log start(RESERVATION:GET)---");
 	console.log("DELETE INFO:GET -> delete_info: ",delete_info);
	if(delete_info == null){
		res.send("null");
	}else{
	res.send(delete_info);
	}
	console.log("sending --->---");
	delete_info = null;
	console.log("DELETE INFO:CHANGE -> delete_info: ", delete_info);
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/reservation/getDelete

/* GET user's seat reservation information */
router.get('/table/user/', function(req, res, next){
	console.log("---log start(RESERVATION:GET)---");
 	console.log("USER'S TABLE:GET -> user id : ", req.query.id);
	sql.getUserReservationTable(req.query.id, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/reservation/table/user?id={client_id}

/* GET owner's seat reservation information */
router.get('/table/owner/', function(req, res, next){
	console.log("---log start(RESERVATION:GET)---");
 	console.log("USER'S TABLE:GET -> shop id : ", req.query.shop);
	sql.getOwnerReservationTable(req.query.shop, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/reservation/table/owner?shop={shop_id}

/* GET user's reservation detailed information */
router.get('/detail/', function(req, res, next){
	console.log("---log start(RESERVATION:GET)---");
	console.log("DETAIL:GET -> user id : ", req.query.id);
	sql.getUserReservationMenu(req.query.id, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/reservation/detail?id={client_id}

/* GET owner's reservation detailed information */
router.get('/owner/', function(req, res, next){
	console.log("---log start(RESERVATION:GET)---");
	console.log("DETAIL:GET -> shop id : ", req.query.shop);
	sql.getOwnerReservationMenu(req.query.shop, function(err, result){
		res.send(result);
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/reservation/owner?shop={shop_id}

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

/* POST new Reservation information */
router.post('/add/', function(req, res, next){
	console.log("---log start(RESERVATION:POST)---");
	console.log("NEW RESERVATION:POST -> current time(YYMMDDHHmmss) : ", req.query.current);
	console.log("NEW RESERVATION:POST -> client_id : ", req.query.user);
	console.log("NEW RESERVATION:POST -> time : ", req.query.time);
	console.log("NEW RESERVATION:POST -> shop_id : ", req.query.shop);
	if(req.query.time==="0"){
		console.error("ADD BASIC:GET FAILED: WRONG INPUTDATA OF TIME");
		res.send([{"result":"ERROR"}]);
	}
	else{
	sql.addReservation(req.query.current, req.query.user, req.query.time, req.query.shop, function(err){
		if(err){
			console.error("ADD BASIC:GET FAILED: ",err);
			res.send([{"result":"ERROR"}]);
		}
		else{
			res.send([{"result":"OK"}]);
		}
	});
	}
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/reservation/add?current={current_time}&user={client_id}&time={reservation_time}&shop={shop_id}

/* POST new Reservation Menu information */
router.post('/add/menu/', function(req, res, next){
	console.log("---log start(RESERVATION:POST)---");
	console.log("NEW RESERVATION:POST -> current time : ", req.query.current);
	console.log("NEW RESERVATION:POST -> client_id : ", req.query.user);
	console.log("NEW RESERVATION:POST -> shop_id : ", req.query.shop);
	console.log("NEW RESERVATION:POST -> menu name : ", req.query.menu);
	console.log("NEW RESERVATION:POST -> count : ", req.query.count);
	sql.addReservationMenu(req.query.current, req.query.user, req.query.shop, req.query.menu, req.query.count, function(err){
		if(err){
			console.error("ADD MENU:GET FAILED: ",err);
			res.send([{"result":"ERROR"}]);
		}
		else{
			res.send([{"result":"OK"}]);
		}
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/reservation/add/menu?current={current_time}&user={client_id}&shop={shop_id}&menu={menu_name}&count={menu_count}

/* POST new Reservation Table information */
router.post('/add/table/', function(req, res, next){
	console.log("---log start(RESERVATION:POST)---");
	console.log("NEW RESERVATION:POST -> current time : ", req.query.current);
	console.log("NEW RESERVATION:POST -> client_id : ", req.query.user);
	console.log("NEW RESERVATION:POST -> shop_id : ", req.query.shop);
	console.log("NEW RESERVATION:POST -> table name : ", req.query.table);
	console.log("NEW RESERVATION:POST -> count : ", req.query.count);
	sql.addReservationTable(req.query.current, req.query.user, req.query.shop, req.query.table, req.query.count, function(err){
		if(err){
			console.error("ADD TABLE:GET FAILED: ",err);
			res.send([{"result":"ERROR"}]);
		}
		else{
			res.send([{"result":"OK"}]);
		}
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/reservation/add/table?current={current_time}&user={client_id}&shop={shop_id}&table={number_of_table}&count={table_count}

/* POST delete all Reservation information */
router.post('/delete/', function(req, res, next){
	console.log("---log start(RESERVATION:POST)---");
	console.log("DELETE:POST -> reservation id : ", req.query.reservation);
	sql.deleteReservationAll(req.query.reservation, function(err, result){
		if(err){
			console.error("DELETE:GET FAILED: ",err);
			res.send([{"result":"ERROR"}]);
		}
		else{
			delete_info = result;
			res.send([{"result":"OK"}]);
		}
	});
	console.log("---log end---");
}); // http://oreh.onyah.net:7080/reservation/delete?reservation={reservation_id}

module.exports = router;
