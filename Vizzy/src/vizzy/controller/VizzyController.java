/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vizzy.controller;

import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Timer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import org.apache.log4j.Logger;
import vizzy.forms.VizzyForm;
import vizzy.forms.panels.AboutPanel;
import vizzy.forms.panels.OptionsForm;
import vizzy.forms.panels.SnapshotForm;
import vizzy.listeners.ILogFileListener;
import vizzy.listeners.IUpdateCheckListener;
import vizzy.listeners.IVizzyView;
import vizzy.model.Conf;
import vizzy.model.FlashPlayerFiles;
import vizzy.model.SettingsModel;
import vizzy.model.SourceAndLine;
import vizzy.tasks.CheckUpdates;
import vizzy.tasks.DebugPlayerDetector;
import vizzy.tasks.DeleteFile;
import vizzy.tasks.FlashPlayerFilesLocator;
import vizzy.tasks.FontsInitializer;
import vizzy.tasks.HandleWordAtPosition;
import vizzy.tasks.HideCodePopupTimerTask;
import vizzy.tasks.KeywordsHighlighter;
import vizzy.tasks.LoadFileTask;
import vizzy.tasks.MMCFGInitializer;
import vizzy.tasks.ShowCodePopupTask;
import vizzy.tasks.ShowCodePopupTimerTask;
import vizzy.tasks.WordSearcher;
import vizzy.util.OSXAdapter;
import vizzy.util.PathUtils;
import vizzy.util.TextTransfer;

/**
 *
 * @author sergeil
 */
public final class VizzyController implements ILogFileListener {

    private static final Logger log = Logger.getLogger(VizzyController.class.getName());
    private static IVizzyView view;
    private static VizzyController controller;

