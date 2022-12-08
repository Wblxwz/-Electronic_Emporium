package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class ShopForm extends JFrame {
        private JScrollPane pane;
        private JPanel shopform;
        private JTable table;
        private JTextField sousuokuang;
        private JButton sousuo;
        private Font font = new Font("宋体",Font.BOLD,20);
        private JLabel ss;
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
        public ShopForm()
        {
            shopform = new JPanel();
            Table();
            Add();
            Event();
            Init();
        }
        private void SouSuo()
        {
            Connection connection = ConnectDB();
            String name = sousuokuang.getText();
            if(name.isEmpty())
            {
                try
                {
                    Statement statement = connection.createStatement();
                    String sql = "SELECT * FROM goods";
                    ResultSet resultSet = statement.executeQuery(sql);
                    DefaultTableModel model = new DefaultTableModel();
                    model.addColumn("商品名称");
                    model.addColumn("商品价格");
                    while(resultSet.next())
                    {
                        Object[] o = {resultSet.getString("name"),resultSet.getFloat("value")};
                        model.addRow(o);
                    }
                    table.setModel(model);
                }
                catch (SQLException sqlException)
                {
                    sqlException.printStackTrace();
                }
            }
            else
            {
                try
                {
                    Statement statement = connection.createStatement();
                    String sql = "SELECT * FROM goods WHERE name = '"+ name +"'";
                    ResultSet resultSet = statement.executeQuery(sql);
                    DefaultTableModel model = new DefaultTableModel();
                    model.addColumn("商品名称");
                    model.addColumn("商品价格");
                    if(resultSet.next())
                    {
                        model.setRowCount(0);
                        Object []o = {resultSet.getString("name"),resultSet.getFloat("value")};
                        model.addRow(o);
                        table.setModel(model);
                    }
                    else
                        JOptionPane.showMessageDialog(null,"未找到该商品！","错误",JOptionPane.ERROR_MESSAGE);
                }
                catch (SQLException sqlException)
                {
                    sqlException.printStackTrace();
                }

            }
        }
        private void Event()
        {
            sousuokuang.setDocument(new PlainDocument(){
                @Override
                public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                    String text = ss.getText();
                    if(text.length() + str.length() > 10)
                    {
                        return;
                    }
                    super.insertString(offs, str, a);
                }
            });
            sousuo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SouSuo();
                }
            });
        }
        private void Add()
        {
            JPanel jPanel = new JPanel();
            jPanel.setLayout(null);
            ss = new JLabel("商品名称:");
            ss.setFont(font);
            sousuokuang = new JTextField();
            sousuokuang.setFont(font);
            sousuo = new JButton("搜索");
            sousuo.setFont(font);
            jPanel.add(sousuokuang);
            jPanel.add(sousuo);
            jPanel.add(ss);
            sousuo.setBounds(600,100,100,50);
            sousuokuang.setBounds(200,100,300,50);
            ss.setBounds(50,75,200,100);
            jPanel.setBounds(0,350,800,250);
            shopform.add(jPanel);
        }
        private void Table()
        {
            Connection connection = ConnectDB();
            try
            {
                Statement statement = connection.createStatement();
                String sql = "SELECT * FROM goods";
                ResultSet resultSet = statement.executeQuery(sql);
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("商品名称");
                model.addColumn("商品价格");
                while(resultSet.next())
                {
                    Object[] o = {resultSet.getString("name"),resultSet.getFloat("value")};
                    model.addRow(o);
                }
                table = new JTable(model){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                table.setRowHeight(50);
                table.setFont(font);
                pane = new JScrollPane(table);
                pane.setBounds(0,0,800,350);
                shopform.add(pane);
            }
            catch (SQLException sqlException)
            {
                sqlException.printStackTrace();
            }
        }
        private void Init()
        {
            setContentPane(shopform);
            setLayout(null);
            setTitle("电子商城");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Dimension dimension = new Dimension(800,600);
            setMinimumSize(dimension);
            setResizable(false);
            setLocationRelativeTo(null);

            pack();
            setVisible(true);
        }
}
