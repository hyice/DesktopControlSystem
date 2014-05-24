package main;

import classroom.ClassroomVCTL;
import classroom.ClassroomVCTLCallBack;
import main.view.MainWindowCallBack;
import tempOpen.TempOpenVCTL;
import history.HistoryVCTL;
import history.HistoryVCTLCallBack;
import lecture.LectureVCTL;
import lecture.LectureVCTLCallBack;
import server.Server;
import main.view.MainWindow;
import tempOpen.TempOpenVCTLCallBack;

/**
 * Created by hyice on 4/23/14.
 */
public class MainWindowVCTL implements MainWindowCallBack,
        ClassroomVCTLCallBack, LectureVCTLCallBack,
        TempOpenVCTLCallBack, HistoryVCTLCallBack {

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
    public void historySearchWindowClosed() {

        historyVCTL = null;
    }



    public static void main(String[] args) {

        MainWindowVCTL vctl = new MainWindowVCTL();

        Server server = new Server();
        server.start();
    }
}
