package classroom.model;

import utilities.Utilities;
import database.ClassroomDatabase;
import database.HistoryDatabase;
import database.LectureDatabase;
import database.TempOpenDatabase;

import java.util.List;

/**
 * Created by hyice on 4/23/14.
 */
public class ClassroomModel {

    private List<FullClassRoom> dataSource;

    public ClassroomModel() {

        dataSource = ClassroomDatabase.getFullClassroomList();
    }

    public String[] getNameArray() {

        String[] result = new String[dataSource.size()];

        for(int i=0; i<dataSource.size();i++) {

            result[i] = dataSource.get(i).getName();
        }

        return result;
    }

    public FullClassRoom getFullClassroomAtIndex(int index) {

        if(index<0 || index>=dataSource.size()) return null;

        return dataSource.get(index);
    }

    public FullClassRoom addANewRoom() {

        FullClassRoom newRoom = new FullClassRoom();

        dataSource.add(newRoom);

        return newRoom;
    }

    public void removeRoomAtIndex(int index) {

        FullClassRoom aRoom = dataSource.get(index);

        aRoom.delete();

        dataSource.remove(index);
    }


    public void updateRoom(int index, String name, int seats,
                                         String guardIp, String forwardIp) {

        FullClassRoom aRoom = dataSource.get(index);
        aRoom.setName(name);
        aRoom.setSeats(seats);
        aRoom.setGuardIp(guardIp);
        aRoom.setForwardIp(forwardIp);
        aRoom.save();
    }

    public String checkRoomInfoIfAnyError(int index, String name, int seats,
                                          String guardIp, String forwardIp) {
        String res = "";

        if(name.equals("")) {

            res = "教室名不能为空！";
        }else if(seats == 0) {

            res = "座位数为0的教室没有开放的必要！";
        }else if(!Utilities.isIpAdress(guardIp)) {

            res = "门禁ip地址格式错误！";
        }else if(!Utilities.isIpAdress(forwardIp)) {

            res = "通信机ip地址格式错误！";
        }else {

            FullClassRoom aRoom = dataSource.get(index);

            String tmpName = ClassroomDatabase.getNameIfGuardIpHasBeenUsed(guardIp, aRoom.getCid());

            if(!tmpName.equals(ClassroomDatabase.NO_CLASSROOM_NAME)) {

                res = "门禁ip已经被教室 " + tmpName + " 使用，ip不可重复！";
            }else {

                tmpName = ClassroomDatabase.getNameIfForwardIpHasBeenUsed(forwardIp, aRoom.getCid());
                if(!tmpName.equals(ClassroomDatabase.NO_CLASSROOM_NAME)) {

                    res = "通信机ip已经被教室 " + tmpName + " 使用，ip不可重复！";
                }
            }
        }

        return res;
    }

    public String checkRoomIfCanBeDeleted(int index) {

        String res = "";

        FullClassRoom aRoom = dataSource.get(index);

        int lectureCount = LectureDatabase.getLecturesCountInClassroom(aRoom.getCid());
        int historyCount = HistoryDatabase.getHistoryCountOfClassroom(aRoom.getCid());
        int tempOpenCount = TempOpenDatabase.getTempOpenCountOfClassroom(aRoom.getCid());

        if(lectureCount!=0) {

            res += "有" + lectureCount + "门课程正在使用此教室。\n";
        }

        if(historyCount!=0) {

            res += "有" + historyCount + "条此教室相关的使用记录。\n";
        }

        if(tempOpenCount!=0) {

            res += "有" + tempOpenCount + "位学生临时开放在此教室。\n";
        }

        if(!res.equals("")) {

            res += "如果继续删除会同时删除这些内容，是否继续？\n";
        }

        return res;
    }
}
