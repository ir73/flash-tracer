/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vizzy.model;

import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import vizzy.forms.panels.SnapshotForm;

/**
 *
 * @author sergeil
 */
public class SettingsModel {
    public static final String OS_LINUX = "linux";
    public static final String OS_MAC_OS_X = "mac os x";
    public static final String OS_WINDOWS = "windows";
    public static final String OS_WINDOWS_VISTA = "vista";

    public static String OSName = System.getProperty("os.name").toLowerCase();
    public static String newLine = System.getProperty("line.separator");
    public static String userHome = System.getProperty("user.home");

    private static SettingsModel instance;
    
    private String defaultFont;
    private Font[] fonts;
    private String flashLogFileName;
    private String policyLogFileName;
    private int logType = 0;
    private String customASEditor = "\"C:\\Program Files\\FlashDevelop\\FlashDevelop.exe\" %file% -line %line%";
    private boolean isDefaultASEditor = true;
    private boolean highlightKeywords = true;
    private boolean isAutoRefresh = true;
    private boolean isUTF = true;
    private boolean isSmartTraceEnabled = true;
    private boolean isCheckUpdates = true;
    private boolean maxNumLinesEnabled = false;
    private long maxNumLines = 20;
    private boolean restoreOnUpdate = false;
    private long refreshFreq = 500;
    private boolean isAlwaysONTop;
    private Date lastUpdateDate = new Date(0L);
    private boolean detectPlayer;
    private boolean isHightlightAll = true;
    private boolean isWordWrap = true;
    private boolean isFilter = false;
    private String[] searchKeywords;
    private Font traceFont;
    private String currentLogFile;
    private Rectangle window;
    private DefaultComboBoxModel searchKeywordsModel;
    private String[] fontNames;

    private SettingsModel() {
        super();
    }

    public synchronized static SettingsModel getInstance() {
        if (instance == null) {
            instance = new SettingsModel();
        }
        return instance;
    }

    public void parseProperties(Properties props, Rectangle rect) {
        setLastUpdateDate(props.getProperty("update.last"));
        setDetectPlayer(props.getProperty("settings.detectplayer", "true").equals("true"));
        setCheckUpdates(props.getProperty("settings.autoupdates", "true").equals("true"));
        setFlashLogFileName(props.getProperty("settings.filename", getFlashLogFileName()));
        setRefreshFreq(props.getProperty("settings.refreshFreq", "500"));
        setUTF(props.getProperty("settings.isUTF", "true").equals("true"));
        setMaxNumLinesEnabled(props.getProperty("settings.maxNumLinesEnabled", "false").equals("true"));
        setMaxNumLines(props.getProperty("settings.maxNumLines", "50000"));
        setRestoreOnUpdate(props.getProperty("settings.restore", "false").equals("true"));
        setAlwaysONTop(props.getProperty("settings.alwaysontop", "false").equals("true"));
        setHighlightAll(props.getProperty("settings.highlight_all", "true").equals("true"));
        setAutoRefresh(props.getProperty("settings.autorefresh", "true").equals("true"));
        setWordWrap(props.getProperty("settings.wordwrap", "true").equals("true"));
        setSmartTraceEnabled(props.getProperty("settings.enabelSmartTrace", "true").equals("true"));
        setHighlightKeywords(props.getProperty("settings.highlightKeywords", "true").equals("true"));
        setCustomASEditor(props.getProperty("settings.customASEditor", null));
        setDefaultASEditor(props.getProperty("settings.isDefaultUsed", "true").equals("true"));
        setTraceFont(props.getProperty("settings.font.name", getDefaultFont()), props.getProperty("settings.font.size", "12"));
        setSearchKeywords(props.getProperty("search.keywords", "").split("\\|\\|\\|"));
        setFilter(false);
        setLogType(props.getProperty("settings.logtype", "0"));
        setMainWindowLocation(props.getProperty("settings.window.x", String.valueOf(rect.getX())),
                props.getProperty("settings.window.y", String.valueOf(rect.getY())),
                props.getProperty("settings.window.width", String.valueOf(rect.getWidth())),
                props.getProperty("settings.window.height", String.valueOf(rect.getHeight())));
    }

    public void setCustomASEditor(String property) {
        if (property != null) {
            customASEditor = property;
        }
    }

    public void setRefreshFreq(String property) {
        setRefreshFreq(Long.parseLong(property));
    }

    public void setLogType(String property) {
        setLogType(Integer.parseInt(property));
    }

    public void setHighlightAll(boolean b) {
        isHightlightAll = b;
    }

    public void setWordWrap(boolean b) {
        isWordWrap = b;
    }

    public void setAutoRefresh(boolean b) {
        isAutoRefresh = b;
    }

    public void setFilter(boolean b) {
        isFilter = b;
    }

    public void setSearchKeywords(String[] keywords) {
        List<String> list = new ArrayList<String>();
        list.add("");
        for (String word : keywords) {
            if (word != null && !"".equals(word)) {
                list.add(word);
            }
        }
        this.searchKeywords = list.toArray(new String[0]);
        this.searchKeywordsModel = new DefaultComboBoxModel(getSearchKeywords());
    }

