package myATM_v3;

public class AccountManager {
	private AccountManager() {}
	static private AccountManager instance = new AccountManager();
	public static AccountManager getInstance() {
		return instance;
	}
	
	UserManager userManager = UserManager.getInstance();

	int showAccList(String msg) { // ****** 용도가 뭐지...?
		return 0;	// temp	
	}

	void createAccount() {
		User currUsr = userManager.userList[userManager.identifier];
		
		if(currUsr.accCount == 3) {
			System.out.println("[메세지] 계좌갯수제한(한 회원당 최대 3개의 계좌를 만들 수 있습니다. )");
			return;
		}
		String strAccNum = "";
		while(true) {
			int nAccNum = ATM.ran.nextInt(90000000) + 10000001;
			strAccNum = Integer.toString(nAccNum);
			if(checkAcc(strAccNum) == -1) { // 중복이아니라면
				break;
			}			
		}
		Account newAcc = new Account();
		newAcc.number = strAccNum;
		newAcc.money = 0;
		
		if(currUsr.accCount == 0) {
			currUsr.accList = new Account[1];
		} else {
			Account[] temp = currUsr.accList;
			currUsr.accList = new Account[currUsr.accCount+1];
			for (int i = 0; i < currUsr.accCount; i++) {
				currUsr.accList[i] = temp[i];
			}
			temp = null;		
		}
		currUsr.accList[currUsr.accCount++] = newAcc;
		System.out.printf("[메세지] %s님 앞으로 계좌(%s)생성완료\n", currUsr.id, newAcc.number);
		FileManager.getInstance().saveData();
	}
	
	void deleteAcc() {
		User currUsr = userManager.userList[userManager.identifier];

		if(currUsr.accCount == 0) {
			System.out.println("[메세지] 고객님은 아직 계좌를 개설하지 않으셨어요.");
			return;
		}
		lookUpAcc();
		System.out.printf("[삭제] 삭제하실 계좌를 입력하세요 : ");
		String inputAcc = ATM.scan.next();

		// 계좌 존재여부 체크, 있으면 그 계좌 인덱스를 target에 받음
		int target = getAccIndex(userManager.identifier, inputAcc);
		if( target == -1) {
			System.out.println("[메세지] 존재하지 않는 계좌입니다. ");
			return;
		}
		
		if(currUsr.accCount == 1) {
			currUsr.accList = null;
		} else {
			Account[] temp = currUsr.accList;
			currUsr.accList = new Account[currUsr.accCount-1];
			int j = 0;
			for (int i = 0; i < currUsr.accCount; i++) {
				if(i != target) {
					currUsr.accList[j++] = temp[i];
				}
			}
			temp = null;		
		}
		currUsr.accCount--;
		System.out.printf("[메세지] %s님 앞으로 계좌(%s)삭제완료\n", currUsr.id, inputAcc);		
		FileManager.getInstance().saveData();
	}

	int checkAcc(String transAcc) {
		// 전체 은행에 해당하는 계좌가있는지 체크하고, 가진 회원의 인덱스를 넘긴다.
		for (int i = 0; i < userManager.userCount; i++) {
			for (int j = 0; j < userManager.userList[i].accCount; j++) {
				if(userManager.userList[i].accList[j].number.equals(transAcc))
					return i;
			}
		}
		return -1;	// 없는 계좌일경우 -1을 리턴 
	}
	
	int getAccIndex (int transUsrIdx, String transAcc) {
		// transUsrIdx회원의 몇번째 계좌인지 인덱스를 리턴
		for (int i = 0; i < userManager.userList[transUsrIdx].accCount; i++) {
			if(userManager.userList[transUsrIdx].accList[i].number.equals(transAcc))
				return i;
		}
		return -1; // 없는 계좌일경우 -1을 리턴 
	}
	
	void income() {
		User currUsr = userManager.userList[userManager.identifier];
		
		if(currUsr.accCount == 0) {
			System.out.println("[메세지] 고객님은 아직 계좌를 개설하지 않으셨어요.");
			return;
		}
		
		lookUpAcc();
		System.out.printf("[입금] 입금하실 계좌를 입력하세요 : ");
		String inputAcc = ATM.scan.next();
		// 계좌 존재여부 체크, 있으면 그 계좌 인덱스를 target에 받음
		int target = getAccIndex(userManager.identifier, inputAcc);
		if( target == -1) {
			System.out.println("[메세지] 존재하지 않는 계좌입니다. ");
			return;
		}
		// 계좌 존재하면?
		System.out.printf("[입금] 입금하실 금액을 입력하세요 : ");
		int inputMoney = ATM.scan.nextInt();
		currUsr.accList[target].money += inputMoney;
		
		System.out.printf("[메세지] %s님의 %s계좌에 %d원을 입금완료\n", currUsr.id, currUsr.accList[target].number, inputMoney);
		FileManager.getInstance().saveData();
	}
	
