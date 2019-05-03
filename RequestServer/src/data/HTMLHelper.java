package data;

import java.sql.Timestamp;

public abstract class HTMLHelper {
	public final int pin_no;
	public final Timestamp timestamp;
	public final String sensor;
	public final String name;
	public final boolean active;
	public final int uid;
	
	public HTMLHelper(int uid,int pin_no,String name,String sensor,Timestamp timestamp,boolean active) {
		this.pin_no=pin_no;
		this.sensor=sensor;
		this.timestamp=timestamp;
		this.name=name;
		this.uid=uid;
		this.active=active;	
	}
	
	public abstract String getData();

	public abstract String getGauge();

	public abstract String drawGraph();
	
	public abstract String getConditionList();
	
	public abstract String getConditionForm();
}
