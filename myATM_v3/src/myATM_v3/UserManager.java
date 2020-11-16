package myATM_v3;

public class UserManager {
	
	private UserManager() {}
	static private UserManager instance = new UserManager();
	public static UserManager getInstance() {
		return instance;
	}
	
	User[] userList;
	int userCount;
	int identifier;
		
	void printAllUserInfo() {
		if(userCount > 0) {
			System.out.printf("id\tpassword\t\tAccountInfo");
			for (int i = 0; i < userCount; i++) {
				System.out.printf("\n%s\t%s\t", userList[i].id, userList[i].password);
				if(userList[i].accCount > 0) {
					for (int j = 0; j < userList[i].accCount; j++) {
						if(j > 0) System.out.print("\n\t\t");
						System.out.printf("\t\t%s : %d\t", userList[i].accList[j].number, userList[i].accList[j].money);
					}
				}
			}
		} else {
			System.out.println("[메세지] 아직 회원수 0명입니다. ");
		}
	}
	
	void setDummy() {
		
		String[] a = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"};
		String[] b = {"l", "b", "c", "m", "e", "f", "g", "n", "i", "p", "k"};
		String[] c = {"s", "t", "u", "w", "v", "o", "x", "n", "q", "p", "r"};
		String[] d = {"1", "8", "9", "4"};
		String[] e = {"2", "7", "0", "6"};
		String[] f = {"5", "3", "2", "7"};
		
	}
	
	int checkId(String id) {
		int nCheck = -1;
		for (int i = 0; i < userCount; i++) {
			if(userList[i].id.equals(id)) {
				nCheck = i;
			}
		}
		return nCheck;
	}
	
	void joinUser() {
		
		System.out.print("[가입] ID : ");
		String tempId = ATM.scan.next();
		
		if(checkId(tempId) != -1) {
			System.out.println("[메세지] 이미 가입되어있는 아이디 입니다. ");
			return;
		}
		// 생성가능한 아이디
		System.out.print("[가입] PW : ");
		String tempPw = ATM.scan.next();
		
		if(this.userCount == 0) {
			userList = new User[1];
		} else {
			// List에 추가
			User[] temp = this.userList;
			userList = new User[this.userCount+1];
			// 배열 복사
			for (int i = 0; i < userCount; i++) {
				userList[i] = temp[i];
			}
		}
		userList[userCount++] = new User(tempId, tempPw);
		System.out.println("[메세지] " + tempId + "님 환영합니다.");
		FileManager.getInstance().saveData();
	}
	
	void leaveUser() {
		// *****************************
		// 계좌가 있는경우 잔액을 돌려준다 
		while(true) {
			System.out.print("[탈퇴] PW (뒤로가기는 -1): ");
			String myPw = ATM.scan.next();
			
			if(myPw.equals("-1")) break;
			if(!myPw.equals(userList[identifier].password)) {
				System.out.println("[메세지] 비밀번호 오류");
				continue;
			} else {
				// 탈퇴진행
				if(userCount == 1) {
					userList = null;
				} else {
					User[] tempList = userList;
					userList = new User[userCount-1];
					
					int j = 0;
					for (int i = 0; i < userCount; i++) {
						if(i != identifier) 
							userList[j++] = tempList[i];
					}
				}
				userCount--;	
				this.logooutUser();
				return;
			}
		}
		FileManager.getInstance().saveData();
	}
	
	void loginUser() {
		System.out.print("[로그인] ID : ");
		String tempId = ATM.scan.next();
		int userIdx = checkId(tempId);
		if(userIdx == -1) {
			System.out.println("[메세지] 존재하지 않는 회원 아이디");
			return;
		}
		while(true) {
			System.out.print("[로그인] PW (뒤로가기는 -1): ");
			String myPw = ATM.scan.next();
			
			if(myPw.equals("-1")) return;
			if(!myPw.equals(userList[userIdx].password)) {
				System.out.println("[메세지] 비밀번호 오류");
				continue;
			} else 
				break;
		}
		// 올바른 비밀번호: 로그인
		identifier = userIdx;
		System.out.println("[메세지] " + tempId + "님 환영합니다.");
		afterLoginMenu();
	}
	
	void logooutUser() {
		if(identifier == -1) {
			System.out.println("[메세지] 로그인 후에 접근가능한 메뉴입니다.");
		} else {
			identifier = -1;
			System.out.println("[메세지] 로그아웃 완료");
		}		
	}
	
	void afterLoginMenu() {
		while(true) {
			System.out.println("\n[1]계좌생성 [2]입금하기 [3]출금하기 [4]이체하기 [5]계좌조회 "
					+ "[6]계좌삭제 [7]회원탈퇴 [0]뒤로가기");
			System.out.print("메뉴를 선택하세요 : ");
			int sel = ATM.scan.nextInt();
			
			if(sel == 0) {
				this.logooutUser();
				return;
			} else if(sel == 1)	AccountManager.getInstance().createAccount();		
			else if(sel == 2) 	AccountManager.getInstance().income();
			else if(sel == 3)	AccountManager.getInstance().outcome(); 
			else if(sel == 4)	AccountManager.getInstance().transfer(); 
			else if(sel == 5)	AccountManager.getInstance().lookUpAcc(); 
			else if(sel == 6) 	AccountManager.getInstance().deleteAcc();
			else if(sel == 7) {
				this.leaveUser();
				return;
			}
		}
		
	}
}
