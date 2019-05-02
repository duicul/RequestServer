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
		StringBuilder temp = new StringBuilder();
		temp.append("<canvas data-type=\"radial-gauge\" class=\"pointer\" onClick=\"inputpinlog(");
		temp.append(this.pin_no);
		temp.append(")\"");
		temp.append("data-major-ticks=\"-10,-5,0,5,10,15,20,25,30,35,40,45,50\"");
		temp.append(" data-highlights='[");
		temp.append("{ \"from\": -10, \"to\": 0, \"color\": \"rgba(10,50,220,1)\"},");
		temp.append("{ \"from\": 0, \"to\": 10, \"color\": \"rgba(0,150,150,1)\"},");
		temp.append("{\"from\": 10, \"to\": 30, \"color\": \"rgba(10,200,20,1)\" },");
		temp.append("{ \"from\": 30, \"to\": 40, \"color\": \"rgba(255,192,60,1)\"},");
		temp.append("{ \"from\": 40, \"to\": 50, \"color\": \"rgba(225,25,0,1)\" }]'");
		temp.append(" data-color-numbers=\"red\" data-animated-value=\"true\" data-width=\"150\" data-height=\"150\"");
		temp.append("data-value=\"");
		temp.append(this.temp);
		temp.append("\" data-min-value=\"-10\" data-max-value=\"50\" data-units=\"C\"></canvas>");
		StringBuilder hum  = new StringBuilder("<canvas data-type=\"radial-gauge\" class=\"pointer\" onClick=\"inputpinlog(");
		hum.append(this.pin_no);
		hum.append(")\"");
		hum.append(" data-major-ticks=\"0,10,20,30,40,50,60,70,80,90,100\"");
		hum.append(" data-highlights='[{\"from\": 0, \"to\": 30, \"color\":\"rgba(0,0,200,1)\" },");
		hum.append("{\"from\": 30, \"to\": 70,\"color\":\"rgba(51,255,51,1)\" },");
		hum.append("{\"from\": 70, \"to\": 100,\"color\":\"rgba(255,51,51,1)\"}]'");
		hum.append("data-color-numbers=\"red\" data-animated-value=\"true\" data-width=\"150\" data-height=\"150\"");
		hum.append("data-value=\"");
		hum.append(this.humid);
		hum.append("\" data-min-value=\"0\" data-max-value=\"100\" data-units=\"%\" ></canvas>");
	return temp+" "+hum;
	}

	@Override
	public String drawGraph(int uid) {
		ServerData sd=new MySqlData(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		List<PinInput> logdata=sd.getPinInputLog(uid, this.pin_no,this.sensor);
		if(logdata==null) {
			System.out.println("logdatanull");
			return null;}
		StringBuilder data=new StringBuilder("["),aux=new StringBuilder();
		data.append("{ \"type\" : \"line\" ,\"name\":\"Humidity [%]\", \"axisYType\" : \"secondary\" , \"showInLegend\" : true , \"markerSize\" : 0 ,");
		data.append("\"toolTipContent\": \"{day}.{month}.{year} {hour}:{minute} {y} %\", ");
		data.append(" \"dataPoints\" : [");
		for(PinInput pi:logdata)
		{DHT d=(DHT)pi;
		Timestamp ts=d.timestamp;
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
		data.append(d.humid);
		data.append("},");
		
		
		aux.append("{\"year\":");
		aux.append(year);
		aux.append(",\"month\":");
		aux.append(month+1);
		aux.append(",\"day\":");
		aux.append(day);
		aux.append(",\"hour\":");
		aux.append(hour);
		aux.append(",\"minute\":");
		aux.append(minute);
		aux.append(",\"x\":new Date(");
		aux.append(year);
		aux.append(",");
		aux.append(month);
		aux.append(",");
		aux.append(day);
		aux.append(",");
		aux.append(hour);
		aux.append(",");
		aux.append(minute);
		aux.append("),\"y\":");
		aux.append(d.temp);
		aux.append("},");
		}
		//data+="\b";
		data.append("]}");
		data.append(",{\"type\":\"line\",\"name\":\"Temperature [C]\",\"axisYType\": \"secondary\",\"showInLegend\": true,\"markerSize\": 0,");
		data.append("\"toolTipContent\": \"{day}.{month}.{year} {hour}:{minute} {y} C\", ");
		data.append("\"dataPoints\": [");
		data.append(aux);
			//data+="\b";
		data.append("]}");
		data.append("]");
		//System.out.println(data);
		return data.toString();
	}
}
