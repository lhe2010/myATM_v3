package myATM_v3;

import java.util.Random;
import java.util.Scanner;

public class ATM {
	
	static Scanner scan = new Scanner(System.in);
	static Random ran = new Random();
	
	UserManager userManager = UserManager.getInstance();
	
	ATM(){ // 생성자에서 데이터 로드
//		userManager.setDummy();
		FileManager.getInstance().loadData();
	}
	
	void showMenu() {
		
		while(true) {
			System.out.println("\n[ATM V3 동작중...]");
			System.out.println("[1] 로그인 [2] 회원가입 [3] 관리자/SAVE [0] 종료");
			System.out.print("메뉴를 선택해주세요 : ");
			int sel = scan.nextInt();
			
			if(sel == 1) 		this.login();
			else if (sel == 2) 	this.join();
			else if (sel == 3) 	{
				this.printDataByAllUser(); 
//				FileManager.getInstance().loadData();
				FileManager.getInstance().saveData();
			} else if (sel == 0) {
				System.out.println("[ATM V3 시스템 종료...]");
				break;
			}
		}
	}
	
	void printDataByAllUser() {
		userManager.printAllUserInfo();
	}
	
	void login() {
		userManager.loginUser();
	}
	
	void join() {
		userManager.joinUser();
	}
}
