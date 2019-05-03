package data;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

public class PIRHelper extends HTMLHelper {
	public final boolean value;
	public PIRHelper(int uid, int pin_no, boolean value, String name, String sensor, Timestamp timestamp,boolean active) {
		super(uid, pin_no, name, sensor, timestamp,active);
		this.value=value;
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
		data.append("<br/>");
		data.append("<i class=\"fas fa-clipboard-list fa-2x pointer\" onclick=\"showcondition(");
		data.append(this.pin_no);
		data.append(",'");
		data.append(this.sensor);
		data.append("')\">Conditions</i>");
		data.append("<br />");
		return data.toString();
	}

	@Override
	public String drawGraph() {
		InputPinData sd=new InputPinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
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

	@Override
	public String getConditionList() {
		ConditionData sdcon=new ConditionMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		PinData sdpin=new PinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		StringBuilder sb=new StringBuilder();
		List<Condition> lc=sdcon.loadConditions(this.uid, pin_no, sensor);
		for(Condition c:lc) {
			Pin p=sdpin.getPin(c.pin_in, this.uid);
			if(p!=null) {
				//System.out.println("Cond List "+c);
				sb.append("<div class=\"row\">");
				sb.append("<div class=\"col\">");
				sb.append(c);
				sb.append("</div>");
				sb.append("<div class=\"col\">");
				sb.append(" "+p.name);
				sb.append("</div>");
				sb.append("<div class=\"col\">");
				sb.append("<button class=\"btn btn-danger\" onclick=\"removecondition("+c.cid+","+pin_no+",'"+sensor+"')\">"+"Remove "+p.name+"</button>");
				sb.append("</div>");
				sb.append("</div>");
				sb.append("<div class=\"row\">");
				sb.append("<div class=\"col\">");
				sb.append("</div>");
				sb.append("</div>");
			}
		}
		return sb.toString();
	}

	@Override
	public String getConditionForm() {
		OutputPinData sdout=new OutputPinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		StringBuilder sb=new StringBuilder();
		sb.append("<div class=\"row\">");
		
		sb.append("<div class=\"col\">");
		sb.append("Last detected within :");
		sb.append("<input type=\"text\" placeholder=\"minutes\" id=\"pir_cond_val\"/>");;
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

}
