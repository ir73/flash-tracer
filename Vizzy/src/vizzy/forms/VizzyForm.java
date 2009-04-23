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

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
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
import java.util.Properties;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import vizzy.tasks.DeleteFile;
import vizzy.tasks.LoadFileTask;
import vizzy.tasks.MMCFGInitializer;
import vizzy.tasks.WordSearcher;

/**
 *
 * @author sergeil
 */
public class VizzyForm extends javax.swing.JFrame {

    private boolean needToScrolldown = true;
    private boolean isCapturingScroll = false;
    private Timer t;
    private WordSearcher searcher;
    public static String fileName = "flashlog.txt";
    private long maxNumLines = 20;
    private boolean maxNumLinesEnabled = false;
    private boolean isUTF = true;
    private JScrollBar vbar;
    private String traceContent;
    private boolean isAutoRefresh = true;
    private Properties props;
    private File settingsFile = new File("tracer.properties");
    private int traceLinesCount;
    private VizzyForm jMainFrame;
    private String recentTraceContent;
    private boolean restoreOnUpdate = false;

    /** Creates new form VizzyForm */
    public VizzyForm() {
        super();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            Logger.getLogger(VizzyForm.class.getName()).log(Level.SEVERE, null, ex);
        } 

        loadProperties();
        initComponents();
        initVars();
        
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

    private void adjustScroller() {
        int sum = vbar.getValue() + vbar.getVisibleAmount();
        int am = vbar.getMaximum();
        if (sum >= am) {
            //System.out.println("max!");
            needToScrolldown = true;
        } else {
            //System.out.println("no!");
            needToScrolldown = false;
        }
    }

    private LoadFileTask createLoadTimerTask() {
        return new LoadFileTask(fileName, maxNumLines, maxNumLinesEnabled, isUTF, this);
    }

    private void initMMCFG() {
        MMCFGInitializer i = new MMCFGInitializer();
        i.init();

        if (i.getTraceFileLocation() != null) {
            fileName = i.getTraceFileLocation();
        }
    }

