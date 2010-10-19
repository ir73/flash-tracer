/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizzy.comp;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxEditor.UIResource;

/**
 *
 * @author sergeil
 */
public class BGComboBoxEditorComponent extends JTextField {

    private boolean isHighighting = false;
    private Color highlightColor;

    public BGComboBoxEditorComponent(String value, int n) {
        super(value, n);
    }

    // workaround for 4530952
    @Override
    public void setText(String s) {
        if (getText().equals(s)) {
            return;
        }
        super.setText(s);
    }

    @Override
    public void setBorder(Border b) {
        if (!(b instanceof UIResource)) {
            super.setBorder(b);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (isHighighting) {
            g.setColor(highlightColor);
            g.fillRect(0, 0, getWidth(), 4);
//            try {
//                g.fillRect(modelToView(getText().length()).x + 1, 0, getWidth() + 10, getHeight());
//            } catch (BadLocationException ex) {
//                Logger.getLogger(BGComboBoxEditorComponent.class.getName()).log(Level.WARNING, "cannot highlight search pane", ex);
//            }
        }
    }

    public void removeHighlight() {
        isHighighting = false;
        repaint();
        revalidate();
    }

    public void highlight(Color color) {
        isHighighting = true;
        highlightColor = color;
        repaint();
        revalidate();
    }
}
