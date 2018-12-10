package ScrabbleClient;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class GameHall extends JFrame {
	public JFrame frmGameHall;
	private JTextField textField;
	private JTextArea textArea1;
	private JTextArea textArea2;
	public String[] onlineUser = new String[99];
	public String[] inGameUser;
	private DataOutputStream dos = null;
	private DataInputStream dis = null;
	private Socket client = null;
	private String argument = "";
	private String command = "";
	private boolean notice = true;
	private JPanel contentPane;
	public static ArrayList<User> ingameUser=new ArrayList<User>();
	public static ArrayList<User> getIngameUser() {
		return ingameUser;
	}

	public GameHall(DataOutputStream dos,DataInputStream dis, Socket client) {
		this.client = client;
		this.dos = dos;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public void updateOnlineUser(String user) {
		System.out.println();
		String[] users = user.split(",");
		textArea1.setText("");
		for(int i=0;i<users.length;i++){

			textArea1.append(users[i] + "\n");
		}
//		for(int i=0;i<users.length;i++){
//			onlineUser[i] = users[i];
//			System.out.println(onlineUser[i]+"here");
//		}


	}
	private void initialize() {
		frmGameHall = new JFrame();
		frmGameHall.setTitle("Game Hall");
		frmGameHall.setBounds(100, 100, 500, 372);
		frmGameHall.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				disconnected();
				System.exit(0);
			}
		});
		frmGameHall.getContentPane().setLayout(null);

		textArea1 = new JTextArea();
		textArea1.setBounds(26, 44, 120, 231);
		frmGameHall.getContentPane().add(textArea1);
		for(int i = 0;i<onlineUser.length;i++){
			textArea1.setText(onlineUser[i]+"\n");
		}


		textArea2 = new JTextArea();
		textArea2.setBounds(160, 44, 127, 231);
		frmGameHall.getContentPane().add(textArea2);


		JLabel lblUserOnline = new JLabel("user online");
		lblUserOnline.setBounds(26, 27, 120, 18);
		frmGameHall.getContentPane().add(lblUserOnline);

		JLabel lblUserInRoom = new JLabel("user in room");
		lblUserInRoom.setBounds(160, 27, 127, 18);
		frmGameHall.getContentPane().add(lblUserInRoom);


		JButton btnNewButton = new JButton("Invite");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				argument = textField.getText().trim();
				if (argument.length() == 0)
					JOptionPane.showMessageDialog(frmGameHall, "Please enter user ID" + "\n");
				else {
					textField.setText("");
					output("invite*" + argument);
				}
			}
		});
		btnNewButton.setBounds(301, 122, 151, 27);
		frmGameHall.getContentPane().add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Start a game");
		btnNewButton_1.setBounds(301, 189, 151, 27);
		frmGameHall.getContentPane().add(btnNewButton_1);
		frmGameHall.setVisible(true);

	}

	public void output(String str) {
		try {
			dos.writeUTF(str);
			dos.flush();
		} catch (SocketException s) {
			notice = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void disconnected() {
		try {
			dos.writeUTF("Client closed");
			dos.close();
			dis.close();
			client.close();
		} catch (SocketException s) {
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
