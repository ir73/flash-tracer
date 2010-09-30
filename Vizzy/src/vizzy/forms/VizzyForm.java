/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * VizzyForm.java
 *
 * Created on 23.04.2009, 10:16:16
 */

package vizzy.forms;

import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import vizzy.comp.JScrollHighlightPanel;
import vizzy.forms.panels.AboutPanel;
import vizzy.forms.panels.OptionsForm;
import vizzy.forms.panels.SnapshotForm;
import vizzy.listeners.ICodePopupListener;
import vizzy.model.SettingsModel;
import vizzy.tasks.CheckUpdates;
import vizzy.tasks.CheckUpdatesAndDebugThread;
import vizzy.tasks.DebugPlayerDetector;
import vizzy.tasks.DeleteFile;
import vizzy.tasks.FlashPlayerFilesLocator;
import vizzy.tasks.HandleWordAtPosition;
import vizzy.tasks.HideCodePopupTimerTask;
import vizzy.tasks.KeywordsHighlighter;
import vizzy.tasks.LoadFileTask;
import vizzy.tasks.MMCFGInitializer;
import vizzy.tasks.ShowCodePopupTask;
import vizzy.tasks.ShowCodePopupTimerTask;
import vizzy.tasks.WordSearcher;
import vizzy.model.FlashPlayerFiles;
import vizzy.util.OSXAdapter;
import vizzy.model.SourceAndLine;
import vizzy.tasks.FontsInitializer;
import vizzy.util.TextTransfer;

/**
 *
 * @author sergeil
 */
public class VizzyForm extends javax.swing.JFrame {

    private boolean needToScrolldown = true;
    private boolean isCapturingScroll = false;
    private Timer showCodePopupTimer;
    private Timer readFileTimer;
    private Timer hideCodePopupTimer;
    private KeywordsHighlighter keywordsHighlighter;
    private HandleWordAtPosition handleWordAtPosition;
    private ShowCodePopupTask codePopupHandler;
    private WordSearcher searcher;
    private JScrollBar vbar;
    private String traceContent;
    private Properties props;
    private File settingsFile = new File("tracer.properties");
    private OptionsForm optionsForm;
    public SettingsModel settings;

