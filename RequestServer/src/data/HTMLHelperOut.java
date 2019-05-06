package data;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

public class HTMLHelperOut implements HTMLHelper {
	public final int uid,pin;
	public final boolean value;
	public final String name;
	public HTMLHelperOut(int uid,int pin,boolean value,String name) {
		this.uid=uid;
		this.pin=pin;
		this.value=value;
		this.name=name;}
	
	@Override
	public String getData() {
		return this.pin+" "+this.value;}

	@Override
	public String getGauge() {
		StringBuilder resp=new StringBuilder();
		resp.append("<div class=\"row\">");
		resp.append("<div class=\"col center\">"+this.pin+" "+this.name+"</div>");
		resp.append("</div>");
		
		resp.append("<div class=\"row\">");
		resp.append("<div class=\"col center\">"+"<button class=\"btn btn-success \" onclick=\"outputpinlog("+this.pin+")\"> Log "+this.name+"</button></div>");
		resp.append("</div>");
		
		resp.append("<div class=\"row\">");
		resp.append("<div class=\"col center\">"+"<button class=\"btn "+(!this.value?"btn-secondary":"btn-warning")+" \" onclick=\"togglepin("+this.pin+")\">"+(!this.value?"OFF":"ON")+"</button></div>");
		resp.append("</div>");
		
		resp.append("<div class=\"row\">");
		resp.append("<div class=\"col center\">"+"<i class=\"fas fa-clipboard-list fa-2x pointer\" onclick=\"showcondition(");
		resp.append(this.pin);
		resp.append(")\">Conditions</i></div>");
		resp.append("</div>");
		
		resp.append("<div class=\"row\">");
		resp.append("<div class=\"col center\">"+"<button class=\"btn btn-danger\" onclick=\"removeoutputpin("+this.pin+")\">"+"Remove "+this.name+"</button>"+"</div>");
		resp.append("</div>");
		
		
		
		return resp.toString();
	}

	@Override
	public String drawGraph() {
	OutputPinData sdout=new OutputPinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
	List<PinOutputLog> logdata=sdout.getPinOutputLog(this.uid,this.pin);
	if(logdata==null) {
		System.out.println("logdatanull");
		return null;}
	StringBuilder data=new StringBuilder("[");
	data.append("{ \"type\" : \"stepLine\" ,\"name\":\"Status\", \"axisYType\" : \"secondary\" , \"showInLegend\" : true , \"markerSize\" : 0 ,");
	data.append("\"toolTipContent\": \"{day}.{month}.{year} {hour}:{minute} {y} \", ");
	data.append(" \"dataPoints\" : [");
	for(PinOutputLog pol:logdata)
	{Timestamp ts=pol.ts;
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
	data.append(pol.value?1:0);
	data.append("},");}

	data.append("]}]");
	
	return data.toString();
	}

	@Override
	public String getConditionList() {
		System.out.println("Condition List out");
		ConditionData sdcon=new ConditionMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		StringBuilder sb=new StringBuilder();
		List<Condition> lc=sdcon.loadConditions(this.uid, pin);
		for(Condition c:lc) {
				sb.append("<div class=\"row\">");
				sb.append("<div class=\"col center\">");
				sb.append(c);
				sb.append("</div>");
				sb.append("<div class=\"col center\">");
				sb.append(" "+this.name);
				sb.append("</div>");
				sb.append("<div class=\"col center\">");
				sb.append("<button class=\"btn btn-danger\" onclick=\"removecondition("+c.cid+","+this.pin+")\">"+"Remove "+this.name+"</button>");
				sb.append("</div>");
				sb.append("</div>");
				sb.append("<div class=\"row\">");
				sb.append("<div class=\"col\">");
				sb.append("</div>");
				sb.append("</div>");
			}
		return sb.toString();
	}

	@Override
	public String getConditionForm() {
		System.out.println("Condition Form out");
		StringBuilder sb=new StringBuilder();
		sb.append("<div class=\"row\">");
		
		sb.append("<div class=\"col\">");
		sb.append("Time start <input type=\"time\" id=\"time_start_val\"/>");;
		sb.append("</div>");
		
		sb.append("<div class=\"col\">");
		sb.append("Time end <input type=\"time\" id=\"time_end_val\"/>");;
		sb.append("</div>");
		
		sb.append("<div class=\"col\">Value <br/>");
		sb.append("<select id=\"cond_pin_out_val\">");
		sb.append("<option value=\"0\">0</option>");
		sb.append("<option value=\"1\">1</option>");
		sb.append("</select>");
		sb.append("</div>");	
						
		sb.append("<div class=\"col\">");
		sb.append("<input type=\"button\" class=\"btn btn-success\" onclick=\"addconditionout("+this.pin+")\" value=\"AddCondition\"></button>");	
		sb.append("</div>");
							
		sb.append("</div>");
		
		return sb.toString();
	}
	
}
