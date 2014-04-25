package model.database;

import java.sql.*;

/**
 * Created by hyice on 4/21/14.
 */
public class MysqlDatabase{
    private static MysqlDatabase ourInstance = new MysqlDatabase();
    private static Connection con;

    public static MysqlDatabase getInstance() {
        return ourInstance;
    }

    public boolean connect() {

        String url = "jdbc:mysql://localhost/DesktopControlSystem?useUnicode=true&characterEncoding=UTF-8";
        String user = "root";
        String password = "";

        boolean res = false;

        try {

            if(con!=null && !con.isClosed()) {

                return false;
            }

            Class.forName("com.mysql.jdbc.Driver").newInstance();

            con = DriverManager.getConnection(url, user, password);

            if(!con.isClosed()) {

                res = true;
                System.out.println("Connect succeed!");
            }else {

                System.out.println("Connect failed!");
            }
        }catch (Exception e) {

            System.err.println("Exception: " + e.getMessage());
        }

        return res;
    }

    public boolean disconnect() {

        boolean res = false;

        try {

            if(con==null || con.isClosed()) {

                return false;
            }

            con.close();
            res = con.isClosed();

        }catch (SQLException e) {

            System.err.println(e.getMessage());
        }

        return res;
    }


    public ResultSet selectDataWithSqlString(String sql) {

        ResultSet result = null;

        try {

            if (con==null || con.isClosed()) {

                return null;
            }

            Statement state = con.createStatement();
            result = state.executeQuery(sql);

        }catch(SQLException e) {

            System.err.println(e.getMessage());
        }

        return result;
    }

    public int updateDataWithSqlString(String sql) {

        int result = 0;

        try {

            if (con==null || con.isClosed()) {

                return 0;
            }

            Statement state = con.createStatement();
            result = state.executeUpdate(sql);

        }catch(SQLException e) {

            System.err.println(e.getMessage());
        }

        return result;
    }

    private MysqlDatabase() {

    }

    public static void main(String[] args) {

        try {

            MysqlDatabase myDatabase = MysqlDatabase.getInstance();
            myDatabase.connect();
            ResultSet result = myDatabase.selectDataWithSqlString("select * from student;");
            while (result!=null && result.next()) {

                System.out.println(result.getString("sid") + " " + result.getString("lid"));
            }

            myDatabase.updateDataWithSqlString("insert into classroom(name, seats, guardIp, forwardIp) values (\"东1-101\", 50, \"192.168.0.2\", \"192.168.1.2\");\n");
            myDatabase.disconnect();

        }catch(SQLException e) {

        }

    }
}
