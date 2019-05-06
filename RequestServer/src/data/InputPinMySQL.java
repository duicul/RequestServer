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
		PinInput pi=null;
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select p.Pin_No, ip.Value , ip.Sensor,ip.TimeStamp,p.NAME from in_pins ip inner join pins p on p.pid=ip.pid  where p.Pin_No="+pin_no+" and p.uid="+uid);  
			if(rs.next()) {
			pi= PinInput.create(rs.getInt(1),rs.getString(2),rs.getString(5),rs.getString(3),rs.getTimestamp(4));
				//System.out.println(pin_num+" "+value+" "+sensor+" "+timestamp);  
			}
			else {con.close();return null;}
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}  
		
		return pi;}

	@Override
	public void insertInputPin(int pin_no, String value, String name, String sensor,int uid) {
		this.insertInputPinNoLog(pin_no, value, name, sensor, uid);
		this.addInputPinLog(pin_no, value, name, sensor, uid);
	}

	@Override
	public void insertInputPinNoLog(int pin_no, String value, String name, String sensor, int uid) {
		PinData pd=new PinMySQL(dbname, uname, pass);
		pd.removePinByPin_no(pin_no,uid);
		pd.insertPin(uid, pin_no, name, false);
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("INSERT INTO in_pins (pid,Value,Sensor) VALUES ((select p.pid from pins p where p.Pin_No="+pin_no+" and p.uid="+uid+"),'"+value+"','"+sensor+"') ");
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
			stmt.executeUpdate("INSERT INTO in_pins_log (ipid,Value) VALUES ((select ip.ipid from iN_pins ip inner join pins p on p.pid=ip.pid where p.uid="+uid+" and p.Pin_No="+pin_no+"),'"+value+"')");
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
			stmt.executeUpdate("UPDATE in_pins ip SET ip.value='"+value+"' , ip.TimeStamp=CURRENT_TIMESTAMP where ip.pid=(select p.pid from pins p where p.Pin_No="+pin_no+" and p.uid="+uid+")");
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
			stmt.executeUpdate("UPDATE in_pins ip SET ip.value='"+value+"' WHERE ip.pid=(select p.pid from pins p  where p.Pin_No="+pin_no+" and p.uid="+uid+")");
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
			String comm="delete p.*,ip.*,ipl.* from pins p left join in_pins ip on p.pid=ip.pid left join in_pins_log ipl on ipl.ipid=ip.ipid where p.Type='IN' AND p.uid="+uid+" AND p.Pin_No="+pin_no;
			//System.out.println(comm);
			stmt.executeUpdate(comm);
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);}  
	}

	@Override
	public List<Pin> getPinsInput(int uid) {
		List<Pin> lp=new ArrayList<Pin>();
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			String querry="select p.Pin_No,ip.Value,ip.Sensor,ip.TimeStamp,p.NAME from pins p left join in_pins ip on p.pid=ip.pid where p.type='IN' and p.uid="+uid;
			//System.out.println(querry);
			ResultSet rs=stmt.executeQuery(querry);  
			while(rs.next()) {
				//System.out.println("PinInputget "+pin_num+" "+value+" "+sensor+" "+timestamp+" "+name);  
				PinInput piaux=PinInput.create(rs.getInt(1),rs.getString(2),rs.getString(5), rs.getString(3),rs.getTimestamp(4));
				if(piaux!=null)
				lp.add(piaux);
			}con.close();  
			}catch(Exception e){ System.out.println(e);}  
			  
		return lp;
	}

	@Override
	public List<Pin> getPinInputLog(int uid, int pin_no,String sensor) {
		List<Pin> inputpinlog=new ArrayList<Pin>();
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select p.Pin_No,ipl.Value,ip.Sensor,ipl.TimeStamp,p.NAME from pins p inner join in_pins ip on p.pid=ip.pid inner join in_pins_log ipl on ip.ipid=ipl.ipid where ip.Sensor='"+sensor+"' and p.Pin_No="+pin_no+" and p.uid="+uid);  
			while(rs.next())  {
				//System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(5)+" "+rs.getString(3)+" "+rs.getTimestamp(4));  			
				inputpinlog.add(PinInput.create(rs.getInt(1),rs.getString(2),rs.getString(5),rs.getString(3),rs.getTimestamp(4)));
			}
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}  
		
		return inputpinlog;
	}

	@Override
	public List<Pin> getTopPinInputLogSensors(int uid, List<String> sensors) {
		List<Pin> inputpinlog=new ArrayList<Pin>();
		if(sensors==null||sensors.size()==0)
			return null;
		String sensorcond=" ip.Sensor='"+sensors.get(0)+"'";
		for(int i=0;i<sensors.size();i++)
			sensorcond+=" or ip.Sensor='"+sensors.get(i)+"'";
		try{ Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			String querry="select p.Pin_No, ipl.Value , ip.Sensor, ipl.TimeStamp , p.NAME from (select ilid , MAX(TimeStamp) lasttime from pins p inner join in_pins ip on p.pid=ip.pid inner join in_pins_log ipl on ip.ipid=ipl.ipid where "+sensorcond+" and p.uid="+uid+" group by Pin_No) lt , in_pins_log ipl where p.uid="+uid;
			//System.out.println(querry);
			ResultSet rs=stmt.executeQuery(querry);  	
			while(rs.next()){
			inputpinlog.add(PinInput.create(rs.getInt(1),rs.getString(2),rs.getString(5),rs.getString(3),rs.getTimestamp(4)));
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
			String querry="select p.pin_no,ipl.value,ip.sensor,ipl.TimeStamp,p.NAME from ";
			querry+="(select MAX(ipl.timestamp) lasttime from in_pins_log ipl inner join in_pins ip on ipl.ipid=ip.ipid inner join pins p on p.pid=ip.pid where p.Pin_No="+pin_no+" and p.uid="+uid+" group by p.Pin_No)";
			querry+="lt inner join in_pins_log ipl on ipl.timestamp=lt.lasttime inner join in_pins ip on ip.ipid=ipl.ipid inner join pins p on p.pid=ip.pid where p.Pin_No="+pin_no+" and p.uid="+uid;
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
