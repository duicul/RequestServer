package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

public class MySqlData implements ServerData {
private String dbname,driver,uname,pass;
	public MySqlData(String dbname, String uname, String pass) {
	this.dbname = dbname;
	this.uname = uname;
	this.pass = pass;
	this.driver="com.mysql.cj.jdbc.Driver";
}

	@Override
	public Pin getPin(int pin_no,int uid) {
		int pin_num=-1;
	String type = "",name="";
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from pins where Pin_No="+pin_no+" and uid="+uid);  
			if(rs.next())  
			{
			pin_num=rs.getInt(2);
			type=rs.getString(3);
			name=rs.getString(4);
				System.out.println(pin_num+" "+type+" "+name);  
			}
			else {con.close();return null;}
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}  
			  
		return new Pin(pin_num,type,name);
	}


	@Override
	public PinOutput getOutputPinbyPin_no(int pin_no,int uid) {
		int pin_num=-1,value=-1;
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select op.Pin_No, op.value from out_pins op  where op.Pin_No="+pin_no+" and op.uid="+uid);  
			if(rs.next())  
			{
			pin_num=rs.getInt(1);
			value=rs.getInt(2);
				System.out.println(pin_num+" "+value);  
			}
			else {con.close();return null;}
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}  
			  
		return new PinOutput(pin_num,value);
	}

	@Override
	public PinOutput getOutputPinbyPin_noUpdate(int pin_no,int uid) {
		int pin_num=-1,value=-1;
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select op.Pin_No, op.value from out_pins op  where op.Pin_No="+pin_no+" and uid="+uid);  
			if(rs.next())  
			{
			pin_num=rs.getInt(1);
			value=rs.getInt(2);
				System.out.println(pin_num+" "+value);  
			}
			else {con.close();return null;}
			con.close();
		}catch(Exception e){ System.out.println(e);return null;}  
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();   
			stmt.executeUpdate("UPDATE out_pins op SET op.CHANGED=FALSE where op.Pin_No="+pin_no+" and uid="+uid);  
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}
			  
		return new PinOutput(pin_num,value);
	}
	
	@Override
	public PinInput getIntputPinbyPin_no(int pin_no,int uid) {
		int pin_num=-1;
		Timestamp timestamp=null;
		float value=0;
		String sensor = "";
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select ip.Pin_No, ip.Value , ip.Sensor,ip.TimeStamp from in_pins ip where ip.Pin_No="+pin_no+" and ip.uid="+uid);  
			if(rs.next())  
			{
			pin_num=rs.getInt(1);
			value=rs.getFloat(2);
			sensor=rs.getString(3);
			timestamp=rs.getTimestamp(4);
				System.out.println(pin_num+" "+value+" "+sensor+" "+timestamp);  
			}
			else {con.close();return null;}
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}  
			  
		return new PinInput(pin_num,value,sensor,timestamp);
	}

	@Override
	public void insertInputPin(int pin_no, float value, String name, String sensor,int uid) {
		this.removePinByPin_no(pin_no,uid);
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
			stmt.executeUpdate("INSERT INTO in_pins ( uid,Pin_No , Value , Sensor ) VALUES ("+uid+","+pin_no+","+value+",'"+sensor+"') ");
			con.close();   
			}catch(Exception e)
		{ System.out.println(e);}
		this.addInputPinLog(pin_no, value, name, sensor, uid);
	}

	@Override
	public void insertOutputPin(int pin_no, int value, String name,int uid) {
		this.removePinByPin_no(pin_no,uid);
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("INSERT INTO pins (uid, Pin_No , TYPE , NAME ) VALUES ("+uid+","+pin_no+",'OUT','"+name+"') ");
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);}  
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("INSERT INTO out_pins (uid, Pin_No , Value ) VALUES ("+uid+","+pin_no+","+value+") ");
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
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);}  

	}

	@Override
	public void removeOutputPinbyPin_no(int pin_no,int uid) {
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("delete p,op from out_pins op inner join pins p on p.Pin_No=op.Pin_No where p.type='OUT' AND p.Pin_No="+pin_no+" AND p.uid="+uid+" AND op.uid="+uid);
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);}   

	}

	@Override
	public void removePinByPin_no(int pin_no,int uid) {
		Pin p=this.getPin(pin_no,uid);
		if(p.type.equals("IN"))
			this.removeInputPinbyPin_no(pin_no,uid);
		else if(p.type.equals("OUT"))
			this.removeOutputPinbyPin_no(pin_no,uid);
		else System.out.println("Type unknown: "+p.type);
	}
	
	
	@Override
	public List<Pin> getPins(int uid) {
		int pin_num=-1;
		String type = "",name="";
		List<Pin> lp=new ArrayList<Pin>();
			try{  
				Class.forName(this.driver);  
				Connection con=DriverManager.getConnection(  
				"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass); 
				Statement stmt=con.createStatement();  
				ResultSet rs=stmt.executeQuery("select * from pins where uid="+uid);  
				while(rs.next())  
				{
				pin_num=rs.getInt(2);
				type=rs.getString(3);
				name=rs.getString(4);
					System.out.println(pin_num+" "+type+" "+name);
					lp.add(new Pin(pin_num,type,name));
				}con.close();  
				}catch(Exception e){ System.out.println(e);}  
				  
			return lp;
	}
	

	@Override
	public List<PinOutput> getPinsOutput(int uid) {
		int pin_num=-1,value=-1;
		List<PinOutput> lp=new ArrayList<PinOutput>();
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from out_pins where uid="+uid);  
			while(rs.next())  
			{
			pin_num=rs.getInt(2);
			value=rs.getInt(3);
				System.out.println(pin_num+" "+value); 
				lp.add(new PinOutput(pin_num,value));
			}con.close();  
			}catch(Exception e){ System.out.println(e);}  
			  
		return lp;
	}
	
	public List<PinOutput> getPinsOutputChanged(boolean update,int uid)
	{int pin_num=-1,value=-1;
	List<PinOutput> lp=new ArrayList<PinOutput>();
	try{  
		Class.forName(this.driver);  
		Connection con=DriverManager.getConnection(  
		"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
		//here sonoo is database name, root is username and password  
		Statement stmt=con.createStatement();  
		ResultSet rs=stmt.executeQuery("select * from out_pins where changed=true and uid="+uid);  
		while(rs.next())  
		{
		pin_num=rs.getInt(2);
		value=rs.getInt(3);
			System.out.println(pin_num+" "+value); 
			lp.add(new PinOutput(pin_num,value));
		}con.close();  
		}catch(Exception e){ System.out.println(e);}  
	if(update) {
	try{  
		Class.forName(this.driver);  
		Connection con=DriverManager.getConnection(  
		"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
		//here sonoo is database name, root is username and password  
		Statement stmt=con.createStatement();   
		stmt.executeUpdate("UPDATE out_pins op SET op.CHANGED=FALSE and uid="+uid);  
		con.close();  
		}catch(Exception e){ System.out.println(e);}}
	return lp;}

	@Override
	public List<PinInput> getPinsInput(int uid) {
		int pin_num=-1;
		Timestamp timestamp=null;
		float value=0;
		String sensor = "";
		List<PinInput> lp=new ArrayList<PinInput>();
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from in_pins where uid="+uid);  
			while(rs.next())  
			{
			pin_num=rs.getInt(2);
			value=rs.getFloat(3);
			sensor=rs.getString(4);
			timestamp=rs.getTimestamp(5);
				System.out.println(pin_num+" "+value+" "+sensor+" "+timestamp);  
				lp.add(new PinInput(pin_num,value,sensor,timestamp));
			}con.close();  
			}catch(Exception e){ System.out.println(e);}  
			  
		return lp;
	}

	@Override
	public void updateInputPinValue(int pin_no, float value,int uid) {
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("UPDATE in_pins SET value="+value+" WHERE Pin_No="+pin_no+" and uid="+uid);
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);} 
		Pin p=this.getPin(pin_no, uid);
		PinInput pi=this.getIntputPinbyPin_no(pin_no, uid);
		this.addInputPinLog(pin_no, value, p.name, pi.sensor, uid);
	}

	@Override
	public void tunonOutputPin(int pin_no,int uid) {
		PinOutput po=this.getOutputPinbyPin_no(pin_no,uid);
		Pin p=this.getPin(pin_no,uid);
		if(po!=null&p!=null)
		this.insertOutputPin(pin_no, 1, p.name,uid);
		
	}

	@Override
	public void tunoffOutputPin(int pin_no,int uid) {
		PinOutput po=this.getOutputPinbyPin_no(pin_no,uid);
		Pin p=this.getPin(pin_no,uid);
		if(po!=null&p!=null)
		this.insertOutputPin(pin_no, 0, p.name,uid);
		
	}

	@Override
	public void toggleOutputPin(int pin_no,int uid) {
		PinOutput po=this.getOutputPinbyPin_no(pin_no,uid);
		Pin p=this.getPin(pin_no,uid);
		if(po!=null&p!=null)
		{int value=this.getOutputPinbyPin_no(pin_no,uid).value==0?1:0;
		this.insertOutputPin(pin_no, value, p.name,uid);}
		
	}

	@Override
	public boolean signup(String user, String pass1) {
		SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
	    byte[] digest = digestSHA3.digest(pass1.getBytes());
	    String code= Hex.toHexString(digest);
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("INSERT INTO user_table ( name , password ) VALUES ('"+user+"','"+code+"') ");
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);
		return false;}
		return true;
		
	}

	@Override
	public int getuid(String user, String pass1) {
		SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
	    byte[] digest = digestSHA3.digest(pass1.getBytes());
	    String code= Hex.toHexString(digest);
		int uid=-1;
	    try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from user_table where name='"+user+"' and password='"+code+"'");  
			if(rs.next())  
			{
			uid=rs.getInt(1);
			}con.close();  
			}catch(Exception e){ System.out.println(e);}  
	    return uid;
	}

	@Override
	public void addInputPinLog(int pin_no, float value, String name, String sensor, int uid) {
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("INSERT INTO in_pins_log ( uid , Pin_No , name , Value , Sensor ) VALUES ("+uid+","+pin_no+",'"+name+"',"+value+",'"+sensor+"') ");
			con.close();   
			}catch(Exception e)
		{ System.out.println(e);}
		
	}
}
