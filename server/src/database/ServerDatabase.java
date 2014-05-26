package database;

import utilities.Utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hyice on 5/1/14.
 */
public class ServerDatabase {

    public static boolean hasBeenTempOpenInClassroomNow(String cardId, int cid) {

        boolean res = false;

        String currentTime = Utilities.getCurrentDateTime();

        String sql = "select count(*) from tempOpen\n" +
                "where cid = " + cid + "\n" +
                "and startTime <= \"" + currentTime + "\"\n" +
                "and endTime >= \"" + currentTime + "\"\n" +
                "and sid = (select sid from card\n" +
                "\t\t  where cardId = \"" + cardId + "\");";

        System.out.println("hasBeenTempOpenInClassroomNow: " + sql);

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();
        ResultSet rs = database.selectDataWithSqlString(sql);

        try {

            while(rs.next()) {

                int count = rs.getInt(1);
                if(count>0) {

                    res = true;
                }
            }
        }catch(SQLException e) {

            System.out.println(e.getMessage());
        }


        return res;
    }

    public static boolean hasLectureInClassroomNow(String cardId, int cid) {

        boolean res = false;

        String currentTime = Utilities.getCurrentTime();

        String sql = "select count(*) from student\n" +
                "where sid = (select sid from card where cardId = \"" + cardId + "\")\n" +
                "and lid = (\n" +
                "\t\tselect lid from lecture\n" +
                "\t\twhere startTime <= \"" + currentTime + "\"\n" +
                "\t\tand endTime >= \"" + currentTime + "\"\n" +
                "\t\tand weekday = \"" + Utilities.getWeekday() + "\"\n" +
                "\t\tand cid = " + cid +
                ");";

        System.out.println(sql);

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();
        ResultSet rs = database.selectDataWithSqlString(sql);

        try {

            while(rs.next()) {

                int count = rs.getInt(1);
                if(count>0) {

                    res = true;
                }
            }
        }catch(SQLException e) {

            System.out.println(e.getMessage());
        }

        return res;
    }

    public static int NO_SEAT = 0;
    public static int seatNumberIfAlreadyAssigned(String sid, int cid) {

        int res = NO_SEAT;

        String sql = "select seat from history\n" +
                "where sid = \"" + sid  + "\"\n" +
                "and cid = " + cid + "\n" +
                "and startTime <= \"" + Utilities.getCurrentDateTime() + "\"\n" +
                "and endTime is null;";

        System.out.println("seatNumberIfAlreadyAssigned:\n"+sql);

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();

        ResultSet tmpRS = database.selectDataWithSqlString(sql);

        try {

            while(tmpRS.next()) {

                res = tmpRS.getInt("seat");
            }
        }catch (SQLException e) {

            System.err.println(e.getMessage());
        }


        database.disconnect();

        return res;
    }

    public static int minutesAllowedToUse(String sid, int cid) {

        int minutes = 0;

        String sql;
        sql = "select TIMESTAMPDIFF(minute,curtime(),l.endTime) as minutes\n" +
                    "from lecture as l, student as s\n" +
                    "where l.lid = s.lid\n" +
                    "and s.sid = \"" + sid + "\"\n" +
                    "and l.startTime <= curtime()\n" +
                    "and l.endTime >= curtime()\n" +
                    "and l.weekday = " + Utilities.getWeekday() + "\n" +
                    "and l.cid = " + cid + ";";

        System.out.println("minutesAllowedToUse:\n"+sql);

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();

        ResultSet tmpRS = database.selectDataWithSqlString(sql);

        try {

            while(tmpRS.next()) {

                minutes = tmpRS.getInt("minutes");
            }
        }catch (SQLException e) {

            System.err.println(e.getMessage());
        }

        database.disconnect();

        if(minutes == 0) {

            sql = "select TIMESTAMPDIFF(minute,now(),t.endTime) as minutes\n" +
                    "from tempOpen as t\n" +
                    "where sid = \"" + sid + "\"\n" +
                    "and cid = " + cid + "\n" +
                    "and endTime >= now();";

            database = MysqlDatabase.getInstance();
            database.connect();

            tmpRS = database.selectDataWithSqlString(sql);

            try {

                while(tmpRS.next()) {

                    minutes = tmpRS.getInt("minutes");
                }
            }catch (SQLException e) {

                System.err.println(e.getMessage());
            }

            database.disconnect();
        }

        return minutes;
    }

    public static List<Integer> getOccupiedSeatsList(int cid) {

        List<Integer> res = new LinkedList<Integer>();

        String sql = "select seat from history\n" +
                "where startTime is not null\n" +
                "and endTime is null\n" +
                "and cid =" + cid +
                " order by seat;";

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();

        ResultSet tmpRS = database.selectDataWithSqlString(sql);

        try {

            while(tmpRS.next()) {

                Integer seat = tmpRS.getInt("seat");
                res.add(seat);
            }
        }catch (SQLException e) {

            System.err.println(e.getMessage());
        }


        database.disconnect();

        return res;
    }

    public static void assignStudentToSeat(String sid, int cid, int seat){

        String sql = "insert into history(sid, cid, seat) " +
                "values (\"" + sid + "\", " + cid + ", " + seat + ");";

        MysqlDatabase.executeSql(sql);

    }

    public static void studentLeaveSeat(String sid, int cid, int seat) {

        String sql = "update history set endTime = \"" + Utilities.getCurrentDateTime() + "\"\n" +
                "where sid = \"" + sid + "\"\n" +
                "and cid = " + cid + "\n" +
                "and seat = " + seat + "\n" +
                "and endTime is null;";

        System.out.println("studentLeaveSeat:\n" + sql);

        MysqlDatabase.executeSql(sql);
    }
}
