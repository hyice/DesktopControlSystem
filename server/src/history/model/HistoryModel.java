package history.model;

import database.HistoryDatabase;

/**
 * Created by hyice on 5/10/14.
 */
public class HistoryModel {

    public Object[][] getHistoryDataByCondition(String sid, int cid, int seat, String startDate, String endDate) {

        return HistoryDatabase.getHistoryDataByCondition(sid, cid, seat, startDate, endDate);
    }

}
