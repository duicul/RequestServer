package data;

import java.util.List;

public interface OutputPinData {
	public PinOutput getOutputPinbyPin_no(int pin_no,int uid);
	public PinOutput getOutputPinbyPin_noUpdate(int pin_no,int uid);
	public void tunonOutputPin(int pin_no,int uid);
	public void tunoffOutputPin(int pin_no,int uid);
	public void toggleOutputPin(int pin_no,int uid);
	public void updateOutputPin(int pin_no,int value,int uid);
	public void removeOutputPinbyPin_no(int pin_no,int uid);
	public void insertOutputPin(int pin_no,int value,String name,int uid);
	public List<PinOutput> getPinsOutput(int uid);
	public List<PinOutput> getPinsOutputChanged(boolean update,int uid);
}
