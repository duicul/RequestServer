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

import data.Pin;
import data.PinInput;
import data.ServerData;
import data.User;

public class InputPinsHandler implements HttpHandler {
	private ServerData sd;
	
	public InputPinsHandler(ServerData sd) {
		super();
		this.sd = sd;}
	
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
			if(!err&&obj.getString("data").equals("inputpins")&&u!=null){
				for(int i=2;i<=26;i++){
					try {
						String value=obj.getString(i+"");
						PinInput pi;
							pi = sd.getIntputPinbyPin_no(i,uid);
							Pin p=sd.getPin(i,uid);
							System.out.println(i+" "+value+" "+p.name+" "+pi.sensor+" "+uid);
							if(pi!=null&p!=null&&pi.active)
							{sd.updateInputPinValueLogtimestamp(i, value,uid);
							/*sd.updateInputPinValue(i, value,uid);*/}
					}catch(JSONException e) {
						/*e.printStackTrace();*/}
					}
			}
	    }
	    catch (JSONException e) {
			//e.printStackTrace();
			err=true;}
	    
	    
	    
	    //set message length!!!
	    String resp= err ? "Error":"Okay";
	    exchange.sendResponseHeaders(200, resp.getBytes().length);
	    OutputStream os = exchange.getResponseBody();
	    OutputStreamWriter osw=new OutputStreamWriter(os,"UTF-8");
	    
	    osw.write(resp);
	    
	    
	    osw.flush();
	    osw.close();
	    os.flush();
	    os.close();

	    System.out.println("data sent "+resp+" uid="+uid);

	}

}
