package ScrabbleClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VoteDialog extends JDialog implements ActionListener, Runnable {
    private JPanel contentPane;
    private JButton jButton_Yes = null;
    private JButton jButton_NO = null;
    private boolean OK = false;
    private Thread thread = null;
    private int seconds = 0;
    private final int max = 10;//max number of seconds
    private String list;

    /**
     * Launch the application.
     */

    /**
     * Create the frame.
     */
    public VoteDialog(Frame frame, String objs1) {
        super(frame,true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 244, 137);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);


        jButton_Yes = new JButton("Yes");
        jButton_Yes.addActionListener(this);
        jButton_Yes.setBounds(36, 64, 63, 23);
        contentPane.add(jButton_Yes);

        jButton_NO = new JButton("No");
        jButton_NO.addActionListener(this);
        jButton_NO.setBounds(121, 64, 63, 23);
        contentPane.add(jButton_NO);
        list=objs1;

        JLabel lblIsA = new JLabel("is "+list+" a word?");
        lblIsA.setHorizontalAlignment(SwingConstants.CENTER);
        lblIsA.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblIsA.setBounds(64, 26, 99, 14);
        contentPane.add(lblIsA);

        thread = new Thread(this);
        thread.start();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void actionPerformed(ActionEvent e){

        if (e.getSource()==jButton_Yes)
            OK = true;
        System.out.println("hhh   "+OK);
        if (e.getSource()==jButton_NO)
            OK = false;
        setVisible(false);
    }

    public void run(){
        while(seconds < max){
            seconds++;
            setTitle("vote "+seconds);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException exc){
            };
        }
        setVisible(false);
    }

    public boolean isOk(){
        System.out.println(OK);
        return OK;}
}
