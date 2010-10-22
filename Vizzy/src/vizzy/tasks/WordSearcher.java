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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import org.apache.log4j.Logger;
import vizzy.comp.JScrollHighlightPanel;
import vizzy.model.Conf;
import vizzy.model.SearchResult;
import vizzy.model.SettingsModel;


/**
 *
 * @author Admin
 */
public class WordSearcher {

    private static final Logger log = Logger.getLogger(WordSearcher.class);
    
    private JTextArea textArea;
    private String word;
    private boolean wasSearching = false;
    private JScrollHighlightPanel highlightPanel;
    private List<Object> highlightObjects = new ArrayList<Object>();
    private final SettingsModel settings;
    private int nextSearchPos = 0;
    private int lastSearchPos = 0;
    
    public WordSearcher(SettingsModel settings) {
        this.settings = settings;
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
        if (word != null) {
            if (!word.toLowerCase().equals(this.word)) {
                nextSearchPos = 0;
                lastSearchPos = 0;
            }
        } else {
            nextSearchPos = 0;
            lastSearchPos = 0;
        }
        this.word = word;
    }

    public void clearSearch() {
        setWord("");
        clearHighlights();
        wasSearching = false;
    }

//    private int findClosest(Pattern pattern, String substring) {
//        int lastIndex = -1;
//        if (settings.isRegexp()) {
//            String[] matcher = pattern.split(substring, 2);
//            if (matcher.length > 1) {
//                lastIndex = matcher[0].length();
//            }
//        } else {
//            lastIndex = substring.indexOf(word);
//        }
//        return lastIndex;
//    }

    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }
    
    // Search for a word and return the offset of the
    // first occurrence. Highlights are added for all
    // occurrences found.
    public SearchResult search(String content, boolean nextPos) {
//        log.info("Search " + word);
//        new Exception().printStackTrace();
        
        //System.out.println("word = "+ word);
        clearHighlights();

        wasSearching = true;
        
        if (word == null || word.equals("")) {
            return null;
        }

        if (!settings.isRegexp()) {
            word = word.toLowerCase();
            content = content.toLowerCase();
        }

        int position = nextPos ? nextSearchPos : lastSearchPos;
        int tmpIndex;
        int wordSize = word.length();
        int lastIndex = -1;
        int firstOffset = -1;
        Highlighter highlighter = getTextArea().getHighlighter();
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        
        // init regexp
        Pattern pattern = null;
        Matcher matcher = null;
        if (settings.isRegexp()) {
            try {
                pattern = Pattern.compile(word);
            } catch (Exception e) {
                return null;
            }
            matcher = pattern.matcher(content);
        }
        

        // find match from current position
        if (settings.isRegexp()) {
            try {
                if (matcher.find(position)) {
                    lastIndex = matcher.start();
                    wordSize = matcher.group().length();
                }
            } catch (Exception e) {
                return null;
            }
        } else {
            lastIndex = content.indexOf(word, position);
        }

        // if match not found try to search from the beginning
        if (lastIndex == -1) {
            if (settings.isRegexp()) {
                try {
                    if (matcher.find(0)) {
                        lastIndex = matcher.start();
                        wordSize = matcher.group().length();
                    }
                } catch (Exception e) {
                    return null;
                }
            } else {
                lastIndex = content.indexOf(word);
            }
        }

        // if found
        if (lastIndex != -1) {
            tmpIndex = lastIndex + wordSize;
            try {
                highlightObjects.add(highlighter.addHighlight(lastIndex, tmpIndex, Conf.highlightedSearchResultPainter));
            } catch (Exception e) {
            }
            firstOffset = lastIndex;
        } else {
            getHighlightPanel().setIndexes(indexes);
            getHighlightPanel().repaint();
            return null;
        }

        if (settings.isHightlightAll()) {
            
            if (settings.isRegexp()) {
                matcher.reset();
                try {
                    while (matcher.find()) {
                        try {
                            highlightObjects.add(highlighter.addHighlight(matcher.start(), matcher.end(), Conf.searchResultPainter));
                            indexes.add(matcher.start());
                        } catch (BadLocationException e) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    return null;
                }
            } else {
                lastIndex = 0;
                while (true) {
                    lastIndex = content.indexOf(word, lastIndex);
                    if (lastIndex == -1) {
                        break;
                    }

                    tmpIndex = lastIndex + wordSize;
                    try {
                        highlightObjects.add(highlighter.addHighlight(lastIndex, tmpIndex, Conf.searchResultPainter));
                        indexes.add(lastIndex);
                    } catch (BadLocationException e) {
                        break;
                    }

                    lastIndex = tmpIndex;
                }
            }
        }

        getHighlightPanel().setIndexes(indexes);
        getHighlightPanel().repaint();

        nextSearchPos = firstOffset + wordSize;
        lastSearchPos = firstOffset;

        return new SearchResult(firstOffset, wordSize);
    }

//    public void setLastCaretPos(int lastCaretPos) {
//        this.lastCaretPos = lastCaretPos;
//    }

    public boolean isWasSearching() {
        return wasSearching;
    }

    public void clearHighlights() {
        Highlighter highlighter = getTextArea().getHighlighter();
        for (Object object : highlightObjects) {
            highlighter.removeHighlight(object);
        }
    }

    public String filter(String content, String word) throws Exception {
        wasSearching = true;

        word = word.toLowerCase();
        content = content.toLowerCase();
        
        clearHighlights();
        
        if (word == null || word.equals("")) {
            return "";
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
