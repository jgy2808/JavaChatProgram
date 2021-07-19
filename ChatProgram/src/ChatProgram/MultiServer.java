package ChatProgram;

import java.io.*;
import java.net.*;
import java.util.*;

public class MultiServer {
	private ArrayList<MultiServerThread> list;
	private Socket socket;
	
	public MultiServer() throws IOException {
		list = new ArrayList<MultiServerThread>();
		ServerSocket serverSocket = new ServerSocket(5000);
		MultiServerThread mst = null;
		boolean isStop = false;
		while (!isStop) {
			System.out.println("Server ready...");
			socket = serverSocket.accept(); // client를 대기하면서 while문 진행도 대기
			mst = new MultiServerThread(this);
			list.add(mst);
			Thread t = new Thread(mst);
			t.start();
		}
	}
	
	public void Ddos() {
		System.exit(0);
	}
	
	public ArrayList<MultiServerThread> getList() {
		return list;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public static void main(String[] args) throws IOException {
		new MultiServer();
	}
}