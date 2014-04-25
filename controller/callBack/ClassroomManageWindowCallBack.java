package controller.callBack;

/**
 * Created by hyice on 4/23/14.
 */
public interface ClassroomManageWindowCallBack {

    public void classroomManageWindowClosed();

    public void selectedClassroomAtIndex(int index);

    public void addBtnPressed();
    public void removeBtnPressedWithIndex(int index);
    public void modifyBtnPressedWithIndex(int index);

    public void cancelBtnPressedWithSelectedIndex(int index);
    public void confirmBtnPressed(int selectedIndex, String name,
                                  int seats, String guardIp, String forwardIp);
}
