package data;

import java.sql.Timestamp;

public class PinOutputLog extends PinOutput {
	public final Timestamp ts;
	public PinOutputLog(int pin_no, boolean value, String name,Timestamp ts) {
		super(pin_no, value, name);
		this.ts=ts;}

}
