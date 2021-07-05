package ChatProgram;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JProgressBar;
import java.io.*;
import java.net.*;

public class MultiClient implements ActionListener {
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private JFrame jframe, login1; // â
	private JTextField jtf, idc, pass; // ����, ���̵�, ���
	private JTextArea jta, jlo; // ŰƼ ���â, �α���â
	private JLabel jlb1, jlb2, jID, jPW; // ŰƼ â���� ���� ���̵�� �������ּ�
	private JPanel jp1, jp2, jp3, jp4; //��ư ���� ��Ƽ� �����ӿ� ���̴� �ٱ���
	private String ip; // �α��� �� �� �ִ� ������
	private String id; // �α��� �� �� �ִ� ���̵�
	private JButton jbtn, jbtn1, jexit; // ���۹�ư, �α���, ����
	public boolean changepower = false;
	public boolean saypower = false;
	private boolean login = false;
	
	Image img = new ImageIcon("images/bg.png").getImage();
	
	public MultiClient() {
		jframe = new JFrame("Multi Chatting");
		login1 = new JFrame("Login");
		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setIndeterminate(true);
		progressBar.setBounds(32, 303, 195, 14);
		
		jtf = new JTextField(20);
		idc = new JPasswordField(20);
		pass = new JTextField(20);
		
		jta = new JTextArea(43, 43) {
			{
				setOpaque(false);
			}
			public void paintComponent(Graphics g) {
				g.drawImage(img, 0, 0, null);
				super.paintComponent(g);
			}
		};
		jlo = new JTextArea(30, 30);
		jlb1 = new JLabel("��Ʈ2�� ä��â") {
			{
				setOpaque(false);
			}
		};
		jlb2 = new JLabel("IP : " + ip) {
			{
				setOpaque(true);
			}
		};
		jID = new JLabel("IP");
		jPW = new JLabel("name");
		jbtn = new JButton("Enter");
		jbtn1 = new JButton("Login");
		jexit = new JButton("exit");
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();
		jp4 = new JPanel();
		jbtn.setFont(new Font("Gothic", Font.PLAIN, 20));
		jlb1.setFont(new Font("Gothic", Font.PLAIN, 15));
		jlb1.setBackground(Color.PINK);
		jlb2.setFont(new Font("Gothic", Font.PLAIN, 15));
		jlb2.setBackground(Color.PINK);
		
		
		
	}
	
	
	public static void main(String[] args) {
		
	}

}
