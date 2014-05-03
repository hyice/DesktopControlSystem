package model.database;

import model.Utilities;

import java.util.Date;

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
}
