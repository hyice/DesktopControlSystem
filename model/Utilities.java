package model;

import java.awt.*;

import static java.awt.Toolkit.getDefaultToolkit;

/**
 * Created by hyice on 4/24/14.
 */
public class Utilities {

    public static int getScreenWidth() {

        return getScreenDimension().width;
    }

    public static int getScreenHeight() {

        return getScreenDimension().height;
    }

    private static Dimension getScreenDimension() {

        return getDefaultToolkit().getScreenSize();
    }
}
