package data;

public abstract class Pin {
public final int pin_no;
public final String type,name;
public Pin(int pin_no,String type,String name)
{this.pin_no=pin_no;
this.type=type;
this.name=name;}

public abstract String toString();
//{return this.pin_no+" "+this.name+" "+this.type;}
 
public abstract HTMLHelper getHelper(int uid);

}
