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
	
	public static ExecutorService threadPool; // ���� Ŭ���̾�Ʈ�� �����ϸ� ����� ���� thread���� ȿ�������� ����
	public static Vector<Client> clients = new Vector<Client>();
	
	ServerSocket serverSocket;
	TextArea textArea;
	
	//������ �������� Ŭ���̾�Ʈ�� ������ ��ٸ��� �޼ҵ� accept()
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
		
		//Ŭ���̾�Ʈ�� ������ ������ ��� ��ٸ��� �������Դϴ�
		Runnable thread = new Runnable() {
			public void run() {
				while(true) {
					try {
						Socket socket = serverSocket.accept();
						clients.add(new Client(socket, textArea));
						System.out.println("[Ŭ���̾�Ʈ ����] "
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
		threadPool = Executors.newCachedThreadPool(); // threadPool�� �ʱ�ȭ���ְ�
		threadPool.submit(thread); // �ű⿡ thread �� �߰����ֱ�
	}
	
	//������ �۵��� ������Ű�� �޼ҵ� close
	public void stopServer() {
		try {
			//���� �۵����� ��� ���� �ݱ�
			Iterator<Client> iterator = clients.iterator();
			while (iterator.hasNext()) {
				Client client = iterator.next();
				client.socket.close();
				iterator.remove();
			}
			// ���� ���� ��ü �ݱ�
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			// ������Ǯ �����ϱ�
			if (threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//UI�� �����ϰ�, ���������� ���α׷��� ���۽�Ű�� �޼ҵ�
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		
		textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Gothic", 15));
		root.setCenter(textArea);
		
		Button toggleButton = new Button("�����ϱ�");
		toggleButton.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(toggleButton, new Insets(1, 0, 0, 0));
		root.setBottom(toggleButton);
		
		String ip = "127.0.0.1"; // local �ּ� -> �� ��ǻ�� �ּ� -> ���� �� �ּ�
		int port = 9876;
		
		toggleButton.setOnAction(event -> {
			if (toggleButton.getText().equals("�����ϱ�")) {
				startServer(ip, port);
				Platform.runLater(() -> { // javafx������ ��ư�� ������ �� textArea�� �ٷ� �����ͼ� ���� �ȵǰ� runLater�� �̿��ؼ� ����ϰų� �������� 
					String message = String.format("[���� ����]\n", ip, port);
					textArea.appendText(message);
					toggleButton.setText("�����ϱ�");
				});
			} else {
				stopServer();
				Platform.runLater(() -> { // javafx������ ��ư�� ������ �� textArea���� �ٷ� �����ͼ� ���� �ȵǰ� runLater�� �̿��ؼ� ����ϰų� �������� 
					String message = String.format("[���� ����]\n", ip, port);
					textArea.appendText(message);
					toggleButton.setText("�����ϱ�");
				});
			}
		});
		
		Scene scene = new Scene(root, 400, 400);
		primaryStage.setTitle(" [ ä�� ���� ] ");
		primaryStage.setOnCloseRequest(event -> stopServer()); // ���α׷��� �����ߴٸ� �޼ҵ� ȣ�� �� ����
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	//���α׷��� ������
	public static void main(String[] args) {
		launch(args);
	}
}
