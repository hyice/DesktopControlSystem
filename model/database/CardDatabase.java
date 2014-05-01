package model.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by hyice on 4/26/14.
 */
public class CardDatabase {

    public static String studentIdByCardId(String cartId) {

        String sid = "";

        String sql = "select sid from card where cardId = " + cartId + ";";

        System.out.println(sql);

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();
        ResultSet rs = database.selectDataWithSqlString(sql);

        try {

            while(rs.next()) {

                sid = rs.getString("sid");
            }
        }catch(SQLException e) {

            System.out.println(e.getMessage());
        }

        database.disconnect();

        return sid;
    }
}
