package requestserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import data.PinInput;
import data.PinOutput;
import data.ServerData;
import data.User;

public class PinsHandler implements HttpHandler {
	private ServerData sd;
	public PinsHandler(ServerData sd) {
		super();
		this.sd = sd;
	}
	@Override
	public void handle(HttpExchange exchange) throws IOException {
	    JSONObject obj = new JSONObject();
	    JSONObject in_obj=new JSONObject();
	    JSONObject out_obj=new JSONObject();
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
			System.out.println("|"+obj.toString()+"|");
			if(obj.getString("data").equals("pins"))
			u=sd.getUser(obj.getString("user"));
			uid=u.uid;
	    } catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	    obj = new JSONObject();
	    if(uid!=-1&&u!=null)
			{for(PinOutput po:sd.getPinsOutputChanged(true,uid))
				try {
					out_obj.put(po.pin_no+"",po.value);} 
				catch (JSONException e) {
					e.printStackTrace();}
			for(PinInput po:sd.getPinsInput(uid))
				try {in_obj.put(po.pin_no+"",po.sensor);} 
				catch (JSONException e) {   		
					e.printStackTrace();}
	    try {
	    	obj.put("IN", in_obj.toString());} 
    	catch (JSONException e) {
    		e.printStackTrace();}
	    
	    try {
	    	obj.put("OUT", out_obj.toString());} 
    	catch (JSONException e) {
    		e.printStackTrace();}
			}
	    
	    System.out.println("|"+obj.toString()+"|");
	    
	    //set message length!!!
	    exchange.sendResponseHeaders(200, obj.toString().getBytes().length);
	    OutputStream os = exchange.getResponseBody();
	    OutputStreamWriter osw=new OutputStreamWriter(os,"UTF-8");
	    
	    osw.write(obj.toString());
	    System.out.println("|"+obj.toString()+"|");
	    
	    osw.flush();
	    osw.close();
	    os.flush();
	    os.close();
	    
	    System.out.println("data sent "+buf+" uid="+uid);


	}

}