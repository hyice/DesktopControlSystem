package history;

import history.model.HistoryModel;
import history.view.HistoryDisplayWindow;
import history.view.HistoryDisplayWindowCallBack;
import history.view.HistorySearchWindow;
import history.view.HistorySearchWindowCallBack;

/**
 * Created by hyice on 5/10/14.
 */
public class HistoryVCTL implements HistoryDisplayWindowCallBack,HistorySearchWindowCallBack {

    private HistoryDisplayWindow displayWindow;
    private HistorySearchWindow searchWindow;
    private HistoryVCTLCallBack callBack;
    private HistoryModel model;

    public HistoryVCTL(HistoryVCTLCallBack aCallBack) {

        callBack = aCallBack;

        model = new HistoryModel();

        searchWindow = new HistorySearchWindow(this);
//        displayWindow = new HistoryDisplayWindow(this, model.getAllHistoryData());
    }

    // @HistoryDisplayWindowCallBack
    public void historyDisplayWindowClosed() {

        displayWindow = null;
    }

    // @HistorySearchWindowCallBack
    public void historySearchWindowClosed() {

        if(displayWindow!=null) {

            displayWindow.setVisible(false);
            displayWindow = null;
        }

        callBack.historySearchWindowClosed();
    }

    public void historySearchBtnPressed(String sid, int cid, int seat, String startDate, String endDate) {

        if(displayWindow==null) {

            displayWindow = new HistoryDisplayWindow(this, model.getHistoryDataByCondition(
                    sid, cid, seat, startDate, endDate));
            displayWindow.setVisible(true);
        }else {

            displayWindow.setTableData(model.getHistoryDataByCondition(
                    sid, cid, seat, startDate, endDate
            ));
            displayWindow.toFront();
        }

    }

    public void showWindow() {

        searchWindow.setVisible(true);
    }

    public static void main(String[] args) {

        HistoryVCTL vctl = new HistoryVCTL(null);
        vctl.showWindow();
    }
}
