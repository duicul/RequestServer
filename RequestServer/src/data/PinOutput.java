package data;

public class PinOutput {
	public final int pin_no;
	public final boolean value;
	public final String name;
	public PinOutput(int pin_no,boolean value,String name)
	{this.pin_no=pin_no;
	this.value=value;
	this.name=name;}
	
	public String toString()
	{return this.pin_no+" "+this.value;}
}
