package data;

public class PinOutput {
	public final int pin_no;
	public final int value;
	public PinOutput(int pin_no,int value)
	{this.pin_no=pin_no;
	this.value=value;}
	
	public String toString()
	{return this.pin_no+" "+this.value;}
}