    public MMCFGInitializer mmcfgInitializer;
    public ArrayList<SnapshotForm> snapshotForms = new ArrayList<SnapshotForm>();
    public String recentHash;

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    VizzyForm vizzyForm = new VizzyForm();
                } catch (Exception ex) {
                    Logger.getLogger(VizzyForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    /** Creates new form VizzyForm */
    public VizzyForm() {
        super();
        initUIManager();
        initComponents();
        init();
    }

    private void initUIManager() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            Logger.getLogger(VizzyForm.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    private void init() {
        preInit();
        initSystemFonts();
        initFlashLog();
        initMMCFG();
        loadProperties();
        initVars();
        initSettings();
        initListeners();
        initThreads();

        setVisible(true);
    }

    public void checkUpdates(boolean reportIfOk) {
        settings.setLastUpdateDate(new Date());
        CheckUpdates cu = new CheckUpdates(reportIfOk);
        cu.start();
    }

    private void preInit() {
        settings = SettingsModel.getInstance();
        if (SettingsModel.OS_MAC_OS_X.equals(SettingsModel.OSName)) {
            try {
                OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("onClose", (Class[]) null));
            } catch (Exception ex) {
                Logger.getLogger(VizzyForm.class.getName()).log(Level.WARNING, null, ex);
            }
        }
    }

    /**
     * Loads system fonts found on machine
     */
    private void initSystemFonts() {
        FontsInitializer f = new FontsInitializer();
        f.loadSystemFonts();
        settings.setFonts(f.getFonts());
        settings.setFontNames(f.getFontNames());
    }

    /**
     * Get flashlog.txt file location depending on user's
     * operation system
     */
    private void initFlashLog() {
        FlashPlayerFiles fpf = FlashPlayerFilesLocator.findFilesPaths();
        if (fpf.getLogPath() != null) {
            setFlashLogFile(fpf.getLogPath());
        }
        if (fpf.getPolicyPath() != null) {
            setPolicyLogFile(fpf.getPolicyPath());
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
        if (mmcfgInitializer.isMmcfgCreated()) {
            JOptionPane.showMessageDialog(null, "Vizzy has created mm.cfg file for you. " +
                    "To see flash trace output, please restart all your open browsers.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
        if (mmcfgInitializer.getTraceFileLocation() != null) {
            setFlashLogFile(mmcfgInitializer.getTraceFileLocation());
        }
    }

    private void initListeners() {
        vbar.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }
            public void mousePressed(MouseEvent e) {
                isCapturingScroll = true;
            }
            public void mouseReleased(MouseEvent e) {
                isCapturingScroll = false;
            }
            public void mouseEntered(MouseEvent e) {
            }
            public void mouseExited(MouseEvent e) {
            }
        });

        vbar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (isCapturingScroll) {
                    adjustScroller();
                }
            }
        });
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

    private void initThreads() {
        CheckUpdatesAndDebugThread cut = new CheckUpdatesAndDebugThread(this);
        cut.start();
    }

    private void initSettings() {
        settings.parseProperties(props, getBounds());
        setLastUpdateDate(props.getProperty("update.last"));
        setDetectPlayer(props.getProperty("settings.detectplayer", "true").equals("true"));
        setCheckUpdates(props.getProperty("settings.autoupdates", "true").equals("true"));
        setFlashLogFile(props.getProperty("settings.filename", settings.getFlashLogFileName()));
        setRefreshFreq(props.getProperty("settings.refreshFreq", "500"));
        setUTF(props.getProperty("settings.isUTF", "true").equals("true"));
        setMaxNumLinesEnabled(props.getProperty("settings.maxNumLinesEnabled", "false").equals("true"));
        setMaxNumLines(props.getProperty("settings.maxNumLines", "50000"));
        setRestoreOnUpdate(props.getProperty("settings.restore", "false").equals("true"));
        setOnTop(props.getProperty("settings.alwaysontop", "false").equals("true"));
        setHighlightAll(props.getProperty("settings.highlight_all", "true").equals("true"));
        setAutoRefresh(props.getProperty("settings.autorefresh", "true").equals("true"));
        setWordWrap(props.getProperty("settings.wordwrap", "true").equals("true"));
        setEnableSmartTrace(props.getProperty("settings.enabelSmartTrace", "true").equals("true"));
        setHighlightKeywords(props.getProperty("settings.highlightKeywords", "true").equals("true"));
        setCustomASEditor(props.getProperty("settings.customASEditor", null));
        setDefaultEditorUsed(props.getProperty("settings.isDefaultASEditor", "true").equals("true"));
        setTraceFont(props.getProperty("settings.font.name", settings.getDefaultFont()), props.getProperty("settings.font.size", "12"));
        setSearchKeywords(props.getProperty("search.keywords", "").split("\\|\\|\\|"));
        setFiltering(false);
        setLogType(props.getProperty("settings.logtype", "0"));
        setDialogLocation(props.getProperty("settings.window.x", String.valueOf(this.getX())),
                props.getProperty("settings.window.y", String.valueOf(this.getY())),
                props.getProperty("settings.window.width", String.valueOf(this.getWidth())),
                props.getProperty("settings.window.height", String.valueOf(this.getHeight())));

        createLoadTimerTask().run();
        if (settings.isAutoRefresh()) {
            startReadLogFileTimer();
        } else {
            stopReadLogFileTimer();
        }
    }

    /**
     * Checks that the user has debug flash player installed
     * Check is only valid for Windows OS and for
     * IE browser only
     */
    public void checkDebugPlayers() {
        if (settings.isDetectPlayer()) {
            DebugPlayerDetector d = new DebugPlayerDetector();
            d.start();
        }
        settings.setDetectPlayer(false);
    }

    private void initVars() {
        try {
            URL myIconUrl = this.getClass().getResource("/img/vizzy.png");
            this.setIconImage(new ImageIcon(myIconUrl, "Vizzy Flash Tracer").getImage());
        } catch (Exception e) {
        }

        JScrollHighlightPanel scrollPanel = (JScrollHighlightPanel)jScrollHighlight;
        this.vbar = jScrollPane1.getVerticalScrollBar();
        this.searcher = new WordSearcher(jTraceTextArea, scrollPanel);
        this.keywordsHighlighter = new KeywordsHighlighter(jTraceTextArea);
        this.handleWordAtPosition = new HandleWordAtPosition(jTraceTextArea);
        this.codePopupHandler = new ShowCodePopupTask(jTraceTextArea, new ICodePopupListener() {
            public void mouseExited(MouseEvent e) {
//                startHideCodeTimer();
            }
            public void mouseEntered(MouseEvent e) {
//                stopHideCodeTimer();
            }
        });

        scrollPanel.setTa(jTraceTextArea);
        scrollPanel.setLocation(jTraceTextArea.getX(), jTraceTextArea.getY());
        scrollPanel.setSize(scrollPanel.getWidth(), jTraceTextArea.getHeight()/2);

        String defaultFont = "Courier New";
        Font[] fonts = settings.getFonts();
        for (Font font : fonts) {
            if (font.getName().toLowerCase().indexOf("courier") > -1) {
                defaultFont = font.getName();
                break;
            }
        }
        settings.setDefaultFont(defaultFont);
    }

    public void onClose() {
        saveSetting("settings.detectplayer", String.valueOf(settings.isDetectPlayer()));
        saveSetting("settings.autoupdates", String.valueOf(settings.isCheckUpdates()));
        saveSetting("settings.refreshFreq", String.valueOf(settings.getRefreshFreq()));
        saveSetting("settings.isUTF", String.valueOf(settings.isUTF()));
        saveSetting("settings.autorefresh", String.valueOf(settings.isAutoRefresh()));
        saveSetting("settings.alwaysontop", String.valueOf(jOnTopCheckbox.isSelected()));
        saveSetting("settings.highlight_all", String.valueOf(jHighlightAllCheckbox.isSelected()));
        saveSetting("settings.wordwrap", String.valueOf(jWordWrapCheckbox.isSelected()));
        saveSetting("settings.font.name", jTraceTextArea.getFont().getName());
        saveSetting("settings.font.size", String.valueOf(jTraceTextArea.getFont().getSize()));
        saveSetting("settings.filename", settings.getFlashLogFileName());
        saveSetting("settings.window.x", String.valueOf(getX()));
        saveSetting("settings.window.y", String.valueOf(getY()));
        saveSetting("settings.window.width", String.valueOf(getWidth()));
        saveSetting("settings.window.height", String.valueOf(getHeight()));
        saveSetting("settings.restore", String.valueOf(settings.isRestoreOnUpdate()));
        saveSetting("settings.maxNumLines", String.valueOf(settings.getMaxNumLines()));
        saveSetting("settings.maxNumLinesEnabled", String.valueOf(settings.isMaxNumLinesEnabled()));
        saveSetting("settings.logtype", String.valueOf(settings.getLogType()));
        saveSetting("settings.highlightKeywords", String.valueOf(settings.isHighlightKeywords()));
        saveSetting("settings.enabelSmartTrace", String.valueOf(settings.isSmartTraceEnabled()));
        saveSetting("settings.customASEditor", String.valueOf(settings.getCustomASEditor()));
        saveSetting("settings.isDefaultASEditor", String.valueOf(settings.isDefaultASEditor()));
        saveSetting("update.last", String.valueOf(settings.getLastUpdateDate().getTime()));

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
        saveSetting("search.keywords", keywords.toString());

        try {
            props.store(new FileOutputStream(settingsFile), "");
        } catch (FileNotFoundException ex) {
            System.err.println("error saving setting 1.");
        } catch (IOException ex) {
            System.err.println("error saving setting 2.");
        }
    }

    private void loadProperties() {
        props = new Properties();
        try {
            props.load(new FileInputStream(new File("tracer.properties")));
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    private void saveSetting(String key, String val) {
        props.setProperty(key, val);
    }

    private void adjustScroller() {
        int sum = vbar.getValue() + vbar.getVisibleAmount();
        int am = vbar.getMaximum();
        if (sum >= am) {
            needToScrolldown = true;
        } else {
            needToScrolldown = false;
        }
    }

    public LoadFileTask createLoadTimerTask() {
        return new LoadFileTask(settings.getCurrentLogFile(), settings.getMaxNumLines(), settings.isMaxNumLinesEnabled(), settings.isUTF(), this);
    }

    public void onOutOfMemory() {
        setMaxNumLinesEnabled(true);
        setMaxNumLines("50000");

        startReadLogFileTimer();

        JOptionPane.showMessageDialog(null, "The log file is too big and Vizzy has run out of memory." +
                " The limit for loading log has been set to " +
                "50KB. Please change this value if needed in Options panel.", "Warning", JOptionPane.ERROR_MESSAGE);
    }

    public void onFileRead(String log) {
        int len = log.length();
        int max = len > 500 ? len - 500 : 0;
        String currentHash = len + "" + log.substring(max, len);

        if (currentHash.equals(recentHash)) {
            return;
        }

        recentHash = currentHash;
        traceContent = log;
        
        boolean isSetTxt = !(searcher.isWasSearching() && searcher.isFilter());
        if (isSetTxt) {
            jTraceTextArea.setText(traceContent);
        }

        if (searcher.isWasSearching()) {
            startSearch(searcher.getLastCaretPos() - 1, false);
        }

        if (needToScrolldown) {
            jTraceTextArea.setCaretPosition(jTraceTextArea.getDocument().getLength());
        }

        highlightKeywords();

        if (settings.isRestoreOnUpdate()) {
            setExtendedState(JFrame.NORMAL);
            repaint();
        }
    }

    private void highlightKeywords() {
        if (settings.isSmartTraceEnabled() && settings.isHighlightKeywords()) {
            keywordsHighlighter.highlight();
        }
    }

    private void handleWordAtPosition() {
        if (settings.isSmartTraceEnabled()) {
            handleWordAtPosition.handle();
        }
    }

    private void startSearch(boolean scrollToSearchResult) {
        startSearch(searcher.getLastCaretPos(), scrollToSearchResult);
    }

//    private void startSearch() {
//        startSearch(true);
//    }

    private void startSearch(int lastCarPos, boolean scrollToSearchResult) {
        if (jSearchComboBox.getSelectedItem() == null) {
            return;
        }
        String word = (String) jSearchComboBox.getSelectedItem();
        if (searcher.isFilter()) {
            try {
                jTraceTextArea.setText(searcher.filter(traceContent, word));
            } catch (Exception ex) {
            }
            jSearchWarnLabel.setVisible(false);
        } else {
            int offset = searcher.search(traceContent, word, lastCarPos);
            if (offset != -1) {
                jSearchWarnLabel.setVisible(true);
                jSearchWarnLabel.setText("<html>Result: <font color=\"blue\"><b>" + word + "</b></font> found!</html>");
                if (scrollToSearchResult) {
                    needToScrolldown = false;
                    try {
                        jTraceTextArea.scrollRectToVisible(jTraceTextArea
                                .modelToView(offset));
                        searcher.setLastCaretPos(offset + 1);
                    } catch (BadLocationException e) {
                    }
                }

            } else {
                searcher.setLastCaretPos(0);
                jSearchWarnLabel.setVisible(true);
                jSearchWarnLabel.setText("<html>Result: <font color=\"red\"><b>" + word + "</b></font> not found!</html>");
            }
        }

    }

    public void stopShowCodeTimer() {
        if (showCodePopupTimer != null) {
            showCodePopupTimer.cancel();
            showCodePopupTimer = null;
        }
    }
    public void startShowCodeTimer(MouseEvent me) {
        stopShowCodeTimer();
        showCodePopupTimer = new Timer();
        showCodePopupTimer.schedule(new ShowCodePopupTimerTask(this, me), 500, 500);
    }
    public void startHideCodeTimer() {
        stopHideCodeTimer();
        hideCodePopupTimer = new Timer();
        hideCodePopupTimer.schedule(new HideCodePopupTimerTask(this), 500, 500);
    }
    public void stopHideCodeTimer() {
        if (hideCodePopupTimer != null) {
            hideCodePopupTimer.cancel();
            hideCodePopupTimer = null;
        }
    }
    public void hideCodePopup() {
        if (!codePopupHandler.isVisible()) {
            return;
        }
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        if (!codePopupHandler.isMouseAtCodePopup(mouseLocation)) {
            codePopupHandler.hide();
        }
    }
    public void onHideCodePopup() {
        stopHideCodeTimer();
        hideCodePopup();
    }
    public void onShowCodePopup(MouseEvent me) {
        stopShowCodeTimer();
        hideCodePopup();

        Point pt = new Point(me.getX(), me.getY());
        int offset = jTraceTextArea.viewToModel(pt);
        SourceAndLine source = handleWordAtPosition.tryPositionForSourceFile(offset);
        if (source == null) {
            return;
        }
        codePopupHandler.show(pt, source);
    }

    public void startReadLogFileTimer() {
        stopReadLogFileTimer();
        readFileTimer = new Timer();
        readFileTimer.schedule(createLoadTimerTask(), 1000, settings.getRefreshFreq());
    }

    public void stopReadLogFileTimer() {
        if (readFileTimer != null) {
            readFileTimer.cancel();
            readFileTimer = null;
        }
    }

    public void setEnableSmartTrace(boolean isSmartTraceEnabled) {
        settings.setSmartTraceEnabled(isSmartTraceEnabled);
    }

    public void setCustomASEditor(String property) {
        settings.setCustomASEditor(property);
        handleWordAtPosition.setCustomASEditor(settings.getCustomASEditor());
    }

    public void setDefaultEditorUsed(boolean isDefaultASEditor) {
        settings.setDefaultASEditor(isDefaultASEditor);
        handleWordAtPosition.setDefaultEditorUsed(settings.isDefaultASEditor());
    }

    public void setCheckUpdates(boolean isCheckUpdates) {
        settings.setCheckUpdates(isCheckUpdates);
    }

    public void setMaxNumLinesEnabled(boolean maxNumLinesEnabled) {
        settings.setMaxNumLinesEnabled(maxNumLinesEnabled);
    }

    private void setDetectPlayer(boolean detectPlayer) {
        settings.setDetectPlayer(detectPlayer);
    }

    public void setRefreshFreq(String property) {
        settings.setRefreshFreq(property);
    }

    public void setUTF(boolean isUTF) {
        settings.setUTF(isUTF);
    }

    private void setOnTop(boolean isAlwaysONTop) {
        settings.setAlwaysONTop(isAlwaysONTop);
        jOnTopCheckbox.setSelected(settings.isAlwaysOnTop());
        setAlwaysOnTop(settings.isAlwaysOnTop());
    }

    private void setLogType(String property) {
        settings.setLogType(property);
        if (settings.getLogType() == 1) {
            if (!mmcfgInitializer.isPolicyFileRecorded()) {
                mmcfgInitializer.recordPolicyFile();
                JOptionPane.showMessageDialog(null, "Vizzy has updated mm.cfg file to enable policy logging. " +
                        "Please restart all your browsers for changes to take effect.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        logTypeCombo.setSelectedIndex(settings.getLogType());
        if (settings.getLogType() == 0) {
            setCurrentLogFile(settings.getFlashLogFileName());
        } else {
            setCurrentLogFile(settings.getPolicyLogFileName());
        }
    }

    public void setHighlightAll(boolean b) {
        settings.setHighlightAll(b);
        jHighlightAllCheckbox.setSelected(settings.isHightlightAll());
        searcher.setHighlightAll(settings.isHightlightAll());
    }

    public void setWordWrap(boolean b) {
        settings.setWordWrap(b);
        jWordWrapCheckbox.setSelected(settings.isWordWrap());
        jTraceTextArea.setLineWrap(settings.isWordWrap());
        for (SnapshotForm snapshotForm : snapshotForms) {
            snapshotForm.setWordWrap(settings.isWordWrap());
        }
    }

    public void setAutoRefresh(boolean b) {
        settings.setAutoRefresh(b);
        jAutorefreshCheckBox.setSelected(settings.isAutoRefresh());
    }

    public void setFiltering(boolean b) {
        settings.setFilter(b);
        jFilterCheckbox.setSelected(settings.isFilter());
        jMultipleLabel.setVisible(settings.isFilter());
        searcher.setIsFilter(settings.isFilter());
        jHighlightAllCheckbox.setEnabled(!settings.isFilter());
    }

    public void setSearchKeywords(String[] keywords) {
        settings.setSearchKeywords(keywords);
        jSearchComboBox.setModel(settings.getSearchKeywordsModel());
    }

    public void setTraceFont(String name, String size) {
        settings.setTraceFont(name, size);
        jTraceTextArea.setFont(settings.getTraceFont());
    }

    public void setFlashLogFile(String flashLogFileName) {
        settings.setFlashLogFileName(flashLogFileName);
    }

    public void setPolicyLogFile(String policyLogFileName) {
        settings.setPolicyLogFileName(policyLogFileName);
    }

    public void setCurrentLogFile(String filen) {
        settings.setCurrentLogFile(filen);
    }

    public void setMaxNumLines(String maxNumLines) {
        settings.setMaxNumLines(maxNumLines);
    }

    public void setDialogLocation(String x, String y, String w, String h) {
        settings.setMainWindowLocation(x, y, w, h);
        setBounds(settings.getMainWindowLocation());
    }

    public void setRestoreOnUpdate(boolean restoreOnUpdate) {
        settings.setRestoreOnUpdate(restoreOnUpdate);
    }
    
    public void setHighlightKeywords(boolean highlightKeywords) {
        settings.setHighlightKeywords(highlightKeywords);
    }

    public void setLastUpdateDate(String last) {
        settings.setLastUpdateDate(last);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jAutorefreshCheckBox = new javax.swing.JCheckBox();
        jClearTraceButton = new javax.swing.JButton();
        jOnTopCheckbox = new javax.swing.JCheckBox();
        jWordWrapCheckbox = new javax.swing.JCheckBox();
        logTypeCombo = new javax.swing.JComboBox();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jHighlightAllCheckbox = new javax.swing.JCheckBox();
        jFilterCheckbox = new javax.swing.JCheckBox();
        jMultipleLabel = new javax.swing.JLabel();
        jSearchWarnLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jSearchComboBox = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTraceTextArea = new javax.swing.JTextArea();
        jScrollHighlight = new vizzy.comp.JScrollHighlightPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Vizzy Flash Tracer");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });

        jAutorefreshCheckBox.setSelected(true);
        jAutorefreshCheckBox.setText("Autorefresh");
        jAutorefreshCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jAutorefreshCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jAutorefreshCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAutorefreshCheckBoxjAutorefreshCheckboxChecked(evt);
            }
        });

        jClearTraceButton.setText("Clear Log");
        jClearTraceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jClearTraceButtondeleteActionPerformed(evt);
            }
        });

        jOnTopCheckbox.setText("Always On Top");
        jOnTopCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jOnTopCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jOnTopCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOnTopCheckboxChecked(evt);
            }
        });

        jWordWrapCheckbox.setSelected(true);
        jWordWrapCheckbox.setText("Word Wrap");
        jWordWrapCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jWordWrapCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jWordWrapCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jWordWrapCheckboxChecked(evt);
            }
        });

        logTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Flash Log", "Policy File" }));
        logTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logTypeComboActionPerformed(evt);
            }
        });

        jLayeredPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));

        jHighlightAllCheckbox.setText("Highlight All");
        jHighlightAllCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jHighlightAllCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jHighlightAllCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jHighlightAllCheckboxChecked(evt);
            }
        });
        jHighlightAllCheckbox.setBounds(10, 20, 120, 15);
        jLayeredPane1.add(jHighlightAllCheckbox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jFilterCheckbox.setText("Filter");
        jFilterCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jFilterCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jFilterCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFilterCheckboxChecked(evt);
            }
        });
        jFilterCheckbox.setBounds(140, 20, 120, 15);
        jLayeredPane1.add(jFilterCheckbox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jMultipleLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        jMultipleLabel.setText("Hint: Use comma to separate keywords");
        jMultipleLabel.setBounds(270, 20, 300, 14);
        jLayeredPane1.add(jMultipleLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jSearchWarnLabel.setText("<html></html>");
        jSearchWarnLabel.setBounds(10, 62, 490, 16);
        jLayeredPane1.add(jSearchWarnLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton1.setText("Clear");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jClearActionPerformed(evt);
            }
        });
        jButton1.setBounds(390, 38, 90, 23);
        jLayeredPane1.add(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jSearchComboBox.setEditable(true);
        jSearchComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jSearchComboBoxKeyReleased(evt);
            }
        });
        jSearchComboBox.setBounds(9, 39, 370, 20);
        jLayeredPane1.add(jSearchComboBox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jTraceTextArea.setColumns(20);
        jTraceTextArea.setFont(new java.awt.Font("Courier New", 0, 12));
        jTraceTextArea.setLineWrap(true);
        jTraceTextArea.setRows(5);
        jTraceTextArea.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jTraceTextAreaMouseWheelMoved(evt);
            }
        });
        jTraceTextArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTraceTextAreaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTraceTextAreaMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTraceTextAreaMouseReleased(evt);
            }
        });
        jTraceTextArea.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTraceTextAreaMouseMoved(evt);
            }
        });
        jTraceTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTraceTextAreaKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTraceTextArea);

        jScrollHighlight.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollHighlight.setPreferredSize(new java.awt.Dimension(12, 325));

        org.jdesktop.layout.GroupLayout jScrollHighlightLayout = new org.jdesktop.layout.GroupLayout(jScrollHighlight);
        jScrollHighlight.setLayout(jScrollHighlightLayout);
        jScrollHighlightLayout.setHorizontalGroup(
            jScrollHighlightLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 8, Short.MAX_VALUE)
        );
        jScrollHighlightLayout.setVerticalGroup(
            jScrollHighlightLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 309, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollHighlight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollHighlight, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jAutorefreshCheckBox)
                .add(77, 77, 77)
                .add(jWordWrapCheckbox)
                .add(103, 103, 103)
                .add(jOnTopCheckbox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 120, Short.MAX_VALUE)
                .add(jClearTraceButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(logTypeCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 119, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(11, 11, 11))
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jLayeredPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 742, Short.MAX_VALUE)
                    .addContainerGap()))
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap(428, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jClearTraceButton)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, logTypeCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jOnTopCheckbox)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jWordWrapCheckbox)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jAutorefreshCheckBox))
                .addContainerGap())
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jLayeredPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(360, Short.MAX_VALUE)))
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                    .add(108, 108, 108)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(41, 41, 41)))
        );

        jMenu1.setText("Edit");

        jMenuItem1.setText("Copy All to Clipboard");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCopyAllClicked(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Extra");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem3.setText("Log snapshot");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSnapshotClicked(evt);
            }
        });
        jMenu2.add(jMenuItem3);
        jMenu2.add(jSeparator1);

        jMenuItem2.setText("Options...");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOptionsClicked(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem4.setText("About");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutClicked(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jHighlightAllCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jHighlightAllCheckboxChecked
        setHighlightAll(jHighlightAllCheckbox.isSelected());
        if (jSearchComboBox.getSelectedItem() != null && !jSearchComboBox.getSelectedItem().equals("")) {
            searcher.setWasSearching(true);
            startSearch(true);
            highlightKeywords();
        }
}//GEN-LAST:event_jHighlightAllCheckboxChecked

    private void jFilterCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFilterCheckboxChecked
        jTraceTextArea.setText(traceContent);
        setFiltering(jFilterCheckbox.isSelected());
        if (jSearchComboBox.getSelectedItem() != null && !jSearchComboBox.getSelectedItem().equals("")) {
            searcher.setWasSearching(true);
            startSearch(false);
            highlightKeywords();
        }
}//GEN-LAST:event_jFilterCheckboxChecked

    private void jTraceTextAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTraceTextAreaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F3) {
            if (jTraceTextArea.getSelectedText() != null && jTraceTextArea.getSelectedText().length() > 0) {
                jSearchComboBox.setSelectedItem(jTraceTextArea.getSelectedText());
            }
            searcher.setWasSearching(true);
            startSearch(true);
            highlightKeywords();
        }
}//GEN-LAST:event_jTraceTextAreaKeyPressed

    private void jAutorefreshCheckBoxjAutorefreshCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAutorefreshCheckBoxjAutorefreshCheckboxChecked
        setAutoRefresh(jAutorefreshCheckBox.isSelected());
        if (settings.isAutoRefresh()) {
            createLoadTimerTask().run();
            startReadLogFileTimer();
        } else {
            stopReadLogFileTimer();
        }
}//GEN-LAST:event_jAutorefreshCheckBoxjAutorefreshCheckboxChecked

    private void jWordWrapCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jWordWrapCheckboxChecked
        setWordWrap(jWordWrapCheckbox.isSelected());
}//GEN-LAST:event_jWordWrapCheckboxChecked

    private void jOnTopCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOnTopCheckboxChecked
        setOnTop(jOnTopCheckbox.isSelected());
}//GEN-LAST:event_jOnTopCheckboxChecked

    private void jClearTraceButtondeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jClearTraceButtondeleteActionPerformed
        new DeleteFile(settings.getCurrentLogFile());
        jTraceTextArea.setText("");
        traceContent = "";
        recentHash = null;
        needToScrolldown = true;
        ((JScrollHighlightPanel)jScrollHighlight).setIndexes(null);
}//GEN-LAST:event_jClearTraceButtondeleteActionPerformed

    private void jTraceTextAreaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTraceTextAreaMousePressed
        if (settings.isAutoRefresh()) {
            stopReadLogFileTimer();
        }
    }//GEN-LAST:event_jTraceTextAreaMousePressed

    private void jTraceTextAreaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTraceTextAreaMouseReleased
        String selection = jTraceTextArea.getSelectedText();
        if (selection == null || "".equals(selection)) {
            handleWordAtPosition();
        }
        if (settings.isAutoRefresh()) {
            startReadLogFileTimer();
        }
    }//GEN-LAST:event_jTraceTextAreaMouseReleased

    private void jClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jClearActionPerformed
        jTraceTextArea.setText(traceContent);
        searcher.clearHighlights();
        searcher.setWasSearching(false);
        jSearchComboBox.setSelectedItem("");
        jSearchWarnLabel.setVisible(false);
        ((JScrollHighlightPanel)jScrollHighlight).setIndexes(null);
        highlightKeywords();
}//GEN-LAST:event_jClearActionPerformed

    private void logTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logTypeComboActionPerformed
        setLogType(String.valueOf(logTypeCombo.getSelectedIndex()));
        createLoadTimerTask().run();
        if (settings.isAutoRefresh()) {
            startReadLogFileTimer();
        }
    }//GEN-LAST:event_logTypeComboActionPerformed

    private void menuCopyAllClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCopyAllClicked
        TextTransfer tt = new TextTransfer();
        tt.setClipboardContents(jTraceTextArea.getText());
    }//GEN-LAST:event_menuCopyAllClicked

    private void menuOptionsClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOptionsClicked
        if (settings.isAlwaysOnTop()) {
            setAlwaysOnTop(false);
        }

        if (optionsForm == null) {
            optionsForm = new OptionsForm(this, settings);
        }

        optionsForm.setVisible(true);
    }//GEN-LAST:event_menuOptionsClicked

    private void menuSnapshotClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSnapshotClicked
        SnapshotForm snapshotForm = new SnapshotForm(this);
        snapshotForm.setLocation(getLocation().x - snapshotForm.getWidth() - snapshotForms.size() * 20, getLocation().y  - snapshotForms.size() * 20);
        snapshotForm.setSize(snapshotForm.getWidth(), getHeight());
        snapshotForm.setVisible(true);
        
        snapshotForms.add(snapshotForm);
    }//GEN-LAST:event_menuSnapshotClicked

    private void aboutClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutClicked
        new AboutPanel(this);
    }//GEN-LAST:event_aboutClicked

    private void jTraceTextAreaMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTraceTextAreaMouseMoved
        if (!codePopupHandler.isVisible()) {
            startShowCodeTimer(evt);
        } else if (hideCodePopupTimer == null) {
            startHideCodeTimer();
        }
    }//GEN-LAST:event_jTraceTextAreaMouseMoved

    private void jTraceTextAreaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTraceTextAreaMouseExited
        stopShowCodeTimer();
    }//GEN-LAST:event_jTraceTextAreaMouseExited

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        stopShowCodeTimer();
        hideCodePopup();
    }//GEN-LAST:event_formWindowDeactivated

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        onClose();
    }//GEN-LAST:event_formWindowClosing

    private void jTraceTextAreaMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jTraceTextAreaMouseWheelMoved
        vbar.dispatchEvent(evt);
        adjustScroller();
    }//GEN-LAST:event_jTraceTextAreaMouseWheelMoved

    private void jSearchComboBoxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSearchComboBoxKeyReleased
        if (jSearchComboBox.getSelectedItem() == null) {
            return;
        }
        if (jSearchComboBox.getSelectedItem().equals("")) {
            jClearActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addSearchKeyword((String) jSearchComboBox.getSelectedItem());
            searcher.setWasSearching(true);
            startSearch(true);
            highlightKeywords();
        } 
    }//GEN-LAST:event_jSearchComboBoxKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JCheckBox jAutorefreshCheckBox;
    public javax.swing.JButton jButton1;
    public javax.swing.JButton jClearTraceButton;
    public javax.swing.JCheckBox jFilterCheckbox;
    public javax.swing.JCheckBox jHighlightAllCheckbox;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    public javax.swing.JLabel jMultipleLabel;
    public javax.swing.JCheckBox jOnTopCheckbox;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jScrollHighlight;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox jSearchComboBox;
    private javax.swing.JLabel jSearchWarnLabel;
    private javax.swing.JSeparator jSeparator1;
    public javax.swing.JTextArea jTraceTextArea;
    public javax.swing.JCheckBox jWordWrapCheckbox;
    public javax.swing.JComboBox logTypeCombo;
    // End of variables declaration//GEN-END:variables

}
