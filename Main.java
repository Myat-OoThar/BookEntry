package group;


import javax.swing.plaf.basic.BasicScrollBarUI;
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


//    String host = "mysql-26ea90b1-myatoothar5-1f5f.c.aivencloud.com";
//    String port = "10007";
//    String database = "defaultdb";
//    String user = "avnadmin";//avnadmin
//    String password = "AVNS_Z5VaWvb_pxjqBMtks36";//AVNS_Z5VaWvb_pxjqBMtks36
//    //String url = "jdbc:mysql://avnadmin:AVNS_Z5VaWvb_pxjqBMtks36@mysql-26ea90b1-myatoothar5-1f5f.c.aivencloud.com:10007/project?ssl-mode=REQUIRED";
//    String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?sslMode=REQUIRED";//aiven
    String url = "jdbc:mysql://localhost:3306/project_copy";//local
    String user = "root";
    String password = "694737";

    JPanel pTop = new JPanel();
    JPanel pBottom = new JPanel(new GridLayout(1,3));
    JPanel pMenu = new JPanel(new GridLayout(6, 1));
    JPanel pMain = new JPanel(new BorderLayout());
    JPanel pMainPanel = new JPanel();
    JPanel panel = new JPanel(new BorderLayout());

    JLabel lTop = new JLabel("Library Management System");
    JLabel lMenu = new JLabel("DashBoard");
    JLabel login = new JLabel("Logged in: "+user);

    JButton b1 = new JButton("Books");
    JButton b2 = new JButton("Members");
    JButton b3 = new JButton("Borrows");
    JButton b4 = new JButton("Returns");
    JButton b5 = new JButton("Librarian");
    //JButton b6 = new JButton("Setting");
    JButton bEdit = new JButton("Rename");
    JButton deleteBook = new JButton("Delete");
    JButton deleteBorrow = new JButton("Delete");
    JButton deleteMember = new JButton("Delete");
    JButton deleteReturn = new JButton("Delete");
    JButton deleteLibrarian = new JButton("Delete");
    JButton bBookEntry = new JButton("Add new Book");
    JButton bMemberEntry = new JButton("Add new Member");
    JButton bBorrowEntry = new JButton("Add new Borrow");
    JButton bReturnEntry = new JButton("Add new Return");
    JButton bLibrarianEntry = new JButton("Add new Librarian");

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

    String[] categories = {
            "All",
            "programming",
            "mathematic",
            "history"
    };

    JComboBox<String> categoryBox = new JComboBox<>(categories);

    String selectedBox;






    JPanel searchPanel = new JPanel();

    private boolean bookEntryOpen = false;
    private boolean memberEntryOpen = false;
    private boolean borrowEntryOpen = false;
    private boolean returnEntryOpen = false;
    private boolean librarianEntryOpen = false;




    class ShowBookList implements ActionListener{

        public void actionPerformed(ActionEvent e) {


            for (ActionListener listener : bEdit.getActionListeners()) {
                bEdit.removeActionListener(listener);
            }



            pMain.removeAll();
            pMain.setLayout(new BorderLayout());



            searchPanel.removeAll();
            searchPanel.revalidate();
            searchPanel.repaint();
            searchPanel.setVisible(true);
            JTextField searchField = new JTextField("Search by book name or author");
            JButton searchButton = new JButton("Search");




            searchField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            });

            searchField.addActionListener(se -> {
                searchButton.doClick();
            });

            searchField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        searchButton.doClick();
                    }
                }
            });

            searchField.setPreferredSize(new Dimension(250, 40));

            searchPanel.add(searchField);
            searchPanel.add(searchButton);
            panel.add(searchPanel,BorderLayout.NORTH);





            categoryBox.addActionListener(ce -> {
                selectedBox = (String) categoryBox.getSelectedItem();
                System.out.println("Selected Box: "+selectedBox);
                b1.doClick();
            });

//            pMainPanel.remove(bBorrowEntry);
//            pMainPanel.remove(bReturnEntry);
//            pMainPanel.remove(bLibrarianEntry);
//            pMainPanel.remove(bMemberEntry);



            pMainPanel.add(categoryBox);


            pMainPanel.add(bEdit);
            pMainPanel.add(deleteBook);
//            pMainPanel.add(bBookEntry);

            deleteBook.setVisible(false);
            deleteMember.setVisible(false);
            deleteBorrow.setVisible(false);
            deleteReturn.setVisible(false);
            deleteLibrarian.setVisible(false);

            bEdit.setVisible(false);

            bBookEntry.setVisible(true);
            bMemberEntry.setVisible(false);
            bBorrowEntry.setVisible(false);
            bReturnEntry.setVisible(false);
            bLibrarianEntry.setVisible(false);







            // creating table for data outputs
            String[] columns = {"Book ID","Book Title","Author Name","Publisher","Year", "Category"};
            DefaultTableModel model = new DefaultTableModel(columns,0);
            JTable table = new JTable(model);
            table.setRowHeight(25);
            table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));// set table header font bold
            table.getTableHeader().setBackground(new Color(207, 108, 37));//table header box bgcolor
            table.getTableHeader().setForeground(new Color(255,255,255));
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
                    deleteBook.setVisible(true);
                    bEdit.setVisible(true);


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

                    }
                }
            });

            deleteBook.addActionListener(be-> {

                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(url, user, password);
                    String sqlDelete = "DELETE FROM BookList WHERE bookID = "+bookId;//"DELETE FROM "+table+" WHERE "+rowIdName+" = "+id;
                    PreparedStatement pstatement = connection.prepareStatement(sqlDelete);


                    pstatement.executeUpdate();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                b1.doClick();


            });

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("Driver created");
                Connection connection = DriverManager.getConnection(url, user, password);
                System.out.println("Database connected");
                Statement statement = connection.createStatement();
                String sqlSelect = "select * from booklist";
//                if(selectedBox == "All"){
//                    sqlSelect = "select * from BookList";
//                }else
                if(selectedBox == "programming"){
                    sqlSelect = "select * from booklist where Category = 'programming'";
                }else if(selectedBox == "mathematic"){
                    sqlSelect = "select * from booklist where Category = 'mathematic'";
                }else if(selectedBox == "history"){
                    sqlSelect = "select * from booklist where Category = 'history'";
                }

                ResultSet resultSet = statement.executeQuery(sqlSelect);
                while(resultSet.next()) {
                    model.addRow(new Object[] {
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getInt(5),
                            resultSet.getString(6)
                    });
                }

                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setPreferredSize(new java.awt.Dimension(500,250));
                JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
                table.getTableHeader().setForeground(new Color(255,255,255));

                verticalBar.setUI(new BasicScrollBarUI() {
                    @Override
                    protected void configureScrollBarColors() {
                        thumbColor = new Color(191, 159, 126);       // Terracotta
                        thumbDarkShadowColor = new Color(191, 159, 126);
                        thumbHighlightColor = new Color(191, 159, 126);
                        thumbLightShadowColor = new Color(191, 159, 126);
                        trackColor = new Color(245, 235, 221);      // Cream
                    }

                    @Override
                    protected JButton createDecreaseButton(int orientation) {
                        return createZeroButton();
                    }

                    @Override
                    protected JButton createIncreaseButton(int orientation) {
                        return createZeroButton();
                    }

                    private JButton createZeroButton() {
                        JButton button = new JButton();
                        button.setPreferredSize(new Dimension(0, 0));
                        button.setMinimumSize(new Dimension(0, 0));
                        button.setMaximumSize(new Dimension(0, 0));
                        return button;
                    }
                });

                verticalBar.setBackground(new Color(191, 159, 126));
                verticalBar.setOpaque(true);
               // vertiScrollBar.setBackground(new Color(191, 159, 126));

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
//                pMainPanel.remove(bBookEntry);
                bBookEntry.setVisible(false);
                bEdit.setVisible(false);
                JFrame bookEditFrame = new JFrame("Book Edit Frame");
                JPanel p1 = new JPanel(new FlowLayout());
                JPanel p2 = new JPanel(new FlowLayout());
                JPanel panel = new JPanel(new BorderLayout(10,10));
                JButton okButton = new JButton("OK");
                JButton cancelButton = new JButton("Cancel");

                JLabel l1 = new JLabel("New Value");

                JTextField jt1 = new JTextField();
                jt1.setPreferredSize(new Dimension(200,30));

                panel.add(p1,BorderLayout.CENTER);
                panel.add(p2,BorderLayout.SOUTH);

                p1.add(l1);
                p1.add(jt1);

                p2.add(okButton);
                p2.add(cancelButton);

                okButton.addActionListener(a -> {
//                    pMainPanel.add(bBookEntry);
                    bEdit.setVisible(false);
                    bBookEntry.setVisible(true);

                    inputedValue = jt1.getText();
                    System.out.println(inputedValue);

                    if (selectedColumn == -1) {
                        System.out.println("Please select a column first.");
                        return;
                    }

                    String[] columnsql = { "bookID", "bookTitle", "author", "publisher", "publishYear", "Category" };

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
                        String booksql = "UPDATE booklist SET " + columnName + " = ? WHERE bookID = ?";

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
                    removeEditFrame(bookEditFrame);

                    b1.doClick();

                });


                bookEditFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                bookEditFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {bEdit.setVisible(true);
                    }
                });
                bookEditFrame.setSize(300,200);
                bookEditFrame.setResizable(false);
                bookEditFrame.setVisible(true);
                bookEditFrame.setLocation(1350,250);
                bookEditFrame.add(panel);

                cancelButton.addActionListener( b3e -> {
                    // pMainPanel.add(bBookEntry);
                    bBookEntry.setVisible(true);
                    bEdit.setVisible(false);
                    table.clearSelection();
                    bookEditFrame.dispose();
                });

                jt1.addActionListener(oe -> {
                    okButton.doClick();
                });

                jt1.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            okButton.doClick();
                        }
                    }
                });



                okButton.setPreferredSize(new Dimension(100,50));
                cancelButton.setPreferredSize(new Dimension(100,50));


                okButton.setBackground(Color.WHITE);

                cancelButton.setBackground(Color.WHITE);

                b2.addActionListener(e2 ->{removeEditFrame(bookEditFrame);});
                b3.addActionListener(e2 ->{removeEditFrame(bookEditFrame);});
                b4.addActionListener(e2 ->{removeEditFrame(bookEditFrame);});
                b5.addActionListener(e2 ->{removeEditFrame(bookEditFrame);});

                // Crucial step: Tell Swing to rebuild the layout and redraw the screen
                pMainPanel.revalidate();
                pMainPanel.repaint();


            });

            bBookEntry.addActionListener(be2 -> {

                if (bookEntryOpen) {
                    return;
                }

                bookEntryOpen = true;

                JFrame bookentry = new JFrame();
                bookentry.setTitle("Book Entry");



                bookentry.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        bookEntryOpen = false;
                        bEdit.setVisible(true);
                        bBookEntry.setVisible(true);
                    }
                });

                // Keep the rest of your existing code here.


