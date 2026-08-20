package test;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.sql.SQLException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookEntry extends JFrame{
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
	
	
		
	public BookEntry(){
		JPanel p1 = new JPanel(new GridLayout(4,2));
		
		Font myFont = new Font("Roboto",Font.PLAIN,20);
		
		lbname.setFont(myFont);
		laname.setFont(myFont);
		lpyear.setFont(myFont);
		lpname.setFont(myFont);
		
		lbname.setForeground(new Color(32,32,32));
		laname.setForeground(new Color(32,32,32));
		lpyear.setForeground(new Color(32,32,32));
		lpname.setForeground(new Color(32,32,32));
		
		tbname.setPreferredSize(new Dimension(20,30));
		taname.setPreferredSize(new Dimension(20,30));
		tpyear.setPreferredSize(new Dimension(20,30));
		tpname.setPreferredSize(new Dimension(20,30));
		
		tbname.setBackground(new Color(255,255,255));
		taname.setBackground(new Color(255,255,255));
		tpyear.setBackground(new Color(255,255,255));
		tpname.setBackground(new Color(255,255,255));
		
		LayoutManager layout = new FlowLayout(FlowLayout.CENTER, 5, 10);
		
		JPanel plb = new JPanel(layout);
		JPanel pla = new JPanel(layout);
		JPanel ply = new JPanel(layout);
		JPanel plp = new JPanel(layout);
		plb.add(lbname);
		pla.add(laname);
		ply.add(lpyear);
		plp.add(lpname);
		
		
		JPanel ptb = new JPanel(layout);
		JPanel pta = new JPanel(layout);
		JPanel pty = new JPanel(layout);
		JPanel ptp = new JPanel(layout);
		
		ptb.add(tbname);
		pta.add(taname);
		pty.add(tpyear);
		ptp.add(tpname);
		
		p1.add(plb);
		p1.add(ptb);
		p1.add(pla);
		p1.add(pta);
		p1.add(ply);
		p1.add(pty);
		p1.add(plp);
		p1.add(ptp);
		
		JPanel p2 = new JPanel(new FlowLayout());
		p2.add(save);
		p2.add(cancel);
		p2.add(bookList);
						
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(p1,BorderLayout.CENTER);
		panel.add(p2,BorderLayout.SOUTH);		
		add(panel);
		
		Color bgcolor = new Color(240,240,240);
		
		p1.setBackground(bgcolor);
		p2.setBackground(new Color(192,192,192));
		
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
			
			String[] columns = {"Book Title","Author Name","Year","Publisher"};
			DefaultTableModel model = new DefaultTableModel(columns,0);
			JTable table = new JTable(model);						
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.3:3306/studentdb","Myat_Oo_Thar","694737");
				System.out.println("Connection created");
				String sqlSelect = "select * from bookEntry";
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sqlSelect);
				while(resultSet.next()) {
					model.addRow(new Object[] {
							resultSet.getString(1),
							resultSet.getString(2),
							resultSet.getInt(3),
							resultSet.getString(4)
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
			String pyear = tpyear.getText();
			String pname = tpname.getText();
			
			if(e.getSource()==cancel) {
				System.exit(0);
			}
			
			if(e.getSource()==save) {
				if(bname.equals("")||aname.equals("")||pyear.equals("")||pname.equals("")) {
					JOptionPane.showMessageDialog(null, "You should input first!");
				}else {
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.3:3306/studentdb","Myat_Oo_Thar","694737");
						String sqlInsert = "insert into bookEntry(book_Title,author_name,publish_year,publisher_name) values('"+bname+"','"+aname+"','"+pyear+"','"+pname+"')";						
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
		BookEntry bookentry = new BookEntry();
		ImageIcon icon = new ImageIcon("C:\\Users\\myato\\Downloads\\onw\\myat\\src\\test\\panda.jpg");
		bookentry.setIconImage(icon.getImage());
		
		bookentry.setTitle("Book Entry");
		bookentry.setSize(500,500);
		bookentry.setLocationRelativeTo(null);
		bookentry.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bookentry.setVisible(true);
		
		
		
	}//main
}//BookEntry
