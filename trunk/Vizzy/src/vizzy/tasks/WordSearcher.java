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
import vizzy.model.Conf;


/**
 *
 * @author Admin
 */
public class WordSearcher {
    
    private JTextArea textArea;
    private int lastCaretPos = 0;
    private String word;
    private boolean wasSearching = false;
    private boolean highlightAll = false;
    private boolean isFilter = false;
    private boolean forcedScreenScroll = true;
    private JScrollHighlightPanel highlightPanel;
    private List<Object> highlightObjects = new ArrayList<Object>();
    
    public WordSearcher() {
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public JScrollHighlightPanel getHighlightPanel() {
        return highlightPanel;
    }

    public void setHighlightPanel(JScrollHighlightPanel highlightPanel) {
        this.highlightPanel = highlightPanel;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }
    
    // Search for a word and return the offset of the
    // first occurrence. Highlights are added for all
    // occurrences found.
    public int search(String content) {
        return search(content, 0);
    }
    
    public int search(String content, int lastCaretPos) {
        //System.out.println("word = "+ word);
        word = word.toLowerCase();
        content = content.toLowerCase();
        
        int firstOffset = -1;
        Highlighter highlighter = getTextArea().getHighlighter();
        
        clearHighlights();
        
        if (word == null || word.equals("")) {
            return -1;
        }
        
        int lastIndex = lastCaretPos;
        int wordSize = word.length();
        if ((lastIndex = content.indexOf(word, lastIndex)) != -1) {
            int endIndex = lastIndex + wordSize;
            
            try {
                highlightObjects.add(highlighter.addHighlight(lastIndex, endIndex, Conf.highlightedSearchResultPainter));
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
                    highlightObjects.add(highlighter.addHighlight(lastIndex, endIndex, Conf.highlightedSearchResultPainter));
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
                    highlightObjects.add(highlighter.addHighlight(lastIndex, endIndex, Conf.searchResultPainter));
                    indexes.add(lastIndex);
                } catch (BadLocationException e) {
                    break;
                }

                lastIndex = endIndex;
            }
            getHighlightPanel().setIndexes(indexes);
            getHighlightPanel().repaint();
        }
        return firstOffset;
    }

    public int getLastCaretPos() {
        return lastCaretPos;
    }

    public void setLastCaretPos(int lastCaretPos) {
        this.lastCaretPos = lastCaretPos;
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
        Highlighter highlighter = getTextArea().getHighlighter();
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

    public void setFilter(boolean isFilter) {
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
        getTextArea().setText(content);
        int totalLines = getTextArea().getLineCount();

        for (int i=0; i < totalLines; i++) {
            int start = getTextArea().getLineStartOffset(i);
            int end   = getTextArea().getLineEndOffset(i);
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
