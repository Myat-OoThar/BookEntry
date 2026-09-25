package group;
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

public class BookEntry extends JFrame{
	Main main;
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
	String password = "AVNS_Z5VaWvb_pxjqBMtks36";//AVNS_Z5VaWvb_pxjqBMtks36
	
	String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?sslMode=REQUIRED";
	
//	String database = "jdbc:mysql://192.168.1.8:3306/myat";
//	String tableName = "bookentry";
//	String user = "myat";
//	String password = "694737";
		
	public BookEntry(Main main){
		this.main = main;
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
		
						
	}

	
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
						Connection connection = DriverManager.getConnection(url, user, password);
						String sqlInsert = "insert into BookList(bookTitle,author,publisher,publishYear) values('"+bname+"','"+aname+"','"+pname+"','"+pyear+"')";						
						PreparedStatement pstatement = connection.prepareStatement(sqlInsert);
						pstatement.executeUpdate();				
						connection.close();
						
						JOptionPane.showMessageDialog(null, "Saved Successfully");
						main.clickB1();
						
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
		
		
		
		
	}//main
}//BookEntry
