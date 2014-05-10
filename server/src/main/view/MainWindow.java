package main.view;

import utilities.Utilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hyice on 4/18/14.
 */
public class MainWindow extends JFrame{

    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 380;

    private MainWindowCallBack callBack;

    public MainWindow(MainWindowCallBack aCallBack) {

        callBack = aCallBack;

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setResizable(false);
        setLocation((Utilities.getScreenWidth()-DEFAULT_WIDTH)/2, (Utilities.getScreenHeight()-DEFAULT_HEIGHT)/2);
        setTitle("专业教室桌面控制系统");

        addButtons();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static int getWindowWidth() {

        return DEFAULT_WIDTH;
    }

    private void addButtons() {

        setLayout(null);

        MainMenuButton classroomManager = new MainMenuButton("教室管理", 0);
        classroomManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                callBack.classroomBtnPressed();
            }
        });
        add(classroomManager);

        MainMenuButton lectureManager = new MainMenuButton("课程管理", 1);
        lectureManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                callBack.lectureBtnPressed();
            }
        });
        add(lectureManager);

        MainMenuButton usageManager = new MainMenuButton("使用查询", 2);
        usageManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                callBack.historyBtnPressed();
            }
        });
        add(usageManager);

        MainMenuButton tmpOpenManager = new MainMenuButton("临时开放", 3);
        tmpOpenManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                callBack.tempOpenBtnPressed();
            }
        });
        add(tmpOpenManager);

        MainMenuButton cardManager = new MainMenuButton("卡号登记", 4);
        cardManager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                BindCardWithSidWindow bindCardWithSidWindow = BindCardWithSidWindow.getInstance();
                bindCardWithSidWindow.setVisible(true);
            }
        });
        add(cardManager);
    }



}
