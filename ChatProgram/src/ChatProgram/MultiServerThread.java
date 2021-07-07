package ChatProgram;

import java.net.*;
import java.io.*;

public class MultiServerThread implements Runnable {
	private Socket socket;
	private MultiServer ms;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public MultiServerThread(MultiServer ms) {
		this.ms = ms;
	}
	
	public synchronized void run() {
		boolean isStop = false;
		try {
			socket = ms.getSocket();
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			String message = null;
			while(!isStop) {
				message = (String)ois.readObject();
				String[] str = message.split("#");
				
				String name = "list" + "#";
				
				for (int i = 0; i < ms.getList().size(); i++) {
					name += ms.getList().get(i) + "#";
				}
				
				if (str[1].equals("exit")) {
					broadCasting(message);
					isStop = true;
				} else if (str[1].equals("list")) {
					broadCasting(name);
				} else if (str[1].equals("")) {
					broadCasting(name);
				} else if (str[1].equals("강퇴")) {
					System.out.println("첫번째" + str[0]);
					System.out.println("두번째" + str[1]);
					System.out.println("세번째" + str[2]);
					
					String taget = str[2];
					System.out.println(taget);
					if (taget.equals(str[0])) {
						socket.close();
					}
				} else if (str[1].equals("디도스공격")) {
					ms.Ddos();
				} else {
					broadCasting(message);
				}
			}
			ms.getList().remove(this);
			System.out.println(socket.getInetAddress() + " 정상적으로 종료하셨습니다");
			System.out.println("list size : " + ms.getList().size());
		} catch (Exception e) {
			ms.getList().remove(this);
			System.out.println(socket.getInetAddress() + " 비정상적으로 종료하셨습니다.");
			System.out.println("list size : " + ms.getList().size());
		}
	}
	
	public void broadCasting(String message) throws IOException {
		for (MultiServerThread ct : ms.getList()) {
			ct.send(message);
		}
	}
	
	public void send(String message) throws IOException {
		oos.writeObject(message);
	}
}
