package data;

import java.util.List;

public interface ConditionData {
	public List<Condition> loadConditions(int uid,int pin_no,String sensor);
	public void removeCondition(int uid,int cid);
	public void addCondition(int uid,int pin_in,int pin_out,String cond,boolean val);	
}