//                pMainPanel.remove(bEdit);
//                pMainPanel.remove(bBookEntry);
                bEdit.setVisible(false);
                bBookEntry.setVisible(false);


                bookentry.setLocation(1372,200);
                bookentry.setSize(550,300);
                bookentry.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                bookentry.setVisible(true);
                JLabel lbname = new JLabel("Book Name");
                JLabel laname = new JLabel("Author Name");
                JLabel lpyear = new JLabel("Publish Year");
                JLabel lpname = new JLabel("Publisher Name");
                JLabel lcategory = new JLabel("Category");

                JTextField tbname = new JTextField(20);
                JTextField taname = new JTextField(20);
                JTextField tpyear = new JTextField(20);
                JTextField tpname = new JTextField(20);
                JTextField tcategory = new JTextField(20);
                JButton save = new JButton("Save Book");
                JButton cancel = new JButton("Cancel");
                JPanel pmain = new JPanel(new BorderLayout());
                JPanel p1 = new JPanel(new GridLayout(5, 2));
                Font myFont = new Font("Roboto", Font.PLAIN, 20);

                //setting labels fonts
                lbname.setFont(myFont);
                laname.setFont(myFont);
                lpyear.setFont(myFont);
                lpname.setFont(myFont);
                lcategory.setFont(myFont);

                Color fontColor = new Color(32, 32, 32);
                //setting labels fonts color
                lbname.setForeground(fontColor);
                laname.setForeground(fontColor);
                lpyear.setForeground(fontColor);
                lpname.setForeground(fontColor);
                lcategory.setForeground(fontColor);

                //setting size of text field box
                tbname.setPreferredSize(new Dimension(20, 30));
                taname.setPreferredSize(new Dimension(20, 30));
                tpyear.setPreferredSize(new Dimension(20, 30));
                tpname.setPreferredSize(new Dimension(20, 30));
                tcategory.setPreferredSize(new Dimension(20, 30));

                // for alignment of label and text fields
                LayoutManager layout = new FlowLayout(FlowLayout.CENTER, 5, 10);
                //setting label panels alignments
                JPanel plb = new JPanel(layout);
                JPanel pla = new JPanel(layout);
                JPanel ply = new JPanel(layout);
                JPanel plp = new JPanel(layout);
                JPanel pcl = new JPanel(layout);

                //adding labels into aligned panels
                plb.add(lbname);
                pla.add(laname);
                ply.add(lpyear);
                plp.add(lpname);
                pcl.add(lcategory);

                //setting text field panels alignments
                JPanel ptb = new JPanel(layout);
                JPanel pta = new JPanel(layout);
                JPanel pty = new JPanel(layout);
                JPanel ptp = new JPanel(layout);
                JPanel pct = new JPanel(layout);
                //adding text fields into aligned panels
                ptb.add(tbname);
                pta.add(taname);
                pty.add(tpyear);
                ptp.add(tpname);
                pct.add(tcategory);

                //adding small panels into main GridLayout panel
                p1.add(plb);
                p1.add(ptb);

                p1.add(pla);
                p1.add(pta);

                p1.add(ply);
                p1.add(pty);

                p1.add(plp);
                p1.add(ptp);

                p1.add(pcl);
                p1.add(pct);

                //creating panel for buttons
                JPanel p2 = new JPanel(new FlowLayout());
                p2.add(save);
                p2.add(cancel);


                //creating main panel
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(p1, BorderLayout.CENTER);
                panel.add(p2, BorderLayout.SOUTH);
                add(panel);

                // background color of main panel
                Color bgcolor = new Color(240, 240, 240);
                // background color of button panel
                p2.setBackground(new Color(192, 192, 192));

                //setting background color of label panels and textfield panels
                plb.setBackground(bgcolor);
                pla.setBackground(bgcolor);
                ply.setBackground(bgcolor);
                plp.setBackground(bgcolor);
                pcl.setBackground(bgcolor);


                ptb.setBackground(bgcolor);
                pta.setBackground(bgcolor);
                pty.setBackground(bgcolor);
                ptp.setBackground(bgcolor);
                pct.setBackground(bgcolor);

                save.addActionListener(se -> {
//                    pMainPanel.add(bEdit);
//                    pMainPanel.add(bBookEntry);
                    bEdit.setVisible(true);
                    bBookEntry.setVisible(true);
                    String bname = tbname.getText();
                    String aname = taname.getText();
                    String pyear = tpyear.getText();
                    String pname = tpname.getText();
                    String pcategory = tcategory.getText();

                    if(bname.equals("") || aname.equals("") || pyear.equals("") || pname.equals("") || pcategory.equals("")) {
                        JOptionPane.showMessageDialog(null, "You should input first!");
                    }else {
                        try {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection connection = DriverManager.getConnection(url, user, password);

                            String sqlInsert = "INSERT INTO booklist(bookTitle, author, publisher, publishYear, Category) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement pstatement = connection.prepareStatement(sqlInsert);
                            pstatement.setString(1, bname);
                            pstatement.setString(2, aname);
                            pstatement.setString(3, pname);
                            pstatement.setString(4, pyear);
                            pstatement.setString(5, pcategory);
                            pstatement.executeUpdate();

                            connection.close();



                            tbname.setText("");
                            taname.setText("");
                            tpyear.setText("");
                            tpname.setText("");
                            tcategory.setText("");

                        }//try
                        catch(ClassNotFoundException e1){
                            System.out.println("ClassNotFound Error");
                        }
                        catch(SQLException se2) {
                            System.out.println("SQLException");
                            se2.printStackTrace();
                        }//catch
                    }//else

                    b1.doClick();
                    bookentry.dispose();


                });

                cancel.addActionListener(ce -> {

                    bEdit.setVisible(true);
                    bBookEntry.setVisible(true);
                    bookentry.dispose();

                });



                tcategory.addActionListener(oe -> {
                    save.doClick();
                });

                tcategory.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            save.doClick();
                        }
                    }
                });

                pmain.add(p1,BorderLayout.CENTER);
                pmain.add(p2,BorderLayout.SOUTH);
                bookentry.add(pmain);



            });

            searchButton.addActionListener(se -> {
                String searchValue =  searchField.getText().trim();
                System.out.println("Search value: "+searchValue);
                if(searchValue.isEmpty()){
                    JOptionPane.showMessageDialog(null, "You should input first!");
                }else{

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        System.out.println("Driver created");
                        Connection connection = DriverManager.getConnection(url, user, password);
                        System.out.println("Database connected");

                        String sqlSearch = "select * from booklist where bookTitle like ? OR author like ?";
                        PreparedStatement pst = connection.prepareStatement(sqlSearch);
                        pst.setString(1,"%" + searchValue + "%");
                        pst.setString(2, "%" + searchValue + "%");



                        model.setRowCount(0);
                        ResultSet resultSet = pst.executeQuery();
                        while(resultSet.next()) {
                            model.addRow(new Object[] {
                                    resultSet.getInt(1),
                                    resultSet.getString(2),
                                    resultSet.getString(3),
                                    resultSet.getString(4),
                                    resultSet.getInt(5),
                                    resultSet.getString(6)
                            });
                        }


                        connection.close();
                    }//try
                    catch(SQLException se1) {
                        se1.printStackTrace();
                    }catch(ClassNotFoundException ce) {
                        System.out.println("ClassNotFoundException");
                        ce.printStackTrace();

                    }//catch
                }
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

            searchPanel.removeAll();
            searchPanel.revalidate();
            searchPanel.repaint();
            searchPanel.setVisible(true);

            JTextField searchField = new JTextField("Search by member name");
            JButton searchButton = new JButton("Search");




            searchField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            });

            searchField.addActionListener(se -> {
                searchButton.doClick();
            });

            searchField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        searchButton.doClick();
                    }
                }
            });

            searchField.setPreferredSize(new Dimension(250, 40));

            searchPanel.add(searchField);
            searchPanel.add(searchButton);
            panel.add(searchPanel,BorderLayout.NORTH);


            categoryBox.setVisible(false);



