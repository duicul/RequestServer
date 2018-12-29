package data;

import java.sql.Timestamp;

public class PinInput {
public final int pin_no;
public final Timestamp timestamp;
public final float value;
public final String sensor;
public PinInput(int pin_no,float value,String sensor,Timestamp timestamp)
{this.pin_no=pin_no;
this.value=value;
this.sensor=sensor;
this.timestamp=timestamp;}

public String toString()
{return this.pin_no+" "+this.sensor+" "+this.value+" "+this.timestamp;}
}
