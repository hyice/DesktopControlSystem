package lecture.model;

/**
 * Created by hyice on 4/25/14.
 */
public class Student {

    private String sid;

    public Student() {

        sid = "";
    }

    public Student(String tmpSid) {

        sid = tmpSid;
    }

    public String getSid() {

        return sid;
    }
}
