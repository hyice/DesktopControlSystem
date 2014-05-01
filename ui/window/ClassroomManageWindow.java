package ui.window;

import controller.callBack.ClassroomManageWindowCallBack;
import model.Utilities;
import model.classroom.FullClassRoom;
import ui.button.SmallButton;
import ui.input.ContentField;
import ui.label.ContentLabel;
import ui.label.PromptLabel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by hyice on 4/23/14.
 */
public class ClassroomManageWindow extends JFrame {

    private int DefaultWindowWidth = 500;
    private int DefaultWindowHeight = 400;

    private boolean isInEditMode = false;

    private ClassroomManageWindowCallBack callBack;
    private JList roomNameList;
    private DefaultListModel listModel;

    private ContentLabel[] contentLbls;
    private ContentField[] contentFields;
    private JButton cancelBtn;
    private JButton confirmBtn;

    public ClassroomManageWindow(ClassroomManageWindowCallBack aCallBack) {

        callBack = aCallBack;

        setSize(500, 400);
        setResizable(false);
        setLocation((Utilities.getScreenWidth() - DefaultWindowWidth) / 2,
                (Utilities.getScreenHeight() - DefaultWindowHeight) / 2);
        setTitle("教室管理");

        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {

                super.windowClosed(e);

                callBack.classroomManageWindowClosed();

            }
        });

        addScrollableJList();
        addButtons();

        addPromptLabels();
        addContentLabels();
    }

    public void setRoomNameListData(String[] nameArray) {

        for (String name : nameArray) {

            listModel.addElement(name);
        }

        roomNameList.setSelectedIndex(0);
        callBack.selectedClassroomAtIndex(0);
    }

    public void setContentLblsWithFullClassroom(FullClassRoom aFullClassroom) {

        if(aFullClassroom != null) {

            contentLbls[0].setText(aFullClassroom.getName());
            contentLbls[1].setText(String.valueOf(aFullClassroom.getSeats()));
            contentLbls[2].setText(aFullClassroom.getGuardIp());
            contentLbls[3].setText(aFullClassroom.getForwardIp());
        }else {

            contentLbls[0].setText("");
            contentLbls[1].setText("");
            contentLbls[2].setText("");
            contentLbls[3].setText("");
        }

    }

    public void addANewClassroom(FullClassRoom newClassroom) {

        turnOnEditMode(newClassroom);
        listModel.addElement((newClassroom.getName()));
        roomNameList.setSelectedIndex(listModel.size() - 1);
    }

    public void cancelAddingRoom() {

        listModel.remove(listModel.size() - 1);
        turnOffEditModeWithSelectedIndex(listModel.size() - 1);
    }

    public void removeRoomAtIndex(int index) {

        listModel.remove(index);
        roomNameList.setSelectedIndex(index - 1);
        callBack.selectedClassroomAtIndex(index - 1);
    }

    public void finishEditing(String newName) {

        int index = roomNameList.getSelectedIndex();

        if (!newName.equals(listModel.elementAt(index))) {

            listModel.removeElementAt(index);
            listModel.insertElementAt(newName, index);
        }

        turnOffEditModeWithSelectedIndex(index);
    }

    public boolean isInEditMode() {

        return isInEditMode;
    }

    public void turnOnEditMode(FullClassRoom aClassroom) {

        if (isInEditMode) return;

        isInEditMode = true;

        if (contentFields == null) {

            contentFields = new ContentField[4];

            int startX = 310;
            int startY = 70;
            int padding = 45;

            for (int i = 0; i < 4; i++) {

                contentFields[i] = new ContentField("", startX, startY + padding * i);
                add(contentFields[i]);
            }
        }

        if (cancelBtn == null) {

            cancelBtn = new JButton("取消");
            cancelBtn.setBounds(270, 270, 60, 40);
            cancelBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    callBack.cancelBtnPressedWithSelectedIndex(roomNameList.getSelectedIndex());
                }
            });
            add(cancelBtn);
        } else {

            cancelBtn.setVisible(true);
        }

        if (confirmBtn == null) {

            confirmBtn = new JButton("确定");
            confirmBtn.setBounds(360, 270, 60, 40);
            confirmBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    callBack.confirmBtnPressed(roomNameList.getSelectedIndex(), contentFields[0].getText(),
                            Integer.parseInt(contentFields[1].getText()), contentFields[2].getText(), contentFields[3].getText());
                }
            });
            add(confirmBtn);
        } else {

            confirmBtn.setVisible(true);
        }

        for (ContentLabel lbl : contentLbls) {

            lbl.setVisible(false);
        }

        for (int i = 0; i < 4; i++) {

            String text;
            switch (i) {
                case 0:
                    text = aClassroom.getName();
                    break;
                case 1:
                    text = String.valueOf(aClassroom.getSeats());
                    break;
                case 2:
                    text = aClassroom.getGuardIp();
                    break;
                case 3:
                    text = aClassroom.getForwardIp();
                    break;
                default:
                    text = "";
                    break;
            }

            contentFields[i].setText(text);
            contentFields[i].setVisible(true);
        }
    }

    public void turnOffEditModeWithSelectedIndex(int index) {

        if (!isInEditMode) return;
        isInEditMode = false;

        for (ContentField field : contentFields) {

            field.setVisible(false);
        }

        cancelBtn.setVisible(false);
        confirmBtn.setVisible(false);

        for (ContentLabel lbl : contentLbls) {

            lbl.setVisible(true);
        }

        roomNameList.setSelectedIndex(index);
        callBack.selectedClassroomAtIndex(index);
    }


    private void addScrollableJList() {

        listModel = new DefaultListModel();
        roomNameList = new JList(listModel);
        roomNameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        roomNameList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getValueIsAdjusting()) {

                    callBack.selectedClassroomAtIndex(roomNameList.getSelectedIndex());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(roomNameList);
        scrollPane.setBounds(10, 10, 150, 330);
        add(scrollPane);
    }

    private void addButtons() {

        SmallButton addBtn = new SmallButton("增加", 10, 340);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                callBack.addBtnPressed();
            }
        });
        add(addBtn);

        SmallButton removeBtn = new SmallButton("删除", 60, 340);
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                callBack.removeBtnPressedWithIndex(roomNameList.getSelectedIndex());
            }
        });
        add(removeBtn);

        SmallButton modifyBtn = new SmallButton("修改", 110, 340);
        modifyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                callBack.modifyBtnPressedWithIndex(roomNameList.getSelectedIndex());
            }
        });
        add(modifyBtn);
    }

    private void addPromptLabels() {

        int startX = 200;
        int startY = 70;
        int padding = 45;

        PromptLabel namePromptLbl = new PromptLabel("教室名称：", startX, startY);
        add(namePromptLbl);

        PromptLabel seatsPromptLbl = new PromptLabel("总座位数：", startX, startY + padding);
        add(seatsPromptLbl);

        PromptLabel guardIpPromptLbl = new PromptLabel("门禁ip：", startX, startY + padding * 2);
        add(guardIpPromptLbl);

        PromptLabel forwardIpPromptLbl = new PromptLabel("通信机ip：", startX, startY + padding * 3);
        add(forwardIpPromptLbl);
    }

    private void addContentLabels() {

        int startX = 310;
        int startY = 70;
        int padding = 45;

        contentLbls = new ContentLabel[4];

        for (int i = 0; i < 4; i++) {

            contentLbls[i] = new ContentLabel("", startX, startY + padding * i);
            add(contentLbls[i]);
        }
    }
}