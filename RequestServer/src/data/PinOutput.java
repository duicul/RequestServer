package data;

public class PinOutput extends Pin{
	public final boolean value;
	public PinOutput(int pin_no,boolean value,String name)
	{super(pin_no,"OUT",name);
	this.value=value;}
	
	public String toString()
	{return this.pin_no+" "+this.value;}
	
	public HTMLHelper getHelper(int uid) {
		return new HTMLHelperOut(uid,this.pin_no,this.value,this.name);}
}
