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

import data.Condition;
import data.ConditionData;
import data.ConditionMySQL;
import data.InputPinData;
import data.InputPinMySQL;
import data.OutputPinData;
import data.OutputPinMySQL;
import data.PinInput;
import data.User;
import data.UserData;
import data.UserMySQL;

public class PirHandler implements HttpHandler {
	private String dbname,user,pass;
	
	public PirHandler(String dbname,String user,String pass)
	{super();
	this.dbname=dbname;
	this.user=user;
	this.pass=pass;}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		OutputPinData sdout=new OutputPinMySQL(dbname,user,pass);
		ConditionData sdcon=new ConditionMySQL(dbname,user,pass);
		InputPinData sdin=new InputPinMySQL(dbname,user,pass);
		UserData sd=new UserMySQL(dbname,user,pass);
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
         		pi = sdin.getIntputPinbyPin_no(pin,uid);
				//Pin p=sdpin.getPin(pin,uid);
				if(pi!=null&&pi.active){
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
				sdin.updateInputPinValueLogtimestamp(pin,"1",uid);
			}			
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
