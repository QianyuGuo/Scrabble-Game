package ScrabbleClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;

public class ClientGUI {
    private Socket client = null;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    static boolean isConnected = false;
    private JFrame frame;
    private JFrame frmGameHall;
    private JFrame frmGame;
    private JTextField textField1 = new JTextField();
    private String argument = "";
    private String command = "";
    private boolean notice = true;
    private JTextArea textArea1 = new JTextArea();
    private JTextArea textArea2 = new JTextArea();
    private JTextArea textArea3 = new JTextArea();
    private JTextField textField2 = new JTextField();
    private ArrayList<String> userInGame = new ArrayList<String>();
    private ArrayList<String> userOnLine = new ArrayList<String>();
    private JPanel contentPane;
    private Boolean waiting = true;
    static JTable table;
    private JTextField textField;
    private JList list_1;
    private JList list;
    private boolean isGuest = false;
    private boolean isGameStart = false;

    public DefaultListModel modelScore = new DefaultListModel();
    public DefaultListModel modeluser = new DefaultListModel();
    private String ID;

    private DefaultTableModel model = new DefaultTableModel(
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
    };


    public ClientGUI(DataInputStream dis, DataOutputStream dos, Socket client) {
        isConnected = true;
        this.dis = dis;
        this.dos = dos;
        this.client = client;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    initializeLogin();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initializeLogin() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                disconnected();
                System.exit(0);
            }
        });
        frame.getContentPane().setLayout(null);

        JButton btnNewButton = new JButton("login");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                argument = textField1.getText().trim();
                if (argument.length() == 0)
                    JOptionPane.showMessageDialog(frame, "Please enter an ID" + "\n");
                else {
                    textField1.setText("");
                    command = "login*" + argument;
                    if (command.length() != 0) {
                        output(command);
                        if (notice == false) {
                            JOptionPane.showMessageDialog(frame, "Server is closed, please close the window" + "\n");
                            disconnected();
                            return;
                        }
                        String result = input();
                        String[] outcome = result.split("\\*");
                        if (outcome[0].equalsIgnoreCase("notification")) {
                            JOptionPane.showMessageDialog(frame, outcome[1]);

                            command = "";
                        } else {
                            JOptionPane.showMessageDialog(frame, result);
                            command = "";
                            frame.setVisible(false);
                            EventQueue.invokeLater(new Runnable() {
                                public void run() {
                                    try {
                                        initializeGameHall();
                                        frmGameHall.setVisible(true);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            ID = argument;
                            ReceiveThread receiveThread = new ReceiveThread();
                            new Thread(receiveThread).start();
                        }
                    }
                }
            }
        });
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnNewButton.setBounds(156, 174, 114, 40);
        frame.getContentPane().add(btnNewButton);

        textField1.setBounds(156, 89, 203, 32);
        frame.getContentPane().add(textField1);
        textField1.setColumns(10);

        JLabel lblNewLabel = new JLabel("User name:");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel.setBounds(78, 89, 104, 32);
        frame.getContentPane().add(lblNewLabel);
        frame.setVisible(true);
    }

    public void output(String str) {
        try {
            dos.writeUTF(str);
            dos.flush();
        } catch (SocketException s) {
            notice = false;
            System.out.println("false");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String input() {
        String command = "";
        try {
            command = dis.readUTF();
            System.out.println(command);
        } catch (SocketException s) {
            notice = false;
            System.out.println("Server is closed, please close the window");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return command;
    }

    public void disconnected() {
        try {
            dos.writeUTF("Client closed");
            isConnected = false;
            if (dis != null)
                dis.close();
            if (dos != null)
                dos.close();
            if (client != null)
                client.close();
        } catch (SocketException s) {
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ReceiveThread implements Runnable {
        public void run() {
            try {
                while (isConnected) {
                    String result = dis.readUTF();
                    String[] outcome = result.split("\\*");
                    System.out.println(outcome[0]);
                    if (outcome[0].equalsIgnoreCase("updateUser")) {
                        if (outcome.length > 1) {
                            updateOnlineUser(outcome[1]);
                            System.out.println(outcome[1]);
                        } else
                            textArea1.setText("");
                        command = "";
                    } else if (outcome[0].equalsIgnoreCase("notification2")) {
                        JOptionPane.showMessageDialog(frmGameHall, outcome[1]);
                        System.out.println(outcome[1]);
                    } else if (outcome[0].equalsIgnoreCase("updatePlayer")) {
                        updateInGameUser(outcome[1]);
                        System.out.println(outcome[1]);
                    } else if (outcome[0].equalsIgnoreCase("gameStart")) {
                        isGameStart = true;
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                try {
//                                    initializeTable();
                                    initializeGame();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else if (outcome[0].equalsIgnoreCase("invite")) {
                        if (confirm(outcome[1])) {
                            output("accept*" + outcome[1]);
                            isGuest = true;
                        }
                    } else if (outcome[0].equalsIgnoreCase("voteRow")) {
                        String[] word = outcome[1].split(":");
                        if (!voteAgree(word[1]))
                            output("voteReply*Row,false," + word[1] + "," + word[2]);
                        else
                            output("voteReply*Row,true," + word[1] + "," + word[2]);
                    } else if (outcome[0].equalsIgnoreCase("voteColumn")) {
                        String[] word = outcome[1].split(":");
                        if (!voteAgree(word[1]))
                            output("voteReply*Column,false," + word[1] + "," + word[2]);
                        else
                            output("voteReply*Column,true," + word[1] + "," + word[2]);
                    } else if (outcome[0].equalsIgnoreCase("userquit")) {
                        JOptionPane.showMessageDialog(frmGame, "Another player quited, your score is " + outcome[1] + "\n");
                        userInGame.clear();
                        modeluser.clear();
                        modelScore.clear();
                        textArea2.setText("");
                        initializeTable();
                        frmGame.setVisible(false);
                        isGuest = false;

                    } else if (outcome[0].equalsIgnoreCase("turn")) {
                        waiting = false;
                        JOptionPane.showMessageDialog(frmGame, "Now it is your turn" + "\n");
                    } else if (outcome[0].equalsIgnoreCase("updateGame")) {
                        System.out.println(outcome[1]);
                        updateGame(outcome[1]);
                    } else if (outcome[0].equalsIgnoreCase("chat")) {
                        System.out.println(outcome[1]);
                        textArea3.append(outcome[1] + '\n');
                    } else if (outcome[0].equalsIgnoreCase("hostquit")) {
                        if (!isGameStart) {
                            JOptionPane.showMessageDialog(frmGameHall, "Host closed, room disbanded  " + "\n");
                            textArea2.setText("");
                            output("disbanded");
                            userInGame.clear();
                            modeluser.clear();
                            modelScore.clear();
                            isGuest = false;
                        }
                    } else if (outcome[0].equalsIgnoreCase("updateScore")) {
                        System.out.println(outcome[1]);
                        String[] parts = outcome[1].split(",");
                        if (isGameStart) {
                            modeluser.clear();
                            modelScore.clear();
                            for (int i = 0; i < parts.length; i++) {
                                modelScore.addElement(parts[i]);
                            }
                            list_1.setModel(modelScore);
                            for (String i : userInGame)
                                modeluser.addElement(i);
                            list.setModel(modeluser);
                        }
                    } else if (outcome[0].equalsIgnoreCase("gameOver")) {
                        String IDs = "";
                        if (outcome[1].equalsIgnoreCase("none")) {
                            JOptionPane.showMessageDialog(frmGame, "Game over,there is no winner and your score is " + outcome[2]);
                        } else {
                            String[] winner = outcome[1].split(",");
                            if (winner.length > 2) {
                                for (int i = 0; i < winner.length - 1; i++) {
                                    IDs = IDs + winner[i] + " ";
                                }
                                JOptionPane.showMessageDialog(frmGame, "Game over,the winners are " + IDs + "and the score is " + winner[winner.length - 1]);
                            } else {
                                JOptionPane.showMessageDialog(frmGame, "Game over,the winner is " + winner[0] + " and the score is " + winner[1]);
                            }
                        }

                        isGameStart = false;
                        isGuest = false;
                        textArea2.setText("");
                        modeluser.clear();
                        modelScore.clear();
                        initializeTable();
                        frmGame.setVisible(false);
                        userInGame.clear();
                        System.out.println("clear2");

                    }

                }
            } catch (SocketException e) {

                if (isConnected) {
                    JOptionPane.showMessageDialog(frame, "Server is closed, please close the window" + "\n");
                    System.out.println("Server is closed, please close the window");
                    System.exit(0);
                }
            } catch (EOFException e) {
                System.out.println("EOF");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void initializeGameHall() {
        frmGameHall = new JFrame();
        frmGameHall.setTitle(this.ID + "' Game Hall");
        frmGameHall.setBounds(100, 100, 536, 421);
        frmGameHall.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (isGameStart)
                    frmGame.setVisible(false);
                disconnected();
                System.exit(0);
            }
        });
        frmGameHall.getContentPane().setLayout(null);

        textArea1.setBounds(14, 36, 100, 118);
        frmGameHall.getContentPane().add(textArea1);

        textArea2.setBounds(151, 36, 100, 118);
        frmGameHall.getContentPane().add(textArea2);


        textArea3.setBounds(14, 191, 490, 170);
        frmGameHall.getContentPane().add(textArea3);

        JLabel lblChattingRoom = new JLabel("Chatting room");
        lblChattingRoom.setBounds(14, 167, 136, 18);
        frmGameHall.getContentPane().add(lblChattingRoom);


        JLabel lblUserOnline = new JLabel("user online");
        lblUserOnline.setBounds(14, 13, 120, 18);
        frmGameHall.getContentPane().add(lblUserOnline);

        textField2.setBounds(285, 35, 219, 24);
        frmGameHall.getContentPane().add(textField2);
        textField2.setColumns(10);

        JLabel lblUserInRoom = new JLabel("user in room");
        lblUserInRoom.setBounds(151, 13, 127, 18);
        frmGameHall.getContentPane().add(lblUserInRoom);


        JButton btnNewButton = new JButton("Invite");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String user = "";
                if (!isGuest) {
                    argument = textField2.getText().trim();
                    System.out.println("argument" + argument);
                    if (argument.length() == 0)
                        JOptionPane.showMessageDialog(frmGameHall, "Please enter user ID" + "\n");
                    else {
                        String[] invitedUser = argument.split(",");
                        for (String i : invitedUser) {
                            if (ID.equalsIgnoreCase(i)) {
                                JOptionPane.showMessageDialog(frmGameHall, "You cannot invite yourself" + "\n");
                                textField2.setText("");
                                return;
                            }

                        }
                        for (String i : invitedUser) {
                            if (userInGame.contains(i))
                                JOptionPane.showMessageDialog(frmGameHall, "User " + i + " is already in room" + "\n");
                            else if (!userOnLine.contains(i)) {
                                JOptionPane.showMessageDialog(frmGameHall, "User " + i + " is not online" + "\n");
                                System.out.println("i:" + i);
                            } else {
                                user = user + i + ",";
                            }
                        }
                        textField2.setText("");
                        System.out.println(user + "user");
                        if (user.length() > 0)
                            output("invite*" + user);
                    }

                } else
                    JOptionPane.showMessageDialog(frmGameHall, "Guest cannot invite" + "\n");
            }
        });
        btnNewButton.setBounds(285, 76, 100, 27);
        frmGameHall.getContentPane().add(btnNewButton);

        JButton btnSend = new JButton("send");
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String message = textField2.getText().trim();
                if (message.length() == 0)
                    JOptionPane.showMessageDialog(frmGameHall, "Please enter a message" + "\n");
                else {
                    textField2.setText("");
                    output("chat*" + message);
                }

            }
        });
        btnSend.setBounds(404, 76, 100, 27);
        frmGameHall.getContentPane().add(btnSend);

        JButton btnNewButton_1 = new JButton("Start a game");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (userInGame.size() < 2) {
                    JOptionPane.showMessageDialog(frmGame, "There is no player in the room" + "\n");
                    return;
                }
                if (!isGuest) {
                    isGameStart = true;
                    waiting = false;
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            try {
//                                initializeTable();
                                initializeGame();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    output("start");
                    JOptionPane.showMessageDialog(frmGame, "It is your turn now" + "\n");

                } else {
                    JOptionPane.showMessageDialog(frmGameHall, "Guest cannot start the game" + "\n");
                }
            }
        });
        btnNewButton_1.setBounds(285, 116, 219, 27);
        frmGameHall.getContentPane().add(btnNewButton_1);
        frmGameHall.setVisible(true);


    }


    public void updateOnlineUser(String user) {
        System.out.println(user);
        userOnLine.clear();
        String[] users = user.split(",");
        textArea1.setText("");
        for (String i : users) {
            textArea1.append(i + "\n");
            userOnLine.add(i);
        }
    }

    public void updateInGameUser(String user) {
        userInGame.clear();
        System.out.println(user);
        String[] users = user.split(",");
        textArea2.setText("");
        for (String i : users) {
            System.out.println(i);
            textArea2.append(i + "\n");
            userInGame.add(i);
        }
    }

    public Boolean confirm(String id) {
        String[] confirmInvite = {
                "Yes", "No"
        };
        int confirmation = JOptionPane.showOptionDialog(null, "User " + id + " invite you to play a game", "Invitation", 0, JOptionPane.QUESTION_MESSAGE, null, confirmInvite, 0);
        if (confirmation == 0)
            return true;
        return false;
    }

    public Boolean voteAgree(String str) {
        VoteDialog cd = new VoteDialog(new JFrame(), str);
        if (cd.isOk())
            return true;
        return false;
    }

    public void initializeGame() {
        frmGame = new JFrame();
        frmGame.setTitle(this.ID + "' Game");
        frmGame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    if (waiting)
                        output("quit");
                    else {
                        output("quit*next");
                        waiting = true;
                    }
                    textArea2.setText("");
                    isGuest = false;
                    isGameStart = false;
                    userInGame.clear();
                    modelScore.clear();
                    modeluser.clear();
                    System.out.println("clear1");
                    initializeTable();
                } catch (Exception i) {
                    System.out.println();
                }

            }
        });
        frmGame.setBounds(100, 100, 714, 468);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frmGame.setContentPane(contentPane);
        contentPane.setLayout(null);


        list = new JList();
        for (String i : userInGame)
            modeluser.addElement(i);
        list.setModel(modeluser);
        list.setBounds(21, 53, 80, 365);
        contentPane.add(list);

        JLabel lblNewLabel = new JLabel("UserID:           Score:");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblNewLabel.setBounds(21, 17, 133, 31);
        contentPane.add(lblNewLabel);

        for (int i = 0; i < userInGame.size(); i++)
            modelScore.addElement(0);
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
                if (waiting) {
                    System.out.println("confirm waiting");
                    JOptionPane.showMessageDialog(frmGame, "It is not your turn, please wait");
                    return;
                }
                if (textField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frmGame, "Please enter inserted letter");
                    return;
                }

                char newLetter = textField.getText().charAt(0);
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                if (row == -1 || column == -1) {
                    JOptionPane.showMessageDialog(frmGame, "Please select a cell to enter your letter");
                    return;
                }
                if (table.getModel().getValueAt(row, column) != null) {
                    JOptionPane.showMessageDialog(frmGame, "the selected cell already have letter");
                    return;
                }
                textField.setText("");
                output("filling*" + newLetter + "," + row + "," + column + "\n");
                waiting = true;
            }
        });
        contentPane.add(btnConfirm);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        textField.setBounds(535, 17, 96, 25);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Type the letter you want to added:");
        lblNewLabel_1.setBounds(221, 16, 287, 28);
        contentPane.add(lblNewLabel_1);

        JButton btnWord = new JButton("vote");
        btnWord.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (waiting) {
                    JOptionPane.showMessageDialog(frmGame, "It is not your turn, please wait");
                    return;
                }
                if (textField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frmGame, "Please enter inserted letter");
                    return;
                }
                char newLetter = textField.getText().charAt(0);
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                if (row == -1 || column == -1) {
                    JOptionPane.showMessageDialog(frmGame, "Please select a cell to enter your letter");
                    return;
                }
                if (table.getModel().getValueAt(row, column) != null) {
                    JOptionPane.showMessageDialog(frmGame, "The selected cell already have letter");
                    return;
                }
                textField.setText("");
                output("updateGame*" + newLetter + "," + row + "," + column + "\n");

                int countRow = 0;
                int countColumn = 0;
                ArrayList<Object> objs1 = null;
                ArrayList<Object> objs2 = null;

                if (row == -1 || column == -1) {
                    JOptionPane.showMessageDialog(frmGame, "Please select a cell to enter your letter");
                    return;
                }
                if (table.getModel().getValueAt(row, column) != null) {
                    JOptionPane.showMessageDialog(frmGame, "the selected cell already have letter");
                    return;
                }
                textField.setText("");
                updateGame(newLetter + "," + row + "," + column);
                int leftcount1 = column;
                int rightcount1 = column;
