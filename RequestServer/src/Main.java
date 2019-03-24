import data.MySqlData;
import data.ServerData;
import data.User;
import requestserver.RequestServer;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

public class Main {
	public static void main(String[] args) {
		System.out.println("Starting the Server .....");
    RequestServer rs=new RequestServer("centralserverdb","root","",6767);
				rs.start();
		ServerData sd=new MySqlData("centralserverdb","root","");
		//sd.signup("duicul", "daniel");
		User u=sd.getUser("duicul", "daniel");
		int uid=u.uid;
		sd.getPin(1,uid);
		sd.getPins(uid);
		sd.getPinsInput(uid);
		sd.getPinsOutput(uid);
		sd.insertInputPin(5,"20.2 60", "Termostat Cada", "DHT11",uid);
		sd.insertOutputPin(3, 1, "Lumina baie",uid);
		sd.insertOutputPin(1, 0, "Lumina hol",uid);
		sd.tunonOutputPin(1,uid);
		sd.tunoffOutputPin(5,uid);
		sd.getPinsOutputChanged(true,uid);
		
		//sd.removeOutputPinbyPin_no(5);
		try {
			rs.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
