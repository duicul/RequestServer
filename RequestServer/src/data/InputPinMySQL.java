package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class InputPinMySQL implements InputPinData {
	private String dbname,driver,uname,pass;
	public InputPinMySQL(String dbname, String uname, String pass) {
		this.dbname = dbname;
		this.uname = uname;
		this.pass = pass;
		this.driver="com.mysql.cj.jdbc.Driver";
	}
	
	@Override
	public PinInput getIntputPinbyPin_no(int pin_no,int uid) {
		int pin_num=-1;
		Timestamp timestamp=null;
		String value="";
		String sensor = "";
		String name="";
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select ip.Pin_No, ip.Value , ip.Sensor,ip.TimeStamp,p.NAME from in_pins ip , pins p  where p.Pin_No=ip.Pin_No and ip.Pin_No="+pin_no+" and ip.uid="+uid);  
			if(rs.next())  
			{
			pin_num=rs.getInt(1);
			value=rs.getString(2);
			sensor=rs.getString(3);
			timestamp=rs.getTimestamp(4);
			name=rs.getString(5);
				//System.out.println(pin_num+" "+value+" "+sensor+" "+timestamp);  
			}
			else {con.close();return null;}
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}  
		
		return PinInput.create(pin_num,value,name,sensor,timestamp);
	}

	@Override
	public void insertInputPin(int pin_no, String value, String name, String sensor,int uid) {
		this.insertInputPinNoLog(pin_no, value, name, sensor, uid);
		this.addInputPinLog(pin_no, value, name, sensor, uid);
	}

	@Override
	public void insertInputPinNoLog(int pin_no, String value, String name, String sensor, int uid) {
		new PinMySQL(this.dbname, this.uname, this.pass).removePinByPin_no(pin_no,uid);
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("INSERT INTO pins (uid, Pin_No , TYPE , NAME ) VALUES ("+uid+","+pin_no+",'IN','"+name+"') ");
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);}  
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("INSERT INTO in_pins ( uid,Pin_No , Value , Sensor ) VALUES ("+uid+","+pin_no+",'"+value+"','"+sensor+"') ");
			con.close();   
			}catch(Exception e)
		{ System.out.println(e);}	
	}

	@Override
	public void addInputPinLog(int pin_no, String value, String name, String sensor, int uid) {
		//System.out.println("Add input pin log");
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("INSERT INTO in_pins_log ( uid , Pin_No , name , Value , Sensor ) VALUES ("+uid+","+pin_no+",'"+name+"','"+value+"','"+sensor+"') ");
			con.close();   
			}catch(Exception e)
		{ System.out.println(e);}
	}

	@Override
	public void updateInputPinValueLogtimestamp(int pin_no, String value,int uid) {
		this.updateInputPinValueNoLogtimestamp(pin_no, value, uid);
		Pin p=new PinMySQL(this.dbname, this.uname, this.pass).getPin(pin_no, uid);
		PinInput pi=this.getIntputPinbyPin_no(pin_no, uid);
		this.addInputPinLog(pin_no, value, p.name, pi.sensor, uid);
	}

	@Override
	public void updateInputPinValueNoLogtimestamp(int pin_no, String value,int uid) {
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("UPDATE in_pins SET value='"+value+"' , TimeStamp=CURRENT_TIMESTAMP WHERE Pin_No="+pin_no+" and uid="+uid);
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);} 
	}

	public void updateInputPinValueLogNotimestamp(int pin_no, String value,int uid) {
		this.updateInputPinValueNoLogNotimestamp(pin_no, value, uid);
		Pin p=new PinMySQL(this.dbname, this.uname, this.pass).getPin(pin_no, uid);
		PinInput pi=this.getIntputPinbyPin_no(pin_no, uid);
		this.addInputPinLog(pin_no, value, p.name, pi.sensor, uid);
	}

	@Override
	public void updateInputPinValueNoLogNotimestamp(int pin_no, String value,int uid) {
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("UPDATE in_pins SET value='"+value+"' WHERE Pin_No="+pin_no+" and uid="+uid);
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);} 
	}

	@Override
	public void removeInputPinbyPin_no(int pin_no,int uid) {
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass); 
			Statement stmt=con.createStatement(); 
			String comm="delete p,ip from in_pins ip inner join pins p on p.Pin_No=ip.Pin_No where p.type='IN' AND p.Pin_No="+pin_no+" AND p.uid="+uid+" AND ip.uid="+uid;
			//System.out.println(comm);
			stmt.executeUpdate(comm);
			comm="delete ipl from in_pins_log ipl where ipl.Pin_No="+pin_no+" AND ipl.uid="+uid;
			//System.out.println(comm);
			stmt.executeUpdate(comm);
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);}  
	}

	@Override
	public List<PinInput> getPinsInput(int uid) {
		int pin_num=-1;
		Timestamp timestamp=null;
		String value="";
		String sensor = "";
		String name="";
		List<PinInput> lp=new ArrayList<PinInput>();
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			String querry="select ip.Pin_No, ip.Value , ip.Sensor,ip.TimeStamp,p.NAME from in_pins ip , pins p  where p.Pin_No=ip.Pin_No and ip.uid=p.uid and ip.uid="+uid;
			//System.out.println(querry);
			ResultSet rs=stmt.executeQuery(querry);  
			while(rs.next())  
			{
			pin_num=rs.getInt(1);
			value=rs.getString(2);
			sensor=rs.getString(3);
			timestamp=rs.getTimestamp(4);
			name=rs.getString(5);
				//System.out.println("PinInputget "+pin_num+" "+value+" "+sensor+" "+timestamp+" "+name);  
				PinInput piaux=PinInput.create(pin_num, value, name, sensor, timestamp);
				if(piaux!=null)
				lp.add(piaux);
			}con.close();  
			}catch(Exception e){ System.out.println(e);}  
			  
		return lp;
	}

	@Override
	public List<PinInput> getPinInputLog(int uid, int pin_no,String sensor) {
		List<PinInput> inputpinlog=null;
		int pin_num=-1;
		Timestamp timestamp=null;
		String value="";
		String name="";
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select ipl.Pin_No, ipl.Value , ipl.Sensor, ipl.TimeStamp , ipl.NAME from in_pins_log ipl where ipl.Sensor='"+sensor+"' and ipl.Pin_No="+pin_no+" and ipl.uid="+uid);  
			while(rs.next())  
			{
			pin_num=rs.getInt(1);
			value=rs.getString(2);
			sensor=rs.getString(3);
			timestamp=rs.getTimestamp(4);
			name=rs.getString(5);
			//System.out.println(pin_num+" "+value+" "+sensor+" "+timestamp);  
			if(inputpinlog==null)
			inputpinlog=new ArrayList<PinInput>();
			
			inputpinlog.add(PinInput.create(pin_num,value,name,sensor,timestamp));
			}
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}  
		
		return inputpinlog;
	}

	@Override
	public List<PinInput> getTopPinInputLogSensors(int uid, List<String> sensors) {
		List<PinInput> inputpinlog=null;
		if(sensors==null||sensors.size()==0)
			return null;
		int pin_num=-1;
		Timestamp timestamp=null;
		String value="";
		String name="";
		String sensor="";
		String sensorcond=" Sensor='"+sensors.get(0)+"'";
		for(int i=0;i<sensors.size();i++)
			sensorcond+=" or Sensor='"+sensors.get(i)+"'";
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			String querry="select ipl.Pin_No, ipl.Value , ipl.Sensor, ipl.TimeStamp , ipl.NAME from (select Pin_No , MAX(TimeStamp) lasttime from in_pins_log where "+sensorcond+" and uid="+uid+" group by Pin_No) lt , in_pins_log ipl where ipl.Pin_No=lt.Pin_No and ipl.TimeStamp=lt.lasttime";
			//System.out.println(querry);
			ResultSet rs=stmt.executeQuery(querry);  	
			while(rs.next())  
			{
			pin_num=rs.getInt(1);
			value=rs.getString(2);
			sensor=rs.getString(3);
			timestamp=rs.getTimestamp(4);
			name=rs.getString(5);

			if(inputpinlog==null)
			inputpinlog=new ArrayList<PinInput>();			
			inputpinlog.add(PinInput.create(pin_num,value,name,sensor,timestamp));
			}
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}  
		
		return inputpinlog;
	}

	@Override
	public PinInput getTopPinInputLog(int uid, int pin_no) {
		PinInput inputpinlog=null;
		Timestamp timestamp=null;
		String value="";
		String name="";
		String sensor="";
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			String querry="select ipl.Pin_No, ipl.Value , ipl.Sensor, ipl.TimeStamp , ipl.NAME from (select MAX(TimeStamp) lasttime from in_pins_log where Pin_No="+pin_no+" and uid="+uid+" group by Pin_No) lt , in_pins_log ipl where ipl.Pin_No="+pin_no+" and ipl.TimeStamp=lt.lasttime";
			//System.out.println(querry);
			ResultSet rs=stmt.executeQuery(querry);  	
			if(rs.next()){
			value=rs.getString(2);
			sensor=rs.getString(3);
			timestamp=rs.getTimestamp(4);
			name=rs.getString(5);
			inputpinlog=PinInput.create(pin_no,value,name,sensor,timestamp);}
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}  
		
		return inputpinlog;
	}

}
