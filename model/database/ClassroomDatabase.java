package model.database;

import model.Utilities;
import model.classroom.FullClassRoom;
import model.classroom.SimpleClassroom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hyice on 4/21/14.
 */
public class ClassroomDatabase {

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
        ClassroomDatabase.executeSql(sql);
    }

    public static void updateClassroom(FullClassRoom aRoom) {

        String sql = "update classroom set name = \"" + aRoom.getName() + "\", " +
                "seats = " + aRoom.getSeats() + ", " +
                "guardIp = \"" + aRoom.getGuardIp() + "\", " +
                "forwardIp = \"" + aRoom.getForwardIp() + "\"" +
                " where cid = " + aRoom.getCid() + ";";

        System.out.println(sql);
        ClassroomDatabase.executeSql(sql);
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

    public static int seatsOfClassroomAllowIn(String sid, String ip) {

        int seatsCount = 0;

        String currentTime = Utilities.getCurrentTime();

        String sql = "select seats \n" +
                "from classroom c\n" +
                "where \n" +
                "(guardIp = \"" + ip + "\" or forwardIp = \"" + ip + "\") \n" +
                "and cid in \n" +
                "(\n" +
                "select cid\n" +
                "from lecture l, student s\n" +
                "where l.lid = s.lid\n" +
                "\tand s.sid = \"" + sid + "\"\n" +
                "\tand l.startTime <= \"" + currentTime + "\" \n" +
                "\tand l.endTime >= \"" + currentTime + "\"\n" +
                "\tand weekday = " + Utilities.getWeekday() + ");";

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

    private static void executeSql(String sql) {

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();

        database.updateDataWithSqlString(sql);

        database.disconnect();
    }
}