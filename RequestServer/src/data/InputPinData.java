package data;

import java.util.List;

public interface InputPinData {
	public PinInput getIntputPinbyPin_no(int pin_no,int uid);
	public void insertInputPin(int pin_no,String value,String name,String sensor,int uid);
	public void insertInputPinNoLog(int pin_no,String value,String name,String sensor,int uid);
	public void addInputPinLog(int pin_no,String value,String name,String sensor,int uid);
	public void updateInputPinValueLogtimestamp(int pin_no,String value,int uid);
	public void updateInputPinValueNoLogtimestamp(int pin_no,String value,int uid);
	public void updateInputPinValueLogNotimestamp(int pin_no,String value,int uid);
	public void updateInputPinValueNoLogNotimestamp(int pin_no,String value,int uid);
	public void removeInputPinbyPin_no(int pin_no,int uid);
	public List<PinInput> getPinsInput(int uid);
	public List<PinInput> getPinInputLog(int uid,int pin_no,String sensor);
	public List<PinInput> getTopPinInputLogSensors(int uid,List<String> sensor);
	public PinInput getTopPinInputLog(int uid,int pin_no);
}
