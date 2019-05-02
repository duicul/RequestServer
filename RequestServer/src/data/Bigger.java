package data;

public class Bigger implements Operator {
    private double val;
	public Bigger(double val){
		this.val=val;}
	
	@Override
	public boolean test(double value) {
		return value>this.val;
	}

}
