package requestserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import data.Pin;
import data.PinInput;
import data.ServerData;
import data.User;

public class PirHandler implements HttpHandler {
	private ServerData sd;
	
	public PirHandler(ServerData sd)
	{super();
	this.sd=sd;}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		boolean err=false;
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
	    JSONObject obj; 
	    int uid=-1;
	    User u;
	    try {
			obj = new JSONObject(buf); 
			System.out.println("|"+obj.toString()+"|");
			u=sd.getUser(obj.getString("user"));
			uid=u.uid;
			if(!err&&obj.getString("data").equals("pirpin")&&u!=null){
				int pin=obj.getInt("pin_no");
				PinInput pi;
         		pi = sd.getIntputPinbyPin_no(pin,uid);
				Pin p=sd.getPin(pin,uid);
				if(pi!=null&p!=null){
					sd.insertInputPin(pin,"1",p.name,pi.sensor,uid);}			
			}
			}
	    catch (JSONException e) {
			e.printStackTrace();}
	   }


}