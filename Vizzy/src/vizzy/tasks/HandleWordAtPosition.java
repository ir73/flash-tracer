/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vizzy.tasks;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import javax.swing.JTextArea;
import vizzy.model.SourceAndLine;

/**
 *
 * @author sergeil
 */
public class HandleWordAtPosition {
    
    class MinPositionParam {
        public String separator;
        public boolean substract;
        public MinPositionParam(String keyword, boolean substract) {
            this.separator = keyword;
            this.substract = substract;
        }
    }

    public static final String HTTP_TEMPLATE = "http://";
    public static final String FILE_TEMPLATE = "file:///";
    private String customASEditor;
    private boolean defaultASEditor;
    private final JTextArea jTraceTextArea;

    public HandleWordAtPosition(JTextArea jTraceTextArea) {
        this.jTraceTextArea = jTraceTextArea;
    }

    public SourceAndLine tryPositionForSourceFile(int offset) {
        SourceAndLine source = extractSourceFile(offset, jTraceTextArea.getText());
        return source;
    }

    public void handle() {
        if (!Desktop.isDesktopSupported()) {
            return;
        }

        try {
            checkHTTPLink();
        } catch (Exception ex) {
        }

        try {
            checkSourceFile();
        } catch (Exception ex) {
        }
    }

    private void checkHTTPLink() throws Exception {

        int startIndex;
        int endIndex;
        String currentWord;

        int currentIndex = jTraceTextArea.getCaretPosition();
        String text = jTraceTextArea.getText();

        // CHECK FOR HTTP LINK
        startIndex = getMinPosition(text, currentIndex,
                new MinPositionParam[]{
                    new MinPositionParam(HTTP_TEMPLATE, false),
                    new MinPositionParam(FILE_TEMPLATE, false),
                    new MinPositionParam("\n", true),
                    new MinPositionParam("\r", true),
                    new MinPositionParam(" ", true)},
                true);
        endIndex = getMinPosition(text, currentIndex,
                new MinPositionParam[]{
                    new MinPositionParam("\n", true),
                    new MinPositionParam("\r", true),
                    new MinPositionParam("\"", true),
                    new MinPositionParam("'", true),
                    new MinPositionParam(" ", true)},
                false);

        if (endIndex != -1 && startIndex != -1) {
            currentWord = text.substring(startIndex, endIndex);
            if (currentWord != null 
                    && (currentWord.startsWith(HTTP_TEMPLATE) || currentWord.startsWith(FILE_TEMPLATE))) {
                Desktop.getDesktop().browse(new URI(currentWord));
                jTraceTextArea.setSelectionStart(startIndex);
                jTraceTextArea.setSelectionEnd(endIndex);
                return;
            }
        }
    }

    private void checkSourceFile() throws Exception {
        SourceAndLine source = extractSourceFile(jTraceTextArea.getCaretPosition(), jTraceTextArea.getText());
        if (source == null) {
            return;
        }
        
        if (defaultASEditor) {
            File file = new File(source.filePath);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            }
        } else if (customASEditor != null) {
            String command = customASEditor
                    .replace("%file%", "\"" + source.filePath + "\"")
                    .replace("%line%", String.valueOf(source.lineNum));
            Process p = Runtime.getRuntime().exec(command);
        }
        jTraceTextArea.setSelectionStart(source.startPos);
        jTraceTextArea.setSelectionEnd(source.startPos + source.filePath.length());
    }

    private SourceAndLine extractSourceFile(int currentIndex, String text) {
        int startIndex;
        int endIndex;
        String currentWord;

        startIndex = getMinPosition(text, currentIndex,
                new MinPositionParam[]{
                    new MinPositionParam("\n", true),
                    new MinPositionParam("\r", true)},
                true);
        endIndex = getMinPosition(text, currentIndex,
                new MinPositionParam[]{
                    new MinPositionParam("\n", true),
                    new MinPositionParam("\r", true)},
                false);

        if (endIndex != -1 && startIndex != -1
                && startIndex < endIndex) {
            currentWord = text.substring(startIndex, endIndex); // whole line
            if (currentWord != null
                    && currentWord.startsWith("\tat ")
                    && currentWord.endsWith("]")) {
                int sIndex = currentWord.indexOf("[");
                int eIndex = currentWord.lastIndexOf(":");
                if (sIndex != -1 && eIndex != -1
                        && sIndex < eIndex) {
                    sIndex = sIndex + 1;
                    String filePath = currentWord.substring(sIndex, eIndex);
                    int lineNum = Integer.parseInt(currentWord.substring(eIndex + 1, currentWord.length() - 1));
                    return new SourceAndLine(filePath, lineNum, startIndex + sIndex);
                }
            }
        }
        return null;
    }

    private int getMinPosition(String text, int currentIndex, MinPositionParam[] minPositionParam, boolean left) {
        int index = -1;
        for (MinPositionParam param : minPositionParam) {
            int i;
            if (left)
                i = text.lastIndexOf(param.separator, currentIndex);
            else
                i = text.indexOf(param.separator, currentIndex);
            if (i != -1) {
                if (left) {
                    if (param.substract)
                        i = i + param.separator.length();
                    if (index == -1)
                        index = i;
                    else if (index < i)
                        index = i;
                } else {
                    if (index == -1)
                        index = i;
                    else if (index > i)
                        index = i;
                }
            }
        }
        return index;
    }

    public void setCustomASEditor(String customASEditor) {
        this.customASEditor = customASEditor;
    }

    public void setDefaultEditorUsed(boolean defaultASEditor) {
        this.defaultASEditor = defaultASEditor;
    }

}
