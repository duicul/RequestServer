package data;

public abstract class Condition {
	
	public static Condition create(String sensor,String cond,int pin,boolean val){
		if(sensor.contentEquals("DHT11")||sensor.contentEquals("DHT22")) {
			String conds[]=cond.split(" ");
			//System.out.println(conds.length);
			/*for(String s:conds)
				System.out.println(s);*/
			if(cond.length()==0||conds.length==0||(conds[0].length()==0&&conds[1].length()==0))
				return new DHTCondition(null,null,pin,val);
			else 
				if(conds[0].length()==0)
					return new DHTCondition(null,conds[1],pin,val);
				else 
					if(conds.length==1||conds[1].length()==0)
						return new DHTCondition(conds[0],null,pin,val);
					else 
						return new DHTCondition(conds[0],conds[1],pin,val);
		}
		return null;
	
	}
}
