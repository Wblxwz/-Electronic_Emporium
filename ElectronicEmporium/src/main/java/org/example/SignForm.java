package org.example;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class SignForm extends JFrame {

    private JPanel signform;
    private JButton signin,signup;
    private JLabel userlabel,passwdlabel;
    private JTextField usertextField;
    private JPasswordField passwordField;

    public SignForm()
    {
        signform = new JPanel();
        Add();
        Event();
        SetFrame();
    }
    private void SignIn()
    {
        Connection connection = ConnectDB();
        try
        {
            Statement statement = connection.createStatement();
            String username = usertextField.getText();
            String passwd = String.valueOf(passwordField.getPassword());
            String sql = "SELECT passwd FROM users WHERE name = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next() && passwd.equals(resultSet.getString("passwd")))
            {
                ShopForm shopForm = new ShopForm();
                dispose();
            }
            else
                JOptionPane.showMessageDialog(null,"用户名或密码错误！","错误",JOptionPane.ERROR_MESSAGE);
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    private void SignUp()
    {
        Connection connection = ConnectDB();
        try
        {
            Statement statement = connection.createStatement();
            String username = usertextField.getText();
            String passwd = String.valueOf(passwordField.getPassword());
            int rows = 0;
            String sql = "INSERT IGNORE INTO users(name,passwd) VALUES('" + username + "','" + passwd + "')";
            rows = statement.executeUpdate(sql);
            if(rows == 0 || passwd.length() < 6)
                JOptionPane.showMessageDialog(null,"此用户名已被占用或密码长度少于6位！","错误",JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(null,"注册成功！","提示",JOptionPane.PLAIN_MESSAGE);
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    //添加事件
    private void Event()
    {
        signin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignIn();
            }
        });

        signup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignUp();
            }
        });
    }
    private Connection ConnectDB()
    {
        String url = "jdbc:mysql://localhost:3306/shopping?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String uname = "root";
        String upasswd = "a2394559659";
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(url,uname,upasswd);
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return connection;
    }

    //设置登录界面窗口
    private void SetFrame()
    {
        setContentPane(signform);
        setLayout(null);
        setTitle("登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension dimension = new Dimension(800,600);
        setMinimumSize(dimension);
        setResizable(false);
        setLocationRelativeTo(null);

        pack();
        setVisible(true);
    }
    //添加部件
    private void Add()
    {
        Font font = new Font("宋体",Font.BOLD,20);

        //注册登录按钮
        signin = new JButton("登录");
        signup = new JButton("注册");
        signin.setBounds(200,400,100,50);
        signup.setBounds(500,400,100,50);
        signin.setFont(font);
        signup.setFont(font);
        signform.add(signin);
        signform.add(signup);

        //用户名密码标签
        userlabel = new JLabel("用户名：");
        passwdlabel = new JLabel("密码：");
        userlabel.setBounds(200,100,100,50);
        passwdlabel.setBounds(200,250,100,50);
        userlabel.setFont(font);
        passwdlabel.setFont(font);
        signform.add(userlabel);
        signform.add(passwdlabel);

        //用户名密码文本框
        usertextField = new JTextField(6);
        passwordField = new JPasswordField(16);
        usertextField.setBounds(300,100,300,50);
        usertextField.setFont(font);
        passwordField.setBounds(300,250,300,50);
        passwordField.setFont(font);
        //限制文本框输入长度
        LimitLength();
        signform.add(usertextField);
        signform.add(passwordField);
    }
    private void LimitLength()
    {
        usertextField.setDocument(new PlainDocument(){
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                String text = usertextField.getText();
                if(text.length() + str.length() > 6)
                {
                    return;
                }
                super.insertString(offs, str, a);
            }
        });
        passwordField.setDocument(new PlainDocument(){
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                String text = new String(passwordField.getPassword());
                if(text.length() + str.length() > 16)
                {
                    return;
                }
                super.insertString(offs, str, a);
            }
        });
    }

    public static void main(String args[])
    {
        //JFrame.setDefaultLookAndFeelDecorated(true);
        SignForm signForm = new SignForm();
    }
}
