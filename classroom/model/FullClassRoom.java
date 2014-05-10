package classroom.model;

import database.ClassroomDatabase;

/**
 * Created by hyice on 4/23/14.
 */
public class FullClassRoom extends SimpleClassroom {

    private int seats;
    private String guardIp;
    private String forwardIp;

    public FullClassRoom() {

        super(0, "");
        seats = 0;
        guardIp = "0.0.0.0";
        forwardIp = "0.0.0.0";
    }

    public FullClassRoom(int tmpCid, String tmpName, int tmpSeats,
                         String tmpGuardIp, String tmpForwardIp) {

        super(tmpCid, tmpName);

        seats = tmpSeats;
        guardIp = tmpGuardIp;
        forwardIp = tmpForwardIp;
    }

    public int getSeats() {

        return seats;
    }

    public String getGuardIp() {

        return guardIp;
    }

    public String getForwardIp() {

        return forwardIp;
    }

    public void setSeats(int tmpSeats) {

        seats = tmpSeats;
    }

    public void setGuardIp(String tmpGuardIp) {

        guardIp = tmpGuardIp;
    }

    public void setForwardIp(String tmpForwardIp) {

        forwardIp = tmpForwardIp;
    }

    public void save() {

        System.out.println("cid"+getCid());

        if(getCid() == 0) {

            setCid(ClassroomDatabase.newClassroom(this));
        }else {

            ClassroomDatabase.updateClassroom(this);
        }
    }

    public void delete() {

        if(getCid() != 0) {

            ClassroomDatabase.deleteClassroomById(getCid());
        }
    }
}
