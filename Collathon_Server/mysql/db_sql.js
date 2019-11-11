"use strict";
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
	let sql = "select passwd from Supplier where id=\""+owner_id+"\"";
	query_function(sql, callback);
};

let addOwnerUser = function(name, owner_id, password, phone, callback){
	let sql =  "insert into Supplier values(\""+name+"\", \""+owner_id+"\", \""+password+"\", \""+phone+"\")";
	query_function(sql, callback);
};

let getCategoryShop = function(category, callback){
	let sql = "select s.name as shop_name, m.name as menu_name from Shop s, Menu m  where s.id=m.shop_id and s.category=\""+category+"\"";
	query_function(sql, callback);
};

let getOwnerShop = function(owner_id, callback){
	let sql = "select name from Shop where master=\""+owner_id+"\"";
	query_function(sql, callback);
};

let addOwnerShop = function(owner_id, name, tel, addr, category, table, callback){
	let sql = "insert into Shop (master, name, tel, address, category, check_table) values(\""+owner_id+"\", \""+name+"\", \""+tel+"\", \""+addr+"\", \""+category+"\", \""+table+"\")";
	query_function(sql, callback);
};

let getShopDetail = function(shop_id, callback){
	let sql = "select * from Shop where id="+shop_id;
	query_function(sql, callback);
};

let getMenuDetail = function(shop_id, callback){
	let sql = "select name, price, description, count from Menu where shop_id="+shop_id;
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
	let sql = "select * from Likes where name =\""+client_id+"\"";  
	query_function(sql, callback);
};

let addLikeShop = function(shop_id, name, callback){
	let sql = "insert into Likes values("+shop_id+", \""+name+"\")";
	query_function(sql, callback);
};

let getUserReservationTable = function(client_id, callback){
	let sql = "select name, number, count, time from Reservation natural join(Shop) natural join(ReservationTable) where client_id=\""+client_id+"\"";
	query_function(sql, callback);
};

let getReservationInfo = function(client_id, callback){
	let sql = "select Shop.name as shop, ReservationMenu.name as menu, count, time from Reservation natural join(ReservationMenu), Shop where Shop.id=Reservation.shop_id and client_id=\""+client_id+"\"";
	query_function(sql, callback);
};

let getReservationTable = function(shop_id, time, callback){
	let sql = "select r.shop_id, rt.number, (s.count-rt.count) as remain_table from Reservation as r natural join(ReservationTable as rt) inner join(ShopTable as s) on r.shop_id = s.shop_id and rt.number = s.number where r.shop_id="+shop_id+" and time=\""+time+"\"";
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
		getMenuDetail: getMenuDetail,
		getOwnerShop: getOwnerShop,
		addOwnerShop: addOwnerShop,
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
		getReservationTable, getReservationTable,
		getReservationInfo, getReservationInfo,
		getUserReservationTable, getUserReservationTable,
		pool: pool
	}
};

