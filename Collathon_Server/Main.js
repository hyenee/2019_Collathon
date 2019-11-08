// 서버 사용을 위한 http 모듈을 http 변수에 담는다.
let http = require('http');

let app = require('./app');

let port = 7777;
let server;

app.set('port', port);
server = http.createServer(app);
server.listen(app.get('port'), function(){
	console.log('Server is running... port: ',port);
});
server.on('error', onError);
server.on('listening', onListening);

function onError(error){
	if(error.syscall !== 'listen'){
		throw error;
	}

	let bind = typeof port == 'string'
		? 'Pipe'+port
		: 'Port'+port;

	//handle specific listen errors with freindly messages
	switch(error.code){
		case 'EACCES':
			console.error(bind+' requires elevated privileges');
			process.exit(1);
			break;
		case 'EADDRINUSE':
			console.error(bind+' is already in use');
			process.exit(1);
			break;
		default:
			throw error;
		}
}

// Event listener for HTTP server "listening" event.
let debug = require('debug');
function onListening(){
	let addr = server.address();
	let bind = typeof addr === 'string'
		? 'pipe' + addr
		: 'port' + addr.port;
	debug('Listening on '+bind);
}

/*
// http 모듈로 서버를 생성(http 요청이 들어오면 function 실행)
var server = http.createServer(function(request, response){
	console.log('---log start---');
	var parsedUrl = url.parse(request.url); // 브라우저에서 요청한 주소를 parsing하여 객체화
	console.log(parsedUrl);
	var parsedQuery = querystring.parse(parsedUrl.query, '&','=');
	console.log(parsedQuery);
	console.log('---log end---');

	response.writeHead(200, {'Content-Type':'text/html'});
	response.end('JA MUKJA!');
});

// listen 함수로 7777 포트를 가진 서버 실행
// 서버가 실행된 것을 확인하기 위해 log 찍기
server.listen(7777, function(){
	console.log('Server is running...');
});
*/
