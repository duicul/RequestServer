package data;


public abstract class ConditionIn extends Condition {
	public final int pin_in;
	
	public ConditionIn(int cid,int pin_in,int pin_out,boolean val,int uid) {
		super(pin_out,val,cid,uid);
		this.pin_in=pin_in;}
	
	public static ConditionIn create(String sensor,String cond,int pin_in,int pin_out,boolean val,int cid,int uid){
		try{
			if(sensor.contentEquals("DHT11")||sensor.contentEquals("DHT22")) {
				String conds[]=cond.split(" ");
				//System.out.println(conds.length);
				/*for(String s:conds)
					System.out.println(s);*/
				if(cond.length()==0||conds.length==0||(conds[0].length()==0&&conds[1].length()==0))
					return new DHTCondition(null,null,pin_in,pin_out,val,cid,uid);
				else 
					if(conds[0].length()==0)
						return new DHTCondition(null,conds[1],pin_in,pin_out,val,cid,uid);
					else 
						if(conds.length==1||conds[1].length()==0)
							return new DHTCondition(conds[0],null,pin_in,pin_out,val,cid,uid);
						else 
							return new DHTCondition(conds[0],conds[1],pin_in,pin_out,val,cid,uid);
			}
			if(sensor.contentEquals("PIR")&&new InputPinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass).getTopPinInputLog(uid, pin_in)!=null)
				return new PIRCondition(Integer.parseInt(cond),pin_in,pin_out,val,cid,uid);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;}
		return null;
	}
	
	public boolean test(String value) {
		OutputPinData sdout=new OutputPinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		
		PinOutput po=sdout.getOutputPinbyPin_no(this.pin_out,uid);
		if(po==null)
			return false;
		boolean val=this.eval(value);
		if(val) {
			if(po.value!=this.getValue()) {
				sdout.updateOutputPin(this.pin_out,this.val, uid);
				//System.out.println(c.test(value)+" Change value "+c.getOutputPin()+" ->"+c.getValue());
			}
		}
		return true;
	}
	public boolean test() {
		InputPinData sdout=new InputPinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass);
		Pin pinInput=sdout.getIntputPinbyPin_no(this.pin_in,this.uid);
		if(pinInput==null)
			return false;
		return this.test(((PinInput)pinInput).value);
	}
	protected abstract boolean eval(String s);
}
