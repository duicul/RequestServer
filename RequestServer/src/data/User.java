package data;

public class User {
public final String name,email,adress,phone,info;
public final int uid;
public User(int uid , String name, String email, String adress, String phone, String info) {
	super();
	this.uid=uid;
	this.name = name;
	this.email = email;
	this.adress = adress;
	this.phone = phone;
	this.info = info;
}
}
