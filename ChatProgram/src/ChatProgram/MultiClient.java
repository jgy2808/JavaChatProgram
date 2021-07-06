package ChatProgram;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

import java.io.*;
import java.net.*;

public class MultiClient implements ActionListener {
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private JFrame jframe, login1; // 창
	private JTextField jtf, idc, pass; // 전송, 아이디, 비번
	private JTextArea jta, jlo; // 키티 배경창, 로그인창
	private JLabel jlb1, jlb2, jID, jPW; // 키티 창에서 유저 아이디와 아이피주소
	private JPanel jp1, jp2, jp3, jp4; //버튼 등을 담아서 프레임에 붙이는 바구니
	private String ip; // 로그인 할 수 있는 아이피
	private String id; // 로그인 할 수 있는 아이디
	private JButton jbtn, jbtn1, jexit; // 전송버튼, 로그인, 종료
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
		}; // 채팅 메세지 내역
		jlo = new JTextArea(30, 30);
		jlb1 = new JLabel("비트2조 채팅창") {
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
		jbtn.setBackground(Color.PINK);
		jlb1.setFont(new Font("Gothic", Font.PLAIN, 15));
		jlb1.setBackground(Color.PINK);
		jlb2.setFont(new Font("Gothic", Font.PLAIN, 15));
		jlb2.setBackground(Color.PINK);
		
		jID.setFont(new Font("Gothic", Font.PLAIN, 30));
		jID.setHorizontalAlignment(jID.CENTER);
		jPW.setFont(new Font("Gothic", Font.PLAIN, 30));
		jPW.setHorizontalAlignment(jPW.CENTER);
		
		idc.setFont(new Font("Gothic", Font.PLAIN, 30));
		idc.setBackground(Color.WHITE);
		pass.setFont(new Font("Gothic", Font.PLAIN, 30));
		pass.setBackground(Color.WHITE);
		jbtn1.setFont(new Font("Gothic", Font.PLAIN, 30));
		jbtn1.setBackground(Color.PINK);
		jexit.setFont(new Font("Gothic", Font.PLAIN, 30));
		jexit.setBackground(Color.PINK);
		jlo.setBackground(Color.PINK);
		
		jp1.setLayout(new BorderLayout());
		jp2.setLayout(new BorderLayout());
		jp3.setLayout(new GridLayout(3, 2, 10, 10));
		
		jp1.add(jbtn, BorderLayout.EAST);
		jp1.add(jtf, BorderLayout.CENTER);
		jp2.add(jlb1, BorderLayout.CENTER);
		jp2.add(jlb2, BorderLayout.EAST);
		
		jp1.setBackground(Color.PINK);
		jp2.setBackground(Color.PINK);
		jp3.setBackground(Color.PINK);
		jp3.add(jID);
		jp3.add(idc);
		jp3.add(jPW);
		jp3.add(pass);
		jp3.add(jbtn1);
		jp3.add(jexit);
		jframe.add(jp1, BorderLayout.SOUTH); // 채팅창 하단 입력tf, 전송btn
		jframe.add(jp2, BorderLayout.NORTH); // 채팅창 상단 채팅방이름, ip
		login1.add(jp3, BorderLayout.EAST); // 로그인 화면 gridlayout 전부
		login1.add(jp4, BorderLayout.EAST); // 뭔지 모르겠음 질문1 : 무슨 Panel인지
		
		JScrollPane jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jframe.add(jsp, BorderLayout.CENTER); // 채팅창 중간 키티화면의 채팅 내역ta
		JScrollPane jsp1 = new JScrollPane(jlo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		login1.add(jp3, BorderLayout.CENTER); // 질문2 : jsp1(jlo)의 위치, 역할 | jp3은 위에서 add시켜줬는데 왜 또 해야하나
		
		jtf.addActionListener(this);
		jbtn.addActionListener(this); // 채팅창에서 enter btn
		jbtn1.addActionListener(this); // 로그인창에서 login btn
		jexit.addActionListener(this); // 로그인창에서 exit btn
		
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					oos.writeObject(id + "#exit"); // id가 어디서 초기화되는지 보기 -> actionPerformed -> if action Login btn
				} catch (IOException ee) {
					ee.printStackTrace();
				}
				System.exit(0);
			}
			
			public void windowOpened(WindowEvent e) {
				jtf.requestFocus();
			}
		});
		
		jta.setEditable(false);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		
		jframe.pack();
		jframe.setLocation((screenWidth = jframe.getWidth()) / 2, (screenHeight - jframe.getHeight()) / 2);
		jframe.setResizable(false);
		jframe.setVisible(false);
		
		login1.pack();
		login1.setSize(800, 300);
		login1.setLocation((screenWidth = jframe.getWidth()) / 2, (screenHeight - jframe.getHeight()) / 2);
		login1.setResizable(false);
		login1.setVisible(true);		
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		String msg = jtf.getText();
		
		String str = e.getActionCommand();
		
		if (str.equals("Login")) {
			jframe.setVisible(true);
			login1.setVisible(false);
			
			ip = idc.getText();
			id = pass.getText();
		}
		if (str.equals("exit")) {
			System.exit(0);
		}
		if (obj == jtf && changepower == true) {
			changepower = false;
			if (msg == null || msg.length() == 0) { // 채팅 메시지 입력이 없을때 
				JOptionPane.showMessageDialog(jframe, "글을 쓰세요", "경고", JOptionPane.WARNING_MESSAGE);
			} else {
				id = jtf.getText();
				jtf.setText("");
			}
		} else if (obj == jtf && saypower == true) {
			saypower = false;
			if (msg == null || msg.length() == 0) {
				JOptionPane.showMessageDialog(jframe, "글을 쓰세요", "경고", JOptionPane.WARNING_MESSAGE);
			} else {
				id = jtf.getText();
				jtf.setText("");
			}
		}
		
		if (obj == jtf) { // jtf에서 enter가 눌렸을 때
			if (msg == null || msg.length() == 0) {
				JOptionPane.showMessageDialog(jframe, "글을 쓰세요", "경고", JOptionPane.WARNING_MESSAGE);
			} else {
				try {
					oos.writeObject(id + "#" + msg); // 질문3 : 위에 oos.writeObject(id + "#exit") 도 그렇고 이게 어디에 저장되는 것인가??
				} catch (IOException ee) {
					ee.printStackTrace();
				}
				jtf.setText("");
			}
		} else if (obj == jbtn) { // enter btn -> 누르면 종료됨
			try {
				oos.writeObject(id + "#exit");
			} catch (IOException ee) {
				ee.printStackTrace();
			}
			System.exit(0); // 이거 때문에 처음 시연해볼 때 채팅 입력창 옆 enter 버튼 누르면 종료했던 것
		}
	}
	
	public static void main(String[] args) {
		
	}

}