//            pMainPanel.remove(bBorrowEntry);
//            pMainPanel.remove(bReturnEntry);
//            pMainPanel.remove(bLibrarianEntry);
//            pMainPanel.remove(bBookEntry);

            pMainPanel.add(bEdit);
            pMainPanel.add(deleteMember);
//            pMainPanel.add(bMemberEntry);

            deleteBook.setVisible(false);
            deleteMember.setVisible(false);
            deleteBorrow.setVisible(false);
            deleteReturn.setVisible(false);
            deleteLibrarian.setVisible(false);

            bEdit.setVisible(false);

            bBookEntry.setVisible(false);
            bMemberEntry.setVisible(true);
            bBorrowEntry.setVisible(false);
            bReturnEntry.setVisible(false);
            bLibrarianEntry.setVisible(false);


            // creating table for data outputs
            String[] columns = {"Member ID","Name","Phone Number","Email","Address","Registration Date"};
            DefaultTableModel model = new DefaultTableModel(columns,0);
            JTable table = new JTable(model);
            table.setRowHeight(25);

            table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));// set table header font bold
            table.getTableHeader().setBackground(new Color(207, 108, 37));//table header box bgcolor
            table.getTableHeader().setForeground(new Color(255,255,255));
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
                JScrollBar verticalBar = scrollPane.getVerticalScrollBar();


                verticalBar.setUI(new BasicScrollBarUI() {
                    @Override
                    protected void configureScrollBarColors() {
                        thumbColor = new Color(191, 159, 126);       // Terracotta
                        thumbDarkShadowColor = new Color(191, 159, 126);
                        thumbHighlightColor = new Color(191, 159, 126);
                        thumbLightShadowColor = new Color(191, 159, 126);
                        trackColor = new Color(245, 235, 221);      // Cream
                    }

                    @Override
                    protected JButton createDecreaseButton(int orientation) {
                        return createZeroButton();
                    }

                    @Override
                    protected JButton createIncreaseButton(int orientation) {
                        return createZeroButton();
                    }

                    private JButton createZeroButton() {
                        JButton button = new JButton();
                        button.setPreferredSize(new Dimension(0, 0));
                        button.setMinimumSize(new Dimension(0, 0));
                        button.setMaximumSize(new Dimension(0, 0));
                        return button;
                    }
                });

                verticalBar.setBackground(new Color(191, 159, 126));
                verticalBar.setOpaque(true);

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
                    bEdit.setVisible(true);
                    deleteMember.setVisible(true);


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

            deleteMember.addActionListener(be-> {

                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(url, user, password);
                    String sqlDelete = "DELETE FROM members WHERE memberId = "+memberId;//"DELETE FROM "+table+" WHERE "+rowIdName+" = "+id;
                    PreparedStatement pstatement = connection.prepareStatement(sqlDelete);


                    pstatement.executeUpdate();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                b2.doClick();


            });

            bEdit.addActionListener(be -> {
                bEdit.setVisible(false);
                bMemberEntry.setVisible(false);
                JFrame memberEditFrame = new JFrame("Member Edit Frame");

                JPanel p1 = new JPanel(new FlowLayout());
                JPanel p2 = new JPanel(new FlowLayout());
                JPanel panel = new JPanel(new BorderLayout(10,10));
                JButton okBtn = new JButton("OK");

                JButton cancelBtn = new JButton("Cancel");

                JLabel l1 = new JLabel("New Value");

                JTextField jt1 = new JTextField();
                jt1.setPreferredSize(new Dimension(200,30));

                panel.add(p1,BorderLayout.CENTER);
                panel.add(p2,BorderLayout.SOUTH);

                p1.add(l1);
                p1.add(jt1);

                p2.add(okBtn);
                p2.add(cancelBtn);

                okBtn.addActionListener(a -> {
                    bEdit.setVisible(true);
                    bMemberEntry.setVisible(true);

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
                    removeEditFrame(memberEditFrame);
                    b2.doClick();


                });


                memberEditFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                memberEditFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {bEdit.setVisible(true);
                    }
                });
                memberEditFrame.setSize(300,200);
                memberEditFrame.setResizable(false);
                memberEditFrame.setVisible(true);
                memberEditFrame.setLocation(1350,250);
                memberEditFrame.add(panel);

                cancelBtn.addActionListener( b3e -> {
                    bEdit.setVisible(true);
                    bMemberEntry.setVisible(true);
                    table.clearSelection();
                    memberEditFrame.dispose();
                });

                jt1.addActionListener(se -> {
                    okBtn.doClick();
                });

                jt1.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            okBtn.doClick();
                        }
                    }
                });



                okBtn.setPreferredSize(new Dimension(100,50));
                cancelBtn.setPreferredSize(new Dimension(100,50));


                okBtn.setBackground(Color.WHITE);

                cancelBtn.setBackground(Color.WHITE);

                b1.addActionListener(e2 ->{removeEditFrame(memberEditFrame);});
                b3.addActionListener(e2 ->{removeEditFrame(memberEditFrame);});
                b4.addActionListener(e2 ->{removeEditFrame(memberEditFrame);});
                b5.addActionListener(e2 ->{removeEditFrame(memberEditFrame);});

                // Crucial step: Tell Swing to rebuild the layout and redraw the screen
                pMainPanel.revalidate();
                pMainPanel.repaint();


            });

            bMemberEntry.addActionListener( be2 -> {
                if (memberEntryOpen) {
                    return;
                }

                memberEntryOpen = true;

                JFrame memberentry = new JFrame();
                memberentry.setTitle("Member Entry");



                memberentry.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        memberEntryOpen = false;
                        bEdit.setVisible(true);
                        bMemberEntry.setVisible(true);
                    }
                });

                bEdit.setVisible(false);
                bMemberEntry.setVisible(false);

                memberentry.setLocation(1372,550);
                memberentry.setSize(550,300);
                memberentry.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                memberentry.setVisible(true);
                JLabel membername = new JLabel("Member Name");
                JLabel memberphone = new JLabel("Phone Number");
                JLabel memberemail = new JLabel("Email");
                JLabel memberaddress = new JLabel("Address");
                JLabel memberRDate = new JLabel("Registration Date");

                JTextField tmembername = new JTextField(20);
                JTextField tmemberphone = new JTextField(20);
                JTextField tmemberemail = new JTextField(20);
                JTextField tmemberaddress = new JTextField(20);
                JTextField tmemberRDate = new JTextField(20);
                JButton save = new JButton("Save");
                JButton cancel = new JButton("Cancel");
                JPanel pmain = new JPanel(new BorderLayout());
                JPanel p1 = new JPanel(new GridLayout(5, 2));
                Font myFont = new Font("Roboto", Font.PLAIN, 20);

                //setting labels fonts
                membername.setFont(myFont);
                memberphone.setFont(myFont);
                memberemail.setFont(myFont);
                memberaddress.setFont(myFont);
                memberRDate.setFont(myFont);

                Color fontColor = new Color(32, 32, 32);
                //setting labels fonts color
                membername.setForeground(fontColor);
                memberphone.setForeground(fontColor);
                memberemail.setForeground(fontColor);
                memberaddress.setForeground(fontColor);
                tmemberRDate.setForeground(fontColor);

                //setting size of text field box
                tmembername.setPreferredSize(new Dimension(20, 30));
                tmemberphone.setPreferredSize(new Dimension(20, 30));
                tmemberemail.setPreferredSize(new Dimension(20, 30));
                tmemberaddress.setPreferredSize(new Dimension(20, 30));
                tmemberRDate.setPreferredSize(new Dimension(20, 30));

                // for alignment of label and text fields
                LayoutManager layout = new FlowLayout(FlowLayout.CENTER, 5, 10);
                //setting label panels alignments
                JPanel plb = new JPanel(layout);
                JPanel pla = new JPanel(layout);
                JPanel ply = new JPanel(layout);
                JPanel plp = new JPanel(layout);
                JPanel pld = new JPanel(layout);
                //adding labels into aligned panels
                plb.add(membername);
                pla.add(memberphone);
                ply.add(memberemail);
                plp.add(memberaddress);
                pld.add(memberRDate);

                //setting text field panels alignments
                JPanel ptb = new JPanel(layout);
                JPanel pta = new JPanel(layout);
                JPanel pty = new JPanel(layout);
                JPanel ptp = new JPanel(layout);
                JPanel ptd = new JPanel(layout);
                //adding text fields into aligned panels
                ptb.add(tmembername);
                pta.add(tmemberphone);
                pty.add(tmemberemail);
                ptp.add(tmemberaddress);
                ptd.add(tmemberRDate);

                //adding small panels into main GridLayout panel
                p1.add(plb);
                p1.add(ptb);

                p1.add(pla);
                p1.add(pta);

                p1.add(ply);
                p1.add(pty);

                p1.add(plp);
                p1.add(ptp);

                p1.add(pld);
                p1.add(ptd);

                //creating panel for buttons
                JPanel p2 = new JPanel(new FlowLayout());
                p2.add(save);
                p2.add(cancel);


                //creating main panel
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(p1, BorderLayout.CENTER);
                panel.add(p2, BorderLayout.SOUTH);
                add(panel);

                // background color of main panel
                Color bgcolor = new Color(240, 240, 240);
                // background color of button panel
                p2.setBackground(new Color(192, 192, 192));

                //setting background color of label panels and textfield panels
                plb.setBackground(bgcolor);
                pla.setBackground(bgcolor);
                ply.setBackground(bgcolor);
                plp.setBackground(bgcolor);

                ptb.setBackground(bgcolor);
                pta.setBackground(bgcolor);
                pty.setBackground(bgcolor);
                ptp.setBackground(bgcolor);

                save.addActionListener(se -> {
                    bEdit.setVisible(true);
                    bMemberEntry.setVisible(true);
                    String name = tmembername.getText();
                    String phone = tmemberphone.getText();
                    String email = tmemberemail.getText();
                    String address = tmemberaddress.getText();
                    String date = tmemberRDate.getText();

                    if(name.equals("") || phone.equals("") || email.equals("") || address.equals("") || date.equals("")) {
                        JOptionPane.showMessageDialog(null, "You should input first!");
                    }else {
                        try {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection connection = DriverManager.getConnection(url, user, password);
                            String sqlInsert = "insert into members(memberName,phone,email,address,registrationDate) values('"+name+"','"+phone+"','"+email+"','"+address+"','"+date+"')";
                            PreparedStatement pstatement = connection.prepareStatement(sqlInsert);
                            pstatement.executeUpdate();
                            connection.close();
                            tmembername.setText("");
                            tmemberphone.setText("");
                            tmemberemail.setText("");
                            tmemberaddress.setText("");
                            tmemberRDate.setText("");

                        }//try
                        catch(ClassNotFoundException e1){
                            System.out.println("ClassNotFound Error");
                        }
                        catch(SQLException se2) {
                            System.out.println("SQLException");
                            se2.printStackTrace();
                        }//catch
                    }//else

                    b2.doClick();
                    memberentry.dispose();


                });

                cancel.addActionListener(ce -> {
                    bEdit.setVisible(true);
                    bMemberEntry.setVisible(true);
                    memberentry.dispose();
                });

                tmemberRDate.addActionListener(oe -> {
                    save.doClick();
                });

                tmemberRDate.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            save.doClick();
                        }
                    }
                });

                pmain.add(p1,BorderLayout.CENTER);
                pmain.add(p2,BorderLayout.SOUTH);
                memberentry.add(pmain);
                memberentry.add(pmain);


            });

            searchButton.addActionListener(se -> {
                String searchValue =  searchField.getText().trim();
                System.out.println("Search value: "+searchValue);
                if(searchValue.isEmpty()){
                    JOptionPane.showMessageDialog(null, "You should input first!");
                }else{

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        System.out.println("Driver created");
                        Connection connection = DriverManager.getConnection(url, user, password);
                        System.out.println("Database connected");

                        String sqlSearch = "select * from members where memberName like ?";
                        PreparedStatement pst = connection.prepareStatement(sqlSearch);
                        pst.setString(1,"%" + searchValue + "%");




                        model.setRowCount(0);
                        ResultSet resultSet = pst.executeQuery();
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


                        connection.close();
                    }//try
                    catch(SQLException se1) {
                        se1.printStackTrace();
                    }catch(ClassNotFoundException ce) {
                        System.out.println("ClassNotFoundException");
                        ce.printStackTrace();

                    }//catch
                }
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

            searchPanel.setVisible(false);


            categoryBox.setVisible(false);

//            pMainPanel.remove(bReturnEntry);
//            pMainPanel.remove(bMemberEntry);
//            pMainPanel.remove(bLibrarianEntry);
//            pMainPanel.remove(bBookEntry);

            pMainPanel.add(bBorrowEntry);
            pMainPanel.add(bEdit);
            pMainPanel.add(deleteBorrow);


            deleteBook.setVisible(false);
            deleteMember.setVisible(false);
            deleteBorrow.setVisible(false);
            deleteReturn.setVisible(false);
            deleteLibrarian.setVisible(false);

            bEdit.setVisible(false);

            bBookEntry.setVisible(false);
            bMemberEntry.setVisible(false);
            bBorrowEntry.setVisible(true);
            bReturnEntry.setVisible(false);
            bLibrarianEntry.setVisible(false);

            // creating table for data outputs
            String[] columns = {"Borrow ID","Book ID","Member ID","Borrow Date","Due Date"};
            DefaultTableModel model = new DefaultTableModel(columns,0);
            JTable table = new JTable(model);
            table.setRowHeight(25);

            table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));// set table header font bold
            table.getTableHeader().setBackground(new Color(207, 108, 37));//table header box bgcolor
            table.getTableHeader().setForeground(new Color(255,255,255));
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
                            resultSet.getString(5)
                    });
                }

                JScrollPane scrollPane = new JScrollPane(table);
                JScrollBar verticalBar = scrollPane.getVerticalScrollBar();


                verticalBar.setUI(new BasicScrollBarUI() {
                    @Override
                    protected void configureScrollBarColors() {
                        thumbColor = new Color(191, 159, 126);       // Terracotta
                        thumbDarkShadowColor = new Color(191, 159, 126);
                        thumbHighlightColor = new Color(191, 159, 126);
                        thumbLightShadowColor = new Color(191, 159, 126);
                        trackColor = new Color(245, 235, 221);      // Cream
                    }

                    @Override
                    protected JButton createDecreaseButton(int orientation) {
                        return createZeroButton();
                    }

                    @Override
                    protected JButton createIncreaseButton(int orientation) {
                        return createZeroButton();
                    }

                    private JButton createZeroButton() {
                        JButton button = new JButton();
                        button.setPreferredSize(new Dimension(0, 0));
                        button.setMinimumSize(new Dimension(0, 0));
                        button.setMaximumSize(new Dimension(0, 0));
                        return button;
                    }
                });

                verticalBar.setBackground(new Color(191, 159, 126));
                verticalBar.setOpaque(true);
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
                    bEdit.setVisible(true);
                    deleteBorrow.setVisible(true);

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

            deleteBorrow.addActionListener(be-> {

                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(url, user, password);
                    String sqlDelete = "DELETE FROM borrows WHERE borrowId = "+borrowId;//"DELETE FROM "+table+" WHERE "+rowIdName+" = "+id;
                    PreparedStatement pstatement = connection.prepareStatement(sqlDelete);


                    pstatement.executeUpdate();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                b3.doClick();


            });

            bEdit.addActionListener(be -> {
                bEdit.setVisible(false);
                JFrame borrowEditFrame = new JFrame("Borrow Edit Frame");
                JPanel p1 = new JPanel(new FlowLayout());
                JPanel p2 = new JPanel(new FlowLayout());
                JPanel panel = new JPanel(new BorderLayout(10,10));
                JButton okBtn = new JButton("OK");
                JButton cancelBtn = new JButton("Cancel");

                JLabel l1 = new JLabel("New Value");

                JTextField jt1 = new JTextField();
                jt1.setPreferredSize(new Dimension(200,30));

                panel.add(p1,BorderLayout.CENTER);
                panel.add(p2,BorderLayout.SOUTH);

                p1.add(l1);
                p1.add(jt1);

                p2.add(okBtn);
                p2.add(cancelBtn);

                okBtn.addActionListener(a -> {

                    inputedValue = jt1.getText();
                    System.out.println(inputedValue);

                    if (selectedColumn == -1) {
                        System.out.println("Please select a column first.");
                        return;
                    }


                    String[] columnsql = { "borrowId", "bookID", "memberId", "borrowDate", "dueDate" };

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
                    removeEditFrame(borrowEditFrame);

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
                borrowEditFrame.setLocation(1350,250);
                borrowEditFrame.add(panel);

                cancelBtn.addActionListener( b3e -> {
                    table.clearSelection();
                    borrowEditFrame.dispose();
                });

                jt1.addActionListener(se -> {
                    okBtn.doClick();
                });

                jt1.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            okBtn.doClick();
                        }
                    }
                });



                okBtn.setPreferredSize(new Dimension(100,50));
                cancelBtn.setPreferredSize(new Dimension(100,50));


                okBtn.setBackground(Color.WHITE);

                cancelBtn.setBackground(Color.WHITE);

                b2.addActionListener(e2 ->{removeEditFrame(borrowEditFrame);});
                b3.addActionListener(e2 ->{removeEditFrame(borrowEditFrame);});
                b4.addActionListener(e2 ->{removeEditFrame(borrowEditFrame);});
                b5.addActionListener(e2 ->{removeEditFrame(borrowEditFrame);});

                // Crucial step: Tell Swing to rebuild the layout and redraw the screen
                pMainPanel.revalidate();
                pMainPanel.repaint();


            });// end of bEdit

            bBorrowEntry.addActionListener( be2 -> {
                if (borrowEntryOpen) {
                    return;
                }

                borrowEntryOpen = true;

                JFrame borrowentry = new JFrame();

                borrowentry.setTitle("Borrow Entry");



                borrowentry.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        borrowEntryOpen = false;
                        bEdit.setVisible(true);
                        bBorrowEntry.setVisible(true);
                    }
                });



                JLabel bookId = new JLabel("Book Id");
                JLabel memberId = new JLabel("Member ID");
                JLabel borrowDate = new JLabel("Borrow Date");
                JLabel dueDate = new JLabel("Due Date");


                JTextField tbookId = new JTextField(20);
                JTextField tmemberId = new JTextField(20);
                JTextField tborrowDate = new JTextField(20);
                JTextField tdueDate = new JTextField(20);


                JButton save = new JButton("Save");
                JButton cancel = new JButton("Cancel");
                JPanel pmain = new JPanel(new BorderLayout());
                JPanel p1 = new JPanel(new GridLayout(5, 2));
                Font myFont = new Font("Roboto", Font.PLAIN, 20);

                //setting labels fonts
                bookId.setFont(myFont);
                memberId.setFont(myFont);
                borrowDate.setFont(myFont);
                dueDate.setFont(myFont);


                Color fontColor = new Color(32, 32, 32);
                //setting labels fonts color
                bookId.setForeground(fontColor);
                memberId.setForeground(fontColor);
                borrowDate.setForeground(fontColor);
                dueDate.setForeground(fontColor);


                //setting size of text field box
                tbookId.setPreferredSize(new Dimension(20, 30));
                tmemberId.setPreferredSize(new Dimension(20, 30));
                tborrowDate.setPreferredSize(new Dimension(20, 30));
                tdueDate.setPreferredSize(new Dimension(20, 30));


                // for alignment of label and text fields
                LayoutManager layout = new FlowLayout(FlowLayout.CENTER, 5, 10);
                //setting label panels alignments
                JPanel plb = new JPanel(layout);
                JPanel pla = new JPanel(layout);
                JPanel ply = new JPanel(layout);
                JPanel plp = new JPanel(layout);
                //JPanel pld = new JPanel(layout);
                //adding labels into aligned panels
                plb.add(bookId);
                pla.add(memberId);
                ply.add(borrowDate);
                plp.add(dueDate);


                //setting text field panels alignments
                JPanel ptb = new JPanel(layout);
                JPanel pta = new JPanel(layout);
                JPanel pty = new JPanel(layout);
                JPanel ptp = new JPanel(layout);
                //JPanel ptd = new JPanel(layout);
                //adding text fields into aligned panels
                ptb.add(tbookId);
                pta.add(tmemberId);
                pty.add(tborrowDate);
                ptp.add(tdueDate);
                //ptd.add(treturnDate);

                //adding small panels into main GridLayout panel
                p1.add(plb);
                p1.add(ptb);

                p1.add(pla);
                p1.add(pta);

                p1.add(ply);
                p1.add(pty);

                p1.add(plp);
                p1.add(ptp);

