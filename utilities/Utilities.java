package utilities;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

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

    public static String getDateTimeMinutesAfterNow(int minutes) {

        String res;

        Date now = new Date();

        SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        res = dt.format(now.getTime()+minutes*60*1000);

        return res;
    }

    public static void main(String[] args) {

        System.out.print(Utilities.getCurrentDateTime());
        System.out.print(Utilities.getDateTimeMinutesAfterNow(10));
    }

    public static void alertWithText(String text, JFrame parent) {

        JOptionPane.showMessageDialog(parent, text, "错误", JOptionPane.WARNING_MESSAGE);
    }

    public static boolean isIpAdress(String ip) {

        Pattern ipv4Pattern = Pattern.compile(
                   "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
        return ipv4Pattern.matcher(ip).matches();
    }

    public static void showConfirmAlertWithText(String text, JFrame parent,
                                                ConfirmAlertCallBack callBack) {

        int choice = JOptionPane.showConfirmDialog(parent, text,
                "请确认是否继续", JOptionPane.OK_CANCEL_OPTION);

        if(choice == JOptionPane.OK_OPTION) {

            callBack.confirmAlertOkBtnPressed();
        }
    }
}
