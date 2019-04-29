package data;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

public class PIR extends PinInput {
	public PIR(int pin_no, String value, String name, String sensor, Timestamp timestamp) {
		super(pin_no, value, name, sensor, timestamp,value.equals("1")?true:false);
		System.out.println("PIR Sensor pin "+this.pin_no+" "+this.value+"|"+this.active);
	}

	@Override
	public String getData() {
		String resp="";
		//System.out.println("Sensor type |"+this.sensor+"|");
		if(this.sensor=="PIR")
		resp+="<p>"+this.pin_no+" Last movement detected by "+this.name+" on "+this.timestamp+"</p>";
	return resp;
	}

	@Override
	public String getGauge() {
		String data="";
		data+="<div onClick=\"inputpinlog("+this.pin_no+")\">";
		data+=this.active?"<i class=\"far fa-eye fa-7x pointer\"></i>":"<i class=\"far fa-eye-slash fa-7x pointer\"></i>";
		data+="</div>";
		data+="<br />";
		data+="<button class=\"btn "+(this.active?"btn-warning":"btn-secondary")+"\"  onclick=\"toggleinputpin("+this.pin_no+")\"> Turn "+(this.active?"off":"on")+" "+this.name+"</button>";
		data+="<br />";
		data+="<span class=\"badge badge-warning\">"+this.timestamp+"</span>";
		return data;
	}

	@Override
	public String drawGraph(int uid) {
		ServerData sd=new MySqlData(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		List<PinInput> logdata=sd.getPinInputLog(uid, this.pin_no,this.sensor);
		if(logdata==null) {
			System.out.println("logdatanull");
			return null;}
		String data="[";
		data+="{ \"type\" : \"scatter\" ,\"name\":\"Detect\" , \"showInLegend\" : true ,"
			+" \"dataPoints\" : [";
		for(PinInput pi:logdata)
		{PIR pir=(PIR)pi;
		if(pir.active)
		{Timestamp ts=pir.timestamp;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts.getTime());
		data+="{\"x\":new Date("+cal.get(Calendar.YEAR)+","+cal.get(Calendar.MONTH)+","+cal.get(Calendar.DAY_OF_MONTH)+","+cal.get(Calendar.HOUR_OF_DAY)+","+cal.get(Calendar.MINUTE)+"),\"y\":"+(pir.active?1:0)+"},";	
		}}
		//data+="\b";
		data+="]}";
		data+=",{\"type\":\"stepLine\",\"name\":\"Active\",\"axisYType\": \"secondary\",\"showInLegend\": true,\"markerSize\": 0,"
				+"\"dataPoints\": [";
		for(PinInput pi:logdata){
			PIR pir=(PIR)pi;
			Timestamp ts=pir.timestamp;
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(ts.getTime());
			data+="{\"x\":new Date("+cal.get(Calendar.YEAR)+","+cal.get(Calendar.MONTH)+","+cal.get(Calendar.DAY_OF_MONTH)+","+cal.get(Calendar.HOUR_OF_DAY)+","+cal.get(Calendar.MINUTE)+"),\"y\":"+(pir.active?1:0)+"},";
			}
			
			//data+="\b";
		
		data+="]}";
		data+="]";
		System.out.println(data);
		return data;
	}

}