    private SettingsModel settings;
    private MMCFGInitializer mmcfgInitializer;
    private CheckUpdates checkUpdatesThread;
    private Timer showCodePopupTimer;
    private Timer hideCodePopupTimer;
    private Properties props;
    private Timer readFileTimer;
    private OptionsForm optionsForm;
    private DebugPlayerDetector debugPlayerDetector;
    private AboutPanel aboutForm;

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    VizzyController.controller = new VizzyController();
                } catch (Exception ex) {
                    log.error("main()", ex);
                }
            }
        });
    }

    public VizzyController() {
        super();
        start();
    }

    private void start() {
        settings = new SettingsModel();
        view = new VizzyForm(this, settings);
        settings.setListener(view);
        settings.setUIActionsAvailable(false);
        initUIManager();
        settings.onInit();
        init();
    }

    private void init() {
        preInit();
        initSystemFonts();
        initFlashLog();
        initMMCFG();
        loadProperties();
        initVars();
        initSettings(view.getBounds());
        initCurrentLogTimer();
        initPlayerDetection();
        initCheckUpdates();
        settings.setUIActionsAvailable(true);
        settings.onAfterInit();
    }

    private void initUIManager() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            log.error("initUIManager()", ex);
        }
    }

    /**
     * Inites UI before view components are created
     * and packed
     */
    private void preInit() {
        if (Conf.OS_MAC_OS_X.equals(Conf.OSName)) {
            try {
                OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("onClose", (Class[]) null));
            } catch (Exception ex) {
                log.warn("setQuitHandler()", ex);
            }
        }


        String rootDir = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File dir = PathUtils.getDir(new File(rootDir));
        Conf.vizzyRootDir = dir.getAbsolutePath();


        if (Conf.OSName.indexOf(Conf.OS_WINDOWS) > -1) {
            settings.setCustomASEditor(Conf.DEFAULT_WINDOWS_EDITOR_PATH, true);
        } else if (Conf.OSName.indexOf(Conf.OS_MAC_OS_X) > -1) {
            settings.setCustomASEditor(Conf.DEFAULT_MAC_EDITOR_PATH, true);
        } else if (Conf.OSName.indexOf(Conf.OS_LINUX) > -1) {
            settings.setCustomASEditor(Conf.DEFAULT_LINUX_EDITOR_PATH, true);
        }

        ToolTipManager.sharedInstance().setInitialDelay(0);

        try {
            URL myIconUrl = this.getClass().getResource("/img/vizzy.png");
            settings.setAppIcon(new ImageIcon(myIconUrl, "Vizzy Flash Tracer").getImage());
            view.setIconImage(settings.getAppIcon());
        } catch (Exception e) {
//            log.warn("setAppIcon()", e);
        }
    }

    /**
     * Loads system fonts found on machine
     */
    private void initSystemFonts() {
        FontsInitializer f = new FontsInitializer();
        f.loadSystemFonts();
        settings.setFonts(f.getFonts(), false);
        settings.setFontNames(f.getFontNames(), false);
    }


    /**
     * Get flashlog.txt file location depending on user's
     * operation system
     */
    private void initFlashLog() {
        FlashPlayerFiles fpf = FlashPlayerFilesLocator.findFilesPaths();
        if (fpf.getLogPath() != null) {
            settings.setFlashLogFileName(fpf.getLogPath(), false);
        }
    }

    /**
     * Create mm.cfg file if necessary or
     * get flashlog.txt file location if it's written
     * in mm.cfg
     */
    private void initMMCFG() {
        mmcfgInitializer = new MMCFGInitializer();
        mmcfgInitializer.init();
        settings.setMmcfgKeys(mmcfgInitializer.getMmcfgKeys());
        settings.setPolicyFileRecorded(mmcfgInitializer.isPolicyFileRecorded());
        if (mmcfgInitializer.isMmcfgCreated()) {
            JOptionPane.showMessageDialog(null, "Vizzy has created mm.cfg file for you.\n" +
                    "Please restart all your browsers for the\n"
                    + "changes to take effect.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
        if (mmcfgInitializer.getTraceFileLocation() != null) {
             settings.setFlashLogFileName(mmcfgInitializer.getTraceFileLocation(), false);
        }
        
    }

    /**
     * Loads settings file
     */
    private void loadProperties() {
        settings.setSettingsFile(new File(Conf.vizzyRootDir, Conf.VIZZY_PROPERTIES_FILENAME));
        props = new Properties();
        try {
            props.load(new FileInputStream(settings.getSettingsFile()));
        } catch (Exception ex) {
//            log.warn("loadProperties()", ex);
        }
    }

    private void initVars() {
        settings.setSearcher(new WordSearcher());
        settings.setKeywordsHighlighter(new KeywordsHighlighter());
        settings.setHandleWordAtPosition(new HandleWordAtPosition());
        settings.setCodePopupHandler(new ShowCodePopupTask());

        settings.getSearcher().setTextArea(view.getTextArea());
        settings.getSearcher().setHighlightPanel(view.getHighLightScroll());
        settings.getKeywordsHighlighter().setTextArea(view.getTextArea());
        settings.getHandleWordAtPosition().setTextArea(view.getTextArea());
        settings.getCodePopupHandler().setOwner(view.getTextArea());
        
        String defaultFont = "Courier New";
        Font[] fonts = settings.getFonts();
        for (Font font : fonts) {
            if (font.getName().toLowerCase().indexOf("courier") > -1) {
                defaultFont = font.getName();
                break;
            }
        }
        settings.setDefaultFont(defaultFont, false);
    }

    private void initCheckUpdates() {
        if (settings.isCheckUpdates()) {
            Date nowDate = new Date();
            long compareResult = nowDate.getTime() - settings.getLastUpdateDate().getTime();
            if (compareResult > 7 * 24 * 60 * 60 * 1000) {
                settings.setLastUpdateDate(new Date(), false);
                checkUpdatesThread = new CheckUpdates(false, new IUpdateCheckListener() {
                    public void offerUpdate() {
                        settings.setAlwaysOnTopUI(false, true);
                    }
                    public void updateFinished() {
                        settings.setAlwaysOnTopUI(settings.isAlwaysOnTop(), true);
                    }
                });
                checkUpdatesThread.start();
            }
        }
    }

    private void initCurrentLogTimer() {
        createReadLogTimerTask().run();
        if (settings.isAutoRefresh()) {
            startReadLogFileTimer();
        } else {
            stopReadLogFileTimer();
        }
    }

    private LoadFileTask createReadLogTimerTask() {
        return new LoadFileTask(settings, this);
    }

    public void startReadLogFileTimer() {
        stopReadLogFileTimer();
        readFileTimer = new Timer();
        readFileTimer.schedule(createReadLogTimerTask(), 1000, settings.getRefreshFreq());
    }

    public void stopReadLogFileTimer() {
        if (readFileTimer != null) {
            readFileTimer.cancel();
            readFileTimer = null;
        }
    }
    
    private void initSettings(Rectangle rect) {
        settings.setLastUpdateDate(props.getProperty("update.last"), true);
        settings.setDetectPlayer(props.getProperty("settings.detectplayer", "true").equals("true"), true);
        settings.setCheckUpdates(props.getProperty("settings.autoupdates", "true").equals("true"), true);
        settings.setFlashLogFileName(props.getProperty("settings.filename", settings.getFlashLogFileName()), true);
        settings.setRefreshFreq(props.getProperty("settings.refreshFreq", "500"), true);
        settings.setUTF(props.getProperty("settings.isUTF", "true").equals("true"), true);
        settings.setMaxNumLinesEnabled(props.getProperty("settings.maxNumLinesEnabled", "false").equals("true"), true);
        settings.setMaxNumLines(props.getProperty("settings.maxNumLines", "50000"), true);
        settings.setRestoreOnUpdate(props.getProperty("settings.restore", "false").equals("true"), true);
        settings.setAlwaysOnTop(props.getProperty("settings.alwaysontop", "false").equals("true"), true);
        settings.setHighlightAll(props.getProperty("settings.highlight_all", "true").equals("true"), true);
        settings.setAutoRefresh(props.getProperty("settings.autorefresh", "true").equals("true"), true);
        settings.setWordWrap(props.getProperty("settings.wordwrap", "true").equals("true"), true);
        settings.setHighlightStackTraceErrors(props.getProperty("settings.enableHighlightErrors", "true").equals("true"), true);
        settings.setEnableCodePopup(props.getProperty("settings.enableCodePopups", "true").equals("true"), true);
        settings.setEnableTraceClick(props.getProperty("settings.enableTraceClick", "true").equals("true"), true);
        settings.setCustomASEditor(props.getProperty("settings.customASEditor", null), true);
        settings.setDefaultASEditor(props.getProperty("settings.isDefaultASEditor", "true").equals("true"), true);
        settings.setNewFeaturesPanelShown(props.getProperty("settings.newFeaturesShown" + Conf.VERSION, "false").equals("true"), true);
        settings.setTraceFont(props.getProperty("settings.font.name", settings.getDefaultFont()), 
                props.getProperty("settings.font.size", "12"), true);
        settings.setSearchKeywords(props.getProperty("search.keywords", "").split("\\|\\|\\|"), true);
        settings.setFilter(false, true);
        setLogType(props.getProperty("settings.logtype", "0"), true);
        settings.setMainWindowLocation(props.getProperty("settings.window.x", String.valueOf(rect.getX())),
                props.getProperty("settings.window.y", String.valueOf(rect.getY())),
                props.getProperty("settings.window.width", String.valueOf(rect.getWidth())),
                props.getProperty("settings.window.height", String.valueOf(rect.getHeight())), true);


    }

    @Override
    public void onLogFileRead(String log) {
        int len = log.length();
        int max = len > 500 ? len - 500 : 0;
        String currentHash = len + "" + log.substring(max, len);

        if (currentHash.equals(settings.getRecentHash())) {
            return;
        }

        settings.setRecentHash(currentHash, false);
        settings.setTraceContent(log, true);

        if (settings.getSearcher().isWasSearching()) {
            startSearch(settings.getSearcher().getLastCaretPos() - 1, false);
        }

        highlightStackTraceErrors();
    }

    @Override
    public void onOutOfMemory() {
        settings.setMaxNumLinesEnabled(true, false);
        settings.setMaxNumLines(50000, false);

        startReadLogFileTimer();

        JOptionPane.showMessageDialog(null, "The log file is too big and Vizzy has\n"
                + "run out of memory. Vizzy has set the limit\n"
                + "of log file to 50KB. You can customize this\n"
                + "value in Options menu.", "Warning", JOptionPane.ERROR_MESSAGE);
    }

    private void highlightStackTraceErrors() {
        if (settings.isHighlightStackTraceErrors()) {
            boolean highlighted = settings.getKeywordsHighlighter().highlight();
            if (!settings.wasNewFeaturesPanelShown() && highlighted) {
                settings.showNewFeaturesPanel();
            }
        }
    }

    private void handleWordAtPosition() {
        if (settings.isEnableTraceClick()) {
            settings.getHandleWordAtPosition().findObjectAtPositionAndExecute();
        }
    }

    public void stopShowCodeTimer() {
        if (!settings.isEnableCodePopup()) {
            return;
        }
        if (showCodePopupTimer != null) {
            showCodePopupTimer.cancel();
            showCodePopupTimer = null;
        }
    }
    public void startShowCodePopupTimer(Point pt) {
        if (!settings.isEnableCodePopup()) {
            return;
        }
        stopShowCodeTimer();
        showCodePopupTimer = new Timer();
        showCodePopupTimer.schedule(new ShowCodePopupTimerTask(this, pt), 500, 500);
    }
    public void startHideCodePopupTimer() {
        if (!settings.isEnableCodePopup()) {
            return;
        }
        stopHideCodePopupTimer();
        hideCodePopupTimer = new Timer();
        hideCodePopupTimer.schedule(new HideCodePopupTimerTask(this), 500, 500);
    }
    public void stopHideCodePopupTimer() {
        if (!settings.isEnableCodePopup()) {
            return;
        }
        if (hideCodePopupTimer != null) {
            hideCodePopupTimer.cancel();
            hideCodePopupTimer = null;
        }
    }
    public void hideCodePopup() {
        if (!settings.isEnableCodePopup()) {
            return;
        }
        if (!settings.getCodePopupHandler().isVisible()) {
            return;
        }
        settings.getHandleWordAtPosition().removeHighlight();
        settings.getCodePopupHandler().hide();
    }
    public void onHideCodePopup() {
        if (!settings.isEnableCodePopup()) {
            return;
        }
        stopHideCodePopupTimer();
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        if (mouseLocation == null || !settings.getCodePopupHandler().isMouseAtCodePopup(mouseLocation)) {
            hideCodePopup();
            Point mousePosition = view.getTextArea().getMousePosition();
            if (mousePosition != null) {
                startShowCodePopupTimer(mousePosition);
            }
        }
    }
    public void onShowCodePopup(Point pt) {
        if (!settings.isEnableCodePopup()) {
            return;
        }
        stopShowCodeTimer();
        hideCodePopup();

        int offset = view.getTextArea().viewToModel(pt);
        SourceAndLine source = null;
        try {
            source = settings.getHandleWordAtPosition().checkSourceFile(offset, false);
        } catch (Exception ex) {
//            log.warn("checkSourceFile()", ex);
        }
        if (source == null) {
            return;
        }
        settings.getCodePopupHandler().show(pt, source);
    }













    private void setLogType(String property, boolean uiTrigger) {
        settings.setLogType(property, uiTrigger);
        if (settings.getLogType() == 1) {
            if (!settings.isPolicyFileRecorded()) {
                mmcfgInitializer.recordPolicyFile();
                settings.setPolicyFileRecorded(true);
                JOptionPane.showMessageDialog(null, "Vizzy has updated mm.cfg file to enable\n"
                        + "policy logging. Please restart all your browsers\n"
                        + "for the changes to take effect.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        if (settings.getLogType() == 0) {
            settings.setCurrentLogFile(settings.getFlashLogFileName(), false);
        } else {
            settings.setCurrentLogFile(settings.getPolicyLogFileName(), false);
        }
    }

    private void setHighlightAll(boolean selected, boolean uiTrigger) {
        settings.setHighlightAll(selected, uiTrigger);
    }

    private void setFilter(boolean selected, boolean uiTrigger) {
        settings.setFilter(selected, uiTrigger);
    }

    public void clearTraceClicked() {
        new DeleteFile(settings.getCurrentLogFile());
        settings.clearTrace(true);
    }

    public void textAreaMousePressed() {
        if (settings.isAutoRefresh()) {
            stopReadLogFileTimer();
        }
        stopShowCodeTimer();
        hideCodePopup();
        stopHideCodePopupTimer();
    }

    public void textAreaMouseReleased(String selection) {
        if (selection == null || "".equals(selection)) {
            handleWordAtPosition();
        }
        if (settings.isAutoRefresh()) {
            startReadLogFileTimer();
        }
    }

    public void clearSearchClicked() {
        settings.getSearcher().setWord("");
        settings.getSearcher().clearHighlights();
        settings.getSearcher().setWasSearching(false);
        highlightStackTraceErrors();
        settings.clearSearch();
    }

    public void setLogTypeClicked(String value) {
        setLogType(value, true);
        createReadLogTimerTask().run();
        if (settings.isAutoRefresh()) {
            startReadLogFileTimer();
        }
    }

    public void copyAllClicked(String text) {
        TextTransfer tt = new TextTransfer();
        tt.setClipboardContents(text);
    }

    public void openOptionsClicked() {
        settings.setAlwaysOnTopUI(false, true);
        if (optionsForm == null) {
            optionsForm = new OptionsForm(view.getBounds(), this, settings);
        }

        optionsForm.setVisible(true);
    }

    public void snapshotClicked(String text) {
        Point location = view.getLocation();
        SnapshotForm snapshotForm = new SnapshotForm(this, settings);
        snapshotForm.setLocation(location.x - snapshotForm.getWidth() - settings.getSnapshotForms().size() * 20,
                location.y  - settings.getSnapshotForms().size() * 20);
        snapshotForm.setSize(snapshotForm.getWidth(), view.getHeight());
        snapshotForm.init(text);

        settings.getSnapshotForms().add(snapshotForm);
    }

    public void wordWrapClicked(boolean selected) {
        settings.setWordWrap(selected, true);
        for (SnapshotForm snapshotForm : settings.getSnapshotForms()) {
            snapshotForm.setWordWrap(settings.isWordWrap());
        }

        if (settings.getSearcher().isWasSearching()) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    startSearch(settings.getSearcher().getLastCaretPos() - 1, false);
                }
            });
        }
    }

    public void aboutOpenClicked() {
        settings.setAlwaysOnTopUI(false, true);
        if (aboutForm == null) {
            aboutForm = new AboutPanel(view.getBounds(), this, settings);
        }

        aboutForm.setVisible(true);
    }

    public void textAreaMouseMoved(MouseEvent evt) {
        if (!settings.getCodePopupHandler().isVisible()) {
            startShowCodePopupTimer(new Point(evt.getX(), evt.getY()));
        } else if(hideCodePopupTimer == null) {
            startHideCodePopupTimer();
        }
    }

    public void textAreaMouseExited(MouseEvent evt) {
        stopShowCodeTimer();
    }

    public void formWindowDeactivated() {
        stopShowCodeTimer();
        stopHideCodePopupTimer();
        hideCodePopup();
    }

    public void onClose() {
        settings.setMainWindowLocation(view.getBounds(), false);
        props.setProperty("settings.detectplayer", String.valueOf(settings.isDetectPlayer()));
        props.setProperty("settings.autoupdates", String.valueOf(settings.isCheckUpdates()));
        props.setProperty("settings.refreshFreq", String.valueOf(settings.getRefreshFreq()));
        props.setProperty("settings.isUTF", String.valueOf(settings.isUTF()));
        props.setProperty("settings.autorefresh", String.valueOf(settings.isAutoRefresh()));
        props.setProperty("settings.alwaysontop", String.valueOf(settings.isAlwaysOnTop()));
        props.setProperty("settings.highlight_all", String.valueOf(settings.isHightlightAll()));
        props.setProperty("settings.wordwrap", String.valueOf(settings.isWordWrap()));
        props.setProperty("settings.font.name", settings.getTraceFont().getName());
        props.setProperty("settings.font.size", String.valueOf(settings.getTraceFont().getSize()));
        props.setProperty("settings.filename", settings.getFlashLogFileName());
        props.setProperty("settings.window.x", String.valueOf(settings.getMainWindowLocation().getX()));
        props.setProperty("settings.window.y", String.valueOf(settings.getMainWindowLocation().getY()));
        props.setProperty("settings.window.width", String.valueOf(settings.getMainWindowLocation().getWidth()));
        props.setProperty("settings.window.height", String.valueOf(settings.getMainWindowLocation().getHeight()));
        props.setProperty("settings.restore", String.valueOf(settings.isRestoreOnUpdate()));
        props.setProperty("settings.maxNumLines", String.valueOf(settings.getMaxNumLines()));
        props.setProperty("settings.maxNumLinesEnabled", String.valueOf(settings.isMaxNumLinesEnabled()));
        props.setProperty("settings.logtype", String.valueOf(settings.getLogType()));
        props.setProperty("settings.enableHighlightErrors", String.valueOf(settings.isHighlightStackTraceErrors()));
        props.setProperty("settings.enableCodePopups", String.valueOf(settings.isEnableCodePopup()));
        props.setProperty("settings.enableTraceClick", String.valueOf(settings.isEnableTraceClick()));
        props.setProperty("settings.customASEditor", String.valueOf(settings.getCustomASEditor()));
        props.setProperty("settings.isDefaultASEditor", String.valueOf(settings.isDefaultASEditor()));
        props.setProperty("settings.newFeaturesShown" + Conf.VERSION, String.valueOf(settings.wasNewFeaturesPanelShown()));
        props.setProperty("update.last", String.valueOf(settings.getLastUpdateDate().getTime()));

        StringBuilder keywords = new StringBuilder();
        DefaultComboBoxModel searchKeywordsModel = settings.getSearchKeywordsModel();
        for (int i = 0; i < searchKeywordsModel.getSize(); i++) {
            String keyword = (String) searchKeywordsModel.getElementAt(i);
            if (i != 0) {
                keywords.append("|||");
            }
            if (!"".equals(keyword)) {
                keywords.append(keyword);
            }
        }
        props.setProperty("search.keywords", keywords.toString());

        try {
            props.store(new FileOutputStream(settings.getSettingsFile()), "");
        } catch (FileNotFoundException ex) {
            log.error("error saving setting 1.");
        } catch (IOException ex) {
            log.error("error saving setting 2.");
        }
    }

    public void searchKeyReleased(String selectedItem, KeyEvent evt) {
        if (selectedItem == null) {
            return;
        }
        if (selectedItem.equals("")) {
            clearSearchClicked();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addSearchKeyword(selectedItem);
            settings.getSearcher().setWord(selectedItem);
            settings.getSearcher().setWasSearching(true);
            startSearch(true);
            highlightStackTraceErrors();
        } 
    }

    public void highlightAllClicked(boolean selected, Object selectedItem) {
        setHighlightAll(selected, true);
        if (selectedItem != null && !selectedItem.equals("")) {
            settings.getSearcher().setWasSearching(true);
            startSearch(true);
            highlightStackTraceErrors();
        }
    }

    public void filterClicked(boolean selected, Object selectedItem) {
        setFilter(selected, true);
        if (selectedItem != null && !selectedItem.equals("")) {
            settings.getSearcher().setWasSearching(true);
            startSearch(false);
            highlightStackTraceErrors();
        }
    }

    private void startSearch(boolean scrollToSearchResult) {
        startSearch(settings.getSearcher().getLastCaretPos(), scrollToSearchResult);
    }

    private void startSearch(int lastCarPos, boolean scrollToSearchResult) {
        String word = (String) settings.getSearcher().getWord();
        if (word == null) {
            return;
        }
        
        if (settings.getSearcher().isFilter()) {
            settings.search(word, -1, scrollToSearchResult);
        } else {
            int offset = settings.getSearcher().search(settings.getTraceContent(), lastCarPos);
            settings.search(word, offset, scrollToSearchResult);
            if (offset != -1) {
                if (scrollToSearchResult) {
                        settings.getSearcher().setLastCaretPos(offset + 1);
                }
            } else {
                settings.getSearcher().setLastCaretPos(0);
            }
        }
    }


    

    public void textAreaKeyPressed(String text, KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_F3
                && text != null
                && text.length() > 0) {
            addSearchKeyword(text);
            settings.getSearcher().setWord(text);
            settings.getSearcher().setWasSearching(true);
            settings.highlightTraceKeyword(text);
            startSearch(true);
            highlightStackTraceErrors();
        }
    }

    public void autoRefreshClicked(boolean selected) {
        settings.setAutoRefresh(selected, true);
        if (settings.isAutoRefresh()) {
            createReadLogTimerTask().run();
            startReadLogFileTimer();
        } else {
            stopReadLogFileTimer();
        }
    }

    private void addSearchKeyword(String selectedItem) {
        DefaultComboBoxModel searchKeywordsModel = settings.getSearchKeywordsModel();
        if (searchKeywordsModel.getIndexOf(selectedItem) == -1) {
            searchKeywordsModel.insertElementAt(selectedItem, 0);
            if (searchKeywordsModel.getSize() > 7) {
                searchKeywordsModel.removeElementAt(7);
            }
        }
    }

    public void alwaysOnTopClicked(boolean selected) {
        settings.setAlwaysOnTop(selected, true);
    }

    public void aboutOKClick() {
        aboutForm.setVisible(false);
        settings.setAlwaysOnTop(settings.isAlwaysOnTop(), true);
    }

    public void snapshotFormsClose(SnapshotForm frame) {
        settings.getSnapshotForms().remove(frame);
    }

    public void optionsCancelled() {
        optionsForm.setVisible(false);
        settings.setAlwaysOnTopUI(settings.isAlwaysOnTop(), true);
    }

    public void optionsOK(SettingsModel s, HashMap<String, String> mmCFG) {
        settings.setUIActionsAvailable(false);
        settings.setTraceFont(s.getTraceFont(), true);

        settings.setFlashLogFileName(s.getFlashLogFileName(), true);
        if (settings.getLogType() == 0) {
            settings.setCurrentLogFile(s.getFlashLogFileName(), true);
        } else if (settings.getLogType() == 1) {
            settings.setCurrentLogFile(settings.getPolicyLogFileName(), true);
        }
        settings.setCheckUpdates(s.isCheckUpdates(), true);
        settings.setMaxNumLinesEnabled(s.isMaxNumLinesEnabled(), true);
        settings.setMaxNumLines(s.getMaxNumLines(), true);
        settings.setUTF(s.isUTF(), true);
        settings.setRefreshFreq(s.getRefreshFreq(), true);
        settings.setRestoreOnUpdate(s.isRestoreOnUpdate(), true);
        settings.setHighlightStackTraceErrors(s.isHighlightStackTraceErrors(), true);
        settings.setCustomASEditor(s.getCustomASEditor(), true);
        settings.setDefaultASEditor(s.isDefaultASEditor(), true);
        settings.setEnableCodePopup(s.isEnableCodePopup(), true);
        settings.setEnableTraceClick(s.isEnableTraceClick(), true);

        mmcfgInitializer.saveKeys(mmCFG);

        settings.setRecentHash(null, true);
        createReadLogTimerTask().run();
        if (settings.isAutoRefresh()) {
            startReadLogFileTimer();
        } else {
            stopReadLogFileTimer();
        }

        optionsForm.setVisible(false);
        settings.setAlwaysOnTopUI(settings.isAlwaysOnTop(), true);

        settings.setUIActionsAvailable(true);
        settings.optionsClosed();
    }

    public void checkForUpdatesClicked() {
        settings.setLastUpdateDate(new Date(), false);
        checkUpdatesThread = new CheckUpdates(true, new IUpdateCheckListener() {
            public void offerUpdate() {
            }
            public void updateFinished() {
            }
        });
        checkUpdatesThread.start();
    }

    private void initPlayerDetection() {
        if (settings.isDetectPlayer()) {
            settings.setDetectPlayer(false, false);
            debugPlayerDetector = new DebugPlayerDetector();
            debugPlayerDetector.offerDetection();
        }
    }

    public void traceAreaMouseWheel(MouseWheelEvent evt) {
        hideCodePopup();
    }

    public void newFeaturesPanelClosed() {
        settings.setNewFeaturesPanelShown(true, true);
    }

    public void searchComboboxChanged(String text) {
        if (text != null
                && text.length() > 0) {
            addSearchKeyword(text);
            settings.getSearcher().setWord(text);
            settings.getSearcher().setWasSearching(true);
            startSearch(true);
            highlightStackTraceErrors();
        }
    }

}
