package tempOpen;

import database.TempOpenDatabase;

/**
 * Created by hyice on 5/3/14.
 */
public class TempOpenModel {

    public void newTempOpen(String sid, int cid, int minutes) {

        TempOpenDatabase.newTempOpenRecord(sid, cid, minutes);
    }
}
