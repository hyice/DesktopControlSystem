package ui.label;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hyice on 4/25/14.
 */
public class PromptLabel extends JLabel{

    public PromptLabel(String title, int x, int y) {

        setText(title);
        setHorizontalAlignment(JLabel.RIGHT);
        setFont(new Font("Courier New", 0, 15));
        setBounds(x, y, 100, 40);
    }
}
