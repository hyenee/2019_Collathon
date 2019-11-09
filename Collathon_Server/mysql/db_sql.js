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
	let sql = "select name, price, description from Menu where shop_id="+shop_id;
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
/*
let getLikeShop = function(shop_id, client_id, callback){
	let sql = "select * from Likes where shop_id=\""+shop_id+"\" And name =\""+client_id+"\"";  
	query_function(sql, callback);
};

let addLikeShop = function(shop_id, client_id, callback){
	let sql = "insert into Likes (shop_id, name) values(\""+shop_id+"\", "+client_id +")";
	query_function(sql, callback);
};

let getBlackList = function(client_id, shop_id, callback){
	let sql = "select * from BlackList where client_id=\""+client_id+"\" And shop_id =\""+shop_id+"\"";
	query_function(sql, callback);
};
*/


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
/*
		getLikeShop: getLikeShop,
		addLikeShop: addLikeShop,
		getBlackList: getBlackList,
*/
		pool: pool
	}
};

