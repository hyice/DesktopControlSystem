package controller;

import controller.callBack.*;
import server.Server;
import ui.window.MainWindow;

/**
 * Created by hyice on 4/23/14.
 */
public class MainWindowVCTL implements MainWindowCallBack,
        ClassroomVCTLCallBack, LectureVCTLCallBack,
        TempOpenVCTLCallBack, HistoryVCTLCallBack{

    private MainWindow mainWindow = null;
    private ClassroomVCTL classroomVCTL = null;
    private LectureVCTL lectureVCTL = null;
    private TempOpenVCTL tempOpenVCTL = null;
    private HistoryVCTL historyVCTL = null;

    public MainWindowVCTL () {

        mainWindow = new MainWindow(this);

        mainWindow.setVisible(true);
    }

    // @MainWindowCallBack
    public void classroomBtnPressed() {

        if (classroomVCTL == null) {

            classroomVCTL = new ClassroomVCTL(this);
        }

        classroomVCTL.showWindow();

    }

    public void lectureBtnPressed() {

        if (lectureVCTL == null) {

            lectureVCTL = new LectureVCTL(this);
        }

        lectureVCTL.showWindow();
    }

    public void historyBtnPressed() {

        if(historyVCTL == null) {

            historyVCTL = new HistoryVCTL(this);
        }

        historyVCTL.showWindow();
    }

    public void tempOpenBtnPressed() {

        if(tempOpenVCTL == null) {

            tempOpenVCTL = new TempOpenVCTL(this);
        }

        tempOpenVCTL.showWindow();
    }

    // @ClassroomVCTLCallBack
    public void classroomManageWindowClosed() {

        classroomVCTL = null;
    }

    // @LectureVCTLCallBack
    public void lectureManageWindowClosed() {

        lectureVCTL = null;
    }

    // @TempOpenVCTLCallBack
    public void tempOpenManageWindowClosed() {

        tempOpenVCTL = null;
    }

    // @HistoryVCTLCallBack
    public void historyDisplayWindowClosed() {

        historyVCTL = null;
    }



    public static void main(String[] args) {

        MainWindowVCTL vctl = new MainWindowVCTL();

        Server server = new Server();
        server.start();
    }
}
