package requestserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.PinOutput;
import data.ServerData;
import org.json.JSONException;
import org.json.JSONObject;

public class OutputPinsHandler implements HttpHandler {
private ServerData sd;

	public OutputPinsHandler(ServerData sd) {
	super();
	this.sd = sd;
}
	@Override
	public void handle(HttpExchange exchange) throws IOException {
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
	    try {
			obj = new JSONObject(buf);
			System.out.println("|"+obj.toString()+"|");
			if(obj.getString("data").equals("outputpins"))
			uid=sd.getuid(obj.getString("user"), obj.getString("password"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
			if(uid!=-1)
			{for(PinOutput po:sd.getPinsOutputChanged(true, uid))
				try {obj.put(po.pin_no+"",po.value);} 
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
