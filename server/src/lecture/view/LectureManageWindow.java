package lecture.view;

import utilities.Utilities;
import lecture.model.FullLecture;
import utilities.SmallButton;
import utilities.ClassroomInputBox;
import utilities.ContentField;
import utilities.ContentLabel;
import utilities.PromptLabel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.BorderUIResource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by hyice on 4/24/14.
 */
public class LectureManageWindow extends JFrame{

    private LectureManageWindowCallBack callBack;
    private int DefaultWindowWidth = 650;
    private int DefaultWindowHeight = 400;

    private DefaultListModel lectureNameListModel;
    private DefaultListModel studentListModel;

    private JList lectureNameList;
    private JList studentList;

    private ContentLabel[] contentLbls;
    private ContentField[] contentFields;
    private ClassroomInputBox classroomInputBox;
    private JComboBox weekdayInputBox;
    private JButton cancelBtn;
    private JButton confirmBtn;

    private boolean isInEditMode = false;

    public LectureManageWindow(LectureManageWindowCallBack aCallBack) {

        callBack = aCallBack;

        setSize(DefaultWindowWidth, DefaultWindowHeight);
        setLocation((Utilities.getScreenWidth()-DefaultWindowWidth)/2,
                (Utilities.getScreenHeight()-DefaultWindowHeight)/2);

        setTitle("课程管理");

        setLayout(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {

                super.windowClosed(e);

                callBack.lectureManageWindowClosed();

            }
        });