     /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new VizzyForm().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(VizzyForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void onOutOfMemory() {
        setMaxNumLinesEnabled(true);
        setMaxNumLines("100000");

        startTimer();

        JOptionPane.showMessageDialog(this, "The log file is too big and our of memory error occured. Tracer has changed your settings to limit file " +
                "to 100 KB. Please change this value if needed in Options panel.", "Error", 1);
    }


    public void onFileRead(String inputFileBuff) {
        traceContent = inputFileBuff;

        if (restoreOnUpdate && traceIsModified()) {
            setExtendedState(JFrame.NORMAL);
        }
        recentTraceContent = traceContent;

        jTraceTextArea.setText(traceContent);

        traceLinesCount = jTraceTextArea.getLineCount();

        if (searcher.isWasSearching()) {
            startSearch(searcher.getLastCaretPos() - 1, false);
        }


        if (needToScrolldown) {

              jTraceTextArea.setCaretPosition( jTraceTextArea.getDocument().getLength() );



        }

    }

    private void setHTMLRender(boolean equals) {
        //jTraceTextArea.set = equals;
    }

    private void setMaxNumLinesEnabled(boolean equals) {
        jNumLinesEnabledCheckBox.setSelected(equals);
        maxNumLinesEnabled = equals;
    }

    private void setUTF(boolean equals) {
        isUTF = equals;
        jUTFCheckBox.setSelected(equals);
    }

    private void startTimer() {
        stopTimer();
        t = new Timer();
        t.schedule(createLoadTimerTask(), 1000, new Long(1000));
    }

    private void stopTimer() {
        if (t != null) {
            t.cancel();
        }
    }

    private void startSearch(int lastCarPos, boolean forcedScroll) {
        String word = jSearchTextField.getText();
        if (searcher.isFilter()) {
            try {
                jTraceTextArea.setText(searcher.filter(traceContent, word, traceLinesCount));
            } catch (Exception ex) {
            }
            jSearchWarnLabel.setVisible(false);
        } else {
            int offset = searcher.search(traceContent, word, lastCarPos);
            if (offset != -1) {
                jSearchWarnLabel.setVisible(true);
                jSearchWarnLabel.setText("<html>Result: <font color=\"blue\"><b>" + word + "</b></font> found!</html>");
                if (forcedScroll) {
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

    private void startSearch(boolean forcedScroll) {
        startSearch(searcher.getLastCaretPos(), forcedScroll);
    }

    private void startSearch() {
        startSearch(true);
    }

    private void initVars() {
        this.vbar = jScrollPane1.getVerticalScrollBar();
        this.searcher = new WordSearcher(jTraceTextArea);

        jOptionsDialog.setVisible(false);
        jOptionsDialog.setModal(true);
        jOptionsDialog.pack();

        //Get the screen size
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        //Calculate the frame location
        int x = (screenSize.width - getWidth()) / 2 + jOptionsDialog.getWidth()/2;
        int y = (screenSize.height - getHeight()) / 2 + jOptionsDialog.getHeight()/2;

        //Set the new frame location
        jOptionsDialog.setLocation(x, y);


        setFlashLogFile(props.getProperty("settings.filename", ""));


        //setHTMLRender(props.getProperty("settings.html", "false").equals("true"));
        setUTF(props.getProperty("settings.isUTF", "true").equals("true"));
        setMaxNumLinesEnabled(props.getProperty("settings.maxNumLinesEnabled", "false").equals("true"));
        setMaxNumLines(props.getProperty("settings.maxNumLines", "1000"));
        setRestoreOnUpdate(props.getProperty("settings.restore", "false").equals("true"));
        setOnTop(props.getProperty("settings.alwaysontop", "false").equals("true"));
        setHighlightAll(props.getProperty("settings.highlight_all", "true").equals("true"));
        setAutoRefresh(props.getProperty("settings.autorefresh", "true").equals("true"));
        setWordWrap(props.getProperty("settings.wordwrap", "true").equals("true"));
        setFiltering(props.getProperty("settings.filter", "false").equals("true"));
        setTraceFont(props.getProperty("settings.font.name", "Courier New"), props.getProperty("settings.font.size", "12"));
        setDialogLocation(props.getProperty("settings.window.x", String.valueOf(this.getX())),
                props.getProperty("settings.window.y", String.valueOf(this.getY())),
                props.getProperty("settings.window.width", String.valueOf(this.getWidth())),
                props.getProperty("settings.window.height", String.valueOf(this.getHeight())));
        jMainFrame = this;
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                saveSetting("isUTF", String.valueOf(isUTF));
                saveSetting("autorefresh", String.valueOf(jAutorefreshCheckBox.isSelected()));
                saveSetting("alwaysontop", String.valueOf(jOnTopCheckbox.isSelected()));
                saveSetting("highlight_all", String.valueOf(jHighlightAllCheckbox.isSelected()));
                saveSetting("wordwrap", String.valueOf(jWordWrapCheckbox.isSelected()));
                saveSetting("filter", String.valueOf(jFilterCheckbox.isSelected()));
                saveSetting("font.name", jTraceTextArea.getFont().getName());
                saveSetting("font.size", String.valueOf(jTraceTextArea.getFont().getSize()));
                saveSetting("filename", fileName);
                saveSetting("window.x", String.valueOf(jMainFrame.getX()));
                saveSetting("window.y", String.valueOf(jMainFrame.getY()));
                saveSetting("window.width", String.valueOf(jMainFrame.getWidth()));
                saveSetting("window.height", String.valueOf(jMainFrame.getHeight()));
                saveSetting("restore", String.valueOf(restoreOnUpdate));
                saveSetting("maxNumLines", String.valueOf(maxNumLines));
                saveSetting("maxNumLinesEnabled", String.valueOf(maxNumLinesEnabled));
                //saveSetting("html", String.valueOf(jTraceTextArea.));
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
    }



    private void loadProperties() {
        props = new Properties();
        try {
            //throw new FileNotFoundException("dsfs");
            props.load(new FileInputStream(new File("tracer.properties")));
        } catch (FileNotFoundException ex) {
            initMMCFG();
        } catch (IOException ex) {

        }
    }

    private void saveSetting(String key, String val) {
        props.setProperty("settings." + key, val);
    }

    public void setOnTop (boolean b) {

        jOnTopCheckbox.setSelected(b);
        setAlwaysOnTop(b);
    }

    private void setHighlightAll(boolean b) {
        jHighlightAllCheckbox.setSelected(b);
        searcher.setHighlightAll(b);
    }

    private void setWordWrap(boolean b) {
        jWordWrapCheckbox.setSelected(b);
        this.jTraceTextArea.setLineWrap(b);
    }

    private void setAutoRefresh(boolean b) {
        jAutorefreshCheckBox.setSelected(b);
        isAutoRefresh = b;

        stopTimer();
        if (b) {
            startTimer();
        }

    }

    private void setFiltering(boolean b) {
        jFilterCheckbox.setSelected(b);
        jMultipleLabel.setVisible(b);
        searcher.setIsFilter(b);
        jHighlightAllCheckbox.setEnabled(!b);
        if (jSearchTextField.getText() != null && jSearchTextField.getText().length() > 0) {
            startSearch();
        }
    }

    private void setTraceFont(String name, String size) {
        jTraceTextArea.setFont(new Font(name, 0, Integer.parseInt(size)));
    }

    private void setFlashLogFile(String filen) {
        if (!filen.equals("")) {
            fileName = filen;
        }
    }

    private void setMaxNumLines(String lines) {
        maxNumLines = Long.valueOf(lines);
        jNumLinesTextField.setText(String.valueOf(maxNumLines));
    }

    private void setDialogLocation(String x, String y, String w, String h) {
        this.setLocation(Integer.parseInt(x), Integer.parseInt(y));
        this.setSize(Integer.parseInt(w), Integer.parseInt(h));
    }

    private boolean traceIsModified() {
        return !traceContent.equals(recentTraceContent);
    }

    private void setRestoreOnUpdate(boolean b) {
        jRestoreCheckBox.setSelected(b);
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

        jOptionsDialog = new javax.swing.JDialog();
        jLayeredPane4 = new javax.swing.JLayeredPane();
        jNumLinesTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jNumLinesEnabledCheckBox = new javax.swing.JCheckBox();
        jLayeredPane3 = new javax.swing.JLayeredPane();
        jUTFCheckBox = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jFlashLogTextField = new javax.swing.JTextField();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        jFontNameTextField = new javax.swing.JTextField();
        jWarnLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jFontSizeTextField = new javax.swing.JTextField();
        jLayeredPane5 = new javax.swing.JLayeredPane();
        jHTMLCheckBox = new javax.swing.JCheckBox();
        jRestoreCheckBox = new javax.swing.JCheckBox();
        jLayeredPane6 = new javax.swing.JLayeredPane();
        jUpdatesLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jOKButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jAutorefreshCheckBox = new javax.swing.JCheckBox();
        jClearTraceButton = new javax.swing.JButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jHighlightAllCheckbox = new javax.swing.JCheckBox();
        jFilterCheckbox = new javax.swing.JCheckBox();
        jMultipleLabel = new javax.swing.JLabel();
        jSearchTextField = new javax.swing.JTextField();
        jClearSearchButton = new javax.swing.JButton();
        jSearchWarnLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTraceTextArea = new javax.swing.JTextArea();
        jOnTopCheckbox = new javax.swing.JCheckBox();
        jWordWrapCheckbox = new javax.swing.JCheckBox();
        jOptionsButton = new javax.swing.JButton();

        jOptionsDialog.setTitle("Options");
        jOptionsDialog.setResizable(false);

        jLayeredPane4.setBorder(javax.swing.BorderFactory.createTitledBorder("Limit"));

        jNumLinesTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumLinesTextFieldActionPerformed(evt);
            }
        });
        jNumLinesTextField.setBounds(190, 86, 140, 20);
        jLayeredPane4.add(jNumLinesTextField, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel8.setText("Max amount of bytes to load:");
        jLabel8.setBounds(10, 90, 180, 14);
        jLayeredPane4.add(jLabel8, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel11.setText("<html>This is usually required when your log file gets too big and Tracer crashes.</html>");
        jLabel11.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel11.setBounds(10, 40, 360, 40);
        jLayeredPane4.add(jLabel11, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jNumLinesEnabledCheckBox.setText("Load limited amount of bytes only");
        jNumLinesEnabledCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jNumLinesEnabledCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jNumLinesEnabledCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumLinesEnabledCheckBoxActionPerformed(evt);
            }
        });
        jNumLinesEnabledCheckBox.setBounds(10, 20, 360, 15);
        jLayeredPane4.add(jNumLinesEnabledCheckBox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLayeredPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("Log file"));

        jUTFCheckBox.setText("read file as UTF-8");
        jUTFCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jUTFCheckBoxActionPerformed(evt);
            }
        });
        jUTFCheckBox.setBounds(200, 16, 170, 20);
        jLayeredPane3.add(jUTFCheckBox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel4.setText("flashlog.txt location:");
        jLabel4.setBounds(10, 20, 190, 14);
        jLayeredPane3.add(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jFlashLogTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFlashLogTextFieldActionPerformed(evt);
            }
        });
        jFlashLogTextField.setBounds(10, 40, 360, 20);
        jLayeredPane3.add(jFlashLogTextField, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLayeredPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Font"));
        jFontNameTextField.setBounds(10, 40, 260, 20);
        jLayeredPane2.add(jFontNameTextField, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jWarnLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jWarnLabel.setForeground(new java.awt.Color(255, 0, 0));
        jWarnLabel.setBounds(10, 63, 360, 14);
        jLayeredPane2.add(jWarnLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel3.setText("Font size:");
        jLabel3.setBounds(290, 20, 80, 14);
        jLayeredPane2.add(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel2.setText("Font:");
        jLabel2.setBounds(10, 20, 260, 14);
        jLayeredPane2.add(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jFontSizeTextField.setBounds(283, 40, 90, 20);
        jLayeredPane2.add(jFontSizeTextField, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLayeredPane5.setBorder(javax.swing.BorderFactory.createTitledBorder("General"));

        jHTMLCheckBox.setText("Render log text as html");
        jHTMLCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jHTMLCheckBox.setEnabled(false);
        jHTMLCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jHTMLCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jHTMLCheckBoxActionPerformed(evt);
            }
        });
        jHTMLCheckBox.setBounds(10, 40, 360, 15);
        jLayeredPane5.add(jHTMLCheckBox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jRestoreCheckBox.setText("Restore window on trace update if minimized");
        jRestoreCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRestoreCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRestoreCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRestoreCheckBoxActionPerformed(evt);
            }
        });
        jRestoreCheckBox.setBounds(10, 20, 360, 15);
        jLayeredPane5.add(jRestoreCheckBox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLayeredPane6.setBorder(javax.swing.BorderFactory.createTitledBorder("Updates"));

        jUpdatesLabel.setText("<html><a href=\"http://google.com\">Check for updates...</a></html>");
        jUpdatesLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jUpdatesLabelMouseClicked(evt);
            }
        });
        jUpdatesLabel.setBounds(10, 40, 360, 14);
        jLayeredPane6.add(jUpdatesLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel5.setText("Current version is: 1.12");
        jLabel5.setBounds(10, 20, 360, 14);
        jLayeredPane6.add(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jOKButton.setText("OK");
        jOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOKButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jOptionsDialogLayout = new org.jdesktop.layout.GroupLayout(jOptionsDialog.getContentPane());
        jOptionsDialog.getContentPane().setLayout(jOptionsDialogLayout);
        jOptionsDialogLayout.setHorizontalGroup(
            jOptionsDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jOptionsDialogLayout.createSequentialGroup()
                .add(jOptionsDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jOptionsDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(jOptionsDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLayeredPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                            .add(jLayeredPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                            .add(jLayeredPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)))
                    .add(jOptionsDialogLayout.createSequentialGroup()
                        .add(176, 176, 176)
                        .add(jOKButton))
                    .add(jOptionsDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLayeredPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE))
                    .add(jOptionsDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLayeredPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jOptionsDialogLayout.setVerticalGroup(
            jOptionsDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jOptionsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLayeredPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLayeredPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLayeredPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLayeredPane5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLayeredPane6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jOKButton)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Vizzy Flash Tracer");

        jAutorefreshCheckBox.setSelected(true);
        jAutorefreshCheckBox.setText("Autorefresh");
        jAutorefreshCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jAutorefreshCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jAutorefreshCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAutorefreshCheckBoxjAutorefreshCheckboxChecked(evt);
            }
        });

        jClearTraceButton.setText("Clear");
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
        jFilterCheckbox.setBounds(140, 20, 110, 15);
        jLayeredPane1.add(jFilterCheckbox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jMultipleLabel.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jMultipleLabel.setText("Use , to filter multiple phrases");
        jMultipleLabel.setBounds(270, 20, 143, 14);
        jLayeredPane1.add(jMultipleLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jSearchTextField.setToolTipText("Hit enter to start searching");
        jSearchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jSearchTextFieldjTextFieldKeyReleased(evt);
            }
        });
        jSearchTextField.setBounds(10, 40, 370, 20);
        jLayeredPane1.add(jSearchTextField, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jClearSearchButton.setFont(new java.awt.Font("Tahoma", 0, 9));
        jClearSearchButton.setText("Clear Search");
        jClearSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jClearSearchButtonActionPerformed(evt);
            }
        });
        jClearSearchButton.setBounds(390, 40, 87, 21);
        jLayeredPane1.add(jClearSearchButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jSearchWarnLabel.setText("<html></html>");
        jSearchWarnLabel.setBounds(10, 62, 580, 16);
        jLayeredPane1.add(jSearchWarnLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jTraceTextArea.setColumns(20);
        jTraceTextArea.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTraceTextArea.setLineWrap(true);
        jTraceTextArea.setRows(5);
        jTraceTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTraceTextAreaKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTraceTextArea);

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

        jOptionsButton.setText("Options...");
        jOptionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOptionsButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jAutorefreshCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 154, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jWordWrapCheckbox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 127, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jOnTopCheckbox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 148, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jClearTraceButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 58, Short.MAX_VALUE)
                .add(jOptionsButton)
                .addContainerGap())
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jLayeredPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(436, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jWordWrapCheckbox)
                    .add(jOptionsButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jAutorefreshCheckBox)
                    .add(jOnTopCheckbox)
                    .add(jClearTraceButton))
                .addContainerGap())
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createSequentialGroup()
                    .add(10, 10, 10)
                    .add(jLayeredPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                    .add(45, 45, 45)))
        );

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
        //saveSetting("highlight_all", String.valueOf(jHighlightAllCheckbox.isSelected()));
        setHighlightAll(jHighlightAllCheckbox.isSelected());
        if (jSearchTextField.getText() != null && !jSearchTextField.getText().equals("")) {
            searcher.setWasSearching(true);
            startSearch(true);
        }
}//GEN-LAST:event_jHighlightAllCheckboxChecked

    private void jFilterCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFilterCheckboxChecked
        setFiltering(jFilterCheckbox.isSelected());
        if (jSearchTextField.getText() != null && !jSearchTextField.getText().equals("")) {
            searcher.setWasSearching(true);
            startSearch(true);
        }
}//GEN-LAST:event_jFilterCheckboxChecked

    private void jSearchTextFieldjTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSearchTextFieldjTextFieldKeyReleased
        if (jSearchTextField.getText().equals("")) {
            searcher.clearHighlights();
            searcher.setWasSearching(false);
            jSearchTextField.setText("");
            jSearchWarnLabel.setVisible(false);
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

    private void jClearSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jClearSearchButtonActionPerformed
        searcher.clearHighlights();
        searcher.setWasSearching(false);
        jSearchTextField.setText("");
        jSearchWarnLabel.setVisible(false);
}//GEN-LAST:event_jClearSearchButtonActionPerformed

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
}//GEN-LAST:event_jAutorefreshCheckBoxjAutorefreshCheckboxChecked

    private void jWordWrapCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jWordWrapCheckboxChecked
        setWordWrap(jWordWrapCheckbox.isSelected());
}//GEN-LAST:event_jWordWrapCheckboxChecked

    private void jOnTopCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOnTopCheckboxChecked
        //        saveSetting("alwaysontop", String.valueOf(jOnTopCheckbox.isSelected()));
        setOnTop(jOnTopCheckbox.isSelected());
}//GEN-LAST:event_jOnTopCheckboxChecked

    private void jClearTraceButtondeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jClearTraceButtondeleteActionPerformed
        new DeleteFile(fileName);
        jTraceTextArea.setText("");
}//GEN-LAST:event_jClearTraceButtondeleteActionPerformed

    private void jOptionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOptionsButtonActionPerformed
        if (jOnTopCheckbox.isSelected()) {
            setAlwaysOnTop(false);
        }
        jFontNameTextField.setText(jTraceTextArea.getFont().getName());
        jFontSizeTextField.setText(String.valueOf(jTraceTextArea.getFont().getSize()));
        jFlashLogTextField.setText(fileName);
        jRestoreCheckBox.setSelected(restoreOnUpdate);
        jOptionsDialog.setVisible(true);
        jNumLinesEnabledCheckBox.setSelected(maxNumLinesEnabled);
        jNumLinesTextField.setText(String.valueOf(maxNumLines));
}//GEN-LAST:event_jOptionsButtonActionPerformed

    private void jUTFCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUTFCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jUTFCheckBoxActionPerformed

    private void jFlashLogTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFlashLogTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFlashLogTextFieldActionPerformed

    private void jNumLinesEnabledCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumLinesEnabledCheckBoxActionPerformed
        if (jNumLinesEnabledCheckBox.isSelected()) {
            long n = Long.parseLong(jNumLinesTextField.getText());
            if (!(n > 0)) {
                jNumLinesTextField.setText("1000");
            }
        }
}//GEN-LAST:event_jNumLinesEnabledCheckBoxActionPerformed

    private void jNumLinesTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumLinesTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jNumLinesTextFieldActionPerformed

    private void jRestoreCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRestoreCheckBoxActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jRestoreCheckBoxActionPerformed

    private void jHTMLCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jHTMLCheckBoxActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jHTMLCheckBoxActionPerformed

    private void jUpdatesLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jUpdatesLabelMouseClicked
        try {
            new BrowserLauncher().openURLinBrowser("http://code.google.com/p/flash-tracer/downloads/list");
        } catch (BrowserLaunchingInitializingException ex) {
            Logger.getLogger(VizzyForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedOperatingSystemException ex) {
            Logger.getLogger(VizzyForm.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_jUpdatesLabelMouseClicked

    private void jOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOKButtonActionPerformed
        boolean fontExists = false;
        try {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font[] allfonts = env.getAllFonts();
            String someFont = null;
            for (Font font : allfonts) {
                someFont = font.getName();
                if (font.getName().equals(jFontNameTextField.getText())) {
                    fontExists = true;
                    break;
                }
            }
            if (fontExists) {
                setTraceFont(jFontNameTextField.getText(), jFontSizeTextField.getText());
                jWarnLabel.setText("");
                jOptionsDialog.setVisible(false);
            } else {
                jWarnLabel.setText("No such font! Please use e.g. \"" + someFont + "\" font.");
            }

            if (!fileName.equals(jFlashLogTextField.getText())) {
                setFlashLogFile(jFlashLogTextField.getText());
                jTraceTextArea.setText("");
            }

            setMaxNumLinesEnabled(jNumLinesEnabledCheckBox.isSelected());
            setMaxNumLines(jNumLinesTextField.getText());
            setUTF(jUTFCheckBox.isSelected());

            setAutoRefresh(jAutorefreshCheckBox.isSelected());

            setAlwaysOnTop(jOnTopCheckbox.isSelected());

            setRestoreOnUpdate(jRestoreCheckBox.isSelected());

        } catch (Exception ex) {

        }
    }//GEN-LAST:event_jOKButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jAutorefreshCheckBox;
    private javax.swing.JButton jClearSearchButton;
    private javax.swing.JButton jClearTraceButton;
    private javax.swing.JCheckBox jFilterCheckbox;
    private javax.swing.JTextField jFlashLogTextField;
    private javax.swing.JTextField jFontNameTextField;
    private javax.swing.JTextField jFontSizeTextField;
    private javax.swing.JCheckBox jHTMLCheckBox;
    private javax.swing.JCheckBox jHighlightAllCheckbox;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JLayeredPane jLayeredPane3;
    private javax.swing.JLayeredPane jLayeredPane4;
    private javax.swing.JLayeredPane jLayeredPane5;
    private javax.swing.JLayeredPane jLayeredPane6;
    private javax.swing.JLabel jMultipleLabel;
    private javax.swing.JCheckBox jNumLinesEnabledCheckBox;
    private javax.swing.JTextField jNumLinesTextField;
    private javax.swing.JButton jOKButton;
    private javax.swing.JCheckBox jOnTopCheckbox;
    private javax.swing.JButton jOptionsButton;
    private javax.swing.JDialog jOptionsDialog;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JCheckBox jRestoreCheckBox;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jSearchTextField;
    private javax.swing.JLabel jSearchWarnLabel;
    private javax.swing.JTextArea jTraceTextArea;
    private javax.swing.JCheckBox jUTFCheckBox;
    private javax.swing.JLabel jUpdatesLabel;
    private javax.swing.JLabel jWarnLabel;
    private javax.swing.JCheckBox jWordWrapCheckbox;
    // End of variables declaration//GEN-END:variables

}
