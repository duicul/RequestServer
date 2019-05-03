package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OutputPinMySQL implements OutputPinData {
	private String dbname,driver,uname,pass;
	public OutputPinMySQL(String dbname, String uname, String pass) {
	this.dbname = dbname;
	this.uname = uname;
	this.pass = pass;
	this.driver="com.mysql.cj.jdbc.Driver";
}
	@Override
	public PinOutput getOutputPinbyPin_no(int pin_no,int uid) {
		int pin_num=-1;
		boolean value=false;
		String name="";
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select op.Pin_No, op.value,p.NAME from out_pins op, pins p  where p.Pin_No=op.Pin_No and op.uid=p.uid and op.Pin_No="+pin_no+" and op.uid="+uid);  
			if(rs.next())  
			{
			pin_num=rs.getInt(1);
			value=rs.getBoolean(2);
			name=rs.getString(3);
				//System.out.println(pin_num+" "+value);  
			}
			else {con.close();return null;}
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}  
			  
		return new PinOutput(pin_num,value,name);
	}

	@Override
	public PinOutput getOutputPinbyPin_noUpdate(int pin_no,int uid) {
		int pin_num=-1;
		boolean value=false;
		String name="";
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select op.Pin_No, op.value,p.NAME from out_pins op,pins p where p.Pin_No=ip.Pin_No and ip.uid=p.uid and op.Pin_No="+pin_no+" and uid="+uid);  
			if(rs.next())  
			{
			pin_num=rs.getInt(1);
			value=rs.getBoolean(2);
				//System.out.println(pin_num+" "+value);  
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
			  
		return new PinOutput(pin_num,value,name);
	}

	@Override
	public void tunonOutputPin(int pin_no,int uid) {
		PinOutput po=this.getOutputPinbyPin_no(pin_no,uid);
		if(po!=null)
		this.updateOutputPin(pin_no,true,uid);}

	@Override
	public void tunoffOutputPin(int pin_no,int uid) {
		PinOutput po=this.getOutputPinbyPin_no(pin_no,uid);
		Pin p=new PinMySQL(dbname, uname, pass).getPin(pin_no,uid);
		if(po!=null&p!=null)
			this.updateOutputPin(pin_no, false,uid);}

	@Override
	public void toggleOutputPin(int pin_no,int uid) {
		PinOutput po=this.getOutputPinbyPin_no(pin_no,uid);
		Pin p=new PinMySQL(dbname, uname, pass).getPin(pin_no,uid);
		if(po!=null&p!=null){
			boolean value=this.getOutputPinbyPin_no(pin_no,uid).value;
			this.updateOutputPin(pin_no,!value,uid);}
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
		}
		catch(Exception e){
			System.out.println(e);}   
	}

	@Override
	public void insertOutputPin(int pin_no, boolean value, String name,int uid) {
		new PinMySQL(dbname, uname, pass).removePinByPin_no(pin_no,uid);
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
			stmt.executeUpdate("INSERT INTO out_pins (uid, Pin_No , Value ) VALUES ("+uid+","+pin_no+","+(value?1:0)+") ");
			con.close();   
			}catch(Exception e)
		{ System.out.println(e);}

	}

	
	
	@Override
	public List<PinOutput> getPinsOutput(int uid) {
		int pin_num=-1;
		boolean value=false;
		String name="";
		List<PinOutput> lp=new ArrayList<PinOutput>();
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select op.Pin_No, op.value,p.NAME from out_pins op , pins p where p.Pin_No=op.Pin_No and op.uid=p.uid and op.uid="+uid);  
			while(rs.next())  
			{
			pin_num=rs.getInt(1);
			value=rs.getBoolean(2);
			name=rs.getString(3);
			//System.out.println("OutputpinList "+pin_num+" "+value); 
			lp.add(new PinOutput(pin_num,value,name));
			}con.close();  
			}catch(Exception e){ System.out.println(e);}  
			  
		return lp;}

	public List<PinOutput> getPinsOutputChanged(boolean update,int uid){
		int pin_num=-1;
		boolean value=false;
		List<PinOutput> lp=new ArrayList<PinOutput>();
		String name="";
		try{
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select op.Pin_No, op.value,p.NAME from out_pins op, pins p where p.Pin_No=op.Pin_No and op.uid=p.uid and changed=true and op.uid="+uid);  
			while(rs.next()){
				pin_num=rs.getInt(1);
				value=rs.getBoolean(2);
				name=rs.getString(3);
				//System.out.println(pin_num+" "+value); 
				lp.add(new PinOutput(pin_num,value,name));
			}con.close();  
		}
		catch(Exception e){ System.out.println(e);}  
		if(update) {
			try{  
				Class.forName(this.driver);  
				Connection con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
				//here sonoo is database name, root is username and password  
				Statement stmt=con.createStatement();   
				stmt.executeUpdate("UPDATE out_pins op SET op.CHANGED=FALSE and uid="+uid);  
				con.close();  
			}
			catch(Exception e){ System.out.println(e);}}
		return lp;
		}
	@Override
	public void updateOutputPin(int pin_no, boolean value, int uid) {
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("UPDATE out_pins op SET op.CHANGED=1 , op.Value="+(value?1:0)+" where op.uid="+uid+" and op.Pin_No="+pin_no);
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);}
		
	}

}
