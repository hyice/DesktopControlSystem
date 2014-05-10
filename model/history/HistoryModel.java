package model.history;

import model.database.HistoryDatabase;

/**
 * Created by hyice on 5/10/14.
 */
public class HistoryModel {

    public Object[][] getAllHistoryData() {

        return HistoryDatabase.getAllHistoryData();
    }

}
