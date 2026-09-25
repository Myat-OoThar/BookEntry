package group;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends JFrame {

	// --- Color Palette Definitions ---
	private static final Color COLOR_BG_DARK = new Color(18, 18, 18);        // Main content background
	private static final Color COLOR_PANEL_DARK = new Color(30, 30, 46);     // Sidebar & Top/Bottom panel
	private static final Color COLOR_BTN_BG = new Color(42, 43, 61);        // Normal button color
	private static final Color COLOR_BTN_HOVER = new Color(108, 92, 231);   // Hover accent (Purple)
	private static final Color COLOR_TEXT_MAIN = Color.WHITE;               // Main text color
	private static final Color COLOR_TEXT_MUTED = new Color(160, 160, 160);  // Secondary status text
	private static final Color COLOR_TABLE_HEADER = new Color(45, 52, 54);  // Table header background

	String host = "mysql-26ea90b1-myatoothar5-1f5f.c.aivencloud.com";
	String port = "10007";
	String database = "project";
	String user = "avnadmin";
	String password = "AVNS_Z5VaWvb_pxjqBMtks36";
	String url = "jdbc:mysql://avnadmin:AVNS_Z5VaWvb_pxjqBMtks36@mysql-26ea90b1-myatoothar5-1f5f.c.aivencloud.com:10007/project?ssl-mode=REQUIRED";

	JPanel pTop = new JPanel();
	JPanel pBottom = new JPanel(new BorderLayout());
	JPanel pMenu = new JPanel(new GridLayout(7, 1, 0, 8)); // 8px vertical gap between menu items
	JPanel pMain = new JPanel(new BorderLayout());
	JPanel pMainPanel = new JPanel();
	JPanel panel = new JPanel(new BorderLayout());

	JLabel lTop = new JLabel("📚 Library Management System");
	JLabel lMenu = new JLabel("DashBoard");
	JLabel login = new JLabel("Logged in: " + user);

	JButton b1 = createStyledButton("Books");
	JButton b2 = createStyledButton("Members");
	JButton b3 = createStyledButton("Borrows");
	JButton b4 = createStyledButton("Returns");
	JButton b5 = createStyledButton("Users");
	JButton b6 = createStyledButton("Setting");
	JButton bEditBook = createStyledButton("Edit");

	JButton newBtn1 = createStyledButton("Add");
	JButton newBtn2 = createStyledButton("Delete");
	JButton newBtn3 = createStyledButton("Cancel");

	LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	String formattedDate = now.format(formatter);

	// Helper method to style buttons with modern flat look and dynamic hover effects
	private static JButton createStyledButton(String text) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btn.setForeground(COLOR_TEXT_MAIN);
		btn.setBackground(COLOR_BTN_BG);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setBackground(COLOR_BTN_HOVER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btn.setBackground(COLOR_BTN_BG);
			}
		});

		return btn;
	}

	// Helper method to apply table dark theme styling
	private void applyTableStyle(JTable table) {
		table.setRowHeight(28);
		table.setBackground(COLOR_BG_DARK);
		table.setForeground(COLOR_TEXT_MAIN);
		table.setGridColor(new Color(50, 50, 50));
		table.setSelectionBackground(COLOR_BTN_HOVER);
		table.setSelectionForeground(COLOR_TEXT_MAIN);

		// Header Styling
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
		table.getTableHeader().setBackground(COLOR_TABLE_HEADER);
		table.getTableHeader().setForeground(COLOR_TEXT_MAIN);
		table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getPreferredSize().width, 32));

		// Center Align Renderer
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		centerRenderer.setBackground(COLOR_BG_DARK);
		centerRenderer.setForeground(COLOR_TEXT_MAIN);

		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
	}

	class ShowBookList implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			pMain.removeAll();
			pMain.setLayout(new BorderLayout());
			pMainPanel.add(bEditBook);

			String[] columns = {"Book ID", "Book Title", "Author Name", "Publisher", "Year"};
			DefaultTableModel model = new DefaultTableModel(columns, 0);
			JTable table = new JTable(model);
			applyTableStyle(table);

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection connection = DriverManager.getConnection(url, user, password);
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
				scrollPane.getViewport().setBackground(COLOR_BG_DARK);
				scrollPane.setBorder(BorderFactory.createEmptyBorder());

				pMain.add(scrollPane, BorderLayout.CENTER);
				pMain.revalidate();
				pMain.repaint();
				connection.close();
			} catch(SQLException | ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}

	class ShowMembers implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			pMain.removeAll();
			pMain.setLayout(new BorderLayout());
			pMainPanel.add(bEditBook);

			String[] columns = {"Member ID", "Name", "Phone Number", "Email", "Address", "Registration Date"};
			DefaultTableModel model = new DefaultTableModel(columns, 0);
			JTable table = new JTable(model);
			applyTableStyle(table);

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection connection = DriverManager.getConnection(url, user, password);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("select * from members");
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
				scrollPane.getViewport().setBackground(COLOR_BG_DARK);
				scrollPane.setBorder(BorderFactory.createEmptyBorder());

				pMain.add(scrollPane, BorderLayout.CENTER);
				pMain.revalidate();
				pMain.repaint();
				connection.close();
			} catch(SQLException | ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}

	class ShowBorrow implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			pMain.removeAll();
			pMain.setLayout(new BorderLayout());
			pMainPanel.add(bEditBook);

			String[] columns = {"Borrow ID", "Book ID", "Member ID", "Borrow Date", "Due Date", "Return Date"};
			DefaultTableModel model = new DefaultTableModel(columns, 0);
			JTable table = new JTable(model);
			applyTableStyle(table);

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection connection = DriverManager.getConnection(url, user, password);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("select * from borrows");
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
				scrollPane.getViewport().setBackground(COLOR_BG_DARK);
				scrollPane.setBorder(BorderFactory.createEmptyBorder());

				pMain.add(scrollPane, BorderLayout.CENTER);
				pMain.revalidate();
				pMain.repaint();
				connection.close();
			} catch(SQLException | ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}

	class ShowReturn implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			pMain.removeAll();
			pMain.setLayout(new BorderLayout());
			pMainPanel.add(bEditBook);

			String[] columns = {"Return ID", "Borrow ID", "Return Date", "Fine"};
			DefaultTableModel model = new DefaultTableModel(columns, 0);
			JTable table = new JTable(model);
			applyTableStyle(table);

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection connection = DriverManager.getConnection(url, user, password);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("select * from returns");
				while(resultSet.next()) {
					model.addRow(new Object[] {
							resultSet.getInt(1),
							resultSet.getInt(2),
							resultSet.getString(3),
							resultSet.getDouble(4)
					});
				}

				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.getViewport().setBackground(COLOR_BG_DARK);
				scrollPane.setBorder(BorderFactory.createEmptyBorder());

				pMain.add(scrollPane, BorderLayout.CENTER);
				pMain.revalidate();
				pMain.repaint();
				connection.close();
			} catch(SQLException | ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}

	class ShowUsers implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			pMain.removeAll();
			pMain.setLayout(new BorderLayout());
			pMainPanel.add(bEditBook);

			String[] columns = {"User ID", "User Name", "Email", "Phone", "Role"};
			DefaultTableModel model = new DefaultTableModel(columns, 0);
			JTable table = new JTable(model);
			applyTableStyle(table);

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection connection = DriverManager.getConnection(url, user, password);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("select * from users");
				while(resultSet.next()) {
					model.addRow(new Object[] {
							resultSet.getInt(1),
							resultSet.getString(2),
							resultSet.getString(3),
							resultSet.getString(4),
							resultSet.getString(5)
					});
				}

				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.getViewport().setBackground(COLOR_BG_DARK);
				scrollPane.setBorder(BorderFactory.createEmptyBorder());

				pMain.add(scrollPane, BorderLayout.CENTER);
				pMain.revalidate();
				pMain.repaint();
				connection.close();
			} catch(SQLException | ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void clickB1() {
		b1.doClick();
	}

	public Main() {
		// --- Base Panel Colors & Padding ---
		pTop.setBackground(COLOR_PANEL_DARK);
		pBottom.setBackground(COLOR_PANEL_DARK);
		pMenu.setBackground(COLOR_PANEL_DARK);
		pMain.setBackground(COLOR_BG_DARK);
		pMainPanel.setBackground(COLOR_BG_DARK);
		panel.setBackground(COLOR_BG_DARK);

		// Sidebar padding
		pMenu.setBorder(BorderFactory.createEmptyBorder(15, 12, 15, 12));

		// Top Header
		lTop.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lTop.setForeground(COLOR_TEXT_MAIN);
		pTop.add(lTop);

		// Sidebar Header Title
		lMenu.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lMenu.setForeground(COLOR_TEXT_MAIN);
		lMenu.setVerticalAlignment(SwingConstants.CENTER);
		lMenu.setHorizontalAlignment(SwingConstants.CENTER);

		pMenu.add(lMenu);
		pMenu.add(b1);
		pMenu.add(b2);
		pMenu.add(b3);
		pMenu.add(b4);
		pMenu.add(b5);
		pMenu.add(b6);

		// Bottom Footer
		pBottom.setPreferredSize(new Dimension(500, 35));
		pBottom.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

		login.setForeground(COLOR_TEXT_MUTED);
		login.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		JLabel lDate = new JLabel("Date: " + formattedDate);
		lDate.setForeground(COLOR_TEXT_MUTED);
		lDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		pBottom.add(login, BorderLayout.WEST);
		pBottom.add(lDate, BorderLayout.EAST);

		b1.addActionListener(new ShowBookList());
		b2.addActionListener(new ShowMembers());
		b3.addActionListener(new ShowBorrow());
		b4.addActionListener(new ShowReturn());
		b5.addActionListener(new ShowUsers());

		bEditBook.addActionListener(e -> {
			BookEntry bookentry = new BookEntry(this);
			ImageIcon icon = new ImageIcon("C:\\Users\\myato\\Downloads\\onw\\myat\\src\\test\\panda.jpg");
			bookentry.setIconImage(icon.getImage());

			pMainPanel.remove(bEditBook);
			pMainPanel.add(newBtn1);
			pMainPanel.add(newBtn2);
			pMainPanel.add(newBtn3);

			pMainPanel.revalidate();
			pMainPanel.repaint();
		});

		newBtn3.addActionListener(e -> {
			pMainPanel.remove(newBtn1);
			pMainPanel.remove(newBtn2);
			pMainPanel.remove(newBtn3);
			pMainPanel.add(bEditBook);
			pMainPanel.revalidate();
			pMainPanel.repaint();
		});

		add(pTop, BorderLayout.NORTH);
		add(pBottom, BorderLayout.SOUTH);
		add(pMenu, BorderLayout.WEST);

		panel.add(pMain, BorderLayout.CENTER);
		panel.add(pMainPanel, BorderLayout.SOUTH);
		pMainPanel.setPreferredSize(new Dimension(500, 45));
		add(panel, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		Main frame = new Main();
		frame.setTitle("Library Management System");
		frame.setSize(700, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}