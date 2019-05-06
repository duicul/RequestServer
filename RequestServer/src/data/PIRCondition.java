package data;

import java.sql.Timestamp;

public class PIRCondition extends ConditionIn {
	public final int interval;
	public final Timestamp ts;
	public PIRCondition(int interval, int pin_in,int pin_out, boolean val,int cid,int uid) {
		super(cid, pin_in,pin_out, val,uid);
		InputPinData sdin=new InputPinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		this.ts=((PinInput)sdin.getTopPinInputLog(uid, pin_in)).timestamp;
		this.interval=interval;}
	
	public String toString() {
			String cond="Last detected within "+interval+" minutes => "+pin_out+" "+(this.val?"ON":"OFF");;
			return cond;
	}
	
	@Override
	public boolean eval(String val) {
		try {System.out.println("PIR value: "+val);
			int active=Integer.parseInt(val);
			if(active==0)
				return true;
			long target=ts.getTime()+interval*1000*60;
			return System.currentTimeMillis()<target&&System.currentTimeMillis()>ts.getTime();
		}catch(Exception e) {
			return false;}
	}

	@Override
	public int getOutputPin() {
		return this.pin_out;
	}

	@Override
	public boolean getValue() {
		return this.val;}

}
