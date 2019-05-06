package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PinMySQL implements PinData {
private String dbname,driver,uname,pass;
	public PinMySQL(String dbname, String uname, String pass) {
	this.dbname = dbname;
	this.uname = uname;
	this.pass = pass;
	this.driver="com.mysql.cj.jdbc.Driver";
}
	
	@Override
	public Pin getPin(int pin_no,int uid) {
		Pin p=null;
		int pin_num=-1;
		String type = "",name="";
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			String querry="select * from pins where Pin_No="+pin_no+" and uid="+uid;
			//System.out.println(querry);
			ResultSet rs=stmt.executeQuery(querry);  
			if(rs.next())  
			{pin_num=rs.getInt(3);
			type=rs.getString(4);
			name=rs.getString(5);
			if(type.equals("IN"))
				p=new InputPinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass).getIntputPinbyPin_no(pin_no, uid);
			else 
				if(type.equals("OUT"))
					p=new OutputPinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass).getOutputPinbyPin_no(pin_no, uid);
			//System.out.println("Pin "+pin_num+" "+type+" "+name+" "+uid);  
			}
			else {con.close();return null;}
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}
		return p;  
	}

	@Override
	public void removePinByPin_no(int pin_no,int uid) {
		Pin p=this.getPin(pin_no,uid);
		if(p==null)
			return;
		if(p.type.equals("IN"))
			new InputPinMySQL(dbname, uname, pass).removeInputPinbyPin_no(pin_no,uid);
		else if(p.type.equals("OUT"))
			new OutputPinMySQL(dbname, uname, pass).removeOutputPinbyPin_no(pin_no,uid);
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
				//System.out.println(pin_num+" "+type+" "+name);
				Pin p=null;
				if(type.equals("IN"))
					p=new InputPinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass).getIntputPinbyPin_no(pin_num, uid);
				else 
					if(type.equals("OUT"))
						p=new OutputPinMySQL(DatabaseSetup.dbname,DatabaseSetup.user,DatabaseSetup.pass).getOutputPinbyPin_no(pin_num, uid);
				lp.add(p);
				}con.close();  
				}catch(Exception e){ System.out.println(e);}  
				  
			return lp;
	}

	@Override
	public void insertPin(int uid,int pin, String name, boolean out) {
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("INSERT INTO pins (uid,Pin_No,TYPE,NAME) VALUES ("+uid+","+pin+",'"+(out?"OUT":"IN")+"','"+name+"')");
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);} 
		
	}
}
