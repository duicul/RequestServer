package data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DHTCondition extends Condition {
	public final char cond_humid,cond_temp;
	public final double val_humid,val_temp;
		
	public DHTCondition(String cond_temp,String cond_humid,int pin_in,int pin_out,boolean val,int cid) {
		super(cid,pin_in,pin_out,val);
		if(cond_humid!=null) {
			this.cond_humid=cond_humid.charAt(0);
			this.val_humid=Double.parseDouble(cond_humid.substring(1));
		}
		else {
			this.cond_humid='\0';
			this.val_humid=0;
		}
		
		if(cond_temp!=null) {
			this.cond_temp=cond_temp.charAt(0);
			this.val_temp=Double.parseDouble(cond_temp.substring(1));
		}
		else {
			this.cond_temp='\0';
			this.val_temp=0;
		}

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
	
	public String toString() {
		String humid=this.cond_humid=='\0'?"":("Temperature "+this.cond_humid+""+this.val_humid);
		String temp=this.cond_temp=='\0'?"":(" Humidity "+this.cond_temp+""+this.val_temp);
		return temp+" "+humid+" => "+this.pin_out+" "+(this.val?"ON":"OFF");}

	@Override
	public boolean test(String args) {
		List<Double> vals=Arrays.asList(args.split(" ")).stream().filter(strval -> strval.length()>0).map(realval -> Double.parseDouble(realval)).collect(Collectors.toList());
		if(vals.size()!=2)
			return false;
		return this.testcond_temp(vals.get(0))&&this.testcond_humid(vals.get(1));
	}
}
