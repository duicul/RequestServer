package data;

public class Pin {
public final int pin_no;
public final String type,name;
public Pin(int pin_no,String type,String name)
{this.pin_no=pin_no;
this.type=type;
this.name=name;}

public String toString()
{return this.pin_no+" "+this.name+" "+this.type;}
}
