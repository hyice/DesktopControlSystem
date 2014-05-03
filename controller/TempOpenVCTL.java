package controller;

import controller.callBack.TempOpenManageWindowCallBack;
import controller.callBack.TempOpenVCTLCallBack;
import model.Utilities;
import model.tempOpen.TempOpenModel;
import ui.window.TempOpenManageWindow;

/**
 * Created by hyice on 5/3/14.
 */
public class TempOpenVCTL implements TempOpenManageWindowCallBack{

    private TempOpenVCTLCallBack callBack;
    private TempOpenManageWindow window;
    private TempOpenModel model;

    public TempOpenVCTL(TempOpenVCTLCallBack aCallBack) {

        callBack = aCallBack;

        window = new TempOpenManageWindow(this);
        model = new TempOpenModel();
    }

    public void showWindow() {

        window.setVisible(true);
    }

    // @TempOpenManageWindowCallBack
    public void tempOpenManageWindowClosed() {

        callBack.tempOpenManageWindowClosed();
    }

    public void confirmBtnPressed(String sid, int cid, int minutes) {

        if(sid==null || sid.length()==0) {

            Utilities.alertWithText("学号不能为空！", window);
        }else if(minutes < 5) {

            Utilities.alertWithText("时间过短！建议时间不少于10分钟。", window);
        }else {

            model.newTempOpen(sid, cid, minutes);
            window.tempOpenSucceed();
        }

    }
}
