/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizzy.tasks;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/**
 *
 * @author sergeil
 */
public class KeywordsHighlighter {

    private static String TEMPLATE_ERROR = ": Error #";
    private List<Object> highlightObjects = new ArrayList<Object>();

    Highlighter.HighlightPainter errorPainter = new MyHighlightPainter(new Color(200, 200, 200));
    Highlighter.HighlightPainter warningPainter = new MyHighlightPainter(new Color(230, 230, 230));

    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {

        public MyHighlightPainter(Color color) {
            super(color);
        }
    }
    private final JTextArea jTraceTextArea;

    public KeywordsHighlighter(JTextArea jTraceTextArea) {
        this.jTraceTextArea = jTraceTextArea;
    }

    public void highlight() {
        Highlighter highlighter = jTraceTextArea.getHighlighter();
        
        clearHighlights();

        int totalLines = jTraceTextArea.getLineCount();
        int start;
        int end;
        int ind;
        String lineText;

        try {
            for (int i=0; i < totalLines; i++) {
                start = jTraceTextArea.getLineStartOffset(i);
                end = jTraceTextArea.getLineEndOffset(i);
                lineText = jTraceTextArea.getText(start, end - start);
                ind = lineText.indexOf(TEMPLATE_ERROR);
                if (ind != -1) {
                    highlightObjects.add(highlighter.addHighlight(start, start + ind, errorPainter));
                }
            }
        } catch (Exception e) {
        }
    }

    public void clearHighlights() {
        Highlighter highlighter = jTraceTextArea.getHighlighter();
        for (Object object : highlightObjects) {
            highlighter.removeHighlight(object);
        }
    }
}
