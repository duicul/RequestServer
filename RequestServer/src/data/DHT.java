package data;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class DHT extends PinInput {
public final Double temp,humid;
	public DHT(int pin_no, String value,String name,String sensor, Timestamp timestamp) {
		super(pin_no, value, name,sensor, timestamp,true);
		List<Double> vals=Arrays.asList(this.value.split(" ")).stream().filter(strval -> strval.length()>0).map(realval -> Double.parseDouble(realval)).collect(Collectors.toList());
		this.temp=vals.get(0);
		this.humid=vals.get(1);
	}

	public String toString()
	{return this.pin_no+" "+this.sensor+" "+this.value+" "+this.timestamp;}

	@Override
	public String getData() {
		String resp="";
		resp+="<p>"+this.pin_no+" "+this.name+" "+this.sensor+" Temperature : "+temp+"C Humidity : "+humid+"% "+this.timestamp+"</p>";
		return resp;
	}

	@Override
	public String getGauge() {
		String temp = "<canvas data-type=\"radial-gauge\" class=\"pointer\" onClick=\"inputpinlog("+this.pin_no+")\""
				+ "data-major-ticks=\"-10,-5,0,5,10,15,20,25,30,35,40,45,50\""
				+" data-highlights='["
				+"{ \"from\": -10, \"to\": 0, \"color\": \"rgba(10,50,220,1)\"},"
				+"{ \"from\": 0, \"to\": 10, \"color\": \"rgba(0,150,150,1)\"},"
				+"{\"from\": 10, \"to\": 30, \"color\": \"rgba(10,200,20,1)\" },"
				+"{ \"from\": 30, \"to\": 40, \"color\": \"rgba(255,192,60,1)\"},"
				+"{ \"from\": 40, \"to\": 50, \"color\": \"rgba(225,25,0,1)\" }]'"
				+" data-color-numbers=\"red\" data-animated-value=\"true\" data-width=\"150\" data-height=\"150\""
				+" data-value=\""+this.temp+"\" data-min-value=\"-10\" data-max-value=\"50\" data-units=\"C\"></canvas>";
		String hum  = "<canvas data-type=\"radial-gauge\" class=\"pointer\" onClick=\"inputpinlog("+this.pin_no+")\""
				+" data-major-ticks=\"0,10,20,30,40,50,60,70,80,90,100\""
				+" data-highlights='[{\"from\": 0, \"to\": 30, \"color\":\"rgba(0,0,200,1)\" },"
				+"{\"from\": 30, \"to\": 70,\"color\":\"rgba(51,255,51,1)\" },"
				+"{\"from\": 70, \"to\": 100,\"color\":\"rgba(255,51,51,1)\"}]'"
				+"data-color-numbers=\"red\" data-animated-value=\"true\" data-width=\"150\" data-height=\"150\""
				+"data-value=\""+this.humid+"\" data-min-value=\"0\" data-max-value=\"100\" data-units=\"%\" ></canvas>";
	return temp+" "+hum;
	}

	@Override
	public String drawGraph(int uid) {
		ServerData sd=new MySqlData(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		List<PinInput> logdata=sd.getPinInputLog(uid, this.pin_no,this.sensor);
		if(logdata==null) {
			System.out.println("logdatanull");
			return null;}
		String data="[";
		data+="{ \"type\" : \"line\" ,\"name\":\"Humidity\", \"axisYType\" : \"secondary\" , \"showInLegend\" : true , \"markerSize\" : 0 ,"
			+" \"dataPoints\" : [";
		for(PinInput pi:logdata)
		{DHT d=(DHT)pi;
		Timestamp ts=d.timestamp;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts.getTime());
		data+="{\"x\":new Date("+cal.get(Calendar.YEAR)+","+cal.get(Calendar.MONTH)+","+cal.get(Calendar.DAY_OF_MONTH)+","+cal.get(Calendar.HOUR_OF_DAY)+","+cal.get(Calendar.MINUTE)+"),\"y\":"+d.humid+"},";	
		}
		//data+="\b";
		data+="]}";
		data+=",{\"type\":\"line\",\"name\":\"Temperature\",\"axisYType\": \"secondary\",\"showInLegend\": true,\"markerSize\": 0,"
				+"\"dataPoints\": [";
			for(PinInput pi:logdata)
			{DHT d=(DHT)pi;
			Timestamp ts=d.timestamp;
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(ts.getTime());
			data+="{\"x\":new Date("+cal.get(Calendar.YEAR)+","+cal.get(Calendar.MONTH)+","+cal.get(Calendar.DAY_OF_MONTH)+","+cal.get(Calendar.HOUR_OF_DAY)+","+cal.get(Calendar.MINUTE)+"),\"y\":"+d.temp+"},";
			}
			//data+="\b";
			data+="]}";
		data+="]";
		//System.out.println(data);
		return data;
	}
}
