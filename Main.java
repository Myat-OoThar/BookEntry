package group;


import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.*;
import java.awt.Color;
import javax.swing.border.Border;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends JFrame {
	
	String host = "mysql-26ea90b1-myatoothar5-1f5f.c.aivencloud.com";
	String port = "10007";
	String database = "project";
	String user = "Myat_Oo_Thar";
	String password = "AVNS_Z5VaWvb_pxjqBMtks36";
	
	String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?sslMode=REQUIRED";
	
	JPanel pTop = new JPanel();
	JPanel pBottom = new JPanel(new GridLayout(1,3));
	JPanel pMenu = new JPanel(new GridLayout(7, 1));
	JPanel pMain = new JPanel(new BorderLayout());

	JLabel lTop = new JLabel("📚 Library Management System");
	JLabel lMenu = new JLabel("DashBoard");
	JLabel login = new JLabel("Logged in: "+user);
		
	JButton b1 = new JButton("Books");
	JButton b2 = new JButton("Members");
	JButton b3 = new JButton("Borrows");
	JButton b4 = new JButton("Returns");
	JButton b5 = new JButton("Users");
	JButton b6 = new JButton("Setting");
	
	LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String formattedDate= now.format(formatter);

	public Main() {
		
		lMenu.setVerticalAlignment(SwingConstants.TOP);
		lMenu.setFont(new Font("Roboto", Font.BOLD, 15));

		pTop.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		pBottom.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		pMenu.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		pMain.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		pTop.add(lTop);

		pMenu.add(lMenu, BorderLayout.NORTH);
		lMenu.setVerticalAlignment(SwingConstants.CENTER);
		lMenu.setHorizontalAlignment(SwingConstants.CENTER);

		pMenu.add(b1);
		pMenu.add(b2);
		pMenu.add(b3);
		pMenu.add(b4);
		pMenu.add(b5);
		pMenu.add(b6);
		
		pBottom.add(login,BorderLayout.WEST);
		pBottom.setPreferredSize(new Dimension(500,50));
			
		JLabel lDate = new JLabel("Date: "+formattedDate);	
		
		pBottom.add(lDate,BorderLayout.CENTER);
		pBottom.add(lDate,BorderLayout.EAST);
						
		b1.addActionListener(e -> {
			pMain.removeAll();
		    pMain.setLayout(new BorderLayout());
			// creating table for data outputs
			String[] columns = {"Book ID","Book Title","Author Name","Publisher","Year"};
			DefaultTableModel model = new DefaultTableModel(columns,0);
			JTable table = new JTable(model);
			table.setRowHeight(25);
			
			table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));// set table header font bold
			table.getTableHeader().setBackground(new Color(160,160,160));//table header box bgcolor
			table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getPreferredSize().width,30));//table header box size
			//make the text in table CENTER
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

			for (int i = 0; i < table.getColumnCount(); i++) {
			    table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			}
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				System.out.println("Driver created");
				Connection connection = DriverManager.getConnection(url, user, password);
				System.out.println("Database connected");
				String sqlSelect = "select * from BookList";
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlSelect);
				while(resultSet.next()) {
					model.addRow(new Object[] {
							resultSet.getInt(1),
							resultSet.getString(2),
							resultSet.getString(3),
							resultSet.getString(4),
							resultSet.getInt(5)
					});
				}
				
				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.setPreferredSize(new java.awt.Dimension(500,250));
				pMain.add(scrollPane, BorderLayout.CENTER);		
				pMain.revalidate();
			    pMain.repaint();
				connection.close();
			}//try
			catch(SQLException se) {
				se.printStackTrace();
			}catch(ClassNotFoundException ce) {
				System.out.println("ClassNotFoundException");
				ce.printStackTrace();
				
			}//catch	
		});
		
		b2.addActionListener(e -> {
			pMain.removeAll();
		    pMain.setLayout(new BorderLayout());
			// creating table for data outputs
			String[] columns = {"Member ID","Name","Phone Number","Email","Address","Registration Date"};
			DefaultTableModel model = new DefaultTableModel(columns,0);
			JTable table = new JTable(model);
			table.setRowHeight(25);
			
			table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));// set table header font bold
			table.getTableHeader().setBackground(new Color(160,160,160));//table header box bgcolor
			table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getPreferredSize().width,30));//table header box size
			//make the text in table CENTER
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

			for (int i = 0; i < table.getColumnCount(); i++) {
			    table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			}
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				System.out.println("Driver created");
				Connection connection = DriverManager.getConnection(url, user, password);
				System.out.println("Database connected");
				String sqlSelect = "select * from members";
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlSelect);
				while(resultSet.next()) {
					model.addRow(new Object[] {
							resultSet.getInt(1),
							resultSet.getString(2),
							resultSet.getString(3),
							resultSet.getString(4),
							resultSet.getString(5),
							resultSet.getString(6)
					});
				}
				
				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.setPreferredSize(new java.awt.Dimension(500,250));
				pMain.add(scrollPane, BorderLayout.CENTER);		
				pMain.revalidate();
			    pMain.repaint();
				connection.close();
			}//try
			catch(SQLException se) {
				se.printStackTrace();
			}catch(ClassNotFoundException ce) {
				System.out.println("ClassNotFoundException");
				ce.printStackTrace();
				
			}//catch	
			
		});
		
		b3.addActionListener(e -> {
			pMain.removeAll();
		    pMain.setLayout(new BorderLayout());
			// creating table for data outputs
			String[] columns = {"Borrow ID","Book ID","Member ID","Borrow Date","Due Date","Return Date"};
			DefaultTableModel model = new DefaultTableModel(columns,0);
			JTable table = new JTable(model);
			table.setRowHeight(25);
			
			table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));// set table header font bold
			table.getTableHeader().setBackground(new Color(160,160,160));//table header box bgcolor
			table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getPreferredSize().width,30));//table header box size
			//make the text in table CENTER
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

			for (int i = 0; i < table.getColumnCount(); i++) {
			    table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			}
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				System.out.println("Driver created");
				Connection connection = DriverManager.getConnection(url, user, password);
				System.out.println("Database connected");
				String sqlSelect = "select * from borrows";
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlSelect);
				while(resultSet.next()) {
					model.addRow(new Object[] {
							resultSet.getInt(1),
							resultSet.getInt(2),
							resultSet.getInt(3),
							resultSet.getString(4),
							resultSet.getString(5),
							resultSet.getString(6)
					});
				}
				
				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.setPreferredSize(new java.awt.Dimension(500,250));
				pMain.add(scrollPane, BorderLayout.CENTER);		
				pMain.revalidate();
			    pMain.repaint();
				connection.close();
			}//try
			catch(SQLException se) {
				se.printStackTrace();
			}catch(ClassNotFoundException ce) {
				System.out.println("ClassNotFoundException");
				ce.printStackTrace();
				
			}//catch	
		});
		
		add(pTop, BorderLayout.NORTH);
		add(pBottom, BorderLayout.SOUTH);
		add(pMenu, BorderLayout.WEST);
		add(pMain, BorderLayout.CENTER);
		
		
	}

	public static void main(String[] args) {
		Main frame = new Main();

		frame.setTitle("Library Management System");
		frame.setSize(700, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

}
