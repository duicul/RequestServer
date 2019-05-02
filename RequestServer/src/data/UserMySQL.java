package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

public class UserMySQL implements UserData {
	private String dbname,driver,uname,pass;
	public UserMySQL(String dbname, String uname, String pass) {
	this.dbname = dbname;
	this.uname = uname;
	this.pass = pass;
	this.driver="com.mysql.cj.jdbc.Driver";
}
	@Override
	public boolean signup(String user,String pass1,String email,String adress,String phone,String info) {
		SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
	    byte[] digest = digestSHA3.digest(pass1.getBytes());
	    String code= Hex.toHexString(digest);
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("INSERT INTO user_table ( name , password , email , adress , phone , info) VALUES ('"+user+"','"+code+"','"+email+"','"+adress+"','"+phone+"','"+info+"') ");
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);
		return false;}
		return true;		
	}

	@Override
	public boolean updateUser(String user,String email, String adress, String phone, String info) {	
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("UPDATE user_table ut SET ut.email='"+email+"' , ut.adress='"+adress+"' , ut.phone='"+phone+"' , ut.info='"+info+"' where name='"+user+"' ");
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);
		return false;}
		return true;
	}

	@Override
	public boolean changePassword(String user,String oldpass,String form_pass) {
		SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
	    byte[] digest = digestSHA3.digest(form_pass.getBytes());
	    String code= Hex.toHexString(digest);
	    digest = digestSHA3.digest(oldpass.getBytes());
	    String oldcode= Hex.toHexString(digest);
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();
			String querry="UPDATE user_table ut SET ut.password='"+code+"' where name='"+user+"' and ut.password='"+oldcode+"'";
			//System.out.println(querry);
			stmt.executeUpdate(querry);
			con.close();  
			}catch(Exception e)
		{ System.out.println(e);
		return false;}
		return true;
	}

	@Override
	public User getUser(String user, String pass1) {
		SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
	    byte[] digest = digestSHA3.digest(pass1.getBytes());
	    String code= Hex.toHexString(digest);
		String name="",email="",adress="",phone="",info="";
	    int uid;
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
			name=rs.getString(2);
			email=rs.getString(4);
			adress=rs.getString(5);
			phone=rs.getString(6);
			info=rs.getString(7);
			}
			else return null;
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;} 
	    return new User(uid,name, email, adress, phone, info);
	}

	@Override
	public User getUser(String user) {
		String name="",email="",adress="",phone="",info="";
	    int uid;
		try{  
			Class.forName(this.driver);  
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://127.0.0.1:3306/"+dbname,uname,pass);  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select * from user_table where name='"+user+"'");  
			if(rs.next())  
			{
			uid=rs.getInt(1);
			name=rs.getString(2);
			email=rs.getString(4);
			adress=rs.getString(5);
			phone=rs.getString(6);
			info=rs.getString(7);
			}
			else return null;
			con.close();  
			}catch(Exception e){ System.out.println(e);return null;} 
	    return new User(uid,name, email, adress, phone, info);
	}

}
