package requestserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.PinOutput;
import data.OutputPinData;
import data.OutputPinMySQL;
import data.User;
import data.UserData;
import data.UserMySQL;

import org.json.JSONException;
import org.json.JSONObject;

public class OutputPinsHandler implements HttpHandler {
	private String dbname,user,pass;

	public OutputPinsHandler(String dbname,String user,String pass) {
	super();
	this.dbname=dbname;
	this.user=user;
	this.pass=pass;}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		UserData sd=new UserMySQL(dbname,user,pass);
		OutputPinData sdout=new OutputPinMySQL(dbname,user,pass);
	    JSONObject obj = new JSONObject();
	    InputStream is=exchange.getRequestBody();
	    InputStreamReader isr =  new InputStreamReader(is,"utf-8");
	    BufferedReader br = new BufferedReader(isr);
	    int b;
	    String buf = "";
	    while ((b = br.read()) != -1) {
	        buf+=((char) b);}
	    is.close();
	    br.close();
	    isr.close();
	    int uid=-1;
	    User u = null;
	    try {
			obj = new JSONObject(buf);
			if(obj.getString("data").equals("outputpins"))
			u=sd.getUser(obj.getString("user"));//extragere nume utilizator din JSON
			uid=u.uid;	//obținere id utilizator						
		} catch (JSONException e1) {
			e1.printStackTrace();
		} 
	    obj = new JSONObject();
			if(uid!=-1&&u!=null)
			{	for(PinOutput po:sdout.getPinsOutputChanged(true, uid))
				try {obj.put(po.pin_no+"",po.value);} //adăugare în JSON pini ieșire schimbați 
				catch (JSONException e) {
					e.printStackTrace();}
			}

	    //set message length!!!
	    exchange.sendResponseHeaders(200, obj.toString().getBytes().length);
	    OutputStream os = exchange.getResponseBody();
	    OutputStreamWriter osw=new OutputStreamWriter(os,"UTF-8");
	    //creare stream către MonitoringAgent
	    
	    osw.write(obj.toString());
	    //scriere JSON în stream
	    
	    osw.flush();
	    osw.close();
	    os.flush();
	    os.close();
	    //System.out.println("data sent "+buf+" uid="+uid);

	}

}
