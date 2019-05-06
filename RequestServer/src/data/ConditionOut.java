package data;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;

public class ConditionOut extends Condition {
	private final Time ts_start,ts_end;
	
	public ConditionOut(Time ts1,Time ts2,boolean val,int pin_no,int cid,int uid){
		super(pin_no,val,cid,uid);
		this.ts_start=ts1;
		this.ts_end=ts2;}
	
	public boolean test() {
		boolean val=this.eval();
		OutputPinData sdout=new OutputPinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		PinOutput po=sdout.getOutputPinbyPin_no(this.pin_out,uid);
		if(po==null)
			return false;
		if(val) {
			if(po.value!=this.getValue()) {
				sdout.updateOutputPin(this.pin_out,this.getValue(),this.uid);
				System.out.println(val+" "+uid+" in interval Change value "+this.pin_out+" ->"+this.getValue());
				}
			}	
		return true;
		}

	private boolean eval() {
		Time ts=Time.valueOf(LocalTime.now());
		return this.ts_start.getTime()<ts.getTime()&&this.ts_end.getTime()>ts.getTime();
	}
	@Override
	public int getOutputPin() {
		return this.pin_out;}

	@Override
	public boolean getValue() {
		return this.val;}
	
	@Override
	public String toString() {
		String data=this.ts_start+" -> "+this.ts_end+" => "+this.pin_out+" "+(this.val?"ON":"OFF");
		return data;}

}
