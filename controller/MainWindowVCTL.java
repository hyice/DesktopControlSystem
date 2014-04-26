package controller;

import controller.callBack.ClassroomVCTLCallBack;
import controller.callBack.LectureVCTLCallBack;
import controller.callBack.MainWindowCallBack;
import server.Server;
import ui.window.MainWindow;

/**
 * Created by hyice on 4/23/14.
 */
public class MainWindowVCTL implements MainWindowCallBack, ClassroomVCTLCallBack, LectureVCTLCallBack{

    private MainWindow mainWindow = null;
    private ClassroomVCTL classroomVCTL = null;
    private LectureVCTL lectureVCTL = null;

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

    // @ClassroomVCTLCallBack
    public void classroomManageWindowClosed() {

        classroomVCTL = null;
    }

    // @LectureVCTLCallBack
    public void lectureManageWindowClosed() {

        lectureVCTL = null;
    }

    public static void main(String[] args) {

        MainWindowVCTL vctl = new MainWindowVCTL();

        Server server = new Server();
        server.start();
    }
}
