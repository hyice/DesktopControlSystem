package database;

import utilities.Utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyice on 5/9/14.
 */
public class HistoryDatabase {

    public static int getHistoryCountOfClassroom(int cid) {

        int res = 0;

        String sql = "select count(*) from history where cid = " + cid + ";";

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();
        ResultSet rs = database.selectDataWithSqlString(sql);

        try {

            while(rs.next()) {

                res = rs.getInt(1);
            }
        }catch(SQLException e) {

            System.out.println(e.getMessage());
        }

        database.disconnect();

        return res;
    }

    public static Object[][] getHistoryDataByCondition(String sid, int cid, int seat, String date) {

        List<Object[]> tmpList = new ArrayList();

        String sql = "select h.sid,c.name,h.seat,\n" +
                "\tDATE_FORMAT(h.startTime, '%Y-%m-%d %H:%i') AS startTime,\n" +
                "\tTIMESTAMPDIFF(minute,h.startTime,h.endTime) as lastTime\n" +
                "from history as h,classroom as c\n" +
                "where h.cid = c.cid\n";

        if(sid!=null && sid.length()!=0) {

            sql += "and h.sid = \"" + sid + "\"\n";
        }

        if(cid!=0) {

            sql += "and h.cid = " + cid + "\n";
        }

        if(seat!=0) {

            sql += "and h.seat = " + seat + "\n";
        }

        if(date!=null && date.length()!=0) {

            sql += "and h.startTime >= \"" + date + " 00:00" + "\"\n";
            sql += "and h.startTime <= \"" + date + " 23:59" + "\"\n";
        }

        sql += ";";

        System.out.println("getHistoryDataByCondition:\n" + sql);

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();
        ResultSet rs = database.selectDataWithSqlString(sql);

        try {

            while(rs.next()) {

                Object[] tmpRow = new Object[5];
                tmpRow[0] = rs.getString("sid");
                tmpRow[1] = rs.getString("name");
                tmpRow[2] = rs.getString("seat");
                tmpRow[3] = rs.getString("startTime");
                tmpRow[4] = rs.getString("lastTime");

                tmpList.add(tmpRow);
            }
        }catch(SQLException e) {

            System.out.println(e.getMessage());
        }

        database.disconnect();

        Object[][] res = new Object[tmpList.size()][5];

        for(int i=0; i<tmpList.size(); i++) {

            res[i] = tmpList.get(i);
        }

        return res;
    }

    public static void fixErrorHistory24HoursBefore() {

        String sql = "update history set endTime = \"" + Utilities.getCurrentDateTime() +
                "\" where endTime is null and startTime <= \"" +
                Utilities.getDateTimeMinutesAfterNow(-24*60) + "\";";

        MysqlDatabase.executeSql(sql);
    }
}
