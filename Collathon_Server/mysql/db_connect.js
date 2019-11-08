"use strict";
let mysql = require('mysql');

module.exports = function() {
	let config = require('./config');
	let pool = mysql.createPool(config);

	return {
		getConnection: function (callback) {
			pool.getConnection(callback);
		},
		end: function(callback){
			pool.end(callback);
		}
	}
}();
