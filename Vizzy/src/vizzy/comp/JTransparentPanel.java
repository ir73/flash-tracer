/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizzy.comp;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 *
 * @author sergeil
 */
public class JTransparentPanel extends JPanel {

    public JTransparentPanel() {
        this(new FlowLayout());
    }

    public JTransparentPanel(LayoutManager lm) {
        super(lm);
        setOpaque(false);
    }
}
