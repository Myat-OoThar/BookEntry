package test;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.sql.SQLException;
import java.awt.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingConstants;

public class BookEntry2 extends JFrame{
	JLabel lbname = new JLabel("Book Name");
	JLabel laname = new JLabel("Author Name");
	JLabel lpyear = new JLabel("Publish Year");
	JLabel lpname = new JLabel("Publisher Name");
	
	JTextField tbname = new JTextField(20);
	JTextField taname = new JTextField(20);
	JTextField tpyear = new JTextField(20);
	JTextField tpname = new JTextField(20);
	
	JButton save = new JButton("Save Book");
	JButton cancel = new JButton("Cancel");
	JButton bookList = new JButton("Book List");
	
	String host = "mysql-26ea90b1-myatoothar5-1f5f.c.aivencloud.com";
	String port = "10007";
	String database = "project";
	String user = "avnadmin";
	String password = "AVNS_Z5VaWvb_pxjqBMtks36";
	
	String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?sslMode=REQUIRED";
	
	String tableName = "BookList";
		
	public BookEntry2(){
		JPanel p1 = new JPanel(new GridLayout(4,2));	
		Font myFont = new Font("Roboto",Font.PLAIN,20);
		
		//setting labels fonts
		lbname.setFont(myFont);
		laname.setFont(myFont);
		lpyear.setFont(myFont);
		lpname.setFont(myFont);
		
		Color fontColor = new Color(32,32,32);
		//setting labels fonts color
		lbname.setForeground(fontColor);
		laname.setForeground(fontColor);
		lpyear.setForeground(fontColor);
		lpname.setForeground(fontColor);
		
		//setting size of text field box
		tbname.setPreferredSize(new Dimension(20,30));
		taname.setPreferredSize(new Dimension(20,30));
		tpyear.setPreferredSize(new Dimension(20,30));
		tpname.setPreferredSize(new Dimension(20,30));
		
		// for alignment of label and text fields
		LayoutManager layout = new FlowLayout(FlowLayout.CENTER, 5, 10);
		//setting label panels alignments
		JPanel plb = new JPanel(layout);
		JPanel pla = new JPanel(layout);
		JPanel ply = new JPanel(layout);
		JPanel plp = new JPanel(layout);
		//adding labels into aligned panels
		plb.add(lbname);
		pla.add(laname);
		ply.add(lpyear);
		plp.add(lpname);
			
		//setting text field panels alignments
		JPanel ptb = new JPanel(layout);
		JPanel pta = new JPanel(layout);
		JPanel pty = new JPanel(layout);
		JPanel ptp = new JPanel(layout);
		//adding text fields into aligned panels
		ptb.add(tbname);
		pta.add(taname);
		pty.add(tpyear);
		ptp.add(tpname);
		
		//adding small panels into main GridLayout panel
		p1.add(plb);
		p1.add(ptb);
		
		p1.add(pla);
		p1.add(pta);
		
		p1.add(ply);
		p1.add(pty);
		
		p1.add(plp);
		p1.add(ptp);
		
		//creating panel for buttons
		JPanel p2 = new JPanel(new FlowLayout());
		p2.add(save);
		p2.add(cancel);
		p2.add(bookList);
						
		//creating main panel
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(p1,BorderLayout.CENTER);
		panel.add(p2,BorderLayout.SOUTH);		
		add(panel);
		
		// background color of main panel
		Color bgcolor = new Color(240,240,240);
		// background color of button panel
		p2.setBackground(new Color(192,192,192));
		
		//setting background color of label panels and textfield panels
		plb.setBackground(bgcolor);
		pla.setBackground(bgcolor);
		ply.setBackground(bgcolor);
		plp.setBackground(bgcolor);
		
		ptb.setBackground(bgcolor);
		pta.setBackground(bgcolor);
		pty.setBackground(bgcolor);
		ptp.setBackground(bgcolor);
		
		save.addActionListener(new ButtonAction());
		cancel.addActionListener(new ButtonAction());
		bookList.addActionListener(new ShowBList());
						
	}
	class ShowBList implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
			//Create a new window when clicked to 'Book List' button
			JFrame bookListFrame = new JFrame("Book List");
			bookListFrame.setSize(500,500);
			bookListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			bookListFrame.setLocationRelativeTo(null);
			bookListFrame.setVisible(true);
			
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
				scrollPane.setPreferredSize(new java.awt.Dimension(500,300));
				bookListFrame.add(scrollPane);				
				connection.close();
			}//try
			catch(SQLException se) {
				se.printStackTrace();
			}catch(ClassNotFoundException ce) {
				System.out.println("ClassNotFoundException");
				
			}//catch			
		}//method
	}//ShowBList
	
	class ButtonAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			String bname = tbname.getText();
			String aname = taname.getText();
			String pname = tpname.getText();
			String pyear = tpyear.getText();
			
			
			if(e.getSource()==cancel) {
				System.exit(0);
			}
			
			if(e.getSource()==save) {
				if(bname.equals("")||aname.equals("")||pyear.equals("")||pname.equals("")) {
					JOptionPane.showMessageDialog(null, "You should input first!");
				}else {
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection connection = DriverManager.getConnection(url, user, password);
						String sqlInsert = "insert into BookList(bookTitle,author,publisher,publishYear) values('"+bname+"','"+aname+"','"+pname+"','"+pyear+"')";						
						PreparedStatement pstatement = connection.prepareStatement(sqlInsert);
						pstatement.executeUpdate();				
						connection.close();
						
						JOptionPane.showMessageDialog(null, "Saved Successfully");
						
						tbname.setText("");
						taname.setText("");
						tpyear.setText("");
						tpname.setText("");						
						
					}//try
					catch(ClassNotFoundException e1){
						System.out.println("ClassNotFound Error");
					}
					catch(SQLException se) {
						System.out.println("SQLException");
						se.printStackTrace();
					}//catch
				}//else
			}//if outside
				
		}//method
	}
	public static void main(String[] args) {
		BookEntry2 bookentry = new BookEntry2();
		ImageIcon icon = new ImageIcon("C:\\Users\\myato\\Downloads\\onw\\myat\\src\\test\\panda.jpg");
		bookentry.setIconImage(icon.getImage());
		
		bookentry.setTitle("Book Entry");
		bookentry.setSize(500,300);
		bookentry.setLocationRelativeTo(null);
		bookentry.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bookentry.setVisible(true);
		
		
		
	}//main
}//BookEntry
