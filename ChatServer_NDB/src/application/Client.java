package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Client {
	
	Socket socket;
	TextArea textArea;
	
	public Client(Socket socket, TextArea textArea) {
		this.socket = socket;
		this.textArea = textArea;
		receive();
	}
	
	//클라이언트로부터 메세지를 전달받는 메소드입니다.
	public void receive() {
		Runnable thread = new Runnable() {
			public void run() {
				try {
					while(true) {
						InputStream in = socket.getInputStream();
						byte[] buffer = new byte[512];
						int length = in.read(buffer);
						while(length == -1) throw new IOException();
						System.out.println("[메세지 수신 성공] "
						+ socket.getRemoteSocketAddress() 
						+ " : "  + Thread.currentThread().getName());
						String message = new String(buffer, 0, length, "UTF-8");
						Platform.runLater(() -> {
							textArea.appendText(message);
						});
						for (Client client : Main.clients ) {
							client.send(message);
						}
					}
				} catch (Exception e) {
					try {
						System.out.println("[메세지 수신 오류] "
								+ socket.getRemoteSocketAddress()
								+ " : " + Thread.currentThread().getName());
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		};
		Main.threadPool.submit(thread);
	}
	
	//클라이언트에게 메세지를 전송하는 메소드입니다.
	public void send(String message) {
		Runnable thread = new Runnable() {
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
				} catch (Exception e) {
					try {
						System.out.println("[메세지 송신 오류] " 
								+ socket.getRemoteSocketAddress()
								+ " : " + Thread.currentThread().getName());
						Main.clients.remove(Client.this);
						socket.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		};
		Main.threadPool.submit(thread);
	}
	
}
