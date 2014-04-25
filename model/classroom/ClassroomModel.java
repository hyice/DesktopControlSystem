package model.classroom;

import model.classroom.FullClassRoom;
import model.database.ClassroomDatabase;

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
}
