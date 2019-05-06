package data;

import java.sql.Timestamp;

public abstract class PinInput extends Pin{
public final Timestamp timestamp;
public final String value;
public final String sensor;
public final boolean active;
public PinInput(int pin_no,String value,String name,String sensor,Timestamp timestamp,boolean active)
{super(pin_no,"IN",name);
this.value=value;
this.sensor=sensor;
this.timestamp=timestamp;
this.active=active;}

public String toString()
{return "Pin input :"+this.pin_no+" "+this.sensor+" "+this.value+" "+this.timestamp;}

/*public abstract String getData();

public abstract String getGauge();

public abstract String drawGraph(int uid);*/

public static PinInput create(int pin_no,String value,String name,String sensor,Timestamp timestamp) {
	System.out.println("create "+name);
	if(sensor.equals("DHT11")||sensor.equals("DHT22"))
	return new DHT(pin_no,value, name, sensor, timestamp);
	else if(sensor.equals("PIR"))
		return new PIR(pin_no,value,name, sensor, timestamp);
	else return null;
}
public abstract HTMLHelper getHelper(int uid);
}
