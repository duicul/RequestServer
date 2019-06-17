package data;

import java.util.List;

public interface OutputPinData {
	public PinOutput getOutputPinbyPin_no(int pin_no,int uid);
	public PinOutput getOutputPinbyPin_noUpdate(int pin_no,int uid);
	public PinOutputLog getTopPinOutputLog(int uid,int pin_no);
	public List<PinOutputLog> getPinOutputLog(int uid,int pin);
	public void tunonOutputPin(int pin_no,int uid);
	public void tunoffOutputPin(int pin_no,int uid);
	public void toggleOutputPin(int pin_no,int uid);
	public void updateOutputPin(int pin_no,boolean value,int uid);
	public void addOutputPinLog(int pin_no,int uid,boolean val);
	public void removeOutputPinbyPin_no(int pin_no,int uid);
	public void insertOutputPin(int pin_no,boolean value,String name,int uid);
	public List<PinOutput> getPinsOutput(int uid);
	public List<PinOutput> getPinsOutputChanged(int uid);
}
