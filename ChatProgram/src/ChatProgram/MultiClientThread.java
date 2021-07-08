package ChatProgram;

import java.awt.*;
import java.util.*;

public class MultiClientThread extends Thread {
	private MultiClient mc;
	
	public MultiClientThread(MultiClient mc) {
		this.mc = mc;
	}
	
	public void run() {
		String message = null;
		String[] receivedMsg = null;
		
		boolean isStop = false;
		while(!isStop) {
			try {
				message = (String)mc.getOis().readObject(); // Server에서 writeObject(객체)로 넘어온 객체를 message에 저장
				receivedMsg = message.split("#"); 
			} catch (Exception e) {
				e.printStackTrace();
				isStop = true;
			}
			System.out.println(receivedMsg[0] + ", " + receivedMsg[1]);
			if (receivedMsg[1].equals("exit")) { // Client에서 입력한 msg가 exit이고 Server로 넘긴 다음 다시 응답받은 msg가 exit일 때
				if (receivedMsg[0].equals(mc.getId())) { // Server로부터 받은 메세지에서 exit 입력한 id가 자신일 때
					mc.exit();
				} else { // exit 입력한게 자신이 아닐 때
					mc.getJta().append(
					receivedMsg[0] + "님이 종료 하셨습니다." +
					System.getProperty("line.separator"));
					mc.getJta().setCaretPosition(
					mc.getJta().getDocument().getLength());
				}
			} else if (receivedMsg[1].equals("change")) {
				mc.changepower = true;
				mc.getJta().append("바꿀 아이디를 입력하세요"+ System.getProperty("line.separator"));
				String name = receivedMsg[1];
				mc.SetName(name);
			} else if (receivedMsg[1].equals("clear")) {
				mc.Clear();
			} else if (receivedMsg[0].equals(mc.getId())) {
				mc.getJta().append(
						receivedMsg[0] + " : " + receivedMsg[1] + 
						System.getProperty("line.separator"));
				mc.getJta().setCaretPosition(mc.getJta().getDocument().getLength());
			}else if (receivedMsg[1].equals("/r")) {
				if (receivedMsg[2].equals(mc.getId())) {
					mc.getJta().append("도착" + receivedMsg[0] + " : " +
							receivedMsg[3] + System.getProperty("line.separator"));
				}
			}else if (receivedMsg[0].equals("list")) {
				int len = receivedMsg.length - 1;
				String numStr2 = String.valueOf(len);
				mc.getJta().append("현재 접속 인원 : " + numStr2+ System.getProperty("line.separator"));
				for(int i = 0; i < receivedMsg.length; i++) {
					mc.getJta().append(receivedMsg[i] + System.getProperty("line.separator"));
				}
			} else if (receivedMsg[1].equals("칼퇴")) {
				mc.getJta().append("이 메시지는 부적절합니다." + System.getProperty("line.separator"));
			} else {
				mc.getJta().append(
				receivedMsg[0] + " : " + receivedMsg[1] + 
				System.getProperty("line.separator"));
				mc.getJta().setCaretPosition(mc.getJta().getDocument().getLength());
			}
		}
	}
}
