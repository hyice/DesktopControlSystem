package model.database;

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

    public static Object[][] getAllHistoryData() {

        List<Object[]> tmpList = new ArrayList();

        String sql = "select h.sid,c.name,h.seat,\n" +
                "\tDATE_FORMAT(h.startTime, '%Y-%m-%d %H:%i') AS startTime,\n" +
                "\tTIMESTAMPDIFF(minute,h.startTime,h.endTime) as lastTime\n" +
                "from history as h,classroom as c\n" +
                "where h.cid = c.cid;";

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
}
