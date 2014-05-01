package model;

import javafx.scene.input.DataFormat;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static int getWeekday() {

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        int res = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (res == 0) {

            res = 7;
        }

        return res;
    }

    public static String getCurrentTime() {

        String res;

        Date now = new Date();

        SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        res = dt.format(now);

        return res;
    }

    public static String getCurrentDateTime() {

        String res;

        Date now = new Date();

        SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        res = dt.format(now);

        return res;
    }

    public static void main(String[] args) {

        System.out.print(Utilities.getCurrentDateTime());
    }
}
