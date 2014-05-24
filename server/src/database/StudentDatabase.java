package database;

import lecture.model.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hyice on 4/25/14.
 */
public class StudentDatabase {

    public static List<Student> getStudentsOfLecture(int lid) {

        List<Student> resList = new LinkedList<Student>();

        String sql = "select sid from student where lid = " + lid + ";";

        MysqlDatabase database = MysqlDatabase.getInstance();
        database.connect();

        ResultSet tmpRS = database.selectDataWithSqlString(sql);

        try {

            while(tmpRS.next()) {

                Student aStudent = new Student(tmpRS.getString("sid"));
                resList.add(aStudent);
            }
        }catch (SQLException e) {

            System.err.println(e.getMessage());
        }


        database.disconnect();

        return resList;
    }

    public static void addStudentToLecture(Student aStudent, int lid) {

        String sql = "insert into student(sid, lid) values (\""
                + aStudent.getSid() + "\", " + lid + ");";

        System.out.println("addStudentToLecture:\n" + sql);

        MysqlDatabase.executeSql(sql);

    }

    public static void removeStudentOfLecture(String sid, int lid) {

        String sql = "delete from student where sid = \"" + sid
                + "\" and lid = " + lid + ";";

        MysqlDatabase.executeSql(sql);
    }
}