//                p1.add(pld);
//                p1.add(ptd);

                //creating panel for buttons
                JPanel p2 = new JPanel(new FlowLayout());
                p2.add(save);
                p2.add(cancel);


                //creating main panel
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(p1, BorderLayout.CENTER);
                panel.add(p2, BorderLayout.SOUTH);
                add(panel);

                // background color of main panel
                Color bgcolor = new Color(240, 240, 240);
                // background color of button panel
                p2.setBackground(new Color(192, 192, 192));

                //setting background color of label panels and textfield panels
                plb.setBackground(bgcolor);
                pla.setBackground(bgcolor);
                ply.setBackground(bgcolor);
                plp.setBackground(bgcolor);

                ptb.setBackground(bgcolor);
                pta.setBackground(bgcolor);
                pty.setBackground(bgcolor);
                ptp.setBackground(bgcolor);

                save.addActionListener(se -> {
                    String tbookIdText = tbookId.getText();
                    String tmemberIdText = tmemberId.getText();
                    String tborrowDateText = tborrowDate.getText();
                    String tdueDateText = tdueDate.getText();
                    //String treturnDateText = treturnDate.getText();

                    if(tbookIdText.equals("") || tmemberIdText.equals("") || tborrowDateText.equals("") || tdueDateText.equals("")) {
                        JOptionPane.showMessageDialog(null, "You should input first!");
                    }else {
                        try {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection connection = DriverManager.getConnection(url, user, password);
                            String sqlInsert = "insert into borrows(bookID,memberId,borrowDate,dueDate) values('"+tbookIdText+"','"+tmemberIdText+"','"+tborrowDateText+"','"+tdueDateText+"')";
                            PreparedStatement pstatement = connection.prepareStatement(sqlInsert);
                            pstatement.executeUpdate();
                            connection.close();
                            tbookId.setText("");
                            tmemberId.setText("");
                            tborrowDate.setText("");
                            tdueDate.setText("");

                        }//try
                        catch(ClassNotFoundException e1){
                            System.out.println("ClassNotFound Error");
                        }
                        catch(SQLException se2) {
                            System.out.println("SQLException");
                            se2.printStackTrace();
                        }//catch
                    }//else

                    b3.doClick();
                    borrowentry.dispose();


                });

                cancel.addActionListener(ce -> {
                    borrowentry.dispose();
                });

                tdueDate.addActionListener(oe -> {
                    save.doClick();
                });

                tdueDate.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            save.doClick();
                        }
                    }
                });

                pmain.add(p1,BorderLayout.CENTER);
                pmain.add(p2,BorderLayout.SOUTH);
                borrowentry.add(pmain);
                borrowentry.add(pmain);
                borrowentry.setLocation(-3,200);
                borrowentry.setSize(550,300);
                borrowentry.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                borrowentry.setVisible(true);

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

            searchPanel.setVisible(false);


            categoryBox.setVisible(false);
