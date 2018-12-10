package ScrabbleServer;

import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


public class ScrabbleServer {
    private JFrame frame;
    private JTextArea textArea;
    private boolean state = false;
    private ServerSocket server = null;
    private Game1 game;
    public int voteNum = 0;
    public int voteReplyNum = 0;
    List<ClientThread> clients;
    List<ClientThread> players;


    public static void main(String[] args) {
        ScrabbleServer scrabbleSever = new ScrabbleServer();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    scrabbleSever.initialize();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        scrabbleSever.start();

    }

    public void start() {
        try {
            server = new ServerSocket(1234);
            game = new Game1();
            System.out.println("This is Scrabble Server");
            textArea.setText("This is Scrabble Server" + '\n');
            this.clients = new ArrayList<ClientThread>();
            this.players = new ArrayList<ClientThread>();
        } catch (BindException b) {
            System.out.println("Port is being used");
            System.out.println("Please turn off and try again");
            JOptionPane.showMessageDialog(frame, "Port is being used, Please turn off and try again" + "\n");
            frame.setVisible(false);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            state = true;
            while (state) {
                Socket socket = server.accept();

                ClientThread clientThread = new ClientThread(socket);
                new Thread(clientThread).start();
            }
        } catch (SocketException s) {
            System.out.println("Server closed");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Scrabble Server");
        frame.setBounds(500, 300, 516, 374);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                disconnected();
                System.exit(0);
            }
        });
        frame.getContentPane().setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(14, 63, 470, 230);
        frame.getContentPane().add(scrollPane);

        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 17));

        JLabel lblClientCommand = new JLabel("Client command");
        lblClientCommand.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        lblClientCommand.setBounds(14, 25, 156, 18);
        frame.getContentPane().add(lblClientCommand);
        frame.setVisible(true);
    }

    public void disconnected() {
        try {
            textArea.setText("Server socket closed");
            state = false;
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class ClientThread implements Runnable {
        private Socket client;
        private String ID;
        private DataInputStream dis = null;
        private DataOutputStream dos = null;
        private boolean isConnected = false;
        public int score = 0;
        private int rowScore = 0;
        private int columnScore = 0;
        private String clientIP = "";


        public ClientThread(Socket socket) {
            this.client = socket;
            InputStream sIn = null;
            OutputStream sOut = null;
            try {
                sIn = client.getInputStream();
                dis = new DataInputStream(sIn);
                sOut = client.getOutputStream();
                dos = new DataOutputStream(sOut);
                isConnected = true;//When thread is created,this boolean will become true to make the DataInputStream work
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }


        public void run() {
            try {
                clientIP = client.getInetAddress().getHostAddress();
                System.out.println("Client " + clientIP + " connected" + '\n');
                textArea.append("Client " + clientIP + " connected" + '\n');
                while (isConnected) {
                    String command = dis.readUTF();
                    if (command.equalsIgnoreCase("Client closed")) {
                        System.out.println("Client " + clientIP + " " + ID + " disconnected" + '\n');
                        textArea.append("Client " + clientIP + " " + ID + " disconnected" + '\n');

                        isConnected = false;
                        clients.remove(this);
                        for (int i = 0; i < clients.size(); i++) {
                            ClientThread c = clients.get(i);
                            c.updateUser();
                            System.out.println("updateUser");

                        }
                        if (players.contains(this)) {
                            if (players.get(0).ID.equalsIgnoreCase(this.ID)) {
                                players.remove(this);
                                for (int i = 0; i < players.size(); i++) {
                                    ClientThread c = players.get(i);
                                    c.send("hostquit");
//                                    c.updatePlayer();
//                                    System.out.println("updatePlayer");
//                                    c.updateScore();
                                }
                                players.clear();
                            } else {
                                players.remove(this);
                                for (int i = 0; i < players.size(); i++) {
                                    ClientThread c = players.get(i);
                                    c.updatePlayer();
                                    System.out.println("updatePlayer");
                                    c.updateScore();
                                }
                            }
                        }

                        voteNum = 0;
                        voteReplyNum = 0;

                    } else {
                        System.out.println(command);
                        String[] argument = command.split("\\*");
                        operation(argument);
                    }
                }
            } catch (SocketException s) {
                System.out.println("Client " + clientIP + " closed");
            } catch (EOFException e) {
                System.out.println("Client closed");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (dis != null)
                        dis.close();
                    if (dos != null)
                        dos.close();
                    if (client != null)
                        client.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }

        public void operation(String[] argument) {
            String result = "";
            if (argument.length == 1) {
                System.out.println("receive a command  from " + clientIP + " : " + argument[0] + '\n');
                textArea.append("receive a command  from " + clientIP + " : " + argument[0] + '\n');
            } else {
                System.out.println("receive a command  from " + clientIP + " : " + argument[0] + "  " + argument[1] + '\n');
                textArea.append("receive a command from " + clientIP + " : " + argument[0] + "  " + argument[1] + '\n');
            }

            if (argument[0].equalsIgnoreCase("login")) {
                result = login(argument[1]);
                this.send(result);
                for (int i = 0; i < clients.size(); i++) {
                    ClientThread c = clients.get(i);
                    c.updateUser();

                }
            } else if (argument[0].equalsIgnoreCase("invite")) {
                invite(this.ID, argument[1]);
//                if (!players.contains(this))
//                    players.add(this);
//                this.updatePlayer();

            } else if (argument[0].equalsIgnoreCase("accept")) {
                for (int i = 0; i < clients.size(); i++) {
                    if (clients.get(i).ID.equalsIgnoreCase(argument[1]) && !players.contains(clients.get(i)))
                        players.add(clients.get(i));
                }
                players.add(this);
                for (int i = 0; i < players.size(); i++) {
                    ClientThread c = players.get(i);
                    c.updatePlayer();

                }

            } else if (argument[0].equalsIgnoreCase("chat")) {
                for (int i = 0; i < clients.size(); i++) {
                    ClientThread c = clients.get(i);
                    c.send("chat*" + this.ID + " : " + argument[1]);
                }

            } else if (argument[0].equalsIgnoreCase("disbanded")) {
                players.clear();
            } else if (argument[0].equalsIgnoreCase("start")) {
                for (int i = 1; i < players.size(); i++) {
                    if (!players.get(i).ID.equalsIgnoreCase(this.ID))
                        players.get(i).send("gameStart");

                }
            } else if (argument[0].equalsIgnoreCase("quit")) {
                if (players.size() == 2) {
                    for (int i = 0; i < players.size(); i++) {
                        if (!players.get(i).ID.equalsIgnoreCase(this.ID)) {
                            ClientThread c = players.get(i);
                            c.send("userquit*" + c.score);
                        }
                    }
                    players.clear();
                    voteNum = 0;
                    voteReplyNum = 0;
                    this.score = 0;
                    return;
                } else if (argument.length > 1) {
                    int i;
                    for (i = 0; i < players.size(); i++) {
                        if (players.get(i).ID.equalsIgnoreCase(this.ID)) {
                            i = i + 1;
                            break;
                        }
                    }
                    i = i % players.size();
                    players.get(i).send("turn");
                }
                players.remove(this);
                for (int i = 0; i < players.size(); i++) {
                    ClientThread c = players.get(i);
                    c.updatePlayer();
                    System.out.println("updatePlayer");
                    c.updateScore();
                }
                this.score = 0;
                voteNum = 0;
                voteReplyNum = 0;

            } else if (argument[0].equalsIgnoreCase("filling")) {
                try {
                    game.pass = 0;
                    game.update(argument[1]);
                    updateGame(argument[1]);
                    int i;
                    for (i = 0; i < players.size(); i++) {
                        if (players.get(i).ID.equalsIgnoreCase(this.ID)) {
                            i = i + 1;
                            break;
                        }
                    }
                    i = i % players.size();
                    players.get(i).send("turn");

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (argument[0].equalsIgnoreCase("next")) {
                int i;
                System.out.println("waaaa");
                for (i = 0; i < players.size(); i++) {
                    if (players.get(i).ID.equalsIgnoreCase(this.ID)) {
                        i = i + 1;
                        break;
                    }
                }
                i = i % players.size();
                System.out.println("yaaaa");
                players.get(i).send("turn");
            } else if (argument[0].equalsIgnoreCase("vote")) {
                game.pass = 0;
                String[] words = argument[1].split("\\+");
                String[] word1;
                if (words.length == 2) {
                    voteNum = players.size() * 2 - 2;
                    voteRow(words[0] + ":" + ID);
                    voteColumn(words[1] + ":" + ID);

                } else {
                    voteNum = players.size() - 1;
                    word1 = words[0].split(":");
                    if (word1[0].equalsIgnoreCase("Row")) {
                        voteRow(words[0] + ":" + ID);
                    } else {
                        voteColumn(words[0] + ":" + ID);
                    }
                }
                System.out.println("voteNum  " + voteNum);

            } else if (argument[0].equalsIgnoreCase("updateGame")) {
                try {
                    game.update(argument[1]);
                    updateGame(argument[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (argument[0].equalsIgnoreCase("voteReply")) {
                voteReplyNum = voteReplyNum + 1;
                System.out.println("voteReplyNum  " + voteReplyNum);
                System.out.println("voteNum  " + voteNum);
                String[] word = argument[1].split(",");
                if (word[0].equalsIgnoreCase("Row")) {
                    rowScore = word[2].length();
                    System.out.println("rowScore  " + rowScore);
                    if (word[1].equalsIgnoreCase("false"))
                        game.voteRow = false;
                } else {
                    columnScore = word[2].length();
                    System.out.println("columnScore  " + columnScore);
                    if (word[1].equalsIgnoreCase("false"))
                        game.voteColumn = false;
                }
                if (voteReplyNum == voteNum) {
                    if (game.voteRow) {
                        setScore(word[3], rowScore);
                    }
                    if (game.voteColumn) {
                        setScore(word[3], columnScore);
                    }
                    updateScore();
                    rowScore = 0;
                    columnScore = 0;
                    game.voteColumn = true;
                    game.voteRow = true;
                    voteReplyNum = 0;
                    int i;
                    for (i = 0; i < players.size(); i++) {
                        if (players.get(i).ID.equalsIgnoreCase(word[3])) {
                            i = i + 1;
                            break;
                        }
                    }
                    i = i % players.size();
                    players.get(i).send("turn");
                }

            } else if (argument[0].equalsIgnoreCase("pass")) {
                String winners = "";
                if (game.pass == players.size() - 1) {
                    int winnerScore = 0;
                    ArrayList<String> winner = new ArrayList<>();
                    for (int j = 0; j < players.size(); j++) {
                        if (players.get(j).score > winnerScore) {
                            winner.clear();
                            winner.add(players.get(j).ID);
                            winnerScore = players.get(j).score;
                        } else if (players.get(j).score == winnerScore && players.get(j).score != 0) {
                            winner.add(players.get(j).ID);
                        }
                    }
                    if (winner.size() > 0) {
                        if (winner.size() == players.size()) {
                            for (int i = 0; i < players.size(); i++) {
                                players.get(i).send("gameOver*none*" + players.get(i).score);
                            }
                        } else if (winner.size() == 1) {
                            for (int i = 0; i < players.size(); i++) {
                                players.get(i).send("gameOver*" + winner.get(0) + "," + winnerScore);
                            }
                        } else {
                            for (String m : winner) {
                                winners = winners + m + ",";
                            }
                            for (int i = 0; i < players.size(); i++) {
                                players.get(i).send("gameOver*" + winners + winnerScore);
                            }
                        }
                    } else {
                        for (int i = 0; i < players.size(); i++) {
                            players.get(i).send("gameOver*none*" + players.get(i).score);
                        }
                    }
                    players.clear();
                    for (int i = 0; i < clients.size(); i++) {
                        ClientThread c = clients.get(i);
                        c.updateUser();
                        System.out.println("updateUser");
                        c.score = 0;
                    }
                    voteNum = 0;
                    voteReplyNum = 0;
                } else {
                    game.pass = game.pass + 1;
                    int i;
                    for (i = 0; i < players.size(); i++) {
                        if (players.get(i).ID.equalsIgnoreCase(this.ID)) {
                            i = i + 1;
                            break;
                        }
                    }
                    i = i % players.size();
                    players.get(i).send("turn");
                }
            }

        }


        public String login(String userID) {
            String notification;
            System.out.println(clients.size());
            if (clients.size() != 0) {
                for (int i = 0; i < clients.size(); i++) {
                    if (clients.get(i).ID.equalsIgnoreCase(userID)) {
                        notification = "notification*This user has already logged in";
                        return notification;
                    }
                }
            }
            clients.add(this);
            System.out.println(clients.size());
            this.ID = userID;
            return "Login successfully";
        }


        private void updateUser() {
            String command = "updateUser*";
            for (int i = 0; i < clients.size(); i++) {
                if (!this.ID.equalsIgnoreCase(clients.get(i).ID))
                    command = command + clients.get(i).ID + ",";
            }
            try {
                dos.writeUTF(command);
            } catch (IOException e) {
                System.out.println("Client" + this.ID + " is closed");
                clients.remove(this);

                //e.printStackTrace();
            }

        }

        private void updatePlayer() {
            String command = "updatePlayer*";
            for (int i = 0; i < players.size(); i++) {
                System.out.println(players.get(i).ID);
                command = command + players.get(i).ID + ",";
            }
            try {
                dos.writeUTF(command);
            } catch (IOException e) {
                System.out.println("Client" + this.ID + " is closed");
                clients.remove(this);

                //e.printStackTrace();
            }
        }


        private void updateScore() {
            String scoreList = "";
            for (int i = 0; i < players.size(); i++) {
                scoreList = scoreList + players.get(i).score + ",";
            }
            System.out.println(scoreList);
            for (int i = 0; i < players.size(); i++) {
                players.get(i).send("updateScore*" + scoreList);
            }
        }


        private void updateGame(String str) {
            for (int i = 0; i < players.size(); i++) {
                players.get(i).send("updateGame*" + str);
            }
        }


        private void voteRow(String str) {
            for (int i = 0; i < players.size(); i++) {
                if (!players.get(i).ID.equalsIgnoreCase(this.ID)) {
                    players.get(i).send("voteRow*" + str);
                }
            }
        }

        private void voteColumn(String str) {
            for (int i = 0; i < players.size(); i++) {
                if (!players.get(i).ID.equalsIgnoreCase(this.ID)) {
                    players.get(i).send("voteColumn*" + str);
                }
            }
        }


        public void invite(String ID, String arguement) {
            String[] userID = arguement.split(",");
            for (int j = 0; j < userID.length; j++) {
                for (int i = 0; i < clients.size(); i++) {
                    if (userID[j].equalsIgnoreCase(clients.get(i).ID)) {
                        if (players.contains(clients.get(i)))
                            this.send("notification2*" + clients.get(i).ID + " is already in a room");
                        else {
                            ClientThread c = clients.get(i);
                            c.send("invite*" + ID);
                        }


                    }

                }
            }
        }


        public void send(String str) {
            try {
                dos.writeUTF(str);
            } catch (IOException e) {
                System.out.println("Client" + this.ID + " is closed");
                clients.remove(this);

                //e.printStackTrace();
            }
        }

        public void setScore(String id, int rowScore) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).ID.equalsIgnoreCase(id)) {
                    players.get(i).score = players.get(i).score + rowScore;
                    System.out.println(players.get(i).ID + "  score  " + players.get(i).score);
                }

            }
        }


    }
}
