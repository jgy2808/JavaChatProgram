package application;
	
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static ExecutorService threadPool; // 여러 클라이언트가 접속하면 생기는 여러 thread들을 효과적으로 관리
	public static Vector<Client> clients = new Vector<Client>();
	
	ServerSocket serverSocket;
	TextArea textArea;
	
	//서버를 구동시켜 클라이언트의 연결을 기다리는 메소드 accept()
	public void startServer(String ip, int port) {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(ip, port));
		} catch (Exception e) {
			if (!serverSocket.isClosed()) {
				stopServer();
			}
			return;
		}
		
		//클라이언트가 접속할 때까지 계속 기다리는 쓰레드입니다
		Runnable thread = new Runnable() {
			public void run() {
				while(true) {
					try {
						Socket socket = serverSocket.accept();
						clients.add(new Client(socket, textArea));
						System.out.println("[클라이언트 접속] "
								+ socket.getRemoteSocketAddress()
								+ " : " + Thread.currentThread().getName());
					} catch (Exception e) {
						if (!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					}
				}
			}
		};
		threadPool = Executors.newCachedThreadPool(); // threadPool을 초기화해주고
		threadPool.submit(thread); // 거기에 thread 를 추가해주기
	}
	
	//서버의 작동을 중지시키는 메소드 close
	public void stopServer() {
		try {
			//현재 작동중인 모든 소켓 닫기
			Iterator<Client> iterator = clients.iterator();
			while (iterator.hasNext()) {
				Client client = iterator.next();
				client.socket.close();
				iterator.remove();
			}
			// 서버 소켓 객체 닫기
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			// 쓰레드풀 종료하기
			if (threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//UI를 생성하고, 실질적으로 프로그램을 동작시키는 메소드
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		
		textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Gothic", 15));
		root.setCenter(textArea);
		
		Button toggleButton = new Button("시작하기");
		toggleButton.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(toggleButton, new Insets(1, 0, 0, 0));
		root.setBottom(toggleButton);
		
		String ip = "127.0.0.1"; // local 주소 -> 내 컴퓨터 주소 -> 루프 백 주소
		int port = 9876;
		
		toggleButton.setOnAction(event -> {
			if (toggleButton.getText().equals("시작하기")) {
				startServer(ip, port);
				Platform.runLater(() -> { // javafx에서는 버튼을 눌렀을 때 textArea를 바로 가져와서 쓰면 안되고 runLater을 이용해서 출력하거나 가져오기 
					String message = String.format("[서버 시작]\n", ip, port);
					textArea.appendText(message);
					toggleButton.setText("종료하기");
				});
			} else {
				stopServer();
				Platform.runLater(() -> { // javafx에서는 버튼을 눌렀을 때 textArea에서 바로 가져와서 쓰면 안되고 runLater을 이용해서 출력하거나 가져오기 
					String message = String.format("[서버 종료]\n", ip, port);
					textArea.appendText(message);
					toggleButton.setText("시작하기");
				});
			}
		});
		
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setTitle(" [ 채팅 서버 ] ");
		primaryStage.setOnCloseRequest(event -> stopServer()); // 프로그램을 종료했다면 메소드 호출 후 종료
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	//프로그램의 진입점
	public static void main(String[] args) {
		launch(args);
	}
}
