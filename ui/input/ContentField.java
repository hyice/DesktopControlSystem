package ui.input;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hyice on 4/25/14.
 */
public class ContentField extends JTextField{

    public ContentField(String text, int x, int y) {

        setText(text);
        setFont(new Font("宋体", 0, 15));
        setBounds(x, y, 180, 40);
    }

    public ContentField(String text, int x, int y, int width, int height) {

        setText(text);
        setFont(new Font("宋体", 0, 15));
        setBounds(x, y, width, height);
    }
}
