package history.view;

import utilities.ClassroomInputBox;
import utilities.ContentField;
import utilities.PromptLabel;
import utilities.Utilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by hyice on 5/14/14.
 */
public class HistorySearchWindow extends JFrame{

    private HistorySearchWindowCallBack callBack;

    private int DefaultWindowWidth = 300;
    private int DefaultWindowHeight = 250;

    public HistorySearchWindow(HistorySearchWindowCallBack aCallBack) {

        callBack = aCallBack;

        setSize(DefaultWindowWidth, DefaultWindowHeight);
        setLocation((Utilities.getScreenWidth() - DefaultWindowWidth) / 2,
                (Utilities.getScreenHeight() - DefaultWindowHeight) / 2);

        setTitle("使用记录查询");

        setLayout(null);

        setResizable(false);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {

                super.windowClosed(e);

                callBack.historySearchWindowClosed();

            }
        });

        drawUI();
    }

    private void drawUI() {

        int startY = 10;
        int height = 30;

        int promptWidth = DefaultWindowWidth*2/7;
        int contentWidth = DefaultWindowWidth*4/7;

        PromptLabel sidPromptLbl = new PromptLabel("学号：", 0, startY, promptWidth, height);
        add(sidPromptLbl);

        PromptLabel classroomPromptLbl = new PromptLabel("教室：", 0, startY + height,
                promptWidth, height);
        add(classroomPromptLbl);

        PromptLabel seatPromptLbl = new PromptLabel("座位号：", 0, startY + height*2,
                promptWidth, height);
        add(seatPromptLbl);

        PromptLabel datePromptLbl = new PromptLabel("日期：", 0, startY + height*3,
                promptWidth, height);
        add(datePromptLbl);

        final ContentField sidInputField = new ContentField("", promptWidth, startY, contentWidth, height);
        add(sidInputField);

        final ClassroomInputBox classroomInputBox = new ClassroomInputBox(promptWidth, startY + height,
                contentWidth, height);
        add(classroomInputBox);

        final ContentField seatInputField = new ContentField("", promptWidth, startY + height*2,
                contentWidth, height);
        add(seatInputField);

        final ContentField dateInputField = new ContentField("", promptWidth, startY + height*3,
                contentWidth, height);
        add(dateInputField);

        JLabel promptLbl = new JLabel("（不填写表示不对该条件进行限制）");
        promptLbl.setBounds(0, startY + height*4, DefaultWindowWidth, height);
        promptLbl.setHorizontalAlignment(JLabel.CENTER);
        add(promptLbl);

        JButton confirmBtn = new JButton("搜索");
        confirmBtn.setBounds(DefaultWindowWidth/2 - 50, startY + height*5, 100, 50);
        add(confirmBtn);
        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int seat = 0;
                String seatStr = seatInputField.getText();
                if(seatStr!=null && seatStr.length()!=0) {

                    seat = Integer.valueOf(seatStr);
                }

                callBack.historySearchBtnPressed(
                        sidInputField.getText(),
                        classroomInputBox.getSelctedCid(),
                        seat,
                        dateInputField.getText());
            }
        });
    }
}
