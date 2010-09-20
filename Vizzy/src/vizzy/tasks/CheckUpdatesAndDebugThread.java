/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizzy.tasks;

import java.util.Date;
import vizzy.forms.VizzyForm;

/**
 *
 * @author sergeil
 */
public class CheckUpdatesAndDebugThread extends Thread {
    private VizzyForm mainForm;

    public CheckUpdatesAndDebugThread(VizzyForm mainForm) {
        this.mainForm = mainForm;
    }

    @Override
    public void run() {
        boolean aut = mainForm.isAlwaysOnTop();
        mainForm.setAlwaysOnTop(false);
        mainForm.checkDebugPlayers();
        if (mainForm.isCheckUpdates) {
            Date nowDate = new Date();
            long compareResult = nowDate.getTime() - mainForm.lastUpdateDate.getTime();
            if (compareResult > 7 * 24 * 60 * 60 * 1000) {
                mainForm.checkUpdates(false);
            }
        }
        mainForm.setAlwaysOnTop(aut);
        mainForm = null;
    }
}
