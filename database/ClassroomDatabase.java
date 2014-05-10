package database;

import classroom.model.FullClassRoom;
import classroom.model.SimpleClassroom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hyice on 4/21/14.
 */
public class ClassroomDatabase {

    public final static int NO_CLASSROOM_CID = 0;
    public final static String NO_CLASSROOM_NAME = "";

    public static int newClassroom(FullClassRoom aRoom) {

        int resCid = 0;

        String sql = "insert into classroom(name, seats, guardIp, forwardIp) " +
                "values (\"" + aRoom.getName() + "\"," + aRoom.getSeats() + ",\"" +
                aRoom.getGuardIp() + "\",\"" + aRoom.getForwardIp() + "\");";

        String sql2 = "select cid from classroom where name = \"" + aRoom.getName() +
                "\" and seats = " + aRoom.getSeats() +
                " and guardIp = \"" + aRoom.getGuardIp() +
                "\" and forwardIp = \"" + aRoom.getForwardIp() + "\";";



        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();
        database.updateDataWithSqlString(sql);
        ResultSet rs = database.selectDataWithSqlString(sql2);

        try {

            while(rs.next()) {

                resCid = rs.getInt("cid");
            }
        }catch(SQLException e) {

            System.out.println(e.getMessage());
        }

        database.disconnect();

        return resCid;
    }

    public static void deleteClassroomById(int cid) {

        String sql = "delete from classroom where cid = " + cid + ";";
        MysqlDatabase.executeSql(sql);
    }

    public static void updateClassroom(FullClassRoom aRoom) {

        String sql = "update classroom set name = \"" + aRoom.getName() + "\", " +
                "seats = " + aRoom.getSeats() + ", " +
                "guardIp = \"" + aRoom.getGuardIp() + "\", " +
                "forwardIp = \"" + aRoom.getForwardIp() + "\"" +
                " where cid = " + aRoom.getCid() + ";";

        System.out.println("updateClassroom: " + sql);
        MysqlDatabase.executeSql(sql);
    }

    public static List<FullClassRoom> getFullClassroomList() {

        List<FullClassRoom> resList = new LinkedList<FullClassRoom>();

        String sql = "select * from classroom;";

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();

        ResultSet tmpRS = database.selectDataWithSqlString(sql);

        try {

            while(tmpRS.next()) {

                FullClassRoom tmpFullClassRoom = new FullClassRoom(tmpRS.getInt("cid"),
                                     tmpRS.getString("name"),
                                     tmpRS.getInt("seats"),
                                     tmpRS.getString("guardIp"),
                                     tmpRS.getString("forwardIp"));
                resList.add(tmpFullClassRoom);
            }
        }catch (SQLException e) {

            System.err.println(e.getMessage());
        }


        database.disconnect();

        return resList;
    }

    public static List<SimpleClassroom> getSimpleClassroomList() {

        List<SimpleClassroom> resList = new LinkedList<SimpleClassroom>();

        String sql = "select cid,name from classroom;";

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();

        ResultSet tmpRS = database.selectDataWithSqlString(sql);

        try {

            while(tmpRS.next()) {

                SimpleClassroom tmpSimpleClassRoom = new SimpleClassroom(tmpRS.getInt("cid"),
                        tmpRS.getString("name"));
                resList.add(tmpSimpleClassRoom);
            }
        }catch (SQLException e) {

            System.err.println(e.getMessage());
        }


        database.disconnect();

        return resList;
    }

    public static String getClassroomNameForId(int cid) {

        String res = "";

        String sql = "select name from classroom where cid =" + cid + ";";

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();

        ResultSet tmpRS = database.selectDataWithSqlString(sql);

        try {

            while(tmpRS.next()) {

                res = tmpRS.getString("name");
            }
        }catch (SQLException e) {

            System.err.println(e.getMessage());
        }

        database.disconnect();

        return res;
    }

    public static int getCidByGuardIp(String guardIp) {

        int res = NO_CLASSROOM_CID;

        String sql = "select cid from classroom where guardIp = \"" + guardIp + "\";";

        System.out.println(sql);

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();

        ResultSet tmpRS = database.selectDataWithSqlString(sql);

        try {

            while(tmpRS.next()) {

                res = tmpRS.getInt("cid");

            }
        }catch (SQLException e) {

            System.err.println(e.getMessage());
        }

        database.disconnect();

        return res;
    }

    public static int getCidByForwardIp(String forwardIp) {

        int res = NO_CLASSROOM_CID;

        String sql = "select cid from classroom where forwardIp = \"" + forwardIp + "\";";

        System.out.println(sql);

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();

        ResultSet tmpRS = database.selectDataWithSqlString(sql);

        try {

            while(tmpRS.next()) {

                res = tmpRS.getInt("cid");

            }
        }catch (SQLException e) {

            System.err.println(e.getMessage());
        }

        database.disconnect();

        return res;
    }

    public static String getNameIfGuardIpHasBeenUsed(String guardIp, int cid) {

        String res = NO_CLASSROOM_NAME;

        String sql = "select name from classroom where guardIp = \"" + guardIp + "\" ";

        if(cid!=0) {

            sql += "and cid != " + cid;
        }

        sql += ";";

        System.out.println(sql);

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();

        ResultSet tmpRS = database.selectDataWithSqlString(sql);

        try {

            while(tmpRS.next()) {

                res = tmpRS.getString("name");

            }
        }catch (SQLException e) {

            System.err.println(e.getMessage());
        }

        database.disconnect();

        return res;
    }

    public static String getNameIfForwardIpHasBeenUsed(String forwardIp, int cid) {

        String res = NO_CLASSROOM_NAME;

        String sql = "select name from classroom where forwardIp = \"" + forwardIp + "\" ";

        if(cid!=0) {

            sql += "and cid != " + cid;
        }

        sql += ";";

        System.out.println(sql);

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();

        ResultSet tmpRS = database.selectDataWithSqlString(sql);

        try {

            while(tmpRS.next()) {

                res = tmpRS.getString("name");

            }
        }catch (SQLException e) {

            System.err.println(e.getMessage());
        }

        database.disconnect();

        return res;
    }

    public static int seatsOfClassroom(int cid) {

        int seatsCount = 0;

        String sql = "select seats \n" +
                "from classroom\n" +
                "where cid = \"" + cid + "\"\n;";

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();

        ResultSet tmpRS = database.selectDataWithSqlString(sql);

        try {

            while(tmpRS.next()) {

                 seatsCount = tmpRS.getInt("seats");

            }
        }catch (SQLException e) {

            System.err.println(e.getMessage());
        }


        database.disconnect();

        return seatsCount;
    }
}