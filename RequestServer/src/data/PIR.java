package data;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

public class PIR extends PinInput {
	public PIR(int pin_no, String value, String name, String sensor, Timestamp timestamp) {
		super(pin_no, value, name, sensor, timestamp,value.equals("1")?true:false);
		//System.out.println("PIR Sensor pin "+this.pin_no+" "+this.value+"|"+this.active);
	}

	@Override
	public String getData() {
		String resp="";
		resp+="<p>"+this.pin_no+" Last movement detected by "+this.name+" on "+this.timestamp+"</p>";
	return resp;
	}

	@Override
	public String getGauge() {
		StringBuilder data=new StringBuilder();
		data.append("<div onClick=\"inputpinlog("+this.pin_no+")\">");
		data.append(this.active?"<i class=\"far fa-eye fa-7x pointer\"></i>":"<i class=\"far fa-eye-slash fa-7x pointer\"></i>");
		data.append("</div>");
		data.append("<br />");
		data.append("<button class=\"btn "+(this.active?"btn-warning":"btn-secondary")+"\"  onclick=\"toggleinputpin("+this.pin_no+")\"> Turn "+(this.active?"off":"on")+" "+this.name+"</button>");
		data.append("<br />");
		data.append("<span class=\"badge badge-warning\">"+this.timestamp+"</span>");
		return data.toString();
	}

	@Override
	public String drawGraph(int uid) {
		ServerData sd=new MySqlData(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		List<PinInput> logdata=sd.getPinInputLog(uid, this.pin_no,this.sensor);
		if(logdata==null) {
			System.out.println("logdatanull");
			return null;}
		StringBuilder data=new StringBuilder("[");
		data.append("{ \"type\" : \"scatter\" ,\"name\":\"Detect\",");
		data.append("\"toolTipContent\": \"{day}.{month}.{year} {hour}:{minute}\",");
		data.append(" \"dataPoints\" : [");
		for(PinInput pi:logdata){
			PIR pir=(PIR)pi;
			if(pir.active){
				Timestamp ts=pir.timestamp;
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(ts.getTime());
				int year,month,hour,day,minute;
				year=cal.get(Calendar.YEAR);
		
				month=cal.get(Calendar.MONTH);
		
				day=cal.get(Calendar.DAY_OF_MONTH);
		
				hour=cal.get(Calendar.HOUR_OF_DAY);
		
				minute=cal.get(Calendar.MINUTE);
				data.append("{\"year\":");
				data.append(year);
				data.append(",\"month\":");
				data.append(month+1);
				data.append(",\"day\":");
				data.append(day);
				data.append(",\"hour\":");
				data.append(hour);
				data.append(",\"minute\":");
				data.append(minute);
				data.append(",\"x\":new Date(");
				data.append(year);
				data.append(",");
				data.append(month);
				data.append(",");
				data.append(day);
				data.append(",");
				data.append(hour);
				data.append(",");
				data.append(minute);
				data.append("),\"y\":");
				data.append((pir.active?1:0));
				data.append("},");	
			}
		}
		data.append("]}");
		data.append("]");
		//System.out.println(data);
		return data.toString();
	}

}
