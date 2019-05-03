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

import data.PinInput;
import data.PinOutput;
import data.Condition;
import data.ConditionData;
import data.ConditionMySQL;
import data.InputPinData;
import data.InputPinMySQL;
import data.OutputPinData;
import data.OutputPinMySQL;
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
			System.out.println("|"+obj.toString()+"|");
			if(obj.getString("data").equals("pins"))
			u=sd.getUser(obj.getString("user"));
			uid=u.uid;
	    } catch (JSONException e1) {
			e1.printStackTrace();
		} 
	    obj = new JSONObject();
	    if(uid!=-1&&u!=null)
			{for(PinOutput po:sdout.getPinsOutputChanged(true,uid))
				try {
					out_obj.put(po.pin_no+"",po.value);} 
				catch (JSONException e) {
					e.printStackTrace();}
			for(PinInput pi:sdin.getPinsInput(uid))
				try {if(pi.active) {
					PinInput toplog=sdin.getTopPinInputLog(uid,pi.pin_no);
					for(Condition c:sdcon.loadConditions(uid,pi.pin_no, pi.sensor)) {
						boolean curr_val=sdout.getOutputPinbyPin_no(c.pin_out, uid).value;
						System.out.println(toplog.name+" Current value "+curr_val+" test:"+c.test(pi.value)+" val:"+c.val);
						if(c.test(pi.value)) {
							if(curr_val!=c.val) {
								sdout.updateOutputPin(c.pin_out,c.val, uid);
								System.out.println(c.test(pi.value)+" Change value "+c.pin_out+" ->"+c.val);}}
						else {
							if(curr_val==c.val) {
								sdout.updateOutputPin(c.pin_out,!c.val, uid);
								System.out.println(c.test(pi.value)+" Change value "+c.pin_out+" ->"+!c.val);}}
					}
					in_obj.put(pi.pin_no+"",pi.sensor);}
				}
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
