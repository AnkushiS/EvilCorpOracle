import java.util.ArrayList;


public class Lists {

	ArrayList<Account> initAcc = new ArrayList<Account>();
	
	ArrayList<Account> tranAcc = new ArrayList<Account>();

	
	public ArrayList<Account> getInitAcc() {
		return initAcc;
	}

	public void setInitAcc(Account acnt) {
		initAcc.add(acnt);

	}

	public ArrayList<Account> getTranAcc() {
		return tranAcc;
	}

	public void setTranAcc(Account acnt) {
		tranAcc.add(acnt);

	}


}
