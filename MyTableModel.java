package viewer;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class MyTableModel extends AbstractTableModel {

    String[] columns;
    Object[][] data;
    public MyTableModel() {
        columns = new String[]{"col1", "col2", "col3", "co4"};
        data = new String[2][];
        data[0] = new String[]{"1 a", "1 b", "1 c", "1 d" };
        data[1] = new String[]{"2 a", "2 b", "2 c", "2 d" };
    }

    public MyTableModel(String[] columns, Object[][] data) {
        this.columns = columns;
        this.data = data;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int i, int j) {
        return data[i][j];
    }

//    @Override
//    public void setValueAt(Object value, int rowIndex, int columnIndex) {
//        data[rowIndex][columnIndex] = value;
//        fireTableCellUpdated(rowIndex,columnIndex);
//        fireTableDataChanged();
//    }
}

class CustomListener implements TableModelListener {
    @Override
    public void tableChanged(TableModelEvent e) {
        System.out.println("Table Updated!");
    }
}

