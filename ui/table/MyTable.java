package ui.table;

import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Enumeration;

/**
 * Created by hyice on 5/10/14.
 */
public class MyTable extends JTable {

    public MyTable(TableModel dm) {

        super(dm);
        paintRow();

        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.getModel());
        this.setRowSorter(sorter);

        this.setIntercellSpacing(new Dimension(1,1));

        fitTableColumns(this);

        DefaultTableCellHeaderRenderer headerRenderer = new DefaultTableCellHeaderRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        getTableHeader().setDefaultRenderer(headerRenderer);
    }

    public void paintRow() {

        TableColumnModel tcm = this.getColumnModel();
        for (int i = 0, n = tcm.getColumnCount(); i < n; i++) {
            TableColumn tc = tcm.getColumn(i);
            tc.setCellRenderer(new RowRenderer());
        }
    }

    public void fitTableColumns(JTable myTable) {

        myTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JTableHeader header = myTable.getTableHeader();
        int rowCount = myTable.getRowCount();
        Enumeration columns = myTable.getColumnModel().getColumns();
        while(columns.hasMoreElements()) {
            TableColumn column = (TableColumn)columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            int width = (int)header.getDefaultRenderer().getTableCellRendererComponent
            (myTable, column.getIdentifier(), false, false, -1, col).getPreferredSize().getWidth();
            for(int row = 0; row < rowCount; row++) {
                int preferedWidth = (int)myTable.getCellRenderer(row, col).getTableCellRendererComponent
                (myTable, myTable.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column); // 此行很重要
            column.setWidth(width+myTable.getIntercellSpacing().width);
        }
    }

    private class RowRenderer extends DefaultTableCellRenderer {

        private RowRenderer() {

            setHorizontalAlignment(JLabel.CENTER);
        }

        public Component getTableCellRendererComponent(JTable t, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {

            if (row % 2 == 0){

                setBackground(Color.LIGHT_GRAY);
            }
            else {

                setBackground(Color.WHITE);
            }

            return super.getTableCellRendererComponent(t, value, isSelected,
                    hasFocus, row, column);
        }
    }
}
