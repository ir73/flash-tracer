/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizzy.tasks;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.text.Highlighter;
import org.apache.log4j.Logger;
import vizzy.model.Conf;

/**
 *
 * @author sergeil
 */
public class KeywordsHighlighter {

    private static final Logger log = Logger.getLogger(KeywordsHighlighter.class);

    private static String TEMPLATE_ERROR = ": Error #";
    private List<Object> highlightObjects = new ArrayList<Object>();

    public KeywordsHighlighter() {
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    private JTextArea textArea;

    public boolean highlight() {
        Highlighter highlighter = getTextArea().getHighlighter();
        
        clearHighlights();

        int totalLines = getTextArea().getLineCount();
        int start;
        int end;
        int ind;
        String lineText;
        boolean highlighted = false;

        try {
            for (int i=0; i < totalLines; i++) {
                start = getTextArea().getLineStartOffset(i);
                end = getTextArea().getLineEndOffset(i);
                lineText = getTextArea().getText(start, end - start);
                ind = lineText.indexOf(TEMPLATE_ERROR);
                if (ind != -1) {
                    highlightObjects.add(highlighter.addHighlight(start, start + ind, Conf.errorPainter));
                    highlighted = true;
                }
            }
        } catch (Exception e) {
            log.warn("highlight() ", e);
        }
        return highlighted;
    }

    public void clearHighlights() {
        Highlighter highlighter = getTextArea().getHighlighter();
        for (Object object : highlightObjects) {
            highlighter.removeHighlight(object);
        }
    }
}