        addLectureNameList();
        addLectureButtons();
        addPromptLbls();
        addContentLbls();
        addStudentList();
        addStudentButtons();

    }

    public void setLectureListData(String[] data) {

        lectureNameListModel.removeAllElements();

        if(data==null || data.length==0) return;

        for(String name : data) {

            lectureNameListModel.addElement(name);
        }

        lectureNameList.setSelectedIndex(0);
        callBack.selectLectureAtIndex(0);
    }

    public void setStudentListData(String[] data) {

        studentListModel.removeAllElements();

        if(data==null || data.length==0) return;

        for(String info : data) {

            studentListModel.addElement(info);
        }

        studentList.setSelectedIndex(0);
    }

    public void setContentLblsWithData(FullLecture aLecture) {

        if(aLecture==null) {

            contentLbls[0].setText("");
            contentLbls[1].setText("");
            contentLbls[2].setText("");
            contentLbls[3].setText("");
            contentLbls[4].setText("");
        }else {

            contentLbls[0].setText(aLecture.getName());;
            contentLbls[1].setText(aLecture.getClassroomName());
            contentLbls[2].setText(aLecture.getStartTime());
            contentLbls[3].setText(aLecture.getEndTime());
            contentLbls[4].setText(aLecture.getWeekdayString());
        }
    }

    public void removeLectureAtIndex(int index) {

        lectureNameListModel.remove(index);

        int newSelectIndex = index==0? 0:index-1;

        lectureNameList.setSelectedIndex(newSelectIndex);
    }

    public void removeStudentAtIndex(int index) {

        studentListModel.remove(index);

        int newSelectIndex = index==0?0:index-1;

        studentList.setSelectedIndex(newSelectIndex);
    }

    public void updateLectureName(int index, String newName) {

        if (!newName.equals(lectureNameListModel.elementAt(index))) {

            lectureNameListModel.removeElementAt(index);
            lectureNameListModel.insertElementAt(newName, index);
            lectureNameList.setSelectedIndex(index);
        }
    }

    public void addANewLecture(FullLecture newLecture) {

        turnOnEditMode(newLecture);
        lectureNameListModel.addElement((newLecture.getName()));
        lectureNameList.setSelectedIndex(lectureNameListModel.size() - 1);
    }

    public void addANewStudent(String sid) {

        studentListModel.addElement(sid);
    }

    public int getLectureSelectedIndex() {

        return lectureNameList.getSelectedIndex();
    }

    public boolean isInEditMode() {

        return isInEditMode;
    }

    public void turnOnEditMode(FullLecture aLecture) {

        if(isInEditMode) return;

        isInEditMode = true;

        changeUIToEditMode();

        contentFields[0].setText(aLecture.getName());
        contentFields[1].setText(aLecture.getStartTime());
        contentFields[2].setText(aLecture.getEndTime());

        classroomInputBox.refreshData();
        classroomInputBox.setSelectedItem(aLecture.getClassroomName());

        weekdayInputBox.setSelectedIndex(aLecture.getWeekday()-1);
    }

    public void turnOffEditMode() {

        if(!isInEditMode) return;

        isInEditMode = false;

        changeUIToNormalMode();

        callBack.selectLectureAtIndex(lectureNameList.getSelectedIndex());
    }

    // draw UI part
    private void addLectureNameList() {

        lectureNameListModel = new DefaultListModel();
        lectureNameList = new JList(lectureNameListModel);
        lectureNameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lectureNameList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if(e.getValueIsAdjusting()) {

                    callBack.selectLectureAtIndex(lectureNameList.getSelectedIndex());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(lectureNameList);
        scrollPane.setBounds(10, 10, 150, 330);
        add(scrollPane);
    }

    private void addLectureButtons() {

        SmallButton addBtn = new SmallButton("增加", 10, 340);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                callBack.lectureAddBtnPressed();
            }
        });
        add(addBtn);

        SmallButton removeBtn = new SmallButton("删除", 60, 340);
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                callBack.lectureRemoveBtnPressedWithIndex(lectureNameList.getSelectedIndex());
            }
        });
        add(removeBtn);

        SmallButton modifyBtn = new SmallButton("修改", 110, 340);
        modifyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                callBack.lectureModifyBtnPressedWithIndex(lectureNameList.getSelectedIndex());
            }
        });
        add(modifyBtn);
    }

    private void addStudentList() {

        studentListModel = new DefaultListModel();
        studentList = new JList(studentListModel);
        studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        studentList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if(e.getValueIsAdjusting()) {

                    callBack.selectStudentAtIndex(studentList.getSelectedIndex());
                }
            }
        });

        studentList.setBackground(getBackground());

        JScrollPane scrollPane = new JScrollPane(studentList);
        scrollPane.setBorder(new BorderUIResource.TitledBorderUIResource("学生列表"));
        scrollPane.setBackground(getBackground());
        scrollPane.setBounds(480, 10, 150, 330);
        add(scrollPane);
    }

    private void addStudentButtons() {

        SmallButton addBtn = new SmallButton("增加", 480, 340);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                callBack.studentAddBtnPressed();
            }
        });
        add(addBtn);

        SmallButton removeBtn = new SmallButton("删除", 530, 340);
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                callBack.studentRemoveBtnPressedWithIndex(studentList.getSelectedIndex());
            }
        });
        add(removeBtn);

        SmallButton importBtn = new SmallButton("导入", 580, 340);
        importBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                callBack.studentImportBtnPressed();
            }
        });
        add(importBtn);
    }

    private void addPromptLbls() {

        int startX = 180;
        int startY = 60;
        int padding = 40;

        PromptLabel namePromptLbl = new PromptLabel("课程名称：", startX, startY);
        add(namePromptLbl);

        PromptLabel classroomPromptLbl = new PromptLabel("教室：", startX, startY+padding);
        add(classroomPromptLbl);

        PromptLabel startTimePromptLbl = new PromptLabel("开始时间：", startX, startY+padding*2);
        add(startTimePromptLbl);

        PromptLabel endTimePromptLbl = new PromptLabel("结束时间：", startX, startY+padding*3);
        add(endTimePromptLbl);

        PromptLabel weekdayPromptLbl = new PromptLabel("星期：", startX, startY+padding*4);
        add(weekdayPromptLbl);
    }

    private void addContentLbls() {

        int startX = 290;
        int startY = 60;
        int padding = 40;

        contentLbls = new ContentLabel[5];

        for(int i=0; i<5; i++) {

            contentLbls[i] = new ContentLabel("", startX, startY+padding*i);
            add(contentLbls[i]);
        }
    }

    private void changeUIToEditMode() {

        if(contentFields==null) {

            initContentFields();
        }else {

            for(ContentField field : contentFields) {

                field.setVisible(true);
            }
        }

        if(classroomInputBox == null) {

            initClassroomInputBox();
        }else {

            classroomInputBox.setVisible(true);
        }

        if(weekdayInputBox==null) {

            initWeekdayInputBox();
        }else {

            weekdayInputBox.setVisible(true);
        }

        if(cancelBtn==null) {

            cancelBtn = new JButton("取消");
            cancelBtn.setBounds(270, 280, 60, 40);
            cancelBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    callBack.cancelBtnPressedWithIndex(lectureNameList.getSelectedIndex());
                }
            });
            add(cancelBtn);
        }else {

            cancelBtn.setVisible(true);
        }

        if(confirmBtn==null) {

            confirmBtn = new JButton("确定");
            confirmBtn.setBounds(360, 280, 60, 40);
            confirmBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    callBack.confirmBtnPressed(
                            lectureNameList.getSelectedIndex(),
                            contentFields[0].getText(),
                            classroomInputBox.getSelctedCid(),
                            contentFields[1].getText(),
                            contentFields[2].getText(),
                            weekdayInputBox.getSelectedIndex()+1);
                }
            });
            add(confirmBtn);
        }else {

            confirmBtn.setVisible(true);
        }

        for(ContentLabel aLbl : contentLbls) {

            aLbl.setVisible(false);
        }
    }

    private void changeUIToNormalMode() {

        for(ContentField field : contentFields) {

            field.setVisible(false);
        }

        classroomInputBox.setVisible(false);

        weekdayInputBox.setVisible(false);

        cancelBtn.setVisible(false);
        confirmBtn.setVisible(false);

        for(ContentLabel label : contentLbls) {

            label.setVisible(true);
        }
    }

    private void initContentFields() {

        int startX = 290;
        int startY = 60;
        int padding = 40;

        contentFields = new ContentField[3];

        contentFields[0] = new ContentField("", startX, startY + padding*0);
        add(contentFields[0]);

        contentFields[1] = new ContentField("", startX, startY + padding*2);
        add(contentFields[1]);

        contentFields[2] = new ContentField("", startX, startY + padding*3);
        add(contentFields[2]);
    }

    private void initWeekdayInputBox() {

        weekdayInputBox = new JComboBox();
        weekdayInputBox.setBounds(290, 60+40*4, 100, 40);
        weekdayInputBox.setEditable(false);
        weekdayInputBox.addItem("星期一");
        weekdayInputBox.addItem("星期二");
        weekdayInputBox.addItem("星期三");
        weekdayInputBox.addItem("星期四");
        weekdayInputBox.addItem("星期五");
        weekdayInputBox.addItem("星期六");
        weekdayInputBox.addItem("星期日");

        add(weekdayInputBox);
    }

    private void initClassroomInputBox() {

        classroomInputBox = new ClassroomInputBox(290, 60 + 40*1);
        add(classroomInputBox);
    }
}
