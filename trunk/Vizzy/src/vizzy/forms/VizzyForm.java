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
import java.awt.GraphicsEnvironment;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import vizzy.comp.JScrollHighlightPanel;
import vizzy.forms.panels.AboutPanel;
import vizzy.forms.panels.OptionsForm;
import vizzy.forms.panels.SnapshotForm;
import vizzy.tasks.CheckUpdates;
import vizzy.tasks.DebugPlayerDetector;
import vizzy.tasks.DeleteFile;
import vizzy.tasks.FlashAndPolicyLogInitializer;
import vizzy.tasks.LoadFileTask;
import vizzy.tasks.MMCFGInitializer;
import vizzy.tasks.WordSearcher;
import vizzy.util.TextTransfer;

/**
 *
 * @author sergeil
 */
public class VizzyForm extends javax.swing.JFrame {

    private boolean detectPlayer;
    private boolean needToScrolldown = true;
    private boolean isCapturingScroll = false;
    private Timer t;
    private WordSearcher searcher;
    private String currentFileName;
    private JScrollBar vbar;
    private String traceContent;
    private Properties props;
    private File settingsFile = new File("tracer.properties");
    private VizzyForm jMainFrame;
    private Font[] fonts;
    private OptionsForm optionsForm;

    public MMCFGInitializer mmcfgInitializer;
    public ArrayList<SnapshotForm> snapshotForms = new ArrayList<SnapshotForm>();
    public DefaultComboBoxModel fontsModel;
    public String flashLogFileName;
    public String policyLogFileName;
    public int logType = 0;
    public String recentHash;
    public boolean isAutoRefresh = true;
    public boolean isUTF = true;
    public boolean isCheckUpdates = true;
    public boolean maxNumLinesEnabled = false;
    public long maxNumLines = 20;
    public boolean restoreOnUpdate = false;
    public long refreshFreq = 500;
    public boolean isAlwaysONTOP;

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new VizzyForm();
                } catch (Exception ex) {
                    Logger.getLogger(VizzyForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    /** Creates new form VizzyForm */
    public VizzyForm() {
        super();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            Logger.getLogger(VizzyForm.class.getName()).log(Level.SEVERE, null, ex);
        } 

        
        initComponents();
        initFlashLog();
        initMMCFG();
        initFonts();
        loadProperties();
        initVars();
        initComplete();
        new Thread(new Runnable() {
            public void run() {
                
                boolean aut = jMainFrame.isAlwaysOnTop();
                jMainFrame.setAlwaysOnTop(false);
                checkDebugPlayers();
                if (isCheckUpdates) {
                    jMainFrame.checkUpdates(false);
                }
                jMainFrame.setAlwaysOnTop(aut);
            }
        }).start();
        
        setVisible(true);
    }

    public void checkUpdates(boolean reportIfOk) {
        CheckUpdates cu = new CheckUpdates(reportIfOk);
        cu.start();
    }

    private void initFonts() {
        try {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            fonts = env.getAllFonts();
            List<String> l = new ArrayList<String>();
            for (Font font : fonts) {
                l.add(font.getName());
            }
            fontsModel = new DefaultComboBoxModel(l.toArray());
        } catch (Exception ex) {
        }
    }

    private void initComplete() {
        jTraceTextArea.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                vbar.dispatchEvent(e);
                adjustScroller();
            }
        });
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

    /**
     * Get flashlog.txt file location depending on user's
     * operation system
     */
    private void initFlashLog() {
        FlashAndPolicyLogInitializer i = new FlashAndPolicyLogInitializer();
        i.init();
        if (i.getTraceFileLocation() != null) {
            setFlashLogFile(i.getTraceFileLocation());
        }
        if (i.getPolicyFileLocation() != null) {
            setPolicyLogFile(i.getPolicyFileLocation());
        }
    }

    /**
     * Checks that the user has debug flash player installed
     * Check is only valid for Windows OS and for
     * IE browser only
     */
    private void checkDebugPlayers() {
        if (detectPlayer) {
            DebugPlayerDetector d = new DebugPlayerDetector();
            d.start();
        }
        detectPlayer = false;
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
                    "Now, in order to see flash trace output, please do the following: \n" +
                    "1. Restart all your currently open browsers.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
        if (mmcfgInitializer.getTraceFileLocation() != null) {
            setFlashLogFile(mmcfgInitializer.getTraceFileLocation());
        }
    }

    private void initVars() {
        JScrollHighlightPanel scrollPanel = (JScrollHighlightPanel)jScrollHighlight;
        this.vbar = jScrollPane1.getVerticalScrollBar();
        this.searcher = new WordSearcher(jTraceTextArea, scrollPanel);

        scrollPanel.setTa(jTraceTextArea);
        scrollPanel.setLocation(jTraceTextArea.getX(), jTraceTextArea.getY());
        scrollPanel.setSize(scrollPanel.getWidth(), jTraceTextArea.getHeight()/2);

        try {
            URL myIconUrl = this.getClass().getResource("/img/vizzy.png");
            this.setIconImage(new ImageIcon(myIconUrl, "Vizzy Flash Tracer").getImage());
        } catch (Exception e) {
        }

        String defaultFont = "Courier New";
        for (Font font : fonts) {
            if (font.getName().indexOf("Courier") > -1) {
                defaultFont = font.getName();
                break;
            }
        }

        setDetectPlayer(props.getProperty("settings.detectplayer", "true").equals("true"));
        setCheckUpdates(props.getProperty("settings.autoupdates", "true").equals("true"));
        setFlashLogFile(props.getProperty("settings.filename", flashLogFileName));
        setLogType(props.getProperty("settings.logtype", "0"));
        setRefreshFreq(props.getProperty("settings.refreshFreq", "500"));
        setUTF(props.getProperty("settings.isUTF", "true").equals("true"));
        setMaxNumLinesEnabled(props.getProperty("settings.maxNumLinesEnabled", "false").equals("true"));
        setMaxNumLines(props.getProperty("settings.maxNumLines", "50000"));
        setRestoreOnUpdate(props.getProperty("settings.restore", "false").equals("true"));
        setOnTop(props.getProperty("settings.alwaysontop", "false").equals("true"));
        setHighlightAll(props.getProperty("settings.highlight_all", "true").equals("true"));
        setAutoRefresh(props.getProperty("settings.autorefresh", "true").equals("true"));
        setWordWrap(props.getProperty("settings.wordwrap", "true").equals("true"));
        setFiltering(props.getProperty("settings.filter", "false").equals("true"));
        setTraceFont(props.getProperty("settings.font.name", defaultFont), props.getProperty("settings.font.size", "12"));
        setDialogLocation(props.getProperty("settings.window.x", String.valueOf(this.getX())),
                props.getProperty("settings.window.y", String.valueOf(this.getY())),
                props.getProperty("settings.window.width", String.valueOf(this.getWidth())),
                props.getProperty("settings.window.height", String.valueOf(this.getHeight())));
        jMainFrame = this;

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                saveSetting("settings.detectplayer", String.valueOf(detectPlayer));
                saveSetting("settings.autoupdates", String.valueOf(isCheckUpdates));
                saveSetting("settings.refreshFreq", String.valueOf(refreshFreq));
                saveSetting("settings.isUTF", String.valueOf(isUTF));
                saveSetting("settings.autorefresh", String.valueOf(jAutorefreshCheckBox.isSelected()));
                saveSetting("settings.alwaysontop", String.valueOf(jOnTopCheckbox.isSelected()));
                saveSetting("settings.highlight_all", String.valueOf(jHighlightAllCheckbox.isSelected()));
                saveSetting("settings.wordwrap", String.valueOf(jWordWrapCheckbox.isSelected()));
                saveSetting("settings.filter", String.valueOf(jFilterCheckbox.isSelected()));
                saveSetting("settings.font.name", jTraceTextArea.getFont().getName());
                saveSetting("settings.font.size", String.valueOf(jTraceTextArea.getFont().getSize()));
                saveSetting("settings.filename", flashLogFileName);
                saveSetting("settings.window.x", String.valueOf(jMainFrame.getX()));
                saveSetting("settings.window.y", String.valueOf(jMainFrame.getY()));
                saveSetting("settings.window.width", String.valueOf(jMainFrame.getWidth()));
                saveSetting("settings.window.height", String.valueOf(jMainFrame.getHeight()));
                saveSetting("settings.restore", String.valueOf(restoreOnUpdate));
                saveSetting("settings.maxNumLines", String.valueOf(maxNumLines));
                saveSetting("settings.maxNumLinesEnabled", String.valueOf(maxNumLinesEnabled));
                saveSetting("settings.logtype", String.valueOf(logType));
                try {
                    props.store(new FileOutputStream(settingsFile), "");
                } catch (FileNotFoundException ex) {
                    System.err.println("error saving setting 1.");
                } catch (IOException ex) {
                    System.err.println("error saving setting 2.");
                }
            }
        });

        createLoadTimerTask().run();
        if (isAutoRefresh) {
            startTimer();
        } else {
            stopTimer();
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
        return new LoadFileTask(currentFileName, maxNumLines, maxNumLinesEnabled, isUTF, this);
    }

    public void onOutOfMemory() {
        setMaxNumLinesEnabled(true);
        setMaxNumLines("50000");

        startTimer();

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

        if (restoreOnUpdate) {
            setExtendedState(JFrame.NORMAL);
            repaint();
        }
    }

    private void startSearch(boolean scrollToSearchResult) {
        startSearch(searcher.getLastCaretPos(), scrollToSearchResult);
    }

