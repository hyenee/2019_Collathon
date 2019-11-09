let express = require("express");
let app = express();
let path = require('path');
let bodyParser = require('body-parser');

let user_login_router = require("./router/USER/login");
let owner_login_router = require("./router/OWNER/login");
let categories_router = require("./router/USER/categories");
let own_shop_router = require("./router/OWNER/ownShop");
//let own_menu_router = require("./router/OWNER/ownShopMenu");

// view 경로 설정
app.set('views', path.join(__dirname, '/views'));

// 화면 engine을 ejs로 설정
app.set('view engine', 'ejs');
app.engine('html', require('ejs').renderFile);

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
// 기본 path를 /public으로 설정(css, javascript 등의 파일 사용을 위해)
app.use(express.static(path.join(__dirname, '/public')));

app.use('/user/login', user_login_router);
app.use('/owner/login', owner_login_router);
app.use('/categories', categories_router);
app.use('/ownShop', own_shop_router);
//app.use('/ownMenu', own_menu_router);

/*
// catch 404 and forward to error handler
app.use(function(req, res, next){
	next(createError(404));
});

// error handler
app.use(function(err, req, res, next){
	// set locals, only providing error in development
	res.locals.message = err.message;
	res.locals.error = req.app.get('env') === 'development' ? err : {};

	// render the error page
	res.status(err.status || 500);
	err.status = 403;
	res.render('error');
});
*/

module.exports = app;
