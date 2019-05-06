package requestserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Time;
import java.time.LocalTime;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import data.PinInput;
import data.PinOutput;
import data.Condition;
import data.ConditionData;
import data.ConditionIn;
import data.ConditionMySQL;
import data.ConditionOut;
import data.InputPinData;
import data.InputPinMySQL;
import data.OutputPinData;
import data.OutputPinMySQL;
import data.Pin;
import data.User;
import data.UserData;
import data.UserMySQL;

public class PinsHandler implements HttpHandler {
	private String dbname,user,pass;
	public PinsHandler(String dbname,String user,String pass) {
		super();
		this.dbname=dbname;
		this.user=user;
		this.pass=pass;}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		OutputPinData sdout=new OutputPinMySQL(dbname,user,pass);
		ConditionData sdcon=new ConditionMySQL(dbname,user,pass);
		InputPinData sdin=new InputPinMySQL(dbname,user,pass);
		UserData sd=new UserMySQL(dbname,user,pass);
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
			//System.out.println("|"+obj.toString()+"|");
			if(obj.getString("data").equals("pins"))
			u=sd.getUser(obj.getString("user"));
			uid=u.uid;
	    } catch (JSONException e1) {
			e1.printStackTrace();
		} 
	    obj = new JSONObject();
	    if(uid!=-1&&u!=null)
			{for(Pin p:sdout.getPinsOutput(uid))
				for(Condition c :sdcon.loadConditions(uid, p.pin_no))
					((ConditionOut)c).test();
	    	for(PinOutput po:sdout.getPinsOutputChanged(true,uid))
				try {out_obj.put(po.pin_no+"",po.value);} 
				catch (JSONException e) {
					e.printStackTrace();}
			for(Pin pi:sdin.getPinsInput(uid))
				try {if(((PinInput)pi).active) {
					//PinInput toplog=sdin.getTopPinInputLog(uid,pi.pin_no);
					for(Condition c:sdcon.loadConditions(uid,pi.pin_no, ((PinInput)pi).sensor)) {
						//System.out.println("ondition "+c);
						PinOutput po=sdout.getOutputPinbyPin_no(c.getOutputPin(), uid);
						if(po!=null) {
							boolean test_val=((ConditionIn)c).test(((PinInput)pi).value);;
							System.out.println(" Current value "+po.value+" test:"+test_val+" val:"+c.getValue());
														
						}
					}
					in_obj.put(pi.pin_no+"",((PinInput)pi).sensor);}
				}
				catch (Exception e) {   		
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
	    
	    //System.out.println("|"+obj.toString()+"|");
	    
	    //set message length!!!
	    exchange.sendResponseHeaders(200, obj.toString().getBytes().length);
	    OutputStream os = exchange.getResponseBody();
	    OutputStreamWriter osw=new OutputStreamWriter(os,"UTF-8");
	    
	    osw.write(obj.toString());
	    //System.out.println("|"+obj.toString()+"|");
	    
	    osw.flush();
	    osw.close();
	    os.flush();
	    os.close();
	    
	    //System.out.println("data sent "+buf+" uid="+uid);


	}

}