//    private void startSearch() {
//        startSearch(true);
//    }

    private void startSearch(int lastCarPos, boolean scrollToSearchResult) {
        String word = jSearchTextField.getText();
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

    public void startTimer() {
        stopTimer();
        t = new Timer();
        t.schedule(createLoadTimerTask(), 1000, refreshFreq);
    }

    public void stopTimer() {
        if (t != null) {
            t.cancel();
        }
    }

    public void setCheckUpdates(boolean equals) {
        isCheckUpdates = equals;
    }

    public void setMaxNumLinesEnabled(boolean equals) {
        maxNumLinesEnabled = equals;
    }

    private void setDetectPlayer(boolean equals) {
        detectPlayer = equals;
    }

    public void setRefreshFreq(String property) {
        refreshFreq = Long.parseLong(property);
    }

    public void setUTF(boolean equals) {
        isUTF = equals;
    }

    private void setOnTop (boolean b) {
        isAlwaysONTOP = b;
        jOnTopCheckbox.setSelected(isAlwaysONTOP);
        setAlwaysOnTop(isAlwaysONTOP);
    }

    private void setLogType(String property) {
        logType = Integer.parseInt(property);
        if (logType == 1) {
            if (!mmcfgInitializer.isPolicyFileRecorded()) {
                mmcfgInitializer.recordPolicyFile();
                JOptionPane.showMessageDialog(null, "Vizzy has updated mm.cfg file to enable policy logging. " +
                        "Please restart all your browsers for changed to take effect.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        logTypeCombo.setSelectedIndex(logType);
        if (logType == 0) {
            setCurrentLogFile(flashLogFileName);
        } else {
            setCurrentLogFile(policyLogFileName);
        }
    }

    public void setHighlightAll(boolean b) {
        jHighlightAllCheckbox.setSelected(b);
        searcher.setHighlightAll(b);
    }

    public void setWordWrap(boolean b) {
        jWordWrapCheckbox.setSelected(b);
        this.jTraceTextArea.setLineWrap(b);

        for (SnapshotForm snapshotForm : snapshotForms) {
            snapshotForm.setWordWrap(b);
        }
    }

    public void setAutoRefresh(boolean b) {
        jAutorefreshCheckBox.setSelected(b);
        isAutoRefresh = b;
    }

    public void setFiltering(boolean b) {
        jFilterCheckbox.setSelected(b);
        jMultipleLabel.setVisible(b);
        searcher.setIsFilter(b);
        jHighlightAllCheckbox.setEnabled(!b);
    }

    public void setTraceFont(String name, String size) {
        jTraceTextArea.setFont(new Font(name, 0, Integer.parseInt(size)));
    }

    public void setFlashLogFile(String filen) {
        flashLogFileName = filen;
    }

    public void setPolicyLogFile(String filen) {
        policyLogFileName = filen;
    }

    public void setCurrentLogFile(String filen) {
        currentFileName = filen;
    }

    public void setMaxNumLines(String lines) {
        maxNumLines = Long.valueOf(lines);
    }

    public void setDialogLocation(String x, String y, String w, String h) {
        this.setLocation(Integer.parseInt(x), Integer.parseInt(y));
        this.setSize(Integer.parseInt(w), Integer.parseInt(h));
    }

    public void setRestoreOnUpdate(boolean b) {
        restoreOnUpdate = b;
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
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jHighlightAllCheckbox = new javax.swing.JCheckBox();
        jFilterCheckbox = new javax.swing.JCheckBox();
        jMultipleLabel = new javax.swing.JLabel();
        jSearchTextField = new javax.swing.JTextField();
        jSearchWarnLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jOnTopCheckbox = new javax.swing.JCheckBox();
        jWordWrapCheckbox = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTraceTextArea = new javax.swing.JTextArea();
        jScrollHighlight = new vizzy.comp.JScrollHighlightPanel();
        logTypeCombo = new javax.swing.JComboBox();
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

        jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel1ComponentResized(evt);
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

        jMultipleLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jMultipleLabel.setText("Hint: Use comma to separate multiple keywords");
        jMultipleLabel.setBounds(270, 20, 300, 14);
        jLayeredPane1.add(jMultipleLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jSearchTextField.setToolTipText("Hit enter to start searching");
        jSearchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jSearchTextFieldjTextFieldKeyReleased(evt);
            }
        });
        jSearchTextField.setBounds(10, 40, 370, 20);
        jLayeredPane1.add(jSearchTextField, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jSearchWarnLabel.setText("<html></html>");
        jSearchWarnLabel.setBounds(10, 62, 490, 16);
        jLayeredPane1.add(jSearchWarnLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton1.setText("Clear");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jClearActionPerformed(evt);
            }
        });
        jButton1.setBounds(390, 38, 80, 23);
        jLayeredPane1.add(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);

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

        jTraceTextArea.setColumns(20);
        jTraceTextArea.setFont(new java.awt.Font("Courier New", 0, 12));
        jTraceTextArea.setLineWrap(true);
        jTraceTextArea.setRows(5);
        jTraceTextArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTraceTextAreaMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTraceTextAreaMouseReleased(evt);
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
            .add(0, 316, Short.MAX_VALUE)
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
            .add(jScrollHighlight, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        );

        logTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Flash Log", "Policy File" }));
        logTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logTypeComboActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                        .add(jAutorefreshCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jWordWrapCheckbox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jOnTopCheckbox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 163, Short.MAX_VALUE)
                        .add(jClearTraceButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(logTypeCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 135, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jLayeredPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 742, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(102, 102, 102)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jAutorefreshCheckBox)
                    .add(jWordWrapCheckbox)
                    .add(jOnTopCheckbox)
                    .add(logTypeCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jClearTraceButton))
                .addContainerGap())
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createSequentialGroup()
                    .add(10, 10, 10)
                    .add(jLayeredPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(364, Short.MAX_VALUE)))
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
        if (jSearchTextField.getText() != null && !jSearchTextField.getText().equals("")) {
            searcher.setWasSearching(true);
            startSearch(true);
        }
}//GEN-LAST:event_jHighlightAllCheckboxChecked

    private void jFilterCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFilterCheckboxChecked
        jTraceTextArea.setText(traceContent);
        setFiltering(jFilterCheckbox.isSelected());
        if (jSearchTextField.getText() != null && !jSearchTextField.getText().equals("")) {
            searcher.setWasSearching(true);
            startSearch(false);
        }
}//GEN-LAST:event_jFilterCheckboxChecked

    private void jSearchTextFieldjTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSearchTextFieldjTextFieldKeyReleased
        if (jSearchTextField.getText().equals("")) {
            jClearActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searcher.setWasSearching(true);
            startSearch(true);
        } else {
            ToolTipManager.sharedInstance().mouseMoved(
                    new MouseEvent(jSearchTextField, 0, 0, 0,
                    (int) jSearchTextField.getCaret().getMagicCaretPosition().getX(), -jSearchTextField.getY(), // X-Y of the mouse for the tool tip
                    0, false));
        }
    }//GEN-LAST:event_jSearchTextFieldjTextFieldKeyReleased

    private void jTraceTextAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTraceTextAreaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F3) {
            if (jTraceTextArea.getSelectedText() != null && jTraceTextArea.getSelectedText().length() > 0) {
                jSearchTextField.setText(jTraceTextArea.getSelectedText());
            }
            searcher.setWasSearching(true);
            startSearch(true);
        }
}//GEN-LAST:event_jTraceTextAreaKeyPressed

    private void jAutorefreshCheckBoxjAutorefreshCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAutorefreshCheckBoxjAutorefreshCheckboxChecked
        setAutoRefresh(jAutorefreshCheckBox.isSelected());
        if (isAutoRefresh) {
            createLoadTimerTask().run();
            startTimer();
        } else {
            stopTimer();
        }
}//GEN-LAST:event_jAutorefreshCheckBoxjAutorefreshCheckboxChecked

    private void jWordWrapCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jWordWrapCheckboxChecked
        setWordWrap(jWordWrapCheckbox.isSelected());
}//GEN-LAST:event_jWordWrapCheckboxChecked

    private void jOnTopCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOnTopCheckboxChecked
        setOnTop(jOnTopCheckbox.isSelected());
}//GEN-LAST:event_jOnTopCheckboxChecked

    private void jClearTraceButtondeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jClearTraceButtondeleteActionPerformed
        new DeleteFile(currentFileName);
        jTraceTextArea.setText("");
        traceContent = "";
        recentHash = null;
        needToScrolldown = true;
        ((JScrollHighlightPanel)jScrollHighlight).setIndexes(null);
}//GEN-LAST:event_jClearTraceButtondeleteActionPerformed

    private void jTraceTextAreaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTraceTextAreaMousePressed
        if (isAutoRefresh) {
            stopTimer();
        }
    }//GEN-LAST:event_jTraceTextAreaMousePressed

    private void jTraceTextAreaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTraceTextAreaMouseReleased
        if (isAutoRefresh) {
            startTimer();
        }
    }//GEN-LAST:event_jTraceTextAreaMouseReleased

    private void jPanel1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentResized
    
    }//GEN-LAST:event_jPanel1ComponentResized

    private void jClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jClearActionPerformed
        jTraceTextArea.setText(traceContent);
        searcher.clearHighlights();
        searcher.setWasSearching(false);
        jSearchTextField.setText("");
        jSearchWarnLabel.setVisible(false);
        ((JScrollHighlightPanel)jScrollHighlight).setIndexes(null);
}//GEN-LAST:event_jClearActionPerformed

    private void logTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logTypeComboActionPerformed
        setLogType(String.valueOf(logTypeCombo.getSelectedIndex()));
        createLoadTimerTask().run();
        if (isAutoRefresh) {
            startTimer();
        }
    }//GEN-LAST:event_logTypeComboActionPerformed

    private void menuCopyAllClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCopyAllClicked
        TextTransfer tt = new TextTransfer();
        tt.setClipboardContents(jTraceTextArea.getText());
    }//GEN-LAST:event_menuCopyAllClicked

    private void menuOptionsClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOptionsClicked
        if (isAlwaysONTOP) {
            setAlwaysOnTop(false);
        }

        if (optionsForm == null) {
            optionsForm = new OptionsForm(this);
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
    public javax.swing.JTextField jSearchTextField;
    private javax.swing.JLabel jSearchWarnLabel;
    private javax.swing.JSeparator jSeparator1;
    public javax.swing.JTextArea jTraceTextArea;
    public javax.swing.JCheckBox jWordWrapCheckbox;
    public javax.swing.JComboBox logTypeCombo;
    // End of variables declaration//GEN-END:variables

}
