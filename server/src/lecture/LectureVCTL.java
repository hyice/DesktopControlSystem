package lecture;

import lecture.view.AddOneStudentWindowCallBack;

import lecture.model.FullLecture;
import lecture.model.LectureModel;
import lecture.view.LectureManageWindow;
import lecture.view.LectureManageWindowCallBack;
import lecture.view.AddOneStudentWindow;
import utilities.Utilities;

import javax.swing.*;
import java.io.*;

/**
 * Created by hyice on 4/24/14.
 */
public class LectureVCTL implements LectureManageWindowCallBack, AddOneStudentWindowCallBack{

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
        window.setStudentListData(model.getStudentsInfoOfLecture(index));
    }

    public void selectStudentAtIndex(int index) {

    }

    public void lectureAddBtnPressed() {

        if(window.isInEditMode()) return;

        FullLecture newLecture = model.addANewLecture();
        window.addANewLecture(newLecture);
    }

    public void lectureRemoveBtnPressedWithIndex(int index) {

        if(window.isInEditMode()) return;

        model.removeLecture(index);
        window.removeLectureAtIndex(index);
    }

    public void lectureModifyBtnPressedWithIndex(int index) {

        window.turnOnEditMode(model.getLectureAtIndex(index));
    }

    public void studentAddBtnPressed() {

        AddOneStudentWindow addOneStudentWindow = new AddOneStudentWindow(window, this);

        addOneStudentWindow.setVisible(true);

    }

    public void studentRemoveBtnPressedWithIndex(int index) {

        model.removeStudentOfLecture(index, window.getLectureSelectedIndex());
        window.removeStudentAtIndex(index);
    }

    public void studentImportBtnPressed() {

        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(window);

        String filePath = chooser.getSelectedFile().getPath();
        System.out.println("studentImportBtnPressed:\n" + filePath);

        if(!filePath.endsWith(".txt")) {

            Utilities.alertWithText("文件格式不对！\n请选择txt格式的文件", window);
        }else {

            File txtFile = new File(filePath);
            try{

                InputStreamReader reader = new InputStreamReader(
                        new FileInputStream(txtFile));
                BufferedReader br = new BufferedReader(reader);
                String sid = "";
                sid = br.readLine();
                while (sid != null && sid.length()!=0) {

                    if(Utilities.isStudentNumber(sid)) {

                        model.addStudentToLecture(sid, window.getLectureSelectedIndex());
                        window.addANewStudent(sid);
                        sid = br.readLine();
                    }else {

                        Utilities.alertWithText("文件格式不对，获取到非法学号\n" + sid, window);
                        break;
                    }

                }

            }catch(Exception e) {

                System.err.println(e.getMessage());
            }

        }
    }

    public void cancelBtnPressedWithIndex(int index) {

        FullLecture aLecture = model.getLectureAtIndex(index);

        if (aLecture.getLid()==0) {

            window.removeLectureAtIndex(index);
            model.removeLecture(index);
        }

        window.turnOffEditMode();
    }

    public void confirmBtnPressed(int index, String name, int cid,
                                  String startTime, String endTime, int weekday) {

        FullLecture aLecture = model.getLectureAtIndex(index);

        if(!aLecture.getName().equals(name)) {

            window.updateLectureName(index, name);
        }

        model.updateLecture(index, name, cid, startTime, endTime, weekday);

        window.turnOffEditMode();
    }

    // @AddOneStudentWindowCallBack
    public void addOneStudentWindowConfirmBtnPressed(String sid) {

        model.addStudentToLecture(sid, window.getLectureSelectedIndex());
        window.addANewStudent(sid);
    }
}