	void outcome() {
		User currUsr = userManager.userList[userManager.identifier];
		
		if(currUsr.accCount == 0) {
			System.out.println("[메세지] 고객님은 아직 계좌를 개설하지 않으셨어요.");
			return;
		}
		
		lookUpAcc();
		System.out.printf("[출금] 출금하실 계좌를 입력하세요 : ");
		String outputAcc = ATM.scan.next();
		// 계좌 존재여부 체크, 있으면 그 계좌 인덱스를 target에 받음
		int target = getAccIndex(userManager.identifier, outputAcc);
		if( target == -1) {
			System.out.println("[메세지] 존재하지 않는 계좌입니다. ");
			return;
		}
		// 계좌 존재하면?
		System.out.printf("[출금] 출금하실 금액을 입력하세요 : ");
		int outputMoney = ATM.scan.nextInt();
		// 출금금액 가능한지 체크
		if(outputMoney > currUsr.accList[target].money) {
			System.out.println("[메세지] 출금가능금액 초과");
			return;
		}
		// 출금 가능한 금액 이면
		currUsr.accList[target].money -= outputMoney;
		System.out.printf("[메세지] %s님의 %s계좌에 %d원을 출금완료\n", currUsr.id, currUsr.accList[target].number, outputMoney);
		FileManager.getInstance().saveData();
	}
	
	void transfer() {
		User currUsr = userManager.userList[userManager.identifier];
		if(currUsr.accCount == 0) {
			System.out.println("[메세지] 고객님은 아직 계좌를 개설하지 않으셨어요.");
			return;
		}

		lookUpAcc();
		System.out.printf("[이체] 이체하실 고객님의 계좌를 입력하세요 : ");
		String outputAcc = ATM.scan.next();
		// 계좌 존재여부 체크, 있으면 그 계좌 인덱스를 targetIdxFrom에 받음
		int outputAccIdx = getAccIndex(userManager.identifier, outputAcc);
		if( outputAccIdx == -1) {
			System.out.println("[메세지] 존재하지 않는 계좌입니다. ");
			return;
		}
		// 계좌 존재하면 상대방 계좌 입력받기
		System.out.printf("[이체] 이체받으실 고객님의 계좌를 입력하세요 : ");
		String transAcc = ATM.scan.next();
		// 계좌 존재여부 체크, 있으면 그 계좌회원 인덱스를 transUsrIdx에 받음
		int transUsrIdx = checkAcc(transAcc);
		if( transUsrIdx == -1) {
			System.out.println("[메세지] 존재하지 않는 계좌입니다. ");
			return;
		}
		// 계좌가 존재하면 이체 금액을 입력받는다. 
		System.out.printf("[이체] 이체하실 금액을 입력하세요 : ");
		int outputMoney = ATM.scan.nextInt();
		// 이체 가능 금액인지 체크
		if(outputMoney > currUsr.accList[outputAccIdx].money) {
			System.out.println("[메세지] 이체가능금액 초과");
			return;
		}
		// 이체 가능한 금액 이면 이체진행
		int transAccIdx = getAccIndex(transUsrIdx, transAcc);
		currUsr.accList[outputAccIdx].money -= outputMoney;
		userManager.userList[transUsrIdx].accList[transAccIdx].money += outputMoney;
		System.out.printf("[메세지] %s님의 %s계좌에서\n%s님의 %s계좌로 %d원을 이체완료\n",
				currUsr.id, currUsr.accList[outputAccIdx].number, 
				userManager.userList[transUsrIdx].id, userManager.userList[transUsrIdx].accList[transAccIdx].number, outputMoney);
		FileManager.getInstance().saveData();
	}
	
	void lookUpAcc () {
		User currUsr = userManager.userList[userManager.identifier];
			
			if(currUsr.accCount == 0) {
				System.out.println("[메세지] 고객님은 아직 계좌를 개설하지 않으셨어요.");
				return;
			} else {
				currUsr.printUserAllAccounts();
			}
	}
	
}
