package data;

import java.sql.Timestamp;

public class PIR extends PinInput {
	public final boolean val;
	public PIR(int pin_no, String value, String name, String sensor, Timestamp timestamp) {
		super(pin_no, value, name, sensor, timestamp,value.equals("1")?true:false);
		this.val=value.equals("1");
		//System.out.println("PIR Sensor pin "+this.pin_no+" "+this.value+"|"+this.active);
	}

	@Override
	public HTMLHelperIn getHelper(int uid) {
		return new PIRHelper(uid,this.pin_no,this.val,this.name,this.sensor,this.timestamp,this.active);
	}
}
