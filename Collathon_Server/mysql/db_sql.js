let pool = require('./db_connect');
const fs = require('fs');
const childprocess = require("child_process");
require("date-utils");
console.log("sql starts on "+(new Date(Date.now())).toISOString());

let query_function = function(sql, callback){
		console.log("DB_SQL : ", sql);
		pool.getConnection(function(err, con){
			if(err){
				console.error(err);
				return;
			}
			con.query(sql, function (err, result, fields){
				con.release();
				if (err) return callback(err);
				callback(null, result);
			});
		});
};

let query_function_no_callback = function(sql){
	console.log("DB_SQL : ", sql);
	pool.getConnection(function(err, con){
		if(err){
			console.error(err);
			return;
		}
		con.query(sql, function (err, result, fields){
			con.release();
			if (err) return "error";
		});
	});
};

let testselect = function(sql, callback){
	query_function(sql, callback);
};

let getClientUser = function(client_id, password, callback){
	let sql = "select passwd from Client where id=\""+client_id+"\"";
	query_function(sql, callback);
};

let addClientUser = function(name, client_id, passwd, phone, email, callback){
	let sql = "insert into Client values(\""+name+"\", \""+client_id+"\", \""+passwd+"\", \""+phone+"\", "+(email===undefined? "NULL" : "\""+email+"\"")+")";
	query_function(sql, callback);
};

let getOwnerUser = function(owner_id, password, callback){
	let sql = "select passwd, name from Supplier where id=\""+owner_id+"\"";
	query_function(sql, callback);
};

let addOwnerUser = function(name, owner_id, password, phone, callback){
	let sql =  "insert into Supplier values(\""+name+"\", \""+owner_id+"\", \""+password+"\", \""+phone+"\")";
	query_function(sql, callback);
};

let getCategoryShop = function(category, callback){
	let sql = "select s.id, s.name as shop_name, m.name as menu_name from Shop s, Menu m  where s.id=m.shop_id and s.category=\""+category+"\" group by s.name";
	query_function(sql, callback);
};

let getOwnerShop = function(owner_id, callback){
	let sql = "select id, name from Shop where master=\""+owner_id+"\"";
	query_function(sql, callback);
};

let addOwnerShop = function(owner_id, name, tel, addr, category, table, callback){
	let sql = "insert into Shop (master, name, tel, address, category, check_table) values(\""+owner_id+"\", \""+name+"\", \""+tel+"\", \""+addr+"\", \""+category+"\", \""+table+"\")";
	query_function(sql, callback);
};

let addOwnerShopTable = function( number, count, callback){
	let sql = "insert into ShopTable values((select max(id) from Shop), "+number+", "+count+")";
	query_function(sql, callback);
};

let deleteOwnerShop = function(shop_id, callback){
	let sql = "delete from Menu where shop_id="+shop_id;
	let check = query_function_no_callback(sql);
	if(check == "error") {
		return callback("ERROR");
	}
	sql = "delete from ShopTable where shop_id="+shop_id;
	query_function_no_callback(sql);
	sql = "delete from BlackList where shop_id="+shop_id;
	query_function_no_callback(sql);
	sql = "delete from Likes where shop_id="+shop_id;
	query_function_no_callback(sql);
	sql = "delete from Shop where id="+shop_id;
	query_function(sql, callback);
};

let getShopDetail = function(shop_id, callback){
	let sql = "select * from Shop where id="+shop_id;
	query_function(sql, callback);
};

let getMenuwithTimeSale = function(shop_id, time, callback){
	time = time+":00-%"
	let sql = "select if(sale_price!=0, 'Y', 'N') as sale, m.name, ifnull(sale_price, price) as price, description, count, check_table from Menu as m inner join(Shop as s) on m.shop_id=s.id left join(TimeSale as t) on m.shop_id=t.shop_id and m.name=t.name and t.time like \""+time+"\" where m.shop_id="+shop_id;
	query_function(sql, callback);
};

