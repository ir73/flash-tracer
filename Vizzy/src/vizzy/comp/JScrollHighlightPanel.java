/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vizzy.comp;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

/**
 *
 * @author sergeil
 */
public class JScrollHighlightPanel extends JPanel {

    private ArrayList<Integer> indexes;
    private JTextArea ta;

    public ArrayList<Integer> getIndexes() {
        return indexes;
    }
    public void setIndexes(ArrayList<Integer> indexes) {
        this.indexes = indexes;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (indexes != null) {
            for (Integer integer : indexes) {
                try {
                    int line = getTa().getLineOfOffset(integer);
                    int lineHeight = (int) (getHeight() * ((double) line / (double) getTa().getLineCount()));
                    
                    int w = getWidth() - 5;
                    int h = 4;
                    int x = 2;
                    int y = lineHeight;
                    
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(x, y, w, h);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, w, h);
                } catch (BadLocationException ex) {
                    Logger.getLogger(JScrollHighlightPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * @return the ta
     */
    public JTextArea getTa() {
        return ta;
    }

    /**
     * @param ta the ta to set
     */
    public void setTa(JTextArea ta) {
        this.ta = ta;
    }

}
