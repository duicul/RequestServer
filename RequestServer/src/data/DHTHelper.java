package data;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

public class DHTHelper extends HTMLHelperIn{
	public final Double temp,humid;
	
	public DHTHelper(int uid, int pin_no, double humid,double temp, String name, String sensor, Timestamp timestamp) {
		super(uid, pin_no,name, sensor, timestamp,true);
		this.temp=temp;
		this.humid=humid;
	}

	@Override
	public String getData() {
		String resp="";
		resp+="<p>"+this.pin_no+" "+this.name+" "+this.sensor+" Temperature : "+temp+"C Humidity : "+humid+"% "+this.timestamp+"</p>";
		return resp;
	}
	
	@Override
	public String getGauge() {
		StringBuilder temp = new StringBuilder();
		temp.append("<div class=\"row\">");
		temp.append("<div class=\"col center\">");
		temp.append(this.name+" Pin "+this.pin_no);
		temp.append("</div>");
		temp.append("</div>");
		//System.out.println(temp);
		//temp.append("<div class=\"row\">");
		
		//temp.append("<div class=\"col center\">");
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
		//temp.append("</div>");
		
		StringBuilder hum  = new StringBuilder("");//<div class=\"col center\">");
		hum.append("<canvas data-type=\"radial-gauge\" class=\"pointer\" onClick=\"inputpinlog(");
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
		//hum.append("</div>");
		
		//hum.append("</div>");
	
		hum.append("<div class=\"row\">");
		hum.append("<div class=\"col center\">");
		hum.append("<i class=\"fas fa-clipboard-list fa-2x pointer\" onclick=\"showcondition(");
		hum.append(this.pin_no);
		hum.append(")\">Conditions</i>");
		hum.append("</div>");
		hum.append("</div>");
		
		hum.append("<div class=\"row\">");
		hum.append("<div class=\"col center\">");
		hum.append("<button class=\"btn btn-danger\" onclick=removeinputpin("+this.pin_no+")>Remove "+this.name+"</button>");
		hum.append("</div>");
		hum.append("</div>");

		return temp+" "+hum;
	}
	
	@Override
	public String drawGraph() {
		InputPinData sd=new InputPinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		List<Pin> logdata=sd.getPinInputLog(this.uid, this.pin_no,this.sensor);
		if(logdata==null) {
			System.out.println("logdatanull");
			return null;}
		StringBuilder data=new StringBuilder("["),aux=new StringBuilder();
		data.append("{ \"type\" : \"line\" ,\"name\":\"Humidity [%]\", \"axisYType\" : \"secondary\" , \"showInLegend\" : true , \"markerSize\" : 0 ,");
		data.append("\"toolTipContent\": \"{day}.{month}.{year} {hour}:{minute} {y} %\", ");
		data.append(" \"dataPoints\" : [");
		for(Pin pi:logdata)
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

	
	
	@Override
	public String getConditionForm() {
		OutputPinData sdout=new OutputPinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		StringBuilder sb=new StringBuilder();
		sb.append("<div class=\"row\">");
		
		sb.append("<div class=\"col\">");
		sb.append("Temperature <select id=\"temp_cond_op\" onchange=\"displaycondition_none()\">");
		sb.append("<option value=\"\">none</option>");
		sb.append("<option value=\"<\"><</option>");
		sb.append("<option value=\">\">></option>");
		sb.append("</select>");
		sb.append("<input type=\"text\" placeholder=\"Temperature value\" id=\"temp_cond_val\"/>");;
		//sb.append("</div>");

		//sb.append("<div class=\"col\">");
		sb.append("Humidity <select id=\"hum_cond_op\" onchange=\"displaycondition_none()\">");
		sb.append("<option value=\"\">none</option>");
		sb.append("<option value=\"<\"><</option>");
		sb.append("<option value=\">\">></option>");
		sb.append("</select>");
		sb.append("<input type=\"text\" placeholder=\"Humidity value\" id=\"hum_cond_val\"/>");
		sb.append("</div>");
			
		sb.append("<div class=\"col\">PinNo <br/>");
		sb.append("<select id=\"cond_pin_out_no\">");
		List<PinOutput> lpo = sdout.getPinsOutput(this.uid);
		for(PinOutput po:lpo)
			sb.append("<option value=\""+po.pin_no+"\">"+po.name+"</option>");
		sb.append("</select>");
		sb.append("</div>");
						
		sb.append("<div class=\"col\">Value <br/>");
		sb.append("<select id=\"cond_pin_out_val\">");
		sb.append("<option value=\"0\">0</option>");
		sb.append("<option value=\"1\">1</option>");
		sb.append("</select>");
		sb.append("</div>");	
						
		sb.append("<div class=\"col\">");
		sb.append("<input type=\"button\" class=\"btn btn-success\" onclick=\"addcondition("+pin_no+",'"+sensor+"')\" value=\"AddCondition\"></button>");	
		sb.append("</div>");
							
		sb.append("</div>");
		
		return sb.toString();
	}

	@Override
	public String getConditionList() {
		ConditionData sdcon=new ConditionMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		PinData sdpin=new PinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		StringBuilder sb=new StringBuilder();
		List<Condition> lc=sdcon.loadConditions(this.uid, pin_no, sensor);
		for(Condition c:lc) {
			Pin p=sdpin.getPin(((ConditionIn)c).pin_in, this.uid);
			if(p!=null) {
				//System.out.println("Cond List "+c);
				sb.append("<div class=\"row\">");
				sb.append("<div class=\"col center\">");
				sb.append(c);
				sb.append("</div>");
				sb.append("<div class=\"col center\">");
				sb.append(" "+p.name);
				sb.append("</div>");
				sb.append("<div class=\"col center\">");
				sb.append("<button class=\"btn btn-danger\" onclick=\"removecondition("+c.cid+","+pin_no+")\">"+"Remove "+p.name+"</button>");
				sb.append("</div>");
				sb.append("</div>");
				sb.append("<div class=\"row\">");
				sb.append("<div class=\"col center\">");
				sb.append("</div>");
				sb.append("</div>");
			}
		}
		return sb.toString();
	}

}
