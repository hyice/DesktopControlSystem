package history.view;

import utilities.Utilities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by hyice on 5/10/14.
 */
public class HistoryDisplayWindow extends JFrame{

    private final int DefaultWindowWidth = 500;
    private final int DefaultWindowHeight = 500;

    private String[] tableHeaders = {"学号", "教室", "座位号", "开始时间", "使用时长(分)"};

    private DefaultTableModel tableModel;
    private MyTable myTable;

    private HistoryDisplayWindowCallBack callBack;

    public HistoryDisplayWindow(HistoryDisplayWindowCallBack aCallBack, Object[][] data){

        callBack = aCallBack;

        setSize(DefaultWindowWidth, DefaultWindowHeight);
        setLocation((Utilities.getScreenWidth()-DefaultWindowWidth)/2,
                (Utilities.getScreenHeight()-DefaultWindowHeight)/2);

        setTitle("使用记录");

//        setLayout(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {

                super.windowClosed(e);

                callBack.historyDisplayWindowClosed();

            }
        });

        showTable(data);
    }

    public void setTableData(Object[][] data) {

        tableModel.setDataVector(data, tableHeaders);
        myTable.paintRow();
        myTable.fitTableColumns();
    }

    private void showTable(Object[][] cellData) {

        tableModel = new DefaultTableModel(cellData, tableHeaders) {

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        myTable = new MyTable(tableModel);
        add(new JScrollPane(myTable));
    }
}
