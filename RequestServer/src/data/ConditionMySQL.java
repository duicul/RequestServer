package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConditionMySQL implements ConditionData{
	private String dbname,driver,uname,pass;
	public ConditionMySQL(String dbname, String uname, String pass) {
		this.dbname = dbname;
		this.uname = uname;
		this.pass = pass;
		this.driver="com.mysql.cj.jdbc.Driver";
	}

	@Override
	public List<Condition> loadConditions(int uid, int sensor_pin_no, String sensor) {
		List<Condition> conditions=new ArrayList<Condition>();
		int pin_num=-1;
		boolean value;
		String cond="";
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			String querry="select op.Pin_No , cp.cond , cp.val from condition_pins cp,in_pins ip,out_pins op where ip.Pin_No=cp.pin_in and op.Pin_No=cp.pin_out and op.uid=ip.uid and cp.pin_in="+sensor_pin_no+" and ip.Sensor='"+sensor+"' and cp.uid="+uid;
			//System.out.println(querry);
			ResultSet rs=stmt.executeQuery(querry);  	
			while(rs.next()){
			pin_num=rs.getInt(1);
			cond=rs.getString(2);
			value=rs.getBoolean(3);		
			conditions.add(Condition.create(sensor,cond,pin_num,value));}
			con.close();  
		}
		catch(Exception e){
			System.out.println(e);return null;}  
		
		return conditions;
	}

	@Override
	public void removeCondition(int uid, int cid) {
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("delete con from condition_pins con where uid="+uid+" and cid="+cid);
			con.close();  
		}
		catch(Exception e){
			System.out.println(e);} 
		
	}

	@Override
	public void addCondition(int uid, int pin_in, int pin_out, String cond, boolean val) {
		if(new InputPinMySQL(dbname,uname,pass).getIntputPinbyPin_no(pin_in, uid)==null)
			return;
		
		if(new OutputPinMySQL(dbname,uname,pass).getOutputPinbyPin_no(pin_out, uid)==null)
			return;
		
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("INSERT INTO condition_pins (uid, pin_in , pin_out , cond , val ) VALUES ("+uid+","+pin_in+","+pin_out+",'"+cond+"',"+val+") ");
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);}  		
	}

}
