package utilities;

import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;
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

    public static boolean isStudentNumber(String sid) {

        boolean res = true;

        for(int i=0; i<sid.length();i++) {

            char c = sid.charAt(i);
            if(c == ' ') {

                res = false;
                break;
            }
        }

        return res;
    }

    public static void showConfirmAlertWithText(String text, JFrame parent,
                                                ConfirmAlertCallBack callBack) {

        int choice = JOptionPane.showConfirmDialog(parent, text,
                "请确认是否继续", JOptionPane.OK_CANCEL_OPTION);

        if(choice == JOptionPane.OK_OPTION) {

            callBack.confirmAlertOkBtnPressed();
        }
    }

    public static String half2Fullchange(String QJstr) {

        StringBuffer outStrBuf = new StringBuffer("");

        try {


            String Tstr = "";

            byte[] b = null;

            for (int i = 0; i < QJstr.length(); i++) {

                Tstr = QJstr.substring(i, i + 1);

                if (Tstr.equals(" ")) {

                    // 半角空格

                    outStrBuf.append(Tstr);

                    continue;

                }

                b = Tstr.getBytes("unicode");

                if (b[2] == 0) {

                    // 半角?

                    b[3] = (byte) (b[3] - 32);

                    b[2] = -1;

                    outStrBuf.append(new String(b, "unicode"));

                } else {

                    outStrBuf.append(Tstr);

                }

            }
        }catch(UnsupportedEncodingException e) {

            System.err.println(e.getMessage());
        }



        return outStrBuf.toString();

    }
}
