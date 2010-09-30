/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vizzy.tasks;

import java.util.TimerTask;
import vizzy.forms.VizzyForm;

/**
 *
 * @author sergeil
 */
public class HideCodePopupTimerTask extends TimerTask {
    private final VizzyForm aThis;

    public HideCodePopupTimerTask(VizzyForm aThis) {
        this.aThis = aThis;
    }

    @Override
    public void run() {
        aThis.onHideCodePopup();
    }

}
