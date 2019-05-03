package data;


public abstract class Condition {
	public final int pin_in,pin_out,cid;
	public final boolean val;
	
	public Condition(int cid,int pin_in,int pin_out,boolean val) {
		this.cid=cid;
		this.pin_in=pin_in;
		this.pin_out=pin_out;
		this.val=val;}
	
	public static Condition create(String sensor,String cond,int pin_in,int pin_out,boolean val,int cid,int uid){
		try{
			if(sensor.contentEquals("DHT11")||sensor.contentEquals("DHT22")) {
				String conds[]=cond.split(" ");
				//System.out.println(conds.length);
				/*for(String s:conds)
					System.out.println(s);*/
				if(cond.length()==0||conds.length==0||(conds[0].length()==0&&conds[1].length()==0))
					return new DHTCondition(null,null,pin_in,pin_out,val,cid);
				else 
					if(conds[0].length()==0)
						return new DHTCondition(null,conds[1],pin_in,pin_out,val,cid);
					else 
						if(conds.length==1||conds[1].length()==0)
							return new DHTCondition(conds[0],null,pin_in,pin_out,val,cid);
						else 
							return new DHTCondition(conds[0],conds[1],pin_in,pin_out,val,cid);
			}
			if(sensor.contentEquals("PIR"))
				return new PIRCondition(Integer.parseInt(cond),pin_in,pin_out,val,cid,uid);
		}
		catch(Exception e) {
			return null;}
		return null;
	}
	
	public abstract boolean test(String args);
}
