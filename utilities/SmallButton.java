package utilities;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hyice on 4/25/14.
 */
public class SmallButton extends JButton{


    public SmallButton(String title, int x, int y) {

        setText(title);
        setFont(new Font("宋体", 1, 12));
        setBounds(x, y, 50, 30);
    }
}
