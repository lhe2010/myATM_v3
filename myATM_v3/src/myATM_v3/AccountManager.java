package myATM_v3;

public class AccountManager {
	private AccountManager() {}
	static private AccountManager instance = new AccountManager();
	public static AccountManager getInstance() {
		return instance;
	}

	void createAccount() {
		
	}
	
	int showAccList(String msg) {
		return 0;	// temp
	}
	
	void income() {
		
	}
	
	void outcome() {
		
	}
	
	int checkAcc(String transAcc) {
		return 0;	// temp
		
	}
	
	int getAccIndex (int transUsrIdx, String transAcc) {
		return 0; // temp
		
	}
	
	void transfer() {
		
	}
	
	void lookUpAcc () {
		
	}
	
	void deleteAcc() {
		
	}
}
