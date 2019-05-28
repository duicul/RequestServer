package data;

public abstract class Condition {
	protected final int pin_out,cid,uid;
	protected final boolean val;
	public Condition(int pin_out,boolean val,int cid,int uid) {
		this.val=val;
		this.uid=uid;
		this.pin_out=pin_out;
		this.cid=cid;}
	public abstract int getOutputPin();
	public abstract boolean getValue();
	public abstract boolean test();
}