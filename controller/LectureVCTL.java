package controller;

import controller.callBack.LectureManageWindowCallBack;
import controller.callBack.LectureVCTLCallBack;

import model.lecture.LectureModel;
import ui.window.LectureManageWindow;

/**
 * Created by hyice on 4/24/14.
 */
public class LectureVCTL implements LectureManageWindowCallBack{

    private LectureVCTLCallBack callBack;
    private LectureManageWindow window;
    private LectureModel model;

    public LectureVCTL(LectureVCTLCallBack aCallBack) {

        callBack = aCallBack;

        model = new LectureModel();

        window = new LectureManageWindow(this);
        window.setLectureListData(model.getNameArray());

    }

    public void showWindow() {

        window.setVisible(true);
    }

    // @LectureManageWindowCallBack
    public void lectureManageWindowClosed() {

        callBack.lectureManageWindowClosed();
    }

    public void selectLectureAtIndex(int index) {

        if(window.isInEditMode()) {

            window.turnOffEditMode();
        }

        window.setContentLblsWithData(model.getLectureAtIndex(index));
    }

    public void lectureAddBtnPressed() {

        if(window.isInEditMode()) return;



    }

    public void lectureRemoveBtnPressedWithIndex(int index) {

        if(window.isInEditMode()) return;

        model.removeLecture(index);
        window.removeLectureAtIndex(index);
    }

    public void lectureModifyBtnPressedWithIndex(int index) {

        window.turnOnEditMode(model.getLectureAtIndex(index));
    }

    public void cancelBtnPressedWithIndex(int index) {

        window.turnOffEditMode();
    }

    public void confirmBtnPressed(int index, String name, int cid,
                                  String startTime, String endTime, int weekday) {

        model.updateLecture(index, name, cid, startTime, endTime, weekday);
        window.turnOffEditMode();
    }
}
