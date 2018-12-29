import data.MySqlData;
import data.ServerData;
import data.User;
import requestserver.RequestServer;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

public class Main {
	public static void main(String[] args) {
    RequestServer rs=new RequestServer("centralserverdb","root","",8765);
				rs.start();
		ServerData sd=new MySqlData("centralserverdb","root","");
		//sd.signup("duicul", "daniel");
		User u=sd.getUser("duicul", "daniel");
		int uid=u.uid;
		sd.getPin(1,uid);
		sd.getPins(uid);
		sd.getPinsInput(uid);
		sd.getPinsOutput(uid);
		sd.insertInputPin(5,(float)11.2, "Termostat Cada", "DTH11",uid);
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
