package application;
	
import java.net.ServerSocket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	
	public static ExecutorService threadPool; // ���� Ŭ���̾�Ʈ�� �����ϸ� ����� ���� thread���� ȿ�������� ����
	public static Vector<Client> clients = new Vector<Client>();
	
	ServerSocket serverSocket;
	
	//������ �������� Ŭ���̾�Ʈ�� ������ ��ٸ��� �޼ҵ� accept()
	public void startServer(String ip, int port) {
		
	}
	
	//������ �۵��� ������Ű�� �޼ҵ� close
	public void stopServer() {
		
	}
	
	//UI�� �����ϰ�, ���������� ���α׷��� ���۽�Ű�� �޼ҵ�
	public void start(Stage primaryStage) {
		
	}
	
	//���α׷��� ������
	public static void main(String[] args) {
		launch(args);
	}
}
