package ui.window;

import controller.callBack.TempOpenManageWindowCallBack;
import model.Utilities;
import model.database.CardDatabase;
import ui.input.ClassroomInputBox;
import ui.input.ContentField;
import ui.label.PromptLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by hyice on 5/3/14.
 */
public class TempOpenManageWindow extends JFrame {

    private TempOpenManageWindowCallBack callBack;

    private int DefaultWindowWidth = 400;
    private int DefaultWindowHeight = 300;

    private ContentField sidInputField;
    private ClassroomInputBox classroomInputBox;
    private ContentField lastTimeInputField;
    private JLabel successPromptLbl;


    public TempOpenManageWindow(TempOpenManageWindowCallBack aCallBack) {

        callBack = aCallBack;

        setSize(DefaultWindowWidth, DefaultWindowHeight);
        setLocation((Utilities.getScreenWidth()-DefaultWindowWidth)/2,
                (Utilities.getScreenHeight()-DefaultWindowHeight)/2);
        setResizable(false);

        setTitle("临时开放管理");

        setLayout(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {

                super.windowClosed(e);

                callBack.tempOpenManageWindowClosed();

            }
        });

        addTempOpenArea();
        addSuccessPromptLbl();
    }

    public void tempOpenSucceed() {

        successPromptLbl.setText(sidInputField.getText() + " 可使用 " +
                classroomInputBox.getSelectedName() + " " +
                lastTimeInputField.getText() + "分钟");

        sidInputField.setText("");
        classroomInputBox.setSelectedIndex(0);
        lastTimeInputField.setText("45");
    }

    private void addTempOpenArea() {

        int promptStartX = 10;
        int contentStatX = 110;

        int startY = 30;

        int lineHeight = 40;

        PromptLabel sidPromptLbl = new PromptLabel("学号：", promptStartX, startY, 100, lineHeight);
        add(sidPromptLbl);

        PromptLabel classroomPromptLbl = new PromptLabel("教室：", promptStartX+20,
                startY + lineHeight, 100, lineHeight);
        add(classroomPromptLbl);

        PromptLabel lastPromptLbl = new PromptLabel("开放时长：", promptStartX+80,
                startY + lineHeight*2, 100, lineHeight);
        add(lastPromptLbl);

        sidInputField = new ContentField("", contentStatX, startY, 200, lineHeight);
        add(sidInputField);

        classroomInputBox = new ClassroomInputBox(contentStatX+20,
                startY + lineHeight, 150, lineHeight);
        add(classroomInputBox);

        lastTimeInputField = new ContentField("45", contentStatX+80, startY + lineHeight*2, 50, lineHeight);
        add(lastTimeInputField);

        PromptLabel minutePromptLbl = new PromptLabel("分钟", contentStatX+130,
                startY + lineHeight*2, 30, lineHeight);
        add(minutePromptLbl);

        final JButton confirmBtn = new JButton("开放");
        confirmBtn.setBounds(DefaultWindowWidth/2 - 40, startY + lineHeight*3 + 10, 80, 40);
        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String sid = sidInputField.getText();
                int cid = classroomInputBox.getSelctedCid();
                int minutes = Integer.valueOf(lastTimeInputField.getText());

                callBack.confirmBtnPressed(sid, cid, minutes);

            }
        });
        add(confirmBtn);
    }

    private void addSuccessPromptLbl() {

        successPromptLbl = new JLabel("");
        successPromptLbl.setForeground(Color.red);
        successPromptLbl.setFont(new Font("黑体", 1, 17));
        successPromptLbl.setBounds(0, 200, DefaultWindowWidth, 50);
        successPromptLbl.setHorizontalAlignment(JLabel.CENTER);
        add(successPromptLbl);
    }

    public static void main(String[] args) {

        TempOpenManageWindow window = new TempOpenManageWindow(null);
        window.setVisible(true);
    }
}
