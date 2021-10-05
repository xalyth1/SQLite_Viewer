package viewer;

import javax.swing.*;
import java.awt.*;

public class SQLiteViewer extends JFrame {

    public SQLiteViewer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 900);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("SQLite Viewer");



        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());

        JTextField textField = new JTextField();
        textField.setName("FileNameTextField");
        textField.setPreferredSize(new Dimension(300, 25));
        JButton openButton = new JButton("Open");
        openButton.setName("OpenFileButton");

        northPanel.add(textField);
        northPanel.add(openButton);

        add(northPanel, BorderLayout.NORTH);

        setVisible(true);


    }
}
