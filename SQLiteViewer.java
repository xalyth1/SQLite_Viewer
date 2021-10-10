package viewer;

import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class SQLiteViewer extends JFrame {



    JTextField textField;
    JButton openButton;
    JComboBox tablesComboBox;
    JTextArea queryTextArea;
    JButton executeButton;
    JTable table;
    JScrollPane scrollPane;



    SQLiteDataSource dataSource;

    public SQLiteViewer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("SQLite Viewer");

//        String url = "jdbc:sqlite:fruits.db";
//        SQLiteDataSource x;

        initializeGUIelements();
        organizeLayout();
        setListeners();


        setVisible(true);
    }

    private void organizeLayout() {
        BoxLayout boxLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        JPanel upPanel = new JPanel();
        upPanel.setLayout(new FlowLayout());

        upPanel.add(textField);
        upPanel.add(openButton);
        add(upPanel);

        add(tablesComboBox);
        add(queryTextArea);
        add(executeButton);
        add(scrollPane);
    }

    private void initializeGUIelements() {
        JTextField textField = new JTextField();
        textField.setName("FileNameTextField");
        //textField.setText("fruits.db");
        textField.setPreferredSize(new Dimension(400,25));

        JButton openButton = new JButton("Open");
        openButton.setName("OpenFileButton");

        JComboBox tablesComboBox = new JComboBox();
        tablesComboBox.setName("TablesComboBox");

        JTextArea queryTextArea = new JTextArea();
        queryTextArea.setName("QueryTextArea");
        //queryTextArea.setText("SELECT * FROM tablename;");

        JButton executeButton = new JButton("Execute");
        executeButton.setName("ExecuteQueryButton");

        MyTableModel model = new MyTableModel();
        model.addTableModelListener(new CustomListener());
        table = new JTable(model);
        table.setName("Table");

        JScrollPane scrollPane = new JScrollPane(table);

        this.textField = textField;
        this.openButton = openButton;
        this.tablesComboBox = tablesComboBox;
        this.queryTextArea = queryTextArea;
        this.executeButton = executeButton;
        //this.table = table;
        this.scrollPane = scrollPane;
    }

    private void setListeners() {
        openButton.addActionListener(actionEvent -> {
            String dbName = textField.getText();
            String path = "jdbc:sqlite:" + dbName;
            dataSource = new SQLiteDataSource();
            dataSource.setUrl(path);
            try (Connection con = dataSource.getConnection()) {
                if (con.isValid(5)) {
                    System.out.println("Connection is valid.");
                    DatabaseMetaData metaData = con.getMetaData();
                    ResultSet rs = metaData.getTables(null, null, "%", null);
                    ArrayList<String> al = new ArrayList<>();

                    tablesComboBox.removeAllItems();
                    while (rs.next()) {
                        String str = rs.getString("TABLE_NAME");
                        System.out.println(str);
                        al.add(str);
                        tablesComboBox.addItem(str);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });


        tablesComboBox.addActionListener(actionEvent -> {
                if (tablesComboBox.getSelectedItem() != null) {
                    String tableName = tablesComboBox.getSelectedItem().toString();
                    queryTextArea.setText("SELECT * FROM " + tableName + ";");
                }
            }
        );

        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String queryText = queryTextArea.getText();
                try (Connection con = dataSource.getConnection()) {

                    ResultSet count_rs = con.createStatement().executeQuery(
                            "SELECT COUNT(*) FROM " +  tablesComboBox.getSelectedItem().toString()+";");
                    int rowCounted = count_rs.getInt(1);



                    Statement stmt = con.createStatement(/*ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY*/);
                    ResultSet rs = stmt.executeQuery(queryText);
                    System.out.println("Result from executing SQL:");

                    ResultSetMetaData rsmd = rs.getMetaData();
                    int numberOfColumns = rsmd.getColumnCount();


                    String[] modelColumns = new String[numberOfColumns];
                    String[][] modelData = new String[rowCounted][numberOfColumns];

                    for (int i = 1; i <= numberOfColumns; i++) {
                        System.out.println("Column " + i + " name: " + rsmd.getColumnName(i));
                        modelColumns[i - 1] = rsmd.getColumnName(i);
                    }

                    int rowCount = 1;
                    while (rs.next()) {
                        System.out.println("Row " + rowCount + ":  ");
                        for (int i = 1; i <= numberOfColumns; i++) {
                            System.out.print("   Column " + i + ":  ");
                            System.out.println(rs.getString(i));
                            modelData[rowCount - 1][i - 1] = rs.getString(i);
                        }
                        System.out.println("");
                        rowCount++;
                    }

                    //MyTableModel model = new MyTableModel(modelColumns, modelData);
                    //table = new JTable(model);
                    updateModel(modelColumns, modelData);

                    //System.out.println("model : " + model.getValueAt(0,1) );

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void updateModel(String[] modelColumns, Object[][] modelData) {
        MyTableModel mtm =  (MyTableModel) this.table.getModel();
        mtm.columns = modelColumns;
        mtm.data = modelData;
        mtm.fireTableStructureChanged();
        mtm.fireTableDataChanged();
    }

}
