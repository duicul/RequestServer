package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
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
		PinOutput po=null;
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select p.Pin_No, op.value,p.NAME from out_pins op inner join pins p on p.pid=op.pid where p.Pin_No="+pin_no+" and p.uid="+uid);  
			if(rs.next())  
			{po=new PinOutput(rs.getInt(1),rs.getBoolean(2),rs.getString(3));
				//System.out.println(pin_num+" "+value);  
			}
			else {con.close();return null;}
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}  
		return po;
	}

	@Override
	public PinOutput getOutputPinbyPin_noUpdate(int pin_no,int uid) {
		PinOutput po=this.getOutputPinbyPin_no(pin_no, uid);
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();   
			stmt.executeUpdate("UPDATE out_pins op  SET op.CHANGED=FALSE where op.pid=(select p.pid from pins p where p.uid="+uid+" and p.Pin_No="+pin_no+")");  
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}
			  
		return po;
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
			stmt.executeUpdate("delete p.*,op.*,opl.* from pins p left join out_pins op on p.pid=op.pid left join out_pins_log opl on opl.opid=op.opid where p.Type='OUT' AND p.uid="+uid+" AND p.Pin_No="+pin_no);
			con.close();  
		}
		catch(Exception e){
			System.out.println(e);}   
	}

	@Override
	public void insertOutputPin(int pin_no, boolean value, String name,int uid) {
		PinData pd=new PinMySQL(dbname, uname, pass);
		pd.removePinByPin_no(pin_no,uid);
		pd.insertPin(uid, pin_no, name, true);
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("INSERT INTO out_pins (pid,Value) VALUES ((select p.pid from pins p where p.Pin_No="+pin_no+" and p.uid="+uid+"),"+(value?1:0)+")");
			con.close();   
			}catch(Exception e)
		{ System.out.println(e);}
		this.addOutputPinLog(pin_no, uid, value);
		}
	
	
	@Override
	public List<PinOutput> getPinsOutput(int uid) {
		List<PinOutput> lp=new ArrayList<PinOutput>();
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select p.Pin_No, op.value,p.NAME from out_pins op inner join  pins p where p.pid=op.pid and p.uid="+uid);  
			while(rs.next()) {
			//System.out.println("OutputpinList "+pin_num+" "+value); 
			lp.add(new PinOutput(rs.getInt(1),rs.getBoolean(2),rs.getString(3)));
			}con.close();  
			}catch(Exception e){ System.out.println(e);}  
			  
		return lp;}
		
	public List<PinOutput> getPinsOutputChanged(boolean update,int uid){
		List<PinOutput> lp=new ArrayList<PinOutput>();
		try{
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("select p.Pin_No, op.value,p.NAME from out_pins op inner join pins p where p.pid=op.pid and op.changed=true and p.uid="+uid);  
			while(rs.next()){
				//System.out.println(pin_num+" "+value); 
				lp.add(new PinOutput(rs.getInt(1),rs.getBoolean(2),rs.getString(3)));
			}
			stmt.executeUpdate("UPDATE out_pins op inner join pins p on p.pid=op.pid SET CHANGED=FALSE where op.changed=true and p.uid="+uid);
			con.close();  
		}
		catch(Exception e){ System.out.println(e);}  
		/*if(update) {
			try{  
				Class.forName(this.driver);  
				Connection con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
				//here sonoo is database name, root is username and password  
				Statement stmt=con.createStatement();   
				stmt.executeUpdate("UPDATE out_pins op SET op.CHANGED=FALSE and uid="+uid);  
				con.close();  
			}
			catch(Exception e){ System.out.println(e);}}*/
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
			stmt.executeUpdate("UPDATE out_pins op SET op.CHANGED=1 , op.Value="+(value?1:0)+" where op.pid=(select p.pid from pins p where p.uid="+uid+" and p.Pin_No="+pin_no+")");
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);}
		this.addOutputPinLog(pin_no, uid, value);
		
	}
	@Override
	public PinOutputLog getTopPinOutputLog(int uid, int pin_no) {
		PinOutputLog outputpinlog=null;
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			String querry="select p.pin_no,opl.value,opl.TimeStamp,p.NAME from ";
			querry+="(select MAX(opl.timestamp) lasttime from out_pins_log opl inner join out_pins op on opl.opid=op.opid inner join pins p on p.pid=op.pid where p.Pin_No="+pin_no+" and p.uid="+uid+" group by p.Pin_No)";
			querry+="lt inner join out_pins_log opl on opl.timestamp=lt.lasttime inner join out_pins op on op.opid=opl.opid inner join pins p on p.pid=op.pid where p.Pin_No="+pin_no+" and p.uid="+uid;
			//System.out.println(querry);
			ResultSet rs=stmt.executeQuery(querry);  	
			if(rs.next()){
			outputpinlog=new PinOutputLog(rs.getInt(1),rs.getBoolean(2),rs.getString(4),rs.getTimestamp(3));}
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}  
		
		return outputpinlog;
	}
	
	@Override
	public void addOutputPinLog(int pin_no, int uid, boolean val) {
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();
			stmt.executeUpdate("INSERT INTO out_pins_log (opid, Value) VALUES ((select op.opid from out_pins op inner join pins p on p.pid=op.pid where p.uid="+uid+" and p.Pin_No="+pin_no+"),"+(val?1:0)+")");
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);}
		
	}
	@Override
	public List<PinOutputLog> getPinOutputLog(int uid,int pin) {
		List<PinOutputLog> outputpinlog=new ArrayList<PinOutputLog>();
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			String querry="select p.pin_no,opl.value,opl.TimeStamp,p.NAME from pins p inner join out_pins op on p.pid=op.pid inner join out_pins_log opl on opl.opid=op.opid where p.Pin_No="+pin+" and p.uid="+uid;
			//System.out.println(querry);
			ResultSet rs=stmt.executeQuery(querry);  	
			while(rs.next()){
			outputpinlog.add(new PinOutputLog(rs.getInt(1),rs.getBoolean(2),rs.getString(4),rs.getTimestamp(3)));}
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;}  
		
		return outputpinlog;
	}

}
