package data;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DHT extends PinInput {
	
public final Double temp,humid;
	public DHT(int pin_no, String value,String name,String sensor, Timestamp timestamp) {
		super(pin_no, value, name,sensor, timestamp,true);
		List<Double> vals=Arrays.asList(this.value.split(" ")).stream()
					.filter(strval -> strval.length()>0).
					map(realval -> Double.parseDouble(realval)).collect(Collectors.toList());
		if(vals.size()>=2)
		{this.temp=vals.get(0);
		this.humid=vals.get(1);}
		else {
			this.temp=0.0;
			this.humid=0.0;
		}
	}

	public String toString()
	{return this.pin_no+" "+this.sensor+" "+this.value+" "+this.timestamp;}

	@Override
	public HTMLHelperIn getHelper(int uid) {
		return new DHTHelper(uid,this.pin_no,this.humid,this.temp,this.name,this.sensor,this.timestamp);}

}
