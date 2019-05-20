package requestserver;
import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class RequestServer extends Thread{
	private String db,user,pass;
	private int port;

	public RequestServer(String db, String user, String pass, int port) {
		super();
		this.db = db;
		this.user = user;
		this.pass = pass;
		this.port = port;
	}

	public void run(){
		HttpServer server;
		try {
			server = HttpServer.create(new InetSocketAddress(port),0);
		} catch (IOException e) {
			e.printStackTrace();
			return;}
		HttpContext contextout = server.createContext("/outputpinsstatus");
		contextout.setHandler( new OutputPinsHandler(db,user,pass) );
		HttpContext context = server.createContext("/pinsstatus");
		context.setHandler( new PinsHandler(db,user,pass) );
		HttpContext contextpir = server.createContext("/pirpins");
		contextpir.setHandler( new PirHandler(db,user,pass) );
		HttpContext contextin = server.createContext("/inputpinsstatus");
		contextin.setHandler( new InputPinsHandler(db,user,pass) );
		server.start();}
}
