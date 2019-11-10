// 서버 사용을 위한 http 모듈을 http 변수에 담는다.
let http = require('http');

let app = require('./app');

let port = 7080;
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
