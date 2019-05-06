package data;

import java.sql.Time;
import java.util.List;

public interface ConditionData {
	public List<Condition> loadConditions(int uid,int pin_no,String sensor);
	public List<Condition> loadConditions(int uid,int pin_no);
	public void removeConditionIn(int cid);
	public void removeConditionOut(int cid);
	public void addConditionIn(int uid,int pin_in,int pin_out,String cond,boolean val);
	public void addConditionOut(int uid,int pin_out,Time start,Time end,boolean val);
}
