package model.database;

import java.sql.ResultSet;
import java.sql.SQLException;

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
}
