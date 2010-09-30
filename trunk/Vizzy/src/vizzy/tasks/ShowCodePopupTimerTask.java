/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vizzy.tasks;

import java.awt.event.MouseEvent;
import java.util.TimerTask;
import vizzy.forms.VizzyForm;

/**
 *
 * @author sergeil
 */
public class ShowCodePopupTimerTask extends TimerTask {
    private final VizzyForm aThis;
    private final MouseEvent me;

    public ShowCodePopupTimerTask(VizzyForm aThis, MouseEvent me) {
        this.aThis = aThis;
        this.me = me;
    }

    @Override
    public void run() {
        aThis.onShowCodePopup(me);
    }

}
