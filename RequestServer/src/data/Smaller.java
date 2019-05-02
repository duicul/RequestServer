package data;

public class Smaller implements Operator {
	private double val;
	public Smaller(double val) {
		this.val=val;}
	
	@Override
	public boolean test(double value) {
		return value<this.val;
	}

}
