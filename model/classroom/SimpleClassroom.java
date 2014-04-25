package model.classroom;

import model.database.ClassroomDatabase;

/**
 * Created by hyice on 4/21/14.
 */
public class SimpleClassroom {

    private int cid;
    private String name;

    public SimpleClassroom () {

        cid = 0;
        name = "";
    }

    public SimpleClassroom(int tmpCid, String tmpName) {

        cid = tmpCid;
        name = tmpName;
    }

    public int getCid() {

        return cid;
    }

    public void setCid(int tmpCid) {

        cid = tmpCid;
    }

    public String getName() {

        return name;
    }

    public void setName(String newName) {

        name = newName;

    }

    public static String getNameForId(int cid) {

        String res = ClassroomDatabase.getClassroomNameForId(cid);

        return res;
    }
}
