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
		}; // ä�� �޼��� ����
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
		jframe.add(jp1, BorderLayout.SOUTH); // ä��â �ϴ� �Է�tf, ����btn
		jframe.add(jp2, BorderLayout.NORTH); // ä��â ��� ä�ù��̸�, ip
		login1.add(jp3, BorderLayout.EAST); // �α��� ȭ�� gridlayout ����
		login1.add(jp4, BorderLayout.EAST); // ���� �𸣰��� ����1 : ���� Panel����
		
		JScrollPane jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jframe.add(jsp, BorderLayout.CENTER); // ä��â �߰� ŰƼȭ���� ä�� ����ta
		JScrollPane jsp1 = new JScrollPane(jlo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		login1.add(jp3, BorderLayout.CENTER); // ����2 : jsp1(jlo)�� ��ġ, ���� | jp3�� ������ add������µ� �� �� �ؾ��ϳ�
		
		jtf.addActionListener(this);
		jbtn.addActionListener(this); // ä��â���� enter btn
		jbtn1.addActionListener(this); // �α���â���� login btn
		jexit.addActionListener(this); // �α���â���� exit btn
		
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					oos.writeObject(id + "#exit"); // id�� ��� �ʱ�ȭ�Ǵ��� ���� -> actionPerformed -> if action Login btn
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
			if (msg == null || msg.length() == 0) { // ä�� �޽��� �Է��� ������ 
				JOptionPane.showMessageDialog(jframe, "���� ������", "���", JOptionPane.WARNING_MESSAGE);
			} else {
				id = jtf.getText();
				jtf.setText("");
			}
		} else if (obj == jtf && saypower == true) {
			saypower = false;
			if (msg == null || msg.length() == 0) {
				JOptionPane.showMessageDialog(jframe, "���� ������", "���", JOptionPane.WARNING_MESSAGE);
			} else {
				id = jtf.getText();
				jtf.setText("");
			}
		}
		
		if (obj == jtf) { // jtf���� enter�� ������ ��
			if (msg == null || msg.length() == 0) {
				JOptionPane.showMessageDialog(jframe, "���� ������", "���", JOptionPane.WARNING_MESSAGE);
			} else {
				try {
					oos.writeObject(id + "#" + msg); // ����3 : ���� oos.writeObject(id + "#exit") �� �׷��� �̰� ��� ����Ǵ� ���ΰ�??
				} catch (IOException ee) {
					ee.printStackTrace();
				}
				jtf.setText("");
			}
		} else if (obj == jbtn) { // enter btn -> ������ �����
			try {
				oos.writeObject(id + "#exit");
			} catch (IOException ee) {
				ee.printStackTrace();
			}
			System.exit(0); // �̰� ������ ó�� �ÿ��غ� �� ä�� �Է�â �� enter ��ư ������ �����ߴ� ��
		}
	}
	
	public static void main(String[] args) {
		
	}

}
