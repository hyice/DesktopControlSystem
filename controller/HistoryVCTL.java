package controller;

import controller.callBack.HistoryDisplayWindowCallBack;
import controller.callBack.HistoryVCTLCallBack;
import model.history.HistoryModel;
import ui.window.HistoryDisplayWindow;

/**
 * Created by hyice on 5/10/14.
 */
public class HistoryVCTL implements HistoryDisplayWindowCallBack {

    private HistoryDisplayWindow displayWindow;
    private HistoryVCTLCallBack callBack;
    private HistoryModel model;

    public HistoryVCTL(HistoryVCTLCallBack aCallBack) {

        callBack = aCallBack;

        model = new HistoryModel();

        displayWindow = new HistoryDisplayWindow(this, model.getAllHistoryData());
    }

    // @HistoryDisplayWindowCallBack
    public void historyDisplayWindowClosed() {

        callBack.historyDisplayWindowClosed();
    }

    public void showWindow() {

        displayWindow.setVisible(true);
    }

    public static void main(String[] args) {

        HistoryVCTL vctl = new HistoryVCTL(null);
        vctl.showWindow();
    }
}