let addShopMenu = function(shop_id, name, price, des, count, callback){
	let sql = "insert into Menu values("+shop_id+",\""+name+"\","+ price+",\""+des+"\","+count+")";
	query_function(sql, callback);
};

let deleteShopMenu = function(shop_id, name, callback){
	let sql = "delete from Menu where shop_id="+shop_id+" and name=\""+name+"\"";
	query_function(sql, callback);
};

let getClientUserDetail = function(client_id, callback){
	let sql = "select * from Client where id=\""+client_id+"\"";
	query_function(sql, callback);
};

let getOwnerUserDetail = function(owner_id, callback){
	let sql = "select * from Supplier where id=\""+owner_id+"\"";
	query_function(sql, callback);
};

let updateClientUser = function(client_id, new_passwd, callback){
	let sql = "update Client set passwd=\""+new_passwd+"\" where id=\""+client_id+"\"";
	query_function(sql, callback);
};

let updateOwnerUser = function(owner_id, new_passwd, callback){
	let sql = "update Supplier set passwd=\""+new_passwd+"\" where id=\""+owner_id+"\"";
	query_function(sql, callback);
};

let getBlackList = function(callback){
	let sql = "select client_id, count(*) as count from BlackList group by client_id"; 
	query_function(sql, callback);
};

let addBlackList = function(client_id, shop_id, comment, callback){
	let sql = "insert into BlackList values(\""+client_id+"\", "+shop_id+", \""+comment+"\")";
	query_function(sql, callback);
};

let getLikeShop = function(client_id, callback){
	let sql = "select l.name as user_id, shop_id, s.name as shop, master as owner, tel, address, category, check_table from Likes as l, Shop as s where l.shop_id=s.id and l.name =\""+client_id+"\"";  
	query_function(sql, callback);
};

let addLikeShop = function(shop_id, name, callback){
	let sql = "insert into Likes values("+shop_id+", \""+name+"\")";
	query_function(sql, callback);
};

let deleteLikeShop = function(shop_id, name, callback){
	let sql = "delete from Likes where shop_id="+shop_id+" and name=\""+name+"\"";
	query_function(sql, callback);
};

let getUserReservationTable = function(client_id, callback){
	let sql = "select r.id as reservation_id, name as shop, number, reservation_count as count, time from Reservation as r inner join(Shop as s) on r.shop_id=s.id inner join(ReservationTable as rt) on r.id=rt.id where client_id=\""+client_id+"\"";
	query_function(sql, callback);
};

let getUserReservationMenu = function(client_id, callback){
	let sql = "select Reservation.id as reservation_id, Shop.name as shop, ReservationMenu.name as menu, count, time from Reservation natural join(ReservationMenu), Shop where Shop.id=Reservation.shop_id and client_id=\""+client_id+"\"";
	query_function(sql, callback);
};

let getOwnerReservationTable = function(shop_id, callback){
	let sql = "select id as reservation_id, client_id as user, number, reservation_count as count, time from Reservation natural join(Shop) natural join(ReservationTable) where shop_id="+shop_id;
	query_function(sql, callback);
};

let getOwnerReservationMenu = function(shop_id, callback){
	let sql = "select Reservation.id as reservation_id, Reservation.client_id as user, ReservationMenu.name as menu, count, time from Reservation natural join(ReservationMenu), Shop where Shop.id=Reservation.shop_id and ReservationMenu.shop_id="+shop_id;
	query_function(sql, callback);
};

let getReservationTable = function(shop_id, time, callback){
	let sql = "select s.shop_id, s.number, if(time is null, count, count-count(reservation_count)) as remain_table from ShopTable as s left join(ReservationTable as rt) on rt.number=s.number and rt.shop_id=s.shop_id left join(Reservation as r) on rt.id=r.id and time=\""+time+"\" where s.shop_id="+shop_id+" group by s.shop_id, s.number";
	query_function(sql, callback);
};

let addReservation = function(classification, client_id, time, shop_id, callback){
	let sql = "insert into Reservation (classification, client_id, time, shop_id) values(\""+classification+"\", \""+client_id+"\", \""+time+"\", "+shop_id+")";
	query_function(sql, callback);
};

