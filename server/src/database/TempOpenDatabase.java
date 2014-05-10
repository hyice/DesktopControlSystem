package database;

import utilities.Utilities;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by hyice on 5/3/14.
 */
public class TempOpenDatabase {

    public static void newTempOpenRecord(String sid, int cid, int minutes) {

        String sql = "insert into tempOpen(sid, cid, startTime, endTime)\n" +
                "values (\"" + sid + "\", " + cid +",\n" +
                "\"" + Utilities.getCurrentDateTime() + "\",\n" +
                "\"" + Utilities.getDateTimeMinutesAfterNow(minutes) + "\");";

        System.out.println("newTempOpenRecord: " + sql);

        MysqlDatabase.executeSql(sql);
    }

    public static int getTempOpenCountOfClassroom(int cid) {

        int res = 0;

        String sql = "select count(*) from tempOpen where cid = " + cid + ";";

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
}
