package history.view;

/**
 * Created by hyice on 5/14/14.
 */
public interface HistorySearchWindowCallBack {

    public void historySearchWindowClosed();
    public void historySearchBtnPressed(String sid, int cid, int seat, String date);
}
