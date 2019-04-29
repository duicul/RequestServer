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
			u=sd.getUser(obj.getString("user"));
			uid=u.uid;
			System.out.println("|"+obj.toString()+"|"+uid+" "+err);
			if(!err&&obj.getString("data").equals("pirpin")&&u!=null){
				int pin=obj.getInt("pin_no");
				PinInput pi;
         		pi = sd.getIntputPinbyPin_no(pin,uid);
				Pin p=sd.getPin(pin,uid);
				System.out.println((pi==null)+" "+(p==null)+" "+pin);
				if(pi!=null&p!=null){
					sd.updateInputPinValueLogtimestamp(pin,"1",uid);
					System.out.println("Pir pin "+p.pin_no+" "+p.name+" detected");}			
			}
			}
	    catch (JSONException e) {
			e.printStackTrace();}
	  
	    exchange.sendResponseHeaders(200, "okay".getBytes().length);
	    OutputStream os = exchange.getResponseBody();
	    OutputStreamWriter osw=new OutputStreamWriter(os,"UTF-8");
	    
	    osw.write("okay");
	    
	    
	    osw.flush();
	    osw.close();
	    os.flush();
	    os.close();
	   }


}
