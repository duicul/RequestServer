package data;

public class DHTCondition extends Condition {
	private char cond_humid,cond_temp;
	private double val_humid,val_temp;
	int pin;
	boolean val;
		
	public DHTCondition(String cond_temp,String cond_humid,int pin,boolean val) {
		if(cond_humid!=null) {
			this.cond_humid=cond_humid.charAt(0);
			this.val_humid=Double.parseDouble(cond_humid.substring(1));
		}
		else this.cond_humid='\0';
		
		if(cond_temp!=null) {
			this.cond_temp=cond_temp.charAt(0);
			this.val_temp=Double.parseDouble(cond_temp.substring(1));
		}
		else this.cond_temp='\0';
		
		this.pin=pin;
		this.val=val;
		//System.out.println(this.cond_temp+""+this.val_temp+" "+this.cond_humid+""+this.val_humid);
	}
	
	private boolean testcond_humid(double humid) {
		if(cond_humid=='\0')
			return true;
		else if(cond_humid=='<') {
			return humid<this.val_humid;
		}
		else if(cond_humid=='>') {
			return humid>this.val_humid;
		}
		else return false;
	}
	
	private boolean testcond_temp(double temp) {
		if(cond_temp=='\0')
			return true;
		else if(cond_temp=='<') {
			return temp<this.val_temp;
		}
		else if(cond_temp=='>') {
			return temp>this.val_temp;
		}
		else return false;
	}
	
	public boolean test(double temp,double humid) {
		return this.testcond_humid(humid)&&this.testcond_temp(temp);
	}
	
	public String toString() {
		String humid=this.cond_humid=='~'?"":(this.cond_humid+""+this.val_humid);
		String temp=this.cond_temp=='~'?"":(this.cond_temp+""+this.val_temp);
		return temp+" "+humid;}
}
