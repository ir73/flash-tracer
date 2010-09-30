/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizzy.comp;

import javax.swing.JTextPane;

/**
 *
 * @author sergeil
 */
public class NoWordWrapTextPane extends JTextPane {

    public NoWordWrapTextPane() {
        super();
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }
}