//            pMainPanel.remove(bBorrowEntry);
//            pMainPanel.remove(bMemberEntry);
//            pMainPanel.remove(bLibrarianEntry);
//            pMainPanel.remove(bBookEntry);

            pMainPanel.add(bReturnEntry);
            pMainPanel.add(bEdit);
            pMainPanel.add(deleteReturn);


            deleteBook.setVisible(false);
            deleteMember.setVisible(false);
            deleteBorrow.setVisible(false);
            deleteReturn.setVisible(false);
            deleteLibrarian.setVisible(false);

            bEdit.setVisible(false);

            bBookEntry.setVisible(false);
            bMemberEntry.setVisible(false);
            bBorrowEntry.setVisible(false);
            bReturnEntry.setVisible(true);
            bLibrarianEntry.setVisible(false);

            // creating table for data outputs
            String[] columns = {"Return ID","Borrow ID","Borrow Date","Return Date","Fine"};
            DefaultTableModel model = new DefaultTableModel(columns,0);
            JTable table = new JTable(model);
            table.setRowHeight(25);

            table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));// set table header font bold
            table.getTableHeader().setBackground(new Color(207, 108, 37));//table header box bgcolor
            table.getTableHeader().setForeground(new Color(255,255,255));
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
                            resultSet.getString(4),
                            resultSet.getDouble(5),
                    });
                }

                JScrollPane scrollPane = new JScrollPane(table);
                JScrollBar verticalBar = scrollPane.getVerticalScrollBar();


                verticalBar.setUI(new BasicScrollBarUI() {
                    @Override
                    protected void configureScrollBarColors() {
                        thumbColor = new Color(191, 159, 126);       // Terracotta
                        thumbDarkShadowColor = new Color(191, 159, 126);
                        thumbHighlightColor = new Color(191, 159, 126);
                        thumbLightShadowColor = new Color(191, 159, 126);
                        trackColor = new Color(245, 235, 221);      // Cream
                    }

                    @Override
                    protected JButton createDecreaseButton(int orientation) {
                        return createZeroButton();
                    }

                    @Override
                    protected JButton createIncreaseButton(int orientation) {
                        return createZeroButton();
                    }

                    private JButton createZeroButton() {
                        JButton button = new JButton();
                        button.setPreferredSize(new Dimension(0, 0));
                        button.setMinimumSize(new Dimension(0, 0));
                        button.setMaximumSize(new Dimension(0, 0));
                        return button;
                    }
                });

                verticalBar.setBackground(new Color(191, 159, 126));
                verticalBar.setOpaque(true);
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
                    bEdit.setVisible(true);
                    deleteReturn.setVisible(true);
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

            deleteReturn.addActionListener(be-> {

                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(url, user, password);
                    String sqlDelete = "DELETE FROM returns WHERE returnId = "+returnId;//"DELETE FROM "+table+" WHERE "+rowIdName+" = "+id;
                    PreparedStatement pstatement = connection.prepareStatement(sqlDelete);


                    pstatement.executeUpdate();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                b4.doClick();


            });

            bEdit.addActionListener(be -> {
                bEdit.setVisible(false);
                JFrame returnEditFrame = new JFrame("Return Edit Frame");
                JPanel p1 = new JPanel(new FlowLayout());
                JPanel p2 = new JPanel(new FlowLayout());
                JPanel panel = new JPanel(new BorderLayout(10,10));
                JButton okBtn = new JButton("OK");
                JButton cancelBtn = new JButton("Cancel");

                JLabel l1 = new JLabel("New Value");

                JTextField jt1 = new JTextField();
                jt1.setPreferredSize(new Dimension(200,30));

                panel.add(p1,BorderLayout.CENTER);
                panel.add(p2,BorderLayout.SOUTH);

                p1.add(l1);
                p1.add(jt1);

                p2.add(okBtn);
                p2.add(cancelBtn);

                okBtn.addActionListener(a -> {

                    inputedValue = jt1.getText();
                    System.out.println(inputedValue);

                    if (selectedColumn == -1) {
                        System.out.println("Please select a column first.");
                        return;
                    }

                    String[] columnsql = { "returnId", "borrowId", "borrowDate", "returnDate", "fine" };

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
                    removeEditFrame(returnEditFrame);

                    b4.doClick();

                });


                returnEditFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                returnEditFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        bEdit.setVisible(true);
                    }
                });
                returnEditFrame.setSize(300,200);
                returnEditFrame.setResizable(false);
                returnEditFrame.setVisible(true);
                returnEditFrame.setLocation(1350,250);
                returnEditFrame.add(panel);

                cancelBtn.addActionListener( b3e -> {
                    table.clearSelection();
                    returnEditFrame.dispose();
                });

                jt1.addActionListener(se -> {
                    okBtn.doClick();
                });

                jt1.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            okBtn.doClick();
                        }
                    }
                });



                newBtn1.setPreferredSize(new Dimension(100,50));
                newBtn3.setPreferredSize(new Dimension(100,50));


                okBtn.setBackground(Color.WHITE);

                cancelBtn.setBackground(Color.WHITE);

                b2.addActionListener(e2 ->{removeEditFrame(returnEditFrame);});
                b3.addActionListener(e2 ->{removeEditFrame(returnEditFrame);});
                b1.addActionListener(e2 ->{removeEditFrame(returnEditFrame);});
                b5.addActionListener(e2 ->{removeEditFrame(returnEditFrame);});

                // Crucial step: Tell Swing to rebuild the layout and redraw the screen
                pMainPanel.revalidate();
                pMainPanel.repaint();


            });//end of bEdit

            bReturnEntry.addActionListener( be2 -> {
                if (returnEntryOpen) {
                    return;
                }

                returnEntryOpen = true;

                JFrame returnentry = new JFrame();
                returnentry.setTitle("Return Entry");



                returnentry.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        returnEntryOpen = false;
                        bEdit.setVisible(true);
                        bReturnEntry.setVisible(true);
                    }
                });


                JLabel borrowId = new JLabel("Borrow ID");
                JLabel borrowDate = new  JLabel("Borrow Date");
                JLabel returnDate = new JLabel("Return Date");
                JLabel fine = new JLabel("Fine");
                Font myFont = new Font("Roboto", Font.PLAIN, 20);
                JPanel p1 = new JPanel(new GridLayout(4, 2));


                JTextField tborrowId = new JTextField(20);
                JTextField tborrowDate = new JTextField(20);
                JTextField treturnDate = new JTextField(20);
                JTextField tfine = new JTextField(20);


                JButton save = new JButton("Save");
                JButton cancel = new JButton("Cancel");
                JPanel pmain = new JPanel(new BorderLayout());


                //setting labels fonts
                borrowId.setFont(myFont);
                borrowDate.setFont(myFont);
                returnDate.setFont(myFont);
                fine.setFont(myFont);


                Color fontColor = new Color(32, 32, 32);
                //setting labels fonts color
                borrowId.setForeground(fontColor);
                borrowDate.setForeground(fontColor);
                returnDate.setForeground(fontColor);
                fine.setForeground(fontColor);


                //setting size of text field box
                tborrowId.setPreferredSize(new Dimension(20, 30));
                tborrowDate.setPreferredSize(new Dimension(20, 30));
                treturnDate.setPreferredSize(new Dimension(20, 30));
                tfine.setPreferredSize(new Dimension(20, 30));


                // for alignment of label and text fields
                LayoutManager layout = new FlowLayout(FlowLayout.CENTER, 5, 10);
                //setting label panels alignments
                JPanel plb = new JPanel(layout);
                JPanel pbd = new JPanel(layout);
                JPanel pla = new JPanel(layout);
                JPanel ply = new JPanel(layout);


                //adding labels into aligned panels
                plb.add(borrowId);
                pbd.add(borrowDate);
                pla.add(returnDate);
                ply.add(fine);


                //setting text field panels alignments
                JPanel ptb = new JPanel(layout);
                JPanel ptd = new JPanel(layout);
                JPanel pta = new JPanel(layout);
                JPanel pty = new JPanel(layout);

                //adding text fields into aligned panels
                ptb.add(tborrowId);
                ptd.add(tborrowDate);
                pta.add(treturnDate);
                pty.add(tfine);


                //adding small panels into main GridLayout panel
                p1.add(plb);
                p1.add(ptb);

                p1.add(pbd);
                p1.add(ptd);

                p1.add(pla);
                p1.add(pta);

                p1.add(ply);
                p1.add(pty);




                //creating panel for buttons
                JPanel p2 = new JPanel(new FlowLayout());
                p2.add(save);
                p2.add(cancel);


                //creating main panel
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(p1, BorderLayout.CENTER);
                panel.add(p2, BorderLayout.SOUTH);
                add(panel);

                // background color of main panel
                Color bgcolor = new Color(240, 240, 240);
                // background color of button panel
                p2.setBackground(new Color(192, 192, 192));

                //setting background color of label panels and textfield panels
                plb.setBackground(bgcolor);
                pbd.setBackground(bgcolor);
                pla.setBackground(bgcolor);
                ply.setBackground(bgcolor);


                ptb.setBackground(bgcolor);
                ptd.setBackground(bgcolor);
                pta.setBackground(bgcolor);
                pty.setBackground(bgcolor);


                save.addActionListener(se -> {
                    String tborrowIdText = tborrowId.getText();
                    String tborrowDateText = tborrowDate.getText();
                    String treturnDateText = treturnDate.getText();
                    String tfineText = tfine.getText();


                    if(tborrowIdText.equals("") || treturnDateText.equals("") || tfineText.equals("") || tborrowDateText.equals("")) {
                        JOptionPane.showMessageDialog(null, "You should input first!");
                    }else {
                        try {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection connection = DriverManager.getConnection(url, user, password);
                            String sqlInsert = "insert into returns(borrowId,borrowDate,returnDate,fine) values('"+tborrowIdText+"','"+tborrowDateText+"','"+treturnDateText+"','"+tfineText+"')";
                            PreparedStatement pstatement = connection.prepareStatement(sqlInsert);
                            pstatement.executeUpdate();
                            connection.close();
                            tborrowId.setText("");
                            tborrowDate.setText("");
                            treturnDate.setText("");
                            tfine.setText("");


                        }//try
                        catch(ClassNotFoundException e1){
                            System.out.println("ClassNotFound Error");
                        }
                        catch(SQLException se2) {
                            System.out.println("SQLException");
                            se2.printStackTrace();
                        }//catch
                    }//else

                    b4.doClick();
                    returnentry.dispose();


                });

                cancel.addActionListener(ce -> {
                    returnentry.dispose();
                });

                tfine.addActionListener(oe -> {
                    save.doClick();
                });

                tfine.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            save.doClick();
                        }
                    }
                });

                pmain.add(p1,BorderLayout.CENTER);
                pmain.add(p2,BorderLayout.SOUTH);
                returnentry.add(pmain);
                returnentry.add(pmain);
                returnentry.setLocation(-3,550);
                returnentry.setSize(550,300);
                returnentry.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                returnentry.setVisible(true);

            });


        }
    }

    class ShowLibrarians implements ActionListener{
        public void actionPerformed(ActionEvent e){

            for (ActionListener listener : bEdit.getActionListeners()) {
                bEdit.removeActionListener(listener);
            }

            pMain.removeAll();
            pMain.setLayout(new BorderLayout());


            searchPanel.removeAll();
            searchPanel.revalidate();
            searchPanel.repaint();
            searchPanel.setVisible(true);

            JTextField searchField = new JTextField("Search by Librarian name");
            JButton searchButton = new JButton("Search");




            searchField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            });

            searchField.addActionListener(se -> {
                searchButton.doClick();
            });

            searchField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        searchButton.doClick();
                    }
                }
            });

            searchField.setPreferredSize(new Dimension(250, 40));

            searchPanel.add(searchField);
            searchPanel.add(searchButton);
            panel.add(searchPanel,BorderLayout.NORTH);


            categoryBox.setVisible(false);

