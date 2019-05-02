import data.*;
import requestserver.RequestServer;

public class Main {
	public static void main(String[] args) {
		System.out.println("Starting the Server .....");
    /*RequestServer rs=new RequestServer("centralserverdb","root","",6767);
				rs.start();*/
		UserData sd=new UserMySQL("centralserverdb","root","");
		InputPinData sdin=new InputPinMySQL("centralserverdb","root","");
		PinData sdpin=new PinMySQL("centralserverdb","root","");
		OutputPinData sdout=new OutputPinMySQL("centralserverdb","root","");
		ConditionData sdcon=new ConditionMySQL("centralserverdb","root","");
		//sd.signup("duicul", "daniel");
		User u=sd.getUser("duicul", "alphaomega");
		int uid=u.uid;
		sdpin.getPin(1,uid);
		sdpin.getPins(uid);
		sdin.getPinsInput(uid);
		sdout.getPinsOutput(uid);
		sdcon.addCondition(uid, 5, 3, ">30 >70", true);
		for(Condition c:sdcon.loadConditions(uid, 5 , "DHT11")) {
			DHTCondition dc=(DHTCondition)c;
			System.out.println("Condition "+c);
			System.out.println(dc.test(32, 71));
			System.out.println(dc.test(19, 71));
			System.out.println(dc.test(32, 30));
		}
		//sd.removeInputPinbyPin_no(10, uid);
		//sd.insertInputPin(10,"1", "Senzor Hol", "PIR",uid);
		//sd.updateInputPinValueLogtimestamp(10,"0",uid);
		//sd.updateInputPinValueNoLogNotimestamp(10,"1",uid);
		//sd.updateInputPinValueLogNotimestamp(10,"1",uid);
		//sd.insertOutputPin(3, 1, "Lumina baie",uid);
		//sd.insertOutputPin(1, 0, "Lumina hol",uid);
		//sd.tunonOutputPin(1,uid);
		//sd.tunoffOutputPin(5,uid);
		//sd.getPinsOutputChanged(true,uid);
		
		//sd.removeOutputPinbyPin_no(5);
		
		/*try {
			rs.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}

}
