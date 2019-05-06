package data;

import java.util.List;

public interface PinData {
public Pin getPin(int pin_no,int uid);
public void insertPin(int uid,int pin,String name,boolean out);
public void removePinByPin_no(int pin_no,int uid);
public List<Pin> getPins(int uid);
}
