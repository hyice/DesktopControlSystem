package model.lecture;

import model.classroom.SimpleClassroom;
import model.database.LectureDatabase;
import model.database.StudentDatabase;

import java.util.List;

/**
 * Created by hyice on 4/25/14.
 */
public class FullLecture {

    private int lid;
    private String name;
    private int cid;
    private String startTime;
    private String endTime;
    private int weekday;

    private String classroomName;
    private List<Student> students;

    public FullLecture() {

        lid = 0;
        name = "";
        cid = 0;
        startTime = "00:00:00";
        endTime = "00:00:00";
        weekday = 0;

        classroomName = null;
    }

    public FullLecture(int tmpLid, String tmpName, int tmpCid,
                       String tmpStartTime, String tmpEndTime, int tmpWeekday) {

        lid = tmpLid;
        name = tmpName;
        cid = tmpCid;
        startTime = tmpStartTime;
        endTime = tmpEndTime;
        weekday = tmpWeekday;

        classroomName = null;
    }

    public int getLid() {

        return lid;
    }

    public void setLid(int tmpLid) {

        lid = tmpLid;
    }

    public String getName() {

        return name;
    }

    public void setName(String tmpName) {

        name = tmpName;
    }

    public int getCid() {

        return cid;
    }

    public void setCid(int tmpCid) {

        cid = tmpCid;

        classroomName = SimpleClassroom.getNameForId(cid);
    }

    public String getStartTime() {

        return startTime;
    }

    public void setStartTime(String tmpStartTime) {

        startTime = tmpStartTime;
    }

    public String getEndTime() {

        return endTime;
    }

    public void setEndTime(String tmpEndTime) {

        endTime = tmpEndTime;
    }

    public int getWeekday() {

        return weekday;
    }

    public String getWeekdayString() {

        switch (weekday) {

            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 7:
                return "日";
            default:
                return "";
        }
    }

    public void setWeekday(int tmpWeekday) {

        weekday = tmpWeekday;
    }

    public String getClassroomName() {

        if (classroomName == null) {

            classroomName = SimpleClassroom.getNameForId(cid);
        }

        return classroomName;
    }

    public void save() {

        if(getLid() == 0) {

            setLid(LectureDatabase.newLecture(this));
        }else {

            LectureDatabase.updateLecture(this);
        }
    }

    public List<Student> getStudents() {

        if (students == null) {

            students = StudentDatabase.getStudentsOfLecture(lid);
        }

        return students;
    }

    public void addStudent(Student aStudent) {

        students.add(aStudent);
        StudentDatabase.addStudentToLecture(aStudent, lid);
    }

    public void removeStudent(int index) {

        Student student = students.get(index);

        StudentDatabase.removeStudentOfLecture(student.getSid(), lid);

        students.remove(index);
    }
}
