package viewer;

import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLiteViewer extends JFrame {

    JComboBox tablesComboBox = new JComboBox();
    JTextArea queryTextArea;

    public SQLiteViewer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("SQLite Viewer");


        String url = "jdbc:sqlite:fruits.db";
        SQLiteDataSource x;



        createNorthPanel();
        createCenterPanel();




        setVisible(true);


    }

    private void createCenterPanel() {
        JPanel centerPanel = new JPanel();

        queryTextArea = new JTextArea();
        queryTextArea.setName("QueryTextArea");



        tablesComboBox.setName("TablesComboBox");
        tablesComboBox.addActionListener(actionEvent -> {
            if (tablesComboBox.getSelectedItem() != null) {
                String tableName = tablesComboBox.getSelectedItem().toString();
                queryTextArea.setText("SELECT * FROM " + tableName + ";");
            }
                }
                );

//        tablesComboBox.setPreferredSize(new Dimension(700, 50));
//        tablesComboBox.setMaximumSize(new Dimension(700, 50));
//        tablesComboBox.setMinimumSize(new Dimension(700, 50));



//        queryTextArea.setPreferredSize(new Dimension(700, 150));
//        queryTextArea.setMaximumSize(new Dimension(700, 150));
//        queryTextArea.setMinimumSize(new Dimension(700, 150));

        JButton executeButton = new JButton("Execute");
        executeButton.setName("ExecuteQueryButton");

        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        //centerPanel.setLayout(new FlowLayout());
        centerPanel.add(tablesComboBox);


        JPanel flowPanel = new JPanel();
        flowPanel.setLayout(new FlowLayout());
        flowPanel.add(queryTextArea);
        flowPanel.add(executeButton);

        centerPanel.add(queryTextArea);
        centerPanel.add(flowPanel);

        add(centerPanel, BorderLayout.CENTER);




    }

    private void createNorthPanel() {
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());

        JTextField textField = new JTextField();
        textField.setName("FileNameTextField");
        textField.setPreferredSize(new Dimension(300, 25));


        JButton openButton = new JButton("Open");
        openButton.setName("OpenFileButton");
        openButton.addActionListener(actionEvent -> {
            String dbName = textField.getText();
            String path = "jdbc:sqlite:" + dbName;
            SQLiteDataSource dataSource = new SQLiteDataSource();
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




        northPanel.add(textField);
        northPanel.add(openButton);


        add(northPanel, BorderLayout.NORTH);
    }
}
