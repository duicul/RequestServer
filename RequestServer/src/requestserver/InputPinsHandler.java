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

import data.Condition;
import data.ConditionData;
import data.ConditionIn;
import data.ConditionMySQL;
import data.InputPinData;
import data.InputPinMySQL;
import data.OutputPinData;
import data.OutputPinMySQL;
import data.Pin;
import data.PinInput;
import data.PinMySQL;
import data.PinOutput;
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
		ConditionData sdcon=new ConditionMySQL(dbname,user,pass);
		InputPinData sdin=new InputPinMySQL(dbname,user,pass);
		OutputPinData sdout=new OutputPinMySQL(dbname,user,pass);
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
			//System.out.println("|"+obj.toString()+"|");
			u=sd.getUser(obj.getString("user"));
			uid=u.uid;
			if(!err&&obj.getString("data").equals("inputpins")&&u!=null){
						JSONObject inpins=obj.getJSONObject("in_pins");
						int logtime=obj.getInt("logtime");
						for(String i:JSONObject.getNames(inpins))
							try {
								int pin=Integer.parseInt(i);
								String value=inpins.getString(i+"");
								Pin p=sdpin.getPin(pin,uid);
								//System.out.println(i+" "+value+" "+p.name+" "+pi.sensor+" "+uid);
								if(p!=null&&((PinInput)p).active){
									Pin toplog=sdin.getTopPinInputLog(uid,pin);
									System.out.println(toplog);
									if(toplog!=null) {
										Timestamp ts=((PinInput)toplog).timestamp;
										if(new java.util.Date().getTime()-ts.getTime()>logtime*60000)
											{System.out.println("Update log");sdin.updateInputPinValueLogtimestamp(pin, value,uid);}
										else sdin.updateInputPinValueNoLogtimestamp(pin, value,uid);
									}
									else 
										sdin.updateInputPinValueLogtimestamp(pin, value,uid);
									for(Condition c:sdcon.loadConditions(uid,p.pin_no, ((PinInput)p).sensor)) {
										//System.out.println("ondition "+c);
										PinOutput po=sdout.getOutputPinbyPin_no(c.getOutputPin(), uid);
										if(po!=null) {
											boolean curr_val=po.value;
											boolean test_val=((ConditionIn)c).test(value);
											//System.out.println(" Current value "+curr_val+" test:"+test_val+" val:"+c.getValue());
																		
										}
									}
									
									//System.out.println("Conditions loaded");
								}
							}catch(JSONException e) {
								e.printStackTrace();}
			}
	    }
	    catch (Exception e) {
			e.printStackTrace();
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

	    //System.out.println("data sent "+resp+" uid="+uid);

	}

}
