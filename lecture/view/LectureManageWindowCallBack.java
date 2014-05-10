package lecture.view;

/**
 * Created by hyice on 4/24/14.
 */
public interface LectureManageWindowCallBack {

    public void lectureManageWindowClosed();

    public void selectLectureAtIndex(int index);
    public void selectStudentAtIndex(int index);

    public void lectureAddBtnPressed();
    public void lectureRemoveBtnPressedWithIndex(int index);
    public void lectureModifyBtnPressedWithIndex(int index);

    public void studentAddBtnPressed();
    public void studentRemoveBtnPressedWithIndex(int index);
    public void studentImportBtnPressed();

    public void cancelBtnPressedWithIndex(int index);
    public void confirmBtnPressed(int index, String name, int cid,
                                  String startTime, String endTime, int weekday);
}
