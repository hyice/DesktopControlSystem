package tempOpen;

/**
 * Created by hyice on 5/3/14.
 */
public interface TempOpenManageWindowCallBack {

    public void tempOpenManageWindowClosed();

    public void confirmBtnPressed(String sid, int cid, int minutes);
}
