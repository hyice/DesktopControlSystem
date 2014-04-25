package ui.button;

import ui.window.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hyice on 4/23/14.
 */
public class MainMenuButton extends JButton{

    public MainMenuButton (String title, int index) {

        setText(title);
        setFont(new Font("华文行楷", 1, 15));
        setBounds((MainWindow.getWindowWidth()-200)/2, 20 + 60*index, 200, 50);
    }
}
