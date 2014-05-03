package ui.window;

import model.Utilities;
import model.database.CardDatabase;
import ui.input.ContentField;
import ui.label.ContentLabel;
import ui.label.PromptLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hyice on 5/3/14.
 */
public class BindCardWithSidWindow extends JFrame {

    private static BindCardWithSidWindow theInstance = new BindCardWithSidWindow();
    private static JTextField sidField;
    private static JLabel cardLbl;
    private static JButton confirmBtn;

    public static BindCardWithSidWindow getInstance() {

        cardLbl.setText("请刷卡...");
        confirmBtn.setEnabled(false);
        return theInstance;
    }

    public void setCardId(String cardId) {

        confirmBtn.setEnabled(true);
        cardLbl.setText(cardId);
    }

    private BindCardWithSidWindow() {

        int width = 230;
        int height = 190;

        setLayout(null);
        setBounds((Utilities.getScreenWidth()-width)/2,
                (Utilities.getScreenHeight()-height)/2,
                width, height);
        setAlwaysOnTop(true);
        setResizable(false);

        JLabel cardPromptLbl = new JLabel("卡号：");
        cardPromptLbl.setFont(new Font("Courier New", 0, 15));
        cardPromptLbl.setBounds(30, 20, 50, 30);
        add(cardPromptLbl);

        JLabel sidPromptLbl = new JLabel("学号：");
        sidPromptLbl.setFont(new Font("Courier New", 0, 15));
        sidPromptLbl.setBounds(30, 50, 50, 30);
        add(sidPromptLbl);

        cardLbl = new JLabel("");
        cardLbl.setBackground(Color.blue);
        cardLbl.setFont(new Font("黑体", 0, 15));
        cardLbl.setHorizontalAlignment(JLabel.CENTER);
        cardLbl.setBounds(80, 20, 120, 30);
        add(cardLbl);


        sidField = new JTextField("");
        sidField.setHorizontalAlignment(JTextField.CENTER);
        sidField.setFont(new Font("黑体", 0, 15));
        sidField.setBounds(80, 50, 120, 30);
        add(sidField);

        confirmBtn = new JButton("确定");
        confirmBtn.setBounds(75, 100, 80, 40);
        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String text = sidField.getText();

                setVisible(false);

                if(text.length() != 0) {

                    CardDatabase.bindCardWithStudent(cardLbl.getText(), text);
                }
            }
        });
        add(confirmBtn);

    }

    public static void main(String[] args) {

        BindCardWithSidWindow window = BindCardWithSidWindow.getInstance();
        window.setVisible(true);
    }

}
