package ui.label;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hyice on 4/25/14.
 */
public class ContentLabel extends JLabel{

    public ContentLabel(String title, int x, int y) {

        setText(title);
        setHorizontalAlignment(JLabel.LEFT);
        setFont(new Font("宋体", 1, 15));
        setBounds(x, y, 180, 40);
    }
}
