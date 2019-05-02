package requestserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import data.InputPinData;
import data.InputPinMySQL;
import data.Pin;
import data.PinInput;
import data.PinMySQL;
import data.PinData;
import data.User;
import data.UserData;
import data.UserMySQL;

public class InputPinsHandler implements HttpHandler {
	private String dbname,user,pass;
	
	public InputPinsHandler(String dbname,String user,String pass) {
		super();
		this.dbname=dbname;
		this.user=user;
		this.pass=pass;
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		UserData sd=new UserMySQL(dbname,user,pass);
		InputPinData sdin=new InputPinMySQL(dbname,user,pass);
		PinData sdpin=new PinMySQL(dbname,user,pass);
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
						int logtime=obj.getInt("logtime");
						PinInput pi;
							pi = sdin.getIntputPinbyPin_no(i,uid);
							Pin p=sdpin.getPin(i,uid);
							System.out.println(i+" "+value+" "+p.name+" "+pi.sensor+" "+uid);
							if(pi!=null&p!=null&&pi.active)
							{PinInput toplog=sdin.getTopPinInputLog(uid,i);
							if(toplog!=null) {
								Timestamp ts=toplog.timestamp;
								if(new java.util.Date().getTime()-ts.getTime()>logtime*60000)
								sdin.updateInputPinValueLogtimestamp(i, value,uid);
								else sdin.updateInputPinValueNoLogtimestamp(i, value,uid);
							}
							else sdin.updateInputPinValueLogtimestamp(i, value,uid);
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
