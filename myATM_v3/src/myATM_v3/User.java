package myATM_v3;

public class User {
	
	String id;
	String password;
	Account[] accList;
	int accCount;
	
	User(String id, String password){
		this.id = id;
		this.password = password;
	}
	
	User(String id, String password, Account[] accList, int accCount){
		this.id = id;
		this.password = password;
		this.accList = accList; // 생성안해도되나?****
		this.accCount = accCount;
	}
	
	void printUserAllAccounts() {
		// 한명의 User 모든 계좌 출력
		if(this.accCount == 0) {
			System.out.printf("%s님은 아직 생성된 계좌가 없습니다.\n", this.id);
			return;
		} else {
			System.out.printf("%s님의 계좌 목록\n", this.id);			
			for (int i = 0; i < accCount; i++) 
				System.out.println(accList[i].number + " : " + accList[i].money);
		}
	}
}
