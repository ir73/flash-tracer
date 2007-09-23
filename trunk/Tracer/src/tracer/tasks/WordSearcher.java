/*
 * WordSearcher.java
 *
 * Created on 22 March 2007, 19:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tracer.tasks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Position;
import javax.swing.text.View;


/**
 *
 * @author Admin
 */
public class WordSearcher {
    
    protected JTextArea comp;
    
    private int _lastCaretPos = 0;
    
    private boolean wasSearching = false;
    
    private boolean highlightAll = false;
    
    private boolean isFilter = false;
    
    private boolean forcedScreenScroll = true;
    
    // An instance of the private subclass of the default highlight painter
    Highlighter.HighlightPainter painter = new MyHighlightPainter(Color.red);
    Highlighter.HighlightPainter yellowPainter = new MyHighlightPainter(Color.yellow);
    
    // A private subclass of the default highlight painter
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }
    
    public WordSearcher(JTextArea comp) {
        this.comp = comp;
    }
    
    // Search for a word and return the offset of the
    // first occurrence. Highlights are added for all
    // occurrences found.
    public int search(String content, String word) {
        return search(content, word, 0);
    }
    
    public int search(String content, String word, int lastCaretPos) {
        //System.out.println("word = "+ word);
        word = word.toLowerCase();
        content = content.toLowerCase();
        
        int firstOffset = -1;
        Highlighter highlighter = comp.getHighlighter();
        
        clearHighlights();
        
        if (word == null || word.equals("")) {
            return -1;
        }
        
        int lastIndex = lastCaretPos;
        int wordSize = word.length();
        if ((lastIndex = content.indexOf(word, lastIndex)) != -1) {
            int endIndex = lastIndex + wordSize;
            
            try {
                highlighter.addHighlight(lastIndex, endIndex, painter);
            } catch (Exception e) {
                // Nothing to do
            }
            if (firstOffset == -1) {
                firstOffset = lastIndex;
            }
        } else {
            lastIndex = 0;
            if ((lastIndex = content.indexOf(word, lastIndex)) != -1) {
                int endIndex = lastIndex + wordSize;

                try {
                    highlighter.addHighlight(lastIndex, endIndex, painter);
                } catch (Exception e) {
                    // Nothing to do
                }
                if (firstOffset == -1) {
                    firstOffset = lastIndex;
                }
            }
        }
        
        lastIndex = 0;
        
        if (highlightAll) {
           while ((lastIndex = content.indexOf(word, lastIndex)) != -1) {
                int endIndex = lastIndex + wordSize;
                try {
                    highlighter.addHighlight(lastIndex, endIndex, yellowPainter);
                } catch (BadLocationException e) {
                    break;
                }

                lastIndex = endIndex;
            }
        }
        return firstOffset;
    }

    public int getLastCaretPos() {
        return _lastCaretPos;
    }

    public void setLastCaretPos(int lastCaretPos) {
        this._lastCaretPos = lastCaretPos;
    }

    public boolean isWasSearching() {
        return wasSearching;
    }

    public void setWasSearching(boolean wasSearching) {
        this.wasSearching = wasSearching;
    }

    public boolean isHighlightAll() {
        return highlightAll;
    }

    public void setHighlightAll(boolean highlightAll) {
        this.highlightAll = highlightAll;
    }

    public void clearHighlights() {
        Highlighter highlighter = comp.getHighlighter();
        highlighter.removeAllHighlights();
    }

    public boolean isForcedScreenScroll() {
        return forcedScreenScroll;
    }

    public void setForcedScreenScroll(boolean forcedScreenScroll) {
        this.forcedScreenScroll = forcedScreenScroll;
    }

    public boolean isFilter() {
        return isFilter;
    }

    public void setIsFilter(boolean isFilter) {
        this.isFilter = isFilter;
    }

    public String filter(String content, String word, int traceLinesCount) throws Exception {
        word = word.toLowerCase();
        
        Highlighter highlighter = comp.getHighlighter();
        
        clearHighlights();
        
        if (word == null || word.equals("")) {
            throw new Exception("invalid argument");
        }
        
        StringBuilder sb = new StringBuilder("");
        comp.setText(content);
        int totalLines = comp.getLineCount();
        for (int i=0; i < totalLines; i++) {
            int start = comp.getLineStartOffset(i);
            int end   = comp.getLineEndOffset(i);
            String lineText = content.substring(start, end);
            if (lineText.toLowerCase().indexOf(word) != -1) {
                sb.append(lineText);
            }
        }
        
        // TODO: implement highlight all functionality for filtering
//        int lastIndex = 0;
//        content = content.toLowerCase();
//        int wordSize = word.length();
//        if (highlightAll) {
//           while ((lastIndex = content.indexOf(word, lastIndex)) != -1) {
//                int endIndex = lastIndex + wordSize;
//                try {
//                    highlighter.addHighlight(lastIndex, endIndex, yellowPainter);
//                } catch (BadLocationException e) {
//                    break;
//                }
//
//                lastIndex = endIndex;
//            }
//        }
        
        return sb.toString();
    }
    
    
    
}