    public void setTraceFont(String name, String size) {
        traceFont = new Font(name, 0, Integer.parseInt(size));
    }

    public void setCurrentLogFile(String filen) {
        currentLogFile = filen;
    }

    public void setMaxNumLines(String lines) {
        setMaxNumLines(Long.valueOf(lines));
    }

    public void setMainWindowLocation(String x, String y, String w, String h) {
        window = new Rectangle(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(w), Integer.parseInt(h));
    }

    public void setLastUpdateDate(String last) {
        if (last != null) {
            Date newDate = new Date(Long.valueOf(last));
            if (lastUpdateDate == null || lastUpdateDate.before(newDate)) {
                setLastUpdateDate(lastUpdateDate);
            }
        }
    }

    public Font[] getFonts() {
        return fonts;
    }

    public void setFonts(Font[] fonts) {
        this.fonts = fonts;
    }

    public String getFlashLogFileName() {
        return flashLogFileName;
    }

    public void setFlashLogFileName(String flashLogFileName) {
        this.flashLogFileName = flashLogFileName;
    }

    public String getPolicyLogFileName() {
        return policyLogFileName;
    }

    public void setPolicyLogFileName(String policyLogFileName) {
        this.policyLogFileName = policyLogFileName;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public String getCustomASEditor() {
        return customASEditor;
    }

    public boolean isDefaultASEditor() {
        return isDefaultASEditor;
    }

    public void setDefaultASEditor(boolean isDefaultASEditor) {
        this.isDefaultASEditor = isDefaultASEditor;
    }

    public boolean isHighlightKeywords() {
        return highlightKeywords;
    }

    public void setHighlightKeywords(boolean highlightKeywords) {
        this.highlightKeywords = highlightKeywords;
    }

    public boolean isAutoRefresh() {
        return isAutoRefresh;
    }

    public void setIsAutoRefresh(boolean isAutoRefresh) {
        this.isAutoRefresh = isAutoRefresh;
    }

    public boolean isUTF() {
        return isUTF;
    }

    public void setUTF(boolean isUTF) {
        this.isUTF = isUTF;
    }

    public boolean isSmartTraceEnabled() {
        return isSmartTraceEnabled;
    }

    public void setSmartTraceEnabled(boolean isSmartTraceEnabled) {
        this.isSmartTraceEnabled = isSmartTraceEnabled;
    }

    public boolean isCheckUpdates() {
        return isCheckUpdates;
    }

    public void setCheckUpdates(boolean isCheckUpdates) {
        this.isCheckUpdates = isCheckUpdates;
    }

    public boolean isMaxNumLinesEnabled() {
        return maxNumLinesEnabled;
    }

    public void setMaxNumLinesEnabled(boolean maxNumLinesEnabled) {
        this.maxNumLinesEnabled = maxNumLinesEnabled;
    }

    public long getMaxNumLines() {
        return maxNumLines;
    }

    public void setMaxNumLines(long maxNumLines) {
        this.maxNumLines = maxNumLines;
    }

    public boolean isRestoreOnUpdate() {
        return restoreOnUpdate;
    }

    public void setRestoreOnUpdate(boolean restoreOnUpdate) {
        this.restoreOnUpdate = restoreOnUpdate;
    }

    public long getRefreshFreq() {
        return refreshFreq;
    }

    public void setRefreshFreq(long refreshFreq) {
        this.refreshFreq = refreshFreq;
    }

    public boolean isAlwaysOnTop() {
        return isAlwaysONTop;
    }

    public void setAlwaysONTop(boolean isAlwaysONTop) {
        this.isAlwaysONTop = isAlwaysONTop;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public boolean isDetectPlayer() {
        return detectPlayer;
    }

    public void setDetectPlayer(boolean detectPlayer) {
        this.detectPlayer = detectPlayer;
    }

    public String getDefaultFont() {
        return defaultFont;
    }

    public void setDefaultFont(String defaultFont) {
        this.defaultFont = defaultFont;
    }

    public boolean isHightlightAll() {
        return isHightlightAll;
    }

    public boolean isWordWrap() {
        return isWordWrap;
    }

    public boolean isFilter() {
        return isFilter;
    }

    public String[] getSearchKeywords() {
        return searchKeywords;
    }

    public Font getTraceFont() {
        return traceFont;
    }

    public String getCurrentLogFile() {
        return currentLogFile;
    }

    public DefaultComboBoxModel getSearchKeywordsModel() {
        return searchKeywordsModel;
    }

    public void setSearchKeywordsModel(DefaultComboBoxModel searchKeywordsModel) {
        this.searchKeywordsModel = searchKeywordsModel;
    }

    public Rectangle getMainWindowLocation() {
        return window;
    }

    public void setFontNames(String[] fontNames) {
        this.fontNames = fontNames;
    }

    public String[] getFontNames() {
        return fontNames;
    }

}
