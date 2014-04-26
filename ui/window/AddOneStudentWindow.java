package ui.window;

import controller.callBack.AddOneStudentWindowCallBack;
import model.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hyice on 4/25/14.
 */
public class AddOneStudentWindow extends JDialog{

    private AddOneStudentWindowCallBack callBack;
    private JTextField sidInputField;

    public AddOneStudentWindow(Frame owner, AddOneStudentWindowCallBack aCallBack) {

        super(owner, true);

        callBack = aCallBack;

        int width = 200;
        int height = 140;

        int x = owner.getX() + (owner.getWidth() - width)/2;
        int y = owner.getY() + (owner.getHeight() - height)/2;

        setBounds(x, y, width, height);
        setResizable(false);

        setLayout(null);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addSidInputArea();
        addConfirmBtn();
    }

    private void addSidInputArea() {

        JLabel promptLbl = new JLabel("学号：");
        promptLbl.setHorizontalAlignment(JLabel.RIGHT);
        promptLbl.setBounds(10, 20, 50, 30);
        add(promptLbl);

        sidInputField = new JTextField();
        sidInputField.setBounds(60, 20, 120, 30);
        add(sidInputField);
    }

    private void addConfirmBtn() {

        JButton confirmBtn = new JButton("确定");
        confirmBtn.setBounds(70, 70, 60, 30);
        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String text = sidInputField.getText();

                setVisible(false);

                if(text.length() != 0) {

                    callBack.addOneStudentWindowConfirmBtnPressed(text);
                }
            }
        });
        add(confirmBtn);
    }
}
