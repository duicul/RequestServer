package data;

public interface UserData {
	public boolean signup(String user,String pass,String email,String adress,String phone,String info);
	public boolean updateUser(String user,String email,String adress,String phone,String info);
	public boolean changePassword(String user,String oldpass,String pass);
	public User getUser(String user,String pass);
	public User getUser(String user);
}