//row
                try {
                    objs1 = new ArrayList<Object>();
                    while (table.getModel().getValueAt(row, leftcount1) != null) {
                        System.out.println(table.getModel().getValueAt(row, leftcount1));
                        objs1.add(table.getModel().getValueAt(row, leftcount1));
                        countRow = countRow + 1;
                        leftcount1 = leftcount1 - 1;

                    }
                    objs1.remove(0);
                    Collections.reverse(objs1);
                    countRow = countRow - 1;
                    while (table.getModel().getValueAt(row, rightcount1) != null) {
                        System.out.println(table.getModel().getValueAt(row, rightcount1));
                        objs1.add(table.getModel().getValueAt(row, rightcount1));
                        countRow = countRow + 1;
                        rightcount1 = rightcount1 + 1;
                    }

                } catch (IndexOutOfBoundsException e) {
                    System.out.println(countRow);
                }
//column
                int leftcount2 = row;
                int rightcount2 = row;
                try {
                    objs2 = new ArrayList<Object>();
                    while (table.getModel().getValueAt(leftcount2, column) != null) {
                        objs2.add(table.getModel().getValueAt(leftcount2, column));
                        System.out.println(table.getModel().getValueAt(leftcount2, column));
                        countColumn = countColumn + 1;
                        leftcount2 = leftcount2 - 1;

                    }
                    objs2.remove(0);
                    Collections.reverse(objs2);
                    countColumn = countColumn - 1;
                    while (table.getModel().getValueAt(rightcount2, column) != null) {
                        objs2.add(table.getModel().getValueAt(rightcount2, column));
                        System.out.println(table.getModel().getValueAt(leftcount2, column));
                        countColumn = countColumn + 1;
                        rightcount2 = rightcount2 + 1;
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(countColumn);
                }
                String rowWord = "";
                String columnWord = "";
                System.out.println(objs1.size());

                if (objs1.size() > 1) {
                    for (Object a : objs1) {
                        rowWord = rowWord + a;
                    }
                    if (objs2.size() > 1) {
                        for (Object a : objs2) {
                            columnWord = columnWord + a;
                        }
                        System.out.println(rowWord + "," + columnWord);
                        output("vote*Row:" + rowWord + "+Column:" + columnWord);
                    } else {
                        System.out.println(rowWord);
                        output("vote*Row:" + rowWord);
                    }
                } else if (objs2.size() > 1) {
                    for (Object a : objs2) {
                        columnWord = columnWord + a;
                    }
                    System.out.println(columnWord);
                    output("vote*Column:" + columnWord);
                } else {
                    JOptionPane.showMessageDialog(frmGame, "It is a single letter, cannot vote");
                    output("next");
                }
                waiting = true;
            }
        });
        btnWord.setBounds(221, 50, 113, 27);
        contentPane.add(btnWord);

        JButton btnPass = new JButton("pass");
        btnPass.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (waiting) {
                    JOptionPane.showMessageDialog(frmGame, "It is not your turn, please wait");
                    return;
                }
                output("pass");
                waiting = true;
            }
        });
        btnPass.setBounds(518, 50, 113, 27);
        contentPane.add(btnPass);

        frmGame.setVisible(true);


    }

    public void updateGame(String str) {
        String[] parts = str.split(",");
        model.setValueAt(parts[0], Integer.parseInt(parts[1].trim()), Integer.parseInt(parts[2].trim()));
        table.setModel(model);
        System.out.println(ID + "updated");
    }

    public void initializeTable() {
        model = new DefaultTableModel(
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
        };
        table.setModel(model);
    }


}
