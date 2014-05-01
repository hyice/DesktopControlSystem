package model.database;

import model.lecture.FullLecture;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hyice on 4/25/14.
 */
public class LectureDatabase {

    public static int newLecture(FullLecture aLecture) {

        int resLid = 0;

        String sql = "insert into lecture(name, cid, startTime, endTime, weekday) " +
                "values (\"" + aLecture.getName() + "\"," + aLecture.getCid() + ",\"" +
                aLecture.getStartTime() + "\",\"" + aLecture.getEndTime() + "\", " +
                aLecture.getWeekday() + ");";

        String sql2 = "select lid from lecture where name = \"" + aLecture.getName() +
                "\" and cid = " + aLecture.getCid() +
                " and startTime = \"" + aLecture.getStartTime() +
                "\" and endTime = \"" + aLecture.getEndTime() +
                "\" and weekday = " + aLecture.getWeekday() + ";";



        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();
        database.updateDataWithSqlString(sql);
        ResultSet rs = database.selectDataWithSqlString(sql2);

        try {

            while(rs.next()) {

                resLid = rs.getInt("lid");
            }
        }catch(SQLException e) {

            System.out.println(e.getMessage());
        }

        database.disconnect();

        return resLid;
    }

    public static void deleteLectureById(int lid) {

        String deleteStudentsSql = "delete from student where lid = " + lid + ";";
        MysqlDatabase.executeSql(deleteStudentsSql);

        String sql = "delete from lecture where lid = " + lid + ";";
        MysqlDatabase.executeSql(sql);
    }

    public static void updateLecture(FullLecture aLecture) {

        String sql = "update lecture set name = \"" + aLecture.getName() + "\", " +
                "cid = " + aLecture.getCid() + ", " +
                "startTime = \"" + aLecture.getStartTime() + "\", " +
                "endTime = \"" + aLecture.getEndTime() + "\", " +
                "weekday = " + aLecture.getWeekday() +
                " where lid = " + aLecture.getLid() + ";";

        System.out.println(sql);
        MysqlDatabase.executeSql(sql);
    }

    public static List<FullLecture> getFullLectureList() {

        List<FullLecture> resList = new LinkedList<FullLecture>();

        String sql = "select * from lecture;";

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();

        ResultSet tmpRS = database.selectDataWithSqlString(sql);

        try {

            while(tmpRS.next()) {

                FullLecture tmpFullLecture = new FullLecture(tmpRS.getInt("lid"),
                        tmpRS.getString("name"),
                        tmpRS.getInt("cid"),
                        tmpRS.getString("startTime"),
                        tmpRS.getString("endTime"),
                        tmpRS.getInt("weekday"));
                resList.add(tmpFullLecture);
            }
        }catch (SQLException e) {

            System.err.println(e.getMessage());
        }


        database.disconnect();

        return resList;
    }
}
