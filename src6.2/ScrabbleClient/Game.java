package ScrabbleClient;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.event.ActionEvent;

public class Game extends JFrame {
    private Socket client = null;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    private JPanel contentPane;
    public Boolean waiting;
    /*private BoardButton buttons[]=new BoardButton[400];*/
    static JTable table;
    private JTextField textField;
    static JList list_1;
    static JList list;
    /*static DefaultListModel modelScore;*/ //nullpointer exception, because have not been initialized
    static DefaultListModel modelScore = new DefaultListModel();
    static DefaultListModel modeluser = new DefaultListModel();

    DefaultTableModel model = new DefaultTableModel(
            new Object[][]{
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            },
            new String[]{
                    "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column"
            }
    ) {
        Class[] columnTypes = new Class[]{
                String.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class
        };

        public Class getColumnClass(int columnIndex) {
            return columnTypes[columnIndex];
        }
    };

    /**
     * Launch the application.
     */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game frame = new Game();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

    /**
     * Create the frame.
     */
    public Game(DataOutputStream dos, DataInputStream dis, Socket client) {
        this.dos = dos;
        this.dis = dis;
        this.client = client;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 714, 468);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        ArrayList<String> ingameUserName = new ArrayList<>();

        list = new JList();
        list.setBounds(21, 53, 80, 365);
        contentPane.add(list);

        JLabel lblNewLabel = new JLabel("UserID:           Score:");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblNewLabel.setBounds(21, 17, 133, 31);
        contentPane.add(lblNewLabel);


        list_1 = new JList(modelScore);
        list_1.setBounds(111, 53, 80, 365);
        contentPane.add(list_1);

        table = new JTable() {
            public boolean isCellEditable(int data, int columns) {
                return false;
            }
        };
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Tahoma", Font.PLAIN, 12));

        table.setModel(model);
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setBounds(201, 98, 443, 320);

        contentPane.add(table);

        JButton btnConfirm = new JButton("confirm");
        btnConfirm.setBounds(369, 50, 113, 27);
        btnConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (textField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter inserted letter");
                    return;
                }

                char newLetter = textField.getText().charAt(0);
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                if (row == -1 || column == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a cell to enter your letter");
                    return;
                }
                if (table.getModel().getValueAt(row, column) != null) {
                    JOptionPane.showMessageDialog(null, "the selected cell already have letter");
                    return;
                }
                textField.setText("");
//                try {
////                    login.writer.write("filling*" + newLetter + "," + row + "," + column + "\n");
////                    login.writer.flush();
//                } catch (IOException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }

                contentPane.add(btnConfirm);

                textField = new JTextField();
                textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
                textField.setBounds(535, 17, 96, 25);
                contentPane.add(textField);
                textField.setColumns(10);

                JLabel lblNewLabel_1 = new JLabel("Type the letter you want to added:");
                lblNewLabel_1.setBounds(221, 16, 287, 28);
                contentPane.add(lblNewLabel_1);

                JButton btnWord = new JButton("word");
                btnWord.setBounds(221, 50, 113, 27);
                contentPane.add(btnWord);

                JButton btnPass = new JButton("pass");
                btnPass.setBounds(518, 50, 113, 27);
                contentPane.add(btnPass);

                JLabel lblNewLabel_2 = new JLabel("Type the letter you want to added:");
                lblNewLabel_2.setBounds(240, 23, 174, 25);
                contentPane.add(lblNewLabel_2);
            }
        });
    }
}
