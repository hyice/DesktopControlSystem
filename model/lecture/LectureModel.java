package model.lecture;

import model.database.LectureDatabase;

import java.util.List;

/**
 * Created by hyice on 4/25/14.
 */
public class LectureModel {

    private List<FullLecture> dataSource;

    public LectureModel() {

        dataSource = LectureDatabase.getFullLectureList();
    }

    public String[] getNameArray() {

        String[] res = new String[dataSource.size()];

        for(int i=0; i<dataSource.size(); i++) {

            res[i] = dataSource.get(i).getName();
        }

        return res;
    }

    public FullLecture getLectureAtIndex(int index) {

        if(index<0 || index>=dataSource.size()) return null;

        return dataSource.get(index);
    }

    public void updateLecture(int index, String name, int cid,
                              String startTime, String endTime, int weekday) {

        FullLecture aLecture = dataSource.get(index);

        aLecture.setName(name);
        aLecture.setCid(cid);
        aLecture.setStartTime(startTime);
        aLecture.setEndTime(endTime);
        aLecture.setWeekday(weekday);

        aLecture.save();
    }

    public void removeLecture(int index) {

        FullLecture aLecture = dataSource.get(index);

        LectureDatabase.deleteLectureById(aLecture.getLid());

        dataSource.remove(index);
    }

    public FullLecture addANewLecture() {

        FullLecture newLecture = new FullLecture();

        dataSource.add(newLecture);
        return newLecture;
    }

    public String[] getStudentsInfoOfLecture(int index) {

        List<Student> students = dataSource.get(index).getStudents();

        String[] res = new String[students.size()];

        for(int i=0; i<students.size(); i++) {

            res[i] = students.get(i).getSid();
        }

        return res;
    }

    public void addStudentToLecture(String sid, int lectureIndex) {

        dataSource.get(lectureIndex).addStudent(new Student(sid));
    }

    public void removeStudentOfLecture(int studentIndex, int lectureIndex) {

        dataSource.get(lectureIndex).removeStudent(studentIndex);
    }
}
