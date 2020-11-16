package myATM_v3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class FileManager {
	
	private static FileManager instance = new FileManager();
	private FileManager() {}
	public static FileManager getInstance() {
		return instance;
	}
	UserManager userManager = UserManager.getInstance();
	
	String filename = "atm_v3.txt";
	File f = null;
	FileWriter fw = null;
	FileReader fr = null;
	BufferedReader br = null;	
	
	boolean loadData() {
		f = new File(filename);
		if(f.exists()) {
			try {
				fr = new FileReader(f);
				br = new BufferedReader(fr);
				 
				userManager.userCount = Integer.parseInt(br.readLine());
				userManager.userList = new User[userManager.userCount];
				
				for (int i = 0; i < userManager.userCount; i++) {
					String[] temp = br.readLine().split("/");
//					System.out.println(Arrays.toString(temp));
					int accCount = Integer.parseInt(temp[2]);
					if(accCount == 0) {
						userManager.userList[i] = new User(temp[0], temp[1]);
						
					} else {
						// 리스트 생성
						Account[] accList = new Account[accCount];
						for (int j = 0; j < accCount; j++) {
							accList[j] = new Account();
							accList[j].number = temp[j*2+3];
							accList[j].money = Integer.parseInt(temp[j*2+4]);
						}
						userManager.userList[i] = new User(temp[0], temp[1], accList, accCount);
					}
				}
				br.close();
				fr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}		
			
			return true;
		} else 
			return false;		
	}
	
	void saveData() {
		String data = (userManager.userCount + "\n");
		
		f = new File(filename);
		
		try {
			fw = new FileWriter(f);
			
			for (int i = 0; i < userManager.userCount; i++) {
				String temp = "";
				User currUsr = UserManager.getInstance().userList[i];
				temp += (currUsr.id + "/" + currUsr.password + "/" + currUsr.accCount);
				if(currUsr.accCount > 0) {
					for (int j = 0; j < currUsr.accCount; j++) 
						temp += ("/" + currUsr.accList[j].number + "/" + currUsr.accList[j].money);						
				} 
				data += (temp + "\n");				
			}
			fw.write(data);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	

}
