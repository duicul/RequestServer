package data;

import java.util.List;

public interface ServerData {
public Pin getPin(int pin_no,int uid);
public PinOutput getOutputPinbyPin_no(int pin_no,int uid);
public PinOutput getOutputPinbyPin_noUpdate(int pin_no,int uid);
public PinInput getIntputPinbyPin_no(int pin_no,int uid);
public void insertInputPin(int pin_no,float value,String name,String sensor,int uid);
public void insertOutputPin(int pin_no,int value,String name,int uid);
public void addInputPinLog(int pin_no,float value,String name,String sensor,int uid);
public void updateInputPinValue(int pin_no,float value,int uid);
public void tunonOutputPin(int pin_no,int uid);
public void tunoffOutputPin(int pin_no,int uid);
public void toggleOutputPin(int pin_no,int uid);
public void removeInputPinbyPin_no(int pin_no,int uid);
public void removeOutputPinbyPin_no(int pin_no,int uid);
public void removePinByPin_no(int pin_no,int uid);
public List<Pin> getPins(int uid);
public List<PinOutput> getPinsOutput(int uid);
public List<PinOutput> getPinsOutputChanged(boolean update,int uid);
public List<PinInput> getPinsInput(int uid);
public boolean signup(String user,String pass,String email,String adress,String phone,String info);
public User getUser(String user,String pass);

}
