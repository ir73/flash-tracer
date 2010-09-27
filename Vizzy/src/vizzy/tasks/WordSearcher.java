/*
 * WordSearcher.java
 *
 * Created on 22 March 2007, 19:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vizzy.tasks;

import java.awt.Color;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import vizzy.comp.JScrollHighlightPanel;


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

    private JScrollHighlightPanel panel;

    private List<Object> highlightObjects = new ArrayList<Object>();
    
    Highlighter.HighlightPainter highlightedSearchResultPainter = new MyHighlightPainter(new Color(0, 150, 0));
    Highlighter.HighlightPainter searchResultPainter = new MyHighlightPainter(new Color(200, 255, 200));

    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }
    
    public WordSearcher(JTextArea comp, JScrollHighlightPanel panel) {
        this.comp = comp;
        this.panel = panel;
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
                highlightObjects.add(highlighter.addHighlight(lastIndex, endIndex, highlightedSearchResultPainter));
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
                    highlightObjects.add(highlighter.addHighlight(lastIndex, endIndex, highlightedSearchResultPainter));
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
            ArrayList<Integer> indexes = new ArrayList<Integer>();
            while ((lastIndex = content.indexOf(word, lastIndex)) != -1) {
                int endIndex = lastIndex + wordSize;
                try {
                    highlightObjects.add(highlighter.addHighlight(lastIndex, endIndex, searchResultPainter));
                    indexes.add(lastIndex);
                } catch (BadLocationException e) {
                    break;
                }

                lastIndex = endIndex;
            }
           panel.setIndexes(indexes);
           panel.repaint();
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
        for (Object object : highlightObjects) {
            highlighter.removeHighlight(object);
        }
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

    public String filter(String content, String word) throws Exception {
        word = word.toLowerCase();
        
        clearHighlights();
        
        if (word == null || word.equals("")) {
            throw new Exception("invalid argument");
        }

        String[] words = word.split(",");
        
        StringBuilder sb = new StringBuilder("");
        comp.setText(content);
        int totalLines = comp.getLineCount();

        for (int i=0; i < totalLines; i++) {
            int start = comp.getLineStartOffset(i);
            int end   = comp.getLineEndOffset(i);
            String lineText = content.substring(start, end);

            for (int j = 0; j < words.length; j++) {
                String w = words[j];
                if (lineText.toLowerCase().indexOf(w) != -1) {
                    sb.append(lineText);
                    break;
                }
            }
            
        }
        
        return sb.toString();
    }
    
    
    
}
