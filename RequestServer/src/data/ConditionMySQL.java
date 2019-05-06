package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;

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
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			String querry="select ip.sensor,cp.cond,p.Pin_No,p1.Pin_No,cp.val,cp.cid,p.uid from pins p inner join in_pins ip on ip.pid=p.pid inner join condition_pins cp on cp.ipid=ip.ipid inner join out_pins op on op.opid=cp.opid inner join pins p1 on p1.pid=op.pid where p.Pin_No="+sensor_pin_no+" and ip.Sensor='"+sensor+"' and p.uid="+uid;
			//String querry="select p.Pin_No,cip.condition,cip.value,cip.cond_id,cip.pin_in from out_pins op inner join (select cp.opid,cp.cond AS condition,cp.val AS value,cp.cid AS cond_id,p.Pin_No AS pin_in from pins p inner join in_pins ip on ip.pid=p.pid inner join condition_pins cp on cp.ipid=ip.ipid where p.Pin_No="+sensor_pin_no+" and ip.Sensor='"+sensor+"' and p.uid="+uid+") cip on cip.cp.opid=op.opid inner join pins p on p.pid=op.pid where p.uid="+uid;
			//System.out.println(querry);
			ResultSet rs=stmt.executeQuery(querry);  	
			while(rs.next()){
				Condition c=ConditionIn.create(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getBoolean(5),rs.getInt(6),rs.getInt(7));
				//Condition c=ConditionIn.create(sensor,rs.getString(2),rs.getInt(5),rs.getInt(1),rs.getBoolean(3),rs.getInt(4),uid);
				
				if(c!=null) {//System.out.println(c);
						conditions.add(c);}
				else {this.removeConditionIn(rs.getInt(6));System.out.println("Remove Condition "+rs.getInt(6));}
			}
				con.close();}
		catch(Exception e){
			System.out.println(e);} 
		return conditions;
	}

	@Override
	public void removeConditionIn(int cid) {
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("delete con from condition_pins con where cid="+cid);
			con.close();  
		}
		catch(Exception e){
			System.out.println(e);} 
		
	}

	@Override
	public void addConditionIn(int uid, int pin_in, int pin_out, String cond, boolean val) {
		if(new OutputPinMySQL(dbname,uname,pass).getOutputPinbyPin_no(pin_out, uid)==null)
			return;
		
		if(new InputPinMySQL(dbname,uname,pass).getIntputPinbyPin_no(pin_in, uid)==null)
			return;
		
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("INSERT INTO condition_pins (ipid , opid , cond , val ) VALUES ( (select ip.ipid from pins p inner join in_pins ip on p.pid=ip.pid where p.uid="+uid+" and p.Pin_no="+pin_in+"), (select op.opid from pins p inner join out_pins op on p.pid=op.pid where p.uid="+uid+" and p.Pin_no="+pin_out+"),'"+cond+"',"+val+") ");
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);}  		
	}
	
	@Override
	public void addConditionOut(int uid,int pin_out,Time start,Time end, boolean val) {
		if(new OutputPinMySQL(dbname,uname,pass).getOutputPinbyPin_no(pin_out, uid)==null)
			return;
		
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("INSERT INTO condition_out (opid,start,end,val) VALUES ( (select op.opid from pins p inner join out_pins op on p.pid=op.pid where p.uid="+uid+" and p.Pin_no="+pin_out+"),'"+start+"','"+end+"',"+val+") ");
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);}  		
	}

	@Override
	public List<Condition> loadConditions(int uid, int pin_no) {
		List<Condition> conditions=new ArrayList<Condition>();
		int pin_out=-1,cid=-1;
		Time ts_start,ts_end;
		boolean value;
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			String querry="select p.pin_no,co.val,co.start,co.end,co.cid from pins p inner join out_pins op on p.pid=op.pid inner join condition_out co on op.opid=co.opid where p.Pin_No="+pin_no+" and p.uid="+uid;
			//System.out.println(querry);
			ResultSet rs=stmt.executeQuery(querry);  	
			while(rs.next()){
			pin_out=rs.getInt(1);
			value=rs.getBoolean(2);
			ts_start=rs.getTime(3);
			ts_end=rs.getTime(4);
			cid=rs.getInt(5);
			conditions.add(new ConditionOut(ts_start,ts_end,value,pin_out,cid,uid));}
			con.close();  
		}
		catch(Exception e){
			System.out.println(e);}  
		
		return conditions;
	}

	@Override
	public void removeConditionOut(int cid) {
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("delete con from condition_out con where cid="+cid);
			con.close();  
		}
		catch(Exception e){
			System.out.println(e);} 
		
	}

}