//            pMainPanel.remove(bBorrowEntry);
//            pMainPanel.remove(bMemberEntry);
//            pMainPanel.remove(bReturnEntry);
//            pMainPanel.remove(bBookEntry);

            pMainPanel.add(bLibrarianEntry);
            pMainPanel.add(bEdit);
            pMainPanel.add(deleteLibrarian);


            deleteBook.setVisible(false);
            deleteMember.setVisible(false);
            deleteBorrow.setVisible(false);
            deleteReturn.setVisible(false);
            deleteLibrarian.setVisible(false);

            bEdit.setVisible(false);

            bBookEntry.setVisible(false);
            bMemberEntry.setVisible(false);
            bBorrowEntry.setVisible(false);
            bReturnEntry.setVisible(false);
            bLibrarianEntry.setVisible(true);


            // creating table for data outputs
            String[] columns = {"User ID","User Name","Email","Phone","Role"};
            DefaultTableModel model = new DefaultTableModel(columns,0);
            JTable table = new JTable(model);
            table.setRowHeight(25);

            table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));// set table header font bold
            table.getTableHeader().setBackground(new Color(207, 108, 37));//table header box bgcolor
            table.getTableHeader().setForeground(new Color(255,255,255));
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
                            resultSet.getString (3),
                            resultSet.getString(4),
                            resultSet.getString(5)
                    });
                }

                JScrollPane scrollPane = new JScrollPane(table);
                JScrollBar verticalBar = scrollPane.getVerticalScrollBar();


                verticalBar.setUI(new BasicScrollBarUI() {
                    @Override
                    protected void configureScrollBarColors() {
                        thumbColor = new Color(191, 159, 126);       // Terracotta
                        thumbDarkShadowColor = new Color(191, 159, 126);
                        thumbHighlightColor = new Color(191, 159, 126);
                        thumbLightShadowColor = new Color(191, 159, 126);
                        trackColor = new Color(245, 235, 221);      // Cream
                    }

                    @Override
                    protected JButton createDecreaseButton(int orientation) {
                        return createZeroButton();
                    }

                    @Override
                    protected JButton createIncreaseButton(int orientation) {
                        return createZeroButton();
                    }

                    private JButton createZeroButton() {
                        JButton button = new JButton();
                        button.setPreferredSize(new Dimension(0, 0));
                        button.setMinimumSize(new Dimension(0, 0));
                        button.setMaximumSize(new Dimension(0, 0));
                        return button;
                    }
                });

                verticalBar.setBackground(new Color(191, 159, 126));
                verticalBar.setOpaque(true);
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
                    bEdit.setVisible(true);
                    deleteLibrarian.setVisible(true);
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

            deleteLibrarian.addActionListener(be-> {

                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(url, user, password);
                    String sqlDelete = "DELETE FROM users WHERE userId = "+userId;
                    PreparedStatement pstatement = connection.prepareStatement(sqlDelete);


                    pstatement.executeUpdate();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                b5.doClick();


            });

            bEdit.addActionListener(be -> {
                bEdit.setVisible(false);
                JFrame userEditFrame = new JFrame("User Edit Frame");
                JPanel p1 = new JPanel(new FlowLayout());
                JPanel p2 = new JPanel(new FlowLayout());
                JPanel panel = new JPanel(new BorderLayout(10,10));
                JButton okBtn = new JButton("OK");
                JButton cancelBtn = new JButton("Cancel");

                JLabel l1 = new JLabel("New Value");

                JTextField jt1 = new JTextField();
                jt1.setPreferredSize(new Dimension(200,30));

                panel.add(p1,BorderLayout.CENTER);
                panel.add(p2,BorderLayout.SOUTH);

                p1.add(l1);
                p1.add(jt1);

                p2.add(okBtn);
                p2.add(cancelBtn);

                okBtn.addActionListener(a -> {

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
                        String usersql = "UPDATE users SET " + columnName + " = ? WHERE userId = ?";

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
                    removeEditFrame(userEditFrame);

                    b5.doClick();

                });


                userEditFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                userEditFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        bEdit.setVisible(true);
                    }
                });
                userEditFrame.setSize(300,200);
                userEditFrame.setResizable(false);
                userEditFrame.setVisible(true);
                userEditFrame.setLocation(1350,250);
                userEditFrame.add(panel);

                cancelBtn.addActionListener( b3e -> {
                    table.clearSelection();
                    userEditFrame.dispose();
                });

                jt1.addActionListener(se -> {
                    okBtn.doClick();
                });

                jt1.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            okBtn.doClick();
                        }
                    }
                });



                okBtn.setPreferredSize(new Dimension(100,50));
                cancelBtn.setPreferredSize(new Dimension(100,50));


                okBtn.setBackground(Color.WHITE);

                cancelBtn.setBackground(Color.WHITE);

                b2.addActionListener(e2 ->{removeEditFrame(userEditFrame);});
                b3.addActionListener(e2 ->{removeEditFrame(userEditFrame);});
                b4.addActionListener(e2 ->{removeEditFrame(userEditFrame);});
                b5.addActionListener(e2 ->{removeEditFrame(userEditFrame);});

                // Crucial step: Tell Swing to rebuild the layout and redraw the screen
                pMainPanel.revalidate();
                pMainPanel.repaint();


            });

            bLibrarianEntry.addActionListener( be2 -> {
                if (librarianEntryOpen) {
                    return;
                }

                librarianEntryOpen = true;

                JFrame librarianentry = new JFrame();
                librarianentry.setTitle("Librarian Entry");




                librarianentry.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        librarianEntryOpen = false;
                        bEdit.setVisible(true);
                        bLibrarianEntry.setVisible(true);
                    }
                });

                librarianentry.setLocation(1372,400);
                librarianentry.setSize(550,300);
                librarianentry.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                librarianentry.setVisible(true);

                JLabel name = new JLabel("Librarian Name");
                JLabel email = new JLabel("Email");
                JLabel phone = new JLabel("Phone");
                JLabel role = new JLabel("Role");
                JLabel passwordLabel = new JLabel("Password");

                JTextField tname = new JTextField(20);
                JTextField temail = new JTextField(20);
                JTextField tphone = new JTextField(20);
                JTextField trole = new JTextField(20);
                JTextField tpasswordLabel = new JTextField(20);
                JButton save = new JButton("Save");
                JButton cancel = new JButton("Cancel");
                JPanel pmain = new JPanel(new BorderLayout());
                JPanel p1 = new JPanel(new GridLayout(5, 2));
                Font myFont = new Font("Roboto", Font.PLAIN, 20);

                //setting labels fonts
                name.setFont(myFont);
                email.setFont(myFont);
                phone.setFont(myFont);
                role.setFont(myFont);
                passwordLabel.setFont(myFont);

                Color fontColor = new Color(32, 32, 32);
                //setting labels fonts color
                name.setForeground(fontColor);
                email.setForeground(fontColor);
                phone.setForeground(fontColor);
                role.setForeground(fontColor);
                passwordLabel.setForeground(fontColor);

                //setting size of text field box
                tname.setPreferredSize(new Dimension(20, 30));
                temail.setPreferredSize(new Dimension(20, 30));
                tphone.setPreferredSize(new Dimension(20, 30));
                trole.setPreferredSize(new Dimension(20, 30));
                tpasswordLabel.setPreferredSize(new Dimension(20, 30));

                // for alignment of label and text fields
                LayoutManager layout = new FlowLayout(FlowLayout.CENTER, 5, 10);
                //setting label panels alignments
                JPanel plb = new JPanel(layout);
                JPanel pla = new JPanel(layout);
                JPanel ply = new JPanel(layout);
                JPanel plp = new JPanel(layout);
                JPanel plpw = new JPanel(layout);
                //adding labels into aligned panels
                plb.add(name);
                pla.add(email);
                ply.add(phone);
                plp.add(role);
                plpw.add(passwordLabel);

                //setting text field panels alignments
                JPanel ptb = new JPanel(layout);
                JPanel pta = new JPanel(layout);
                JPanel pty = new JPanel(layout);
                JPanel ptp = new JPanel(layout);
                JPanel ptpw = new JPanel(layout);
                //adding text fields into aligned panels
                ptb.add(tname);
                pta.add(temail);
                pty.add(tphone);
                ptp.add(trole);
                ptpw.add(tpasswordLabel);

                //adding small panels into main GridLayout panel
                p1.add(plb);
                p1.add(ptb);

                p1.add(pla);
                p1.add(pta);

                p1.add(ply);
                p1.add(pty);

                p1.add(plp);
                p1.add(ptp);

                p1.add(plpw);
                p1.add(ptpw);

                //creating panel for buttons
                JPanel p2 = new JPanel(new FlowLayout());
                p2.add(save);
                p2.add(cancel);


                //creating main panel
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(p1, BorderLayout.CENTER);
                panel.add(p2, BorderLayout.SOUTH);
                add(panel);

                // background color of main panel
                Color bgcolor = new Color(240, 240, 240);
                // background color of button panel
                p2.setBackground(new Color(192, 192, 192));

                //setting background color of label panels and textfield panels
                plb.setBackground(bgcolor);
                pla.setBackground(bgcolor);
                ply.setBackground(bgcolor);
                plp.setBackground(bgcolor);
                plpw.setBackground(bgcolor);

                ptb.setBackground(bgcolor);
                pta.setBackground(bgcolor);
                pty.setBackground(bgcolor);
                ptp.setBackground(bgcolor);
                ptpw.setBackground(bgcolor);


                save.addActionListener(se -> {
                    pMainPanel.add(bEdit);
                    pMainPanel.add(bBookEntry);
                    String tnameText = tname.getText();
                    String temailText = temail.getText();
                    String tphoneText = tphone.getText();
                    String troleText = trole.getText();
                    String tpasswordText = tpasswordLabel.getText();

                    if(tnameText.equals("") || temailText.equals("") || tphoneText.equals("") || troleText.equals("") || tpasswordText.equals("")) {
                        JOptionPane.showMessageDialog(null, "You should input first!");
                    }else {
                        try {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection connection = DriverManager.getConnection(url, user, password);
                            String sqlInsert = "insert into users(userName,email,phone,role,password) values('"+tnameText+"','"+temailText+"','"+tphoneText+"','"+troleText+"','"+tpasswordText+"')";
                            PreparedStatement pstatement = connection.prepareStatement(sqlInsert);
                            pstatement.executeUpdate();
                            connection.close();



                            tname.setText("");
                            temail.setText("");
                            tphone.setText("");
                            trole.setText("");
                            tpasswordLabel.setText("");

                        }//try
                        catch(ClassNotFoundException e1){
                            System.out.println("ClassNotFound Error");
                        }
                        catch(SQLException se2) {
                            System.out.println("SQLException");
                            se2.printStackTrace();
                        }//catch
                    }//else

                    b5.doClick();
                    librarianentry.dispose();


                });

                cancel.addActionListener(ce -> {
                    pMainPanel.add(bEdit);
                    pMainPanel.add(bBookEntry);
                    librarianentry.dispose();
                });

                tpasswordLabel.addActionListener(oe -> {
                    save.doClick();
                });

                tpasswordLabel.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            save.doClick();
                        }
                    }
                });

                pmain.add(p1,BorderLayout.CENTER);
                pmain.add(p2,BorderLayout.SOUTH);
                librarianentry.add(pmain);
                librarianentry.add(pmain);


            });

            searchButton.addActionListener(se -> {
                String searchValue =  searchField.getText().trim();
                System.out.println("Search value: "+searchValue);
                if(searchValue.isEmpty()){
                    JOptionPane.showMessageDialog(null, "You should input first!");
                }else{

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        System.out.println("Driver created");
                        Connection connection = DriverManager.getConnection(url, user, password);
                        System.out.println("Database connected");

                        String sqlSearch = "select * from users where userName like ?";
                        PreparedStatement pst = connection.prepareStatement(sqlSearch);
                        pst.setString(1,"%" + searchValue + "%");




                        model.setRowCount(0);
                        ResultSet resultSet = pst.executeQuery();
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


                        connection.close();
                    }//try
                    catch(SQLException se1) {
                        se1.printStackTrace();
                    }catch(ClassNotFoundException ce) {
                        System.out.println("ClassNotFoundException");
                        ce.printStackTrace();

                    }//catch
                }
            });


        }
    }



    // ===== Warm Library Theme (style-only additions) =====
    private static final Color WOOD = new Color(62, 43, 32);          // #3E2B20
    private static final Color TERRACOTTA = new Color(169, 83, 43);  // #A9532B
    private static final Color CREAM = new Color(245, 235, 221);     // #F5EBDD
    private static final Color BEIGE = new Color(232, 220, 203);     // #E8DCCB
    private static final Color ESPRESSO = new Color(53, 35, 24);     // #352318
    private static final Color TABLE_ALT = new Color(248, 242, 233);
    private static final Color GRID = new Color(220, 207, 190);

    private void installWarmLibraryTheme() {
        applyWarmLibraryStyle(this);

        // Style frames/dialogs created later (for example, Add New Book) without
        // changing their existing listeners, layout, database code, or behavior.
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event instanceof WindowEvent
                    && ((WindowEvent) event).getID() == WindowEvent.WINDOW_OPENED) {
                Window openedWindow = ((WindowEvent) event).getWindow();
                SwingUtilities.invokeLater(() -> applyWarmLibraryStyle(openedWindow));
            }
        }, AWTEvent.WINDOW_EVENT_MASK);
    }

    private void applyWarmLibraryStyle(Component component) {
        if (component == null) return;

        if (component instanceof JFrame) {
            JFrame frame = (JFrame) component;
            frame.getContentPane().setBackground(CREAM);
            styleChildren(frame.getContentPane());
            return;
        }

        if (component instanceof JDialog) {
            JDialog dialog = (JDialog) component;
            dialog.getContentPane().setBackground(CREAM);
            styleChildren(dialog.getContentPane());
            return;
        }

        if (component instanceof JPanel) {
            JPanel currentPanel = (JPanel) component;
            if (currentPanel == pMenu) {
                currentPanel.setBackground(WOOD);
                currentPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, BEIGE));
            } else if (currentPanel == pTop) {
                currentPanel.setBackground(BEIGE);
                currentPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, WOOD));
            } else if (currentPanel == pBottom) {
                currentPanel.setBackground(BEIGE);
                currentPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, GRID));
            } else if (currentPanel == pMain || currentPanel == pMainPanel || currentPanel == panel) {
                currentPanel.setBackground(CREAM);
                currentPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            } else if (currentPanel == searchPanel) {
                currentPanel.setBackground(CREAM);
            } else {
                currentPanel.setBackground(CREAM);
            }
            currentPanel.setOpaque(true);
            styleChildren(currentPanel);
            return;
        }

        if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            scrollPane.setBackground(CREAM);
            scrollPane.getViewport().setBackground(CREAM);
            styleChildren(scrollPane);
            return;
        }

        if (component instanceof JTable) {
            styleWarmTable((JTable) component);
            return;
        }

        if (component instanceof JComboBox) {
            JComboBox<?> combo = (JComboBox<?>) component;
            combo.setBackground(CREAM);
            combo.setForeground(ESPRESSO);
            combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
            combo.setOpaque(true);
            return;
        }

        if (component instanceof JTextField || component instanceof JPasswordField
                || component instanceof JTextArea) {
            component.setBackground(Color.WHITE);
            component.setForeground(ESPRESSO);
            component.setFont(new Font("SansSerif", Font.PLAIN, 13));
            if (component instanceof JTextField) {
                ((JTextField) component).setCaretColor(TERRACOTTA);
                ((JTextField) component).setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GRID),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
            }
            return;
        }

        if (component instanceof JButton) {
            styleWarmButton((JButton) component);
            return;
        }

        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            label.setForeground(ESPRESSO);
            if (label == lTop) {
                label.setForeground(ESPRESSO);
                label.setFont(new Font("Serif", Font.BOLD, 20));
            } else if (label == lMenu) {
                label.setForeground(CREAM);
                label.setFont(new Font("Serif", Font.BOLD, 16));
            } else {
                label.setFont(new Font("SansSerif", Font.PLAIN, 13));
            }
        }
    }

    private void styleChildren(Container container) {
        // Also style components added later by the existing page/navigation code.
        if (container instanceof JComponent) {
            JComponent swingContainer = (JComponent) container;
            if (swingContainer.getClientProperty("warmLibraryThemeListener") == null) {
                swingContainer.putClientProperty("warmLibraryThemeListener", Boolean.TRUE);
                swingContainer.addContainerListener(new ContainerAdapter() {
                    @Override
                    public void componentAdded(ContainerEvent event) {
                        SwingUtilities.invokeLater(() -> applyWarmLibraryStyle(event.getChild()));
                    }
                });
            }
        }
        for (Component child : container.getComponents()) {
            applyWarmLibraryStyle(child);
        }
    }

    private void styleWarmButton(JButton button) {
        String buttonText = button.getText() == null ? "" : button.getText().trim().toLowerCase();
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (button == b1 || button == b2 || button == b3 || button == b4 || button == b5) {
            button.setBackground(WOOD);
            button.setForeground(CREAM);
            button.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        } else if (buttonText.contains("delete")) {
            button.setBackground(new Color(173, 58, 48));
            button.setForeground(Color.WHITE);
        } else if (buttonText.contains("cancel") || buttonText.contains("close")) {
            button.setBackground(BEIGE);
            button.setForeground(ESPRESSO);
            button.setBorder(BorderFactory.createLineBorder(GRID));
            button.setBorderPainted(true);
        } else if (buttonText.contains("edit") || buttonText.contains("rename")) {
            button.setBackground(Color.GREEN);
            button.setForeground(ESPRESSO);

        } else {
            button.setBackground(TERRACOTTA);
            button.setForeground(Color.WHITE);
        }
    }

    private void styleWarmTable(JTable table) {
        table.setBackground(Color.WHITE);
        table.setForeground(ESPRESSO);
        table.setSelectionBackground(new Color(230, 198, 170));
        table.setSelectionForeground(ESPRESSO);
        table.setGridColor(GRID);
        table.setRowHeight(Math.max(table.getRowHeight(), 25));
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        javax.swing.table.JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setBackground(WOOD);
            header.setForeground(CREAM);
            header.setFont(new Font("SansSerif", Font.BOLD, 12));
            header.setOpaque(true);
        }

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable source, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(
                        source, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    cell.setBackground(new Color(230, 198, 170));
                    cell.setForeground(ESPRESSO);
                } else {
                    cell.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALT);
                    cell.setForeground(ESPRESSO);
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
                return cell;
            }
        };
        for (int column = 0; column < table.getColumnCount(); column++) {
            table.getColumnModel().getColumn(column).setCellRenderer(renderer);
        }
    }
    // ===== End warm theme additions =====

    public Main() {




        SwingUtilities.invokeLater(() -> b1.doClick());// starting the program by showing Book Lists


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
        //pMenu.add(b6);

        pMainPanel.add(bBookEntry);
        pMainPanel.add(bMemberEntry);
        pMainPanel.add(bBorrowEntry);
        pMainPanel.add(bReturnEntry);
        pMainPanel.add(bLibrarianEntry);

        pMainPanel.add(categoryBox);

        bBookEntry.setVisible(false);
        bMemberEntry.setVisible(false);
        bBorrowEntry.setVisible(false);
        bReturnEntry.setVisible(false);
        bLibrarianEntry.setVisible(false);


        pBottom.add(login,BorderLayout.WEST);
        pBottom.setPreferredSize(new Dimension(500,50));

        JLabel lDate = new JLabel("Date: "+formattedDate);

        pBottom.add(lDate,BorderLayout.CENTER);
        pBottom.add(lDate,BorderLayout.EAST);

        b1.addActionListener(new ShowBookList());
        b2.addActionListener(new ShowMembers());
        b3.addActionListener(new ShowBorrow());
        b4.addActionListener(new ShowReturn());
        b5.addActionListener(new ShowLibrarians());


        add(pTop, BorderLayout.NORTH);
        add(pBottom, BorderLayout.SOUTH);
        add(pMenu, BorderLayout.WEST);


        panel.add(pMain,BorderLayout.CENTER);
        panel.add(pMainPanel, BorderLayout.SOUTH);
        pMainPanel.setPreferredSize(new Dimension(500,40));
        add(panel, BorderLayout.CENTER);

        // Apply the warm wood-and-cream theme after existing components are assembled.
        installWarmLibraryTheme();

    }
    void removeEditFrame(JFrame frame){
        frame.dispose();
    }

    public static void main(String[] args) {
        Main frame = new Main();
        frame.setTitle("Library Management System");
        frame.setSize(850, 700);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
