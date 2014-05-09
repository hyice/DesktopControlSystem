package controller;

import controller.callBack.ClassroomManageWindowCallBack;
import controller.callBack.ClassroomVCTLCallBack;
import controller.callBack.ConfirmAlertCallBack;
import model.Utilities;
import model.classroom.ClassroomModel;
import model.classroom.FullClassRoom;
import ui.window.ClassroomManageWindow;

/**
 * Created by hyice on 4/23/14.
 */
public class ClassroomVCTL implements ClassroomManageWindowCallBack, ConfirmAlertCallBack{

    private ClassroomManageWindow window;
    private ClassroomVCTLCallBack callBack;
    private ClassroomModel model;


    public void showWindow() {

        window.setVisible(true);
    }

    public ClassroomVCTL(ClassroomVCTLCallBack aCallBack) {

        callBack = aCallBack;

        model = new ClassroomModel();

        window = new ClassroomManageWindow(this);
        window.setRoomNameListData(model.getNameArray());
    }

    // @ClassroomManageWindowCallBack
    public void classroomManageWindowClosed() {

        callBack.classroomManageWindowClosed();
    }

    public void selectedClassroomAtIndex(int index) {

        if(window.isInEditMode()) {

            window.turnOffEditModeWithSelectedIndex(index);
        }else {

            FullClassRoom classroom = model.getFullClassroomAtIndex(index);
            window.setContentLblsWithFullClassroom(classroom);
        }
    }

    public void addBtnPressed() {

        if(window.isInEditMode()) return;

        FullClassRoom newRoom = model.addANewRoom();
        window.addANewClassroom(newRoom);
    }

    public void removeBtnPressedWithIndex(int index) {

        if(window.isInEditMode()) return;

        String alertMsg = model.checkRoomIfCanBeDeleted(index);

        if(alertMsg.equals("")) {

            this.deleteClassroomDirectly();
        }else {

            Utilities.showConfirmAlertWithText(alertMsg, window, this);
        }

    }

    public void modifyBtnPressedWithIndex(int index) {

        if(window.isInEditMode()) return;

        window.turnOnEditMode(model.getFullClassroomAtIndex(index));
    }

    public void cancelBtnPressedWithSelectedIndex(int index) {

        FullClassRoom aRoom = model.getFullClassroomAtIndex(index);

        if(aRoom.getCid() == 0) {

            //取消了新增操作

            model.removeRoomAtIndex(index);
            window.cancelAddingRoom();
        }else {

            //取消了编辑操作
            window.turnOffEditModeWithSelectedIndex(index);
        }


    }

    public void confirmBtnPressed(int selectedIndex, String name,
                                  int seats, String guardIp, String forwardIp) {

        String errMsg = model.checkRoomInfoIfAnyError(selectedIndex, name, seats, guardIp, forwardIp);

        if(errMsg.equals("")) {

            model.updateRoom(selectedIndex, name, seats, guardIp, forwardIp);
            window.finishEditing(name);
        }else {

            Utilities.alertWithText(errMsg, window);
        }

    }

    // @ConfirmAlertCallBack
    public void confirmAlertOkBtnPressed() {

        this.deleteClassroomDirectly();
    }

    // @ private methods
    private void deleteClassroomDirectly() {

        int index = window.getClassroomSelectedIndex();

        model.removeRoomAtIndex(index);
        window.removeRoomAtIndex(index);
    }
}