let addReservationMenu = function(classification, client_id, shop_id, name, count, callback){
	let sql = "insert into ReservationMenu values((select id from Reservation where classification=\""+classification+"\" and client_id=\""+client_id+"\"), "+shop_id+", \""+name+"\", "+count+")";
	query_function_no_callback(sql);
	sql = "update Menu set count = count-"+count+" where shop_id="+shop_id+" and name=\""+name+"\""; //menu count 감소
	query_function(sql, callback);
};

let addReservationTable = function(classification, client_id, shop_id, number, count, callback){
	let sql = "insert into ReservationTable values((select id from Reservation where classification=\""+classification+"\" and client_id=\""+client_id+"\"), "+shop_id+", "+number+", "+count+")";
	query_function(sql, callback);
};

let deleteReservationAll = function(reservation_id, shop_id, callback){
	let sql = "update Menu as m, ReservationMenu as r set m.count=m.count+r.count where m.shop_id=r.shop_id and m.name=r.name and id="+reservation_id; //menu count 되돌리기
	query_function_no_callback(sql);	
	sql = "delete from ReservationTable where id="+reservation_id;
	query_function_no_callback(sql);
	sql = "delete from ReservationMenu where id="+reservation_id;
	query_function_no_callback(sql);
	sql = "delete from Reservation where id="+reservation_id;
	query_function(sql, callback);
};

let getTimeSale = function(shop_id, callback){
	let sql = "select * from TimeSale natural join(Menu) where shop_id="+shop_id+" and count >0";
	query_function(sql, callback);
};

let addTimeSale = function(shop_id, name, sale_price, time, callback){
	let sql = "insert into TimeSale (shop_id, name, sale_price, time) select "+shop_id+", \""+name+"\", "+sale_price+", \""+time+"\" from dual where not exists (select * from TimeSale where shop_id="+shop_id+" and name=\""+name+"\" and time=\""+time+"\")";
	query_function(sql, callback);
};

let deleteTimeSale = function(id,callback){
	let sql = "delete from TimeSale where id="+id;
	query_function(sql, callback);
};

let getStockMenu = function(shop_id, callback){
	let sql = "select * from Menu where shop_id ="+shop_id+" and count > 0";
	query_function(sql, callback);
};

module.exports = function() {
	return {
		getClientUser: getClientUser,
		addClientUser: addClientUser,
		getOwnerUser: getOwnerUser,
		addOwnerUser: addOwnerUser,
		getCategoryShop: getCategoryShop,
		getShopDetail: getShopDetail,
		getMenuwithTimeSale: getMenuwithTimeSale,
		getOwnerShop: getOwnerShop,
		addOwnerShop: addOwnerShop,
		addOwnerShopTable: addOwnerShopTable,
		deleteOwnerShop: deleteOwnerShop,
		addShopMenu: addShopMenu,
		deleteShopMenu: deleteShopMenu,
		getClientUserDetail: getClientUserDetail,
		getOwnerUserDetail: getOwnerUserDetail,
		updateClientUser: updateClientUser,
		updateOwnerUser: updateOwnerUser,
		getBlackList: getBlackList,
		addBlackList: addBlackList,
		getLikeShop: getLikeShop,
		addLikeShop: addLikeShop,
		deleteLikeShop: deleteLikeShop,
		getReservationTable: getReservationTable,
		getUserReservationMenu: getUserReservationMenu,
		getUserReservationTable: getUserReservationTable,
		getOwnerReservationTable: getOwnerReservationTable,
		getOwnerReservationMenu: getOwnerReservationMenu,
		addReservation: addReservation,
		addReservationMenu: addReservationMenu,
		addReservationTable: addReservationTable,
		deleteReservationAll: deleteReservationAll,
		getTimeSale: getTimeSale,
		addTimeSale: addTimeSale,
		deleteTimeSale: deleteTimeSale,
		getStockMenu: getStockMenu,
		pool: pool
	}
};
