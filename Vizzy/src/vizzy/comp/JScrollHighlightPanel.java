/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizzy.comp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
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
    private final static int MARKER_HEIGHT = 3;
    private final static int MARKER_X = 2;

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
            double textAreaHeight = (double)getTa().getHeight();
            double highlightPanelHeight = getHeight() - MARKER_HEIGHT;
            for (Integer integer : indexes) {
                try {
                    Rectangle rect = getTa().modelToView(integer);

                    int lineHeight = (int) (highlightPanelHeight * ((double) rect.getY() / textAreaHeight));

                    int w = getWidth() - 5;
                    int y = lineHeight;

                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(MARKER_X, y, w, MARKER_HEIGHT);
                    g.setColor(Color.BLACK);
                    g.drawRect(MARKER_X, y, w, MARKER_HEIGHT);
                } catch (Exception ex) {
                    Logger.getLogger(JScrollHighlightPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

//        if (indexes != null) {
//            int height = getTa().getHeight();
//            Point coords = new Point(getTa().getWidth(), 1);
//            double columns = getTa().viewToModel(coords);
//            double lineCount = Math.ceil(getTa().getText().length() / columns);
//            double highlightPanelHeight = getHeight() - MARKER_HEIGHT;
//            for (Integer integer : indexes) {
//                try {
//                    int line = (int) ((double) integer / columns);
//                    int lineHeight = (int) (highlightPanelHeight * ((double) line / lineCount));
//
//                    int w = getWidth() - 5;
//                    int y = lineHeight;
//
//                    g.setColor(Color.LIGHT_GRAY);
//                    g.fillRect(MARKER_X, y, w, MARKER_HEIGHT);
//                    g.setColor(Color.BLACK);
//                    g.drawRect(MARKER_X, y, w, MARKER_HEIGHT);
//                } catch (Exception ex) {
//                    Logger.getLogger(JScrollHighlightPanel.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
    }

    public JTextArea getTa() {
        return ta;
    }

    public void setTa(JTextArea ta) {
        this.ta = ta;
    }
}
