package data;

import java.sql.Timestamp;

public class PIRCondition extends Condition {
	public final int interval,uid;
	public final Timestamp ts;
	public PIRCondition(int interval, int pin_in,int pin_out, boolean val,int cid,int uid) {
		super(cid, pin_in,pin_out, val);
		InputPinData sdin=new InputPinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		this.ts=sdin.getTopPinInputLog(uid, pin_in).timestamp;
		this.uid=uid;
		this.interval=interval;
		//System.out.println(this+" created");
		}
	
	public String toString() {
			String cond="Last detected within "+interval+" minutes => "+pin_out+" "+(this.val?"ON":"OFF");;
			return cond;
	}
	
	@Override
	public boolean test(String millis) {
		try {
			long curr_mil=Long.parseLong(millis);
			long target=ts.getTime()+interval*1000*60;
			return curr_mil<target&&curr_mil>ts.getTime();
		}catch(Exception e) {
			return false;}
	}

}
