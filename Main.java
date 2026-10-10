package group;


import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.awt.Font;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.awt.Color;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends JFrame {

	
	String host = "mysql-26ea90b1-myatoothar5-1f5f.c.aivencloud.com";
	String port = "10007";
	String database = "project";
	String user = "avnadmin";//avnadmin
	String password = "AVNS_Z5VaWvb_pxjqBMtks36";//AVNS_Z5VaWvb_pxjqBMtks36
	//String url = "jdbc:mysql://avnadmin:AVNS_Z5VaWvb_pxjqBMtks36@mysql-26ea90b1-myatoothar5-1f5f.c.aivencloud.com:10007/project?ssl-mode=REQUIRED";
	String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?sslMode=REQUIRED";
	
	JPanel pTop = new JPanel();
	JPanel pBottom = new JPanel(new GridLayout(1,3));
	JPanel pMenu = new JPanel(new GridLayout(7, 1));
	JPanel pMain = new JPanel(new BorderLayout());
	JPanel pMainPanel = new JPanel();
	JPanel panel = new JPanel(new BorderLayout());

	JLabel lTop = new JLabel("📚 Library Management System");
	JLabel lMenu = new JLabel("DashBoard");
	JLabel login = new JLabel("Logged in: "+user);
		
	JButton b1 = new JButton("Books");
	JButton b2 = new JButton("Members");
	JButton b3 = new JButton("Borrows");
	JButton b4 = new JButton("Returns");
	JButton b5 = new JButton("Users");
	JButton b6 = new JButton("Setting");
	JButton bEdit = new JButton("Edit");
	
	JButton newBtn1 = new JButton("OK");
    JButton newBtn2 = new JButton("Delete");
    JButton newBtn3 = new JButton("Cancel");
	
	LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String formattedDate= now.format(formatter);


	Object selectedValue;
	Object inputedValue;
	int selectedRow = -1;
	int selectedColumn = -1;
	int bookId;
	int memberId;
	int borrowId;
	int returnId;
	int userId;

    
    class ShowBookList implements ActionListener{

    	public void actionPerformed(ActionEvent e) {

			for (ActionListener listener : bEdit.getActionListeners()) {
				bEdit.removeActionListener(listener);
			}

    		pMain.removeAll();
    	    pMain.setLayout(new BorderLayout());
    	    pMainPanel.add(bEdit);
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


			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {

					selectedRow = table.rowAtPoint(e.getPoint());
					selectedColumn = table.columnAtPoint(e.getPoint());

					if (selectedRow != -1 && selectedColumn != -1) {
						bookId = (int) table.getValueAt(selectedRow, 0);
						System.out.println("Book Id: " + bookId);
						System.out.println("Row: " + selectedRow);
						System.out.println("Column: " + selectedColumn);

						Object selectedValue = table.getValueAt(selectedRow, selectedColumn);
						System.out.println("Value: " + selectedValue);
						newBtn1.setBackground(Color.GREEN);
						newBtn2.setBackground(Color.RED);
						newBtn3.setBackground(Color.YELLOW);
						bEdit.setBackground(Color.GREEN);
					}
				}
			});



			newBtn3.addActionListener( b3e -> {
				table.clearSelection();
				newBtn1.setBackground(Color.WHITE);
				newBtn2.setBackground(Color.WHITE);
				newBtn3.setBackground(Color.WHITE);

			});

    		
    		try {
    			Class.forName("com.mysql.cj.jdbc.Driver");
    			System.out.println("Driver created");
    			Connection connection = DriverManager.getConnection(url, user, password);
    			System.out.println("Database connected");			
    			Statement statement = connection.createStatement();
    			String sqlSelect = "select * from BookList";
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

			bEdit.addActionListener(be -> {
				bEdit.setVisible(false);
				JFrame bookEditFrame = new JFrame("Book Edit Frame");
				JPanel p1 = new JPanel(new FlowLayout());
				JPanel p2 = new JPanel(new FlowLayout());
				JPanel panel = new JPanel(new BorderLayout(10,10));

				JLabel l1 = new JLabel("New Value");

				JTextField jt1 = new JTextField();
				jt1.setPreferredSize(new Dimension(200,30));

				panel.add(p1,BorderLayout.CENTER);
				panel.add(p2,BorderLayout.SOUTH);

				p1.add(l1);
				p1.add(jt1);

				p2.add(newBtn1);
				p2.add(newBtn3);

				newBtn1.addActionListener(a -> {

					inputedValue = jt1.getText();
					System.out.println(inputedValue);

					if (selectedColumn == -1) {
						System.out.println("Please select a column first.");
						return;
					}

					String[] columnsql = { "bookID", "bookTitle", "author", "publisher", "publishYear" };

					if (selectedColumn < 0 || selectedColumn >= columnsql.length) {
						System.out.println("Invalid column index: " + selectedColumn);
						return;
					}

					String columnName = columnsql[selectedColumn];

					System.out.println("Selected column: " + selectedColumn);
					System.out.println("Selected column name: " + columnName);

					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection connection = DriverManager.getConnection(url, user, password);
						String booksql = "UPDATE BookList SET " + columnName + " = ? WHERE bookID = ?";

						PreparedStatement ps = connection.prepareStatement(booksql);
						ps.setObject(1, inputedValue);
						ps.setInt(2, bookId);

						int updated = ps.executeUpdate();
						System.out.println("Rows updated: " + updated);


						ps.close();
						connection.close();

					} catch (ClassNotFoundException | SQLException se) {
						se.printStackTrace();
					}


					jt1.setText("");
					inputedValue = null;

					b1.doClick();

				});


				bookEditFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				bookEditFrame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						bEdit.setVisible(true);
					}
				});
				bookEditFrame.setSize(300,200);
				bookEditFrame.setResizable(false);
				bookEditFrame.setVisible(true);
				bookEditFrame.setLocation(1400,250);
				bookEditFrame.add(panel);

				newBtn3.addActionListener( b3e -> {
					bookEditFrame.dispose();
				});



				newBtn1.setPreferredSize(new Dimension(100,50));
				newBtn3.setPreferredSize(new Dimension(100,50));


				newBtn1.setBackground(Color.WHITE);
				newBtn2.setBackground(Color.WHITE);
				newBtn3.setBackground(Color.WHITE);

				b2.addActionListener(e2 ->{removeEditFrame(bookEditFrame);});
				b3.addActionListener(e2 ->{removeEditFrame(bookEditFrame);});
				b4.addActionListener(e2 ->{removeEditFrame(bookEditFrame);});
				b5.addActionListener(e2 ->{removeEditFrame(bookEditFrame);});

				// Crucial step: Tell Swing to rebuild the layout and redraw the screen
				pMainPanel.revalidate();
				pMainPanel.repaint();


			});

    	}
		
	}

    class ShowMembers implements ActionListener{
    	public void actionPerformed(ActionEvent e) {

			for (ActionListener listener : bEdit.getActionListeners()) {
				bEdit.removeActionListener(listener);
			}

			pMain.removeAll();
		    pMain.setLayout(new BorderLayout());
		    pMainPanel.add(bEdit);
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

			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {


					selectedRow = table.rowAtPoint(e.getPoint());
					selectedColumn = table.columnAtPoint(e.getPoint());


					if (selectedRow != -1 && selectedColumn != -1) {
						memberId = (int) table.getValueAt(selectedRow, 0);
						System.out.println("Row: " + selectedRow);
						System.out.println("Column: " + selectedColumn);

						selectedValue = table.getValueAt(selectedRow, selectedColumn);
						System.out.println("Value: " + selectedValue);
					}
				}
			});

			bEdit.addActionListener(be -> {
				bEdit.setVisible(false);
				JFrame memberEditFrame = new JFrame("Member Edit Frame");

				JPanel p1 = new JPanel(new FlowLayout());
				JPanel p2 = new JPanel(new FlowLayout());
				JPanel panel = new JPanel(new BorderLayout(10,10));

				JLabel l1 = new JLabel("New Value");

				JTextField jt1 = new JTextField();
				jt1.setPreferredSize(new Dimension(200,30));

				panel.add(p1,BorderLayout.CENTER);
				panel.add(p2,BorderLayout.SOUTH);

				p1.add(l1);
				p1.add(jt1);

				p2.add(newBtn1);
				p2.add(newBtn3);

				newBtn1.addActionListener(a -> {

					inputedValue = jt1.getText();
					System.out.println(inputedValue);

					if (selectedColumn == -1) {
						System.out.println("Please select a column first.");
						return;
					}



					String[] columnsql = { "memberId", "memberName", "phone", "email", "address", "registrationDate" };

					if (selectedColumn < 0 || selectedColumn >= columnsql.length) {
						System.out.println("Invalid column index: " + selectedColumn);
						return;
					}

					String columnName = columnsql[selectedColumn];

					System.out.println("Selected column: " + selectedColumn);
					System.out.println("Selected column name: " + columnName);

					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection connection = DriverManager.getConnection(url, user, password);
						String membersql = "UPDATE members SET " + columnName + " = ? WHERE memberId = ?";

						PreparedStatement ps = connection.prepareStatement(membersql);
						ps.setObject(1, inputedValue);
						ps.setInt(2, memberId);

						int updated = ps.executeUpdate();
						System.out.println("SQL: " + membersql);
						System.out.println("Column: " + columnName);
						System.out.println("New value: " + inputedValue);
						System.out.println("Member ID: " + memberId);
						System.out.println("Rows updated: " + updated);


						ps.close();
						connection.close();

					} catch (ClassNotFoundException | SQLException se) {
						se.printStackTrace();
					}


					jt1.setText("");
					inputedValue = null;

					b2.doClick();

				});


				memberEditFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				memberEditFrame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						bEdit.setVisible(true);
					}
				});
				memberEditFrame.setSize(300,200);
				memberEditFrame.setResizable(false);
				memberEditFrame.setVisible(true);
				memberEditFrame.setLocation(1400,250);
				memberEditFrame.add(panel);

				newBtn3.addActionListener( b3e -> {
					memberEditFrame.dispose();
				});



				newBtn1.setPreferredSize(new Dimension(100,50));
				newBtn3.setPreferredSize(new Dimension(100,50));


				newBtn1.setBackground(Color.WHITE);
				newBtn2.setBackground(Color.WHITE);
				newBtn3.setBackground(Color.WHITE);

				b1.addActionListener(e2 ->{removeEditFrame(memberEditFrame);});
				b3.addActionListener(e2 ->{removeEditFrame(memberEditFrame);});
				b4.addActionListener(e2 ->{removeEditFrame(memberEditFrame);});
				b5.addActionListener(e2 ->{removeEditFrame(memberEditFrame);});

				// Crucial step: Tell Swing to rebuild the layout and redraw the screen
				pMainPanel.revalidate();
				pMainPanel.repaint();


			});
			
		}
    }

    class ShowBorrow implements ActionListener{
    	public void actionPerformed(ActionEvent e){

			for (ActionListener listener : bEdit.getActionListeners()) {
				bEdit.removeActionListener(listener);
			}

			pMain.removeAll();
		    pMain.setLayout(new BorderLayout());
		    pMainPanel.add(bEdit);
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

			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {

					selectedRow = table.rowAtPoint(e.getPoint());
					selectedColumn = table.columnAtPoint(e.getPoint());


					if (selectedRow != -1 && selectedColumn != -1) {
						borrowId = (int) table.getValueAt(selectedRow, 0);
						System.out.println("Row: " + selectedRow);
						System.out.println("Column: " + selectedColumn);

						Object selectedValue = table.getValueAt(selectedRow, selectedColumn);
						System.out.println("Value: " + selectedValue);
					}
				}
			});

			bEdit.addActionListener(be -> {
				bEdit.setVisible(false);
				JFrame borrowEditFrame = new JFrame("Borrow Edit Frame");
				JPanel p1 = new JPanel(new FlowLayout());
				JPanel p2 = new JPanel(new FlowLayout());
				JPanel panel = new JPanel(new BorderLayout(10,10));

				JLabel l1 = new JLabel("New Value");

				JTextField jt1 = new JTextField();
				jt1.setPreferredSize(new Dimension(200,30));

				panel.add(p1,BorderLayout.CENTER);
				panel.add(p2,BorderLayout.SOUTH);

				p1.add(l1);
				p1.add(jt1);

				p2.add(newBtn1);
				p2.add(newBtn3);

				newBtn1.addActionListener(a -> {

					inputedValue = jt1.getText();
					System.out.println(inputedValue);

					if (selectedColumn == -1) {
						System.out.println("Please select a column first.");
						return;
					}


					String[] columnsql = { "borrowId", "bookID", "memberId", "borrowDate", "dueDate", "returnDate" };

					if (selectedColumn < 0 || selectedColumn >= columnsql.length) {
						System.out.println("Invalid column index: " + selectedColumn);
						return;
					}

					String columnName = columnsql[selectedColumn];

					System.out.println("Selected column: " + selectedColumn);
					System.out.println("Selected column name: " + columnName);

					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection connection = DriverManager.getConnection(url, user, password);
						String borrowsql = "UPDATE borrows SET " + columnName + " = ? WHERE borrowId = ?";

						PreparedStatement ps = connection.prepareStatement(borrowsql);
						ps.setObject(1, inputedValue);
						ps.setInt(2, borrowId);

						int updated = ps.executeUpdate();
						System.out.println("Rows updated: " + updated);


						ps.close();
						connection.close();

					} catch (ClassNotFoundException | SQLException se) {
						se.printStackTrace();
					}


					jt1.setText("");
					inputedValue = null;

					b3.doClick();

				});


				borrowEditFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				borrowEditFrame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						bEdit.setVisible(true);
					}
				});
				borrowEditFrame.setSize(300,200);
				borrowEditFrame.setResizable(false);
				borrowEditFrame.setVisible(true);
				borrowEditFrame.setLocation(1300,250);
				borrowEditFrame.add(panel);

				newBtn3.addActionListener( b3e -> {
					borrowEditFrame.dispose();
				});



				newBtn1.setPreferredSize(new Dimension(100,50));
				newBtn3.setPreferredSize(new Dimension(100,50));


				newBtn1.setBackground(Color.WHITE);
				newBtn2.setBackground(Color.WHITE);
				newBtn3.setBackground(Color.WHITE);

				b2.addActionListener(e2 ->{removeEditFrame(borrowEditFrame);});
				b3.addActionListener(e2 ->{removeEditFrame(borrowEditFrame);});
				b4.addActionListener(e2 ->{removeEditFrame(borrowEditFrame);});
				b5.addActionListener(e2 ->{removeEditFrame(borrowEditFrame);});

				// Crucial step: Tell Swing to rebuild the layout and redraw the screen
				pMainPanel.revalidate();
				pMainPanel.repaint();


			});

		}
    }
   
    class ShowReturn implements ActionListener{
    	public void actionPerformed(ActionEvent e){

			for (ActionListener listener : bEdit.getActionListeners()) {
				bEdit.removeActionListener(listener);
			}

			pMain.removeAll();
		    pMain.setLayout(new BorderLayout());
		    pMainPanel.add(bEdit);
			// creating table for data outputs
			String[] columns = {"Return ID","Borrow ID","Return Date","Fine"};
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
				String sqlSelect = "select * from returns";
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlSelect);
				while(resultSet.next()) {
					model.addRow(new Object[] {
							resultSet.getInt(1),
							resultSet.getInt(2),
							resultSet.getString(3),
							resultSet.getDouble(4),
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

			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					selectedRow = table.rowAtPoint(e.getPoint());
					selectedColumn = table.columnAtPoint(e.getPoint());

					if (selectedRow != -1 && selectedColumn != -1) {
						returnId = (int) table.getValueAt(selectedRow, 0);
						System.out.println("Row: " + selectedRow);
						System.out.println("Column: " + selectedColumn);

						selectedValue = table.getValueAt(selectedRow, selectedColumn);
						System.out.println("Value: " + selectedValue);
					}
				}
			});

			bEdit.addActionListener(be -> {
				bEdit.setVisible(false);
				JFrame returnEditFrame = new JFrame("Return Edit Frame");
				JPanel p1 = new JPanel(new FlowLayout());
				JPanel p2 = new JPanel(new FlowLayout());
				JPanel panel = new JPanel(new BorderLayout(10,10));

				JLabel l1 = new JLabel("New Value");

				JTextField jt1 = new JTextField();
				jt1.setPreferredSize(new Dimension(200,30));

				panel.add(p1,BorderLayout.CENTER);
				panel.add(p2,BorderLayout.SOUTH);

				p1.add(l1);
				p1.add(jt1);

				p2.add(newBtn1);
				p2.add(newBtn3);

				newBtn1.addActionListener(a -> {

					inputedValue = jt1.getText();
					System.out.println(inputedValue);

					if (selectedColumn == -1) {
						System.out.println("Please select a column first.");
						return;
					}

					String[] columnsql = { "returnId", "borrowId", "returnDate", "fine" };

					if (selectedColumn < 0 || selectedColumn >= columnsql.length) {
						System.out.println("Invalid column index: " + selectedColumn);
						return;
					}

					String columnName = columnsql[selectedColumn];

					System.out.println("Selected column: " + selectedColumn);
					System.out.println("Selected column name: " + columnName);

					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection connection = DriverManager.getConnection(url, user, password);
						String returnsql = "UPDATE returns SET " + columnName + " = ? WHERE returnId = ?";

						PreparedStatement ps = connection.prepareStatement(returnsql);
						ps.setObject(1, inputedValue);
						ps.setInt(2, returnId);

						int updated = ps.executeUpdate();
						System.out.println("Rows updated: " + updated);


						ps.close();
						connection.close();

					} catch (ClassNotFoundException | SQLException se) {
						se.printStackTrace();
					}


					jt1.setText("");
					inputedValue = null;

					b4.doClick();

				});


				returnEditFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				returnEditFrame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						bEdit.setVisible(true);
					}
				});
				returnEditFrame.setSize(300,200);
				returnEditFrame.setResizable(false);
				returnEditFrame.setVisible(true);
				returnEditFrame.setLocation(1400,250);
				returnEditFrame.add(panel);

				newBtn3.addActionListener( b3e -> {
					returnEditFrame.dispose();
				});



				newBtn1.setPreferredSize(new Dimension(100,50));
				newBtn3.setPreferredSize(new Dimension(100,50));


				newBtn1.setBackground(Color.WHITE);
				newBtn2.setBackground(Color.WHITE);
				newBtn3.setBackground(Color.WHITE);

				b2.addActionListener(e2 ->{removeEditFrame(returnEditFrame);});
				b3.addActionListener(e2 ->{removeEditFrame(returnEditFrame);});
				b1.addActionListener(e2 ->{removeEditFrame(returnEditFrame);});
				b5.addActionListener(e2 ->{removeEditFrame(returnEditFrame);});

				// Crucial step: Tell Swing to rebuild the layout and redraw the screen
				pMainPanel.revalidate();
				pMainPanel.repaint();


			});
			
		}
    }
    
    class ShowUsers implements ActionListener{
    	public void actionPerformed(ActionEvent e){

			for (ActionListener listener : bEdit.getActionListeners()) {
				bEdit.removeActionListener(listener);
			}

			pMain.removeAll();
		    pMain.setLayout(new BorderLayout());
		    pMainPanel.add(bEdit);
			// creating table for data outputs
			String[] columns = {"User ID","User Name","Email","Phone","Role"};
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
				String sqlSelect = "select * from users";
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlSelect);
				while(resultSet.next()) {
					model.addRow(new Object[] {
							resultSet.getInt(1),
							resultSet.getString(2),
							resultSet.getString	(3),
							resultSet.getString(4),
							resultSet.getString(5)
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

			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					selectedRow = table.rowAtPoint(e.getPoint());
					selectedColumn = table.columnAtPoint(e.getPoint());
					userId = (int) table.getValueAt(selectedRow, 0);
					System.out.println("User ID: "+ userId);
					if (selectedRow != -1 && selectedColumn != -1) {

						System.out.println("Row: " + selectedRow);
						System.out.println("Column: " + selectedColumn);

						selectedValue = table.getValueAt(selectedRow, selectedColumn);
						System.out.println("Value: " + selectedValue);
					}
				}
			});

			bEdit.addActionListener(be -> {
				bEdit.setVisible(false);
				JFrame userEditFrame = new JFrame("User Edit Frame");
				JPanel p1 = new JPanel(new FlowLayout());
				JPanel p2 = new JPanel(new FlowLayout());
				JPanel panel = new JPanel(new BorderLayout(10,10));

				JLabel l1 = new JLabel("New Value");

				JTextField jt1 = new JTextField();
				jt1.setPreferredSize(new Dimension(200,30));

				panel.add(p1,BorderLayout.CENTER);
				panel.add(p2,BorderLayout.SOUTH);

				p1.add(l1);
				p1.add(jt1);

				p2.add(newBtn1);
				p2.add(newBtn3);

				newBtn1.addActionListener(a -> {

					inputedValue = jt1.getText();
					System.out.println(inputedValue);

					if (selectedColumn == -1) {
						System.out.println("Please select a column first.");
						return;
					}

					String[] columnsql = { "userId", "userName", "email", "phone", "role", "password" };

					if (selectedColumn < 0 || selectedColumn >= columnsql.length) {
						System.out.println("Invalid column index: " + selectedColumn);
						return;
					}

					String columnName = columnsql[selectedColumn];

					System.out.println("Selected column: " + selectedColumn);
					System.out.println("Selected column name: " + columnName);


					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection connection = DriverManager.getConnection(url, user, password);
						String usersql = "UPDATE BookList SET " + columnName + " = ? WHERE userId = ?";

						PreparedStatement ps = connection.prepareStatement(usersql);
						ps.setObject(1, inputedValue);
						ps.setInt(2, userId);

						int updated = ps.executeUpdate();
						System.out.println("Rows updated: " + updated);


						ps.close();
						connection.close();

					} catch (ClassNotFoundException | SQLException se) {
						se.printStackTrace();
					}


					jt1.setText("");
					inputedValue = null;

					b5.doClick();

				});


				userEditFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				userEditFrame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						bEdit.setVisible(true);
					}
				});
				userEditFrame.setSize(300,200);
				userEditFrame.setResizable(false);
				userEditFrame.setVisible(true);
				userEditFrame.setLocation(1400,250);
				userEditFrame.add(panel);

				newBtn3.addActionListener( b3e -> {
					userEditFrame.dispose();
				});



				newBtn1.setPreferredSize(new Dimension(100,50));
				newBtn3.setPreferredSize(new Dimension(100,50));


				newBtn1.setBackground(Color.WHITE);
				newBtn2.setBackground(Color.WHITE);
				newBtn3.setBackground(Color.WHITE);

				b2.addActionListener(e2 ->{removeEditFrame(userEditFrame);});
				b3.addActionListener(e2 ->{removeEditFrame(userEditFrame);});
				b4.addActionListener(e2 ->{removeEditFrame(userEditFrame);});
				b5.addActionListener(e2 ->{removeEditFrame(userEditFrame);});

				// Crucial step: Tell Swing to rebuild the layout and redraw the screen
				pMainPanel.revalidate();
				pMainPanel.repaint();


			});

		}
    }
    
    public void clickB1() {
		b1.doClick();
    }
    
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
						
		b1.addActionListener(new ShowBookList());		
		b2.addActionListener(new ShowMembers());	
		b3.addActionListener(new ShowBorrow());	
		b4.addActionListener(new ShowReturn());		
		b5.addActionListener(new ShowUsers());	


		add(pTop, BorderLayout.NORTH);
		add(pBottom, BorderLayout.SOUTH);
		add(pMenu, BorderLayout.WEST);
		
		panel.add(pMain,BorderLayout.CENTER);
		panel.add(pMainPanel, BorderLayout.SOUTH);
		pMainPanel.setPreferredSize(new Dimension(500,40));
		add(panel, BorderLayout.CENTER);
		
		
		
	}
	void removeEditFrame(JFrame frame){
		frame.dispose();
	}

	public static void main(String[] args) {
		Main frame = new Main();

		frame.setTitle("Library Management System");
		frame.setSize(800, 600);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

}