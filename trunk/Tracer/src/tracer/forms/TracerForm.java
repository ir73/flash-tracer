/*
 * TracerForm.java
 *
 * Created on 21 March 2007, 19:49
 */

package tracer.forms;


import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.text.BadLocationException;
import tracer.tasks.DeleteFile;
import tracer.tasks.LoadFileTask;
import tracer.tasks.WordSearcher;

/**
 *
 * @author  Admin
 */
public class TracerForm extends javax.swing.JFrame {
    
    /** Creates new form TracerForm */
    public TracerForm() {
        loadProperties();
        initComponents();
        initVars();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jOptionsDialog = new javax.swing.JDialog();
        jFontNameTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jFontSizeTextField = new javax.swing.JTextField();
        jOKButton = new javax.swing.JButton();
        jWarnLabel = new javax.swing.JLabel();
        jFlashLogTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jAutorefreshCheckBox = new javax.swing.JCheckBox();
        jWordWrapCheckbox = new javax.swing.JCheckBox();
        jSearchTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTraceTextArea = new javax.swing.JTextArea();
        jClearTraceButton = new javax.swing.JButton();
        label1 = new java.awt.Label();
        jLabel1 = new javax.swing.JLabel();
        jClearSearchButton = new javax.swing.JButton();
        jHighlightAllCheckbox = new javax.swing.JCheckBox();
        jOnTopCheckbox = new javax.swing.JCheckBox();
        jFilterCheckbox = new javax.swing.JCheckBox();
        jOptionsButton = new javax.swing.JButton();

        jOptionsDialog.setTitle("Options");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Font:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText("Font size:");

        jOKButton.setText("OK");
        jOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOKButtonActionPerformed(evt);
            }
        });

        jWarnLabel.setFont(new java.awt.Font("Tahoma", 1, 11));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setText("flashlog.txt location:");

        javax.swing.GroupLayout jOptionsDialogLayout = new javax.swing.GroupLayout(jOptionsDialog.getContentPane());
        jOptionsDialog.getContentPane().setLayout(jOptionsDialogLayout);
        jOptionsDialogLayout.setHorizontalGroup(
            jOptionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jOptionsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jOptionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jOptionsDialogLayout.createSequentialGroup()
                        .addGap(154, 154, 154)
                        .addComponent(jOKButton))
                    .addComponent(jLabel4)
                    .addComponent(jFlashLogTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(jOptionsDialogLayout.createSequentialGroup()
                        .addGroup(jOptionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jOptionsDialogLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jWarnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jFontNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jOptionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFontSizeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                            .addComponent(jLabel3))))
                .addContainerGap())
        );
        jOptionsDialogLayout.setVerticalGroup(
            jOptionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jOptionsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jOptionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jOptionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel2))
                    .addComponent(jWarnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jOptionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFontSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFontNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFlashLogTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(jOKButton)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Flash Debugger");
        jAutorefreshCheckBox.setSelected(true);
        jAutorefreshCheckBox.setText("Autorefresh");
        jAutorefreshCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jAutorefreshCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jAutorefreshCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAutorefreshCheckboxChecked(evt);
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

        jSearchTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jSearchTextFieldFocusLost(evt);
            }
        });
        jSearchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldKeyReleased(evt);
            }
        });

        jTraceTextArea.setColumns(20);
        jTraceTextArea.setFont(new java.awt.Font("Courier New", 0, 12));
        jTraceTextArea.setLineWrap(true);
        jTraceTextArea.setRows(5);
        jTraceTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTraceTextAreaKeyPressed(evt);
            }
        });

        jScrollPane1.setViewportView(jTraceTextArea);

        jClearTraceButton.setText("Clear");
        jClearTraceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText("Search:");

        jClearSearchButton.setFont(new java.awt.Font("Tahoma", 0, 9));
        jClearSearchButton.setText("Clear Search");
        jClearSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jClearSearchButtonActionPerformed(evt);
            }
        });

        jHighlightAllCheckbox.setText("Highlight All");
        jHighlightAllCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jHighlightAllCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jHighlightAllCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jHighlightAllCheckboxChecked(evt);
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

        jFilterCheckbox.setText("Filter");
        jFilterCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jFilterCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jFilterCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFilterCheckboxChecked(evt);
            }
        });

        jOptionsButton.setText("Options >>");
        jOptionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOptionsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jAutorefreshCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jWordWrapCheckbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jOnTopCheckbox)
                        .addGap(33, 33, 33)
                        .addComponent(jClearTraceButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 177, Short.MAX_VALUE)
                        .addComponent(jOptionsButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jHighlightAllCheckbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFilterCheckbox))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jSearchTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jClearSearchButton))
                    .addComponent(label1, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jHighlightAllCheckbox)
                    .addComponent(jFilterCheckbox))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jClearSearchButton)
                    .addComponent(jSearchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jAutorefreshCheckBox)
                    .addComponent(jWordWrapCheckbox)
                    .addComponent(jClearTraceButton)
                    .addComponent(jOnTopCheckbox)
                    .addComponent(jOptionsButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOKButtonActionPerformed
        boolean fontExists = false;
        try {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font[] allfonts = env.getAllFonts();
            for (Font font : allfonts) {
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
                jWarnLabel.setText("No such font!");
            }
            
            if (!fileName.equals(jFlashLogTextField.getText())) {
                setFlashLogFile(jFlashLogTextField.getText());
                jTraceTextArea.setText("");
            }
            
            if (jAutorefreshCheckBox.isSelected()) {
                stopTimer();
                startTimer();
            }
            
            if (jOnTopCheckbox.isSelected()) {
                setAlwaysOnTop(true);
            }
            
        } catch (Exception ex) {
            
        }
        
    }//GEN-LAST:event_jOKButtonActionPerformed

    private void jOptionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOptionsButtonActionPerformed
        if (jOnTopCheckbox.isSelected()) {
            setAlwaysOnTop(false);
        }
        jFontNameTextField.setText(jTraceTextArea.getFont().getName());
        jFontSizeTextField.setText(String.valueOf(jTraceTextArea.getFont().getSize()));
        jFlashLogTextField.setText(fileName);
        jOptionsDialog.setVisible(true);
        
    }//GEN-LAST:event_jOptionsButtonActionPerformed

    private void jFilterCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFilterCheckboxChecked
        setFiltering(jFilterCheckbox.isSelected());
    }//GEN-LAST:event_jFilterCheckboxChecked

    private void jTraceTextAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTraceTextAreaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F3) {
            if (jTraceTextArea.getSelectedText() != null && jTraceTextArea.getSelectedText().length() > 0) {
                jSearchTextField.setText(jTraceTextArea.getSelectedText());
            }
            searcher.setWasSearching(true);
            startSearch(true);
        }
    }//GEN-LAST:event_jTraceTextAreaKeyPressed

    private void jSearchTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jSearchTextFieldFocusLost
    }//GEN-LAST:event_jSearchTextFieldFocusLost

    private void jOnTopCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOnTopCheckboxChecked
//        saveSetting("alwaysontop", String.valueOf(jOnTopCheckbox.isSelected()));
        setOnTop(jOnTopCheckbox.isSelected());
    }//GEN-LAST:event_jOnTopCheckboxChecked

    private void jHighlightAllCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jHighlightAllCheckboxChecked
        //saveSetting("highlight_all", String.valueOf(jHighlightAllCheckbox.isSelected()));
        setHighlightAll(jHighlightAllCheckbox.isSelected());
    }//GEN-LAST:event_jHighlightAllCheckboxChecked

    private void jClearSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jClearSearchButtonActionPerformed
        searcher.clearHighlights();
        searcher.setWasSearching(false);
        jSearchTextField.setText("");
        label1.setText("");
    }//GEN-LAST:event_jClearSearchButtonActionPerformed
    
    private void jTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searcher.setWasSearching(true);
            startSearch(true);
        } else {
            searcher.setWasSearching(false);
            searcher.setLastCaretPos(0);
            label1.setText("");
        }
        
    }//GEN-LAST:event_jTextFieldKeyReleased
    
    private void jWordWrapCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jWordWrapCheckboxChecked
        //saveSetting("wordwrap", String.valueOf(jWordWrapCheckbox.isSelected()));
        setWordWrap(jWordWrapCheckbox.isSelected());
    }//GEN-LAST:event_jWordWrapCheckboxChecked
    
    private void jAutorefreshCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAutorefreshCheckboxChecked
        //saveSetting("autorefresh", String.valueOf(jAutorefreshCheckBox.isSelected()));
        setAutoRefresh(jAutorefreshCheckBox.isSelected());
    }//GEN-LAST:event_jAutorefreshCheckboxChecked
    
    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        new DeleteFile(fileName);
        jTraceTextArea.setText("");
    }//GEN-LAST:event_deleteActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //String file = null;
        if (args.length > 0) {
            TracerForm.fileName = args[0];
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TracerForm().setVisible(true);
            }
        });
    }
    
    public void onFileRead(StringBuffer inputFileBuff) {
        traceContent = inputFileBuff.toString();
        
        boolean needToScrolldown = false;
        if (((vbar.getValue() + vbar.getVisibleAmount()) == vbar.getMaximum())) {
            needToScrolldown = true;
        }
        
        jTraceTextArea.setText(traceContent);
        
        traceLinesCount = jTraceTextArea.getLineCount();
        
        if (searcher.isWasSearching()) {
            //int add = jSearchTextField.getText().length() > 1 ? jSearchTextField.getText().length()-1 : 0;
            startSearch(searcher.getLastCaretPos() - 1, false);
        }
        
        if (needToScrolldown) {
            jTraceTextArea.setCaretPosition( jTraceTextArea.getDocument().getLength() );
//            try {
//                jTraceTextArea.scrollRectToVisible(jTraceTextArea
//                                    .modelToView(traceContent.length()));
//            } catch (BadLocationException ex) {
//                ex.printStackTrace();
//            }
        }
        
    }
    
    private void startTimer() {
        t = new Timer();
        LoadFileTask lft = new LoadFileTask(fileName);
        lft.setActionListener(this);
        t.schedule(lft, 1000, new Long(1000));
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
                //System.out.println("error");
                //ex.printStackTrace();
            }
        } else {
            int offset = searcher.search(traceContent, word, lastCarPos);
            if (offset != -1) {
                label1.setText("Word         '" + word + "'         found.");
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
                label1.setText("'" + word + "'        not found!");
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


        setFlashLogFile(props.getProperty("settings.filename", "flashlog.txt"));
        LoadFileTask lft = new LoadFileTask(fileName);
        lft.setActionListener(this);
        lft.run();
        setOnTop(props.getProperty("settings.alwaysontop", "false").equals("true"));
        setHighlightAll(props.getProperty("settings.highlight_all", "false").equals("true"));
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
                
                try {
                    props.store(new FileOutputStream(settingsFile), "");
                } catch (FileNotFoundException ex) {
                    System.err.println("error saving setting 1.");
                } catch (IOException ex) {
                    System.err.println("error saving setting 2.");
                }
            }
        });
    }
    
    

    private void loadProperties() {
        props = new Properties();
        try {
            props.load(new FileInputStream(new File("tracer.properties")));
        } catch (FileNotFoundException ex) {
            //ex.printStackTrace();
        } catch (IOException ex) {
            //ex.printStackTrace();
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
        if (b) {
            startTimer();
        } else {
            stopTimer();
        }
    }

    private void setFiltering(boolean b) {
        jFilterCheckbox.setSelected(b);
        searcher.setIsFilter(b);
        jHighlightAllCheckbox.setEnabled(!b);
        if (b && jSearchTextField.getText() != null && jSearchTextField.getText().length() > 0) {
            startSearch();
        }
    }

    private void setTraceFont(String name, String size) {
        jTraceTextArea.setFont(new Font(name, 0, Integer.parseInt(size)));
    }

    private void setFlashLogFile(String filen) {
        fileName = filen;
    }

    private void setDialogLocation(String x, String y, String w, String h) {
        this.setLocation(Integer.parseInt(x), Integer.parseInt(y));
        this.setSize(Integer.parseInt(w), Integer.parseInt(h));
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jAutorefreshCheckBox;
    private javax.swing.JButton jClearSearchButton;
    private javax.swing.JButton jClearTraceButton;
    private javax.swing.JCheckBox jFilterCheckbox;
    private javax.swing.JTextField jFlashLogTextField;
    private javax.swing.JTextField jFontNameTextField;
    private javax.swing.JTextField jFontSizeTextField;
    private javax.swing.JCheckBox jHighlightAllCheckbox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton jOKButton;
    private javax.swing.JCheckBox jOnTopCheckbox;
    private javax.swing.JButton jOptionsButton;
    private javax.swing.JDialog jOptionsDialog;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jSearchTextField;
    private javax.swing.JTextArea jTraceTextArea;
    private javax.swing.JLabel jWarnLabel;
    private javax.swing.JCheckBox jWordWrapCheckbox;
    private java.awt.Label label1;
    // End of variables declaration//GEN-END:variables
    
    private Timer t;
    
    private WordSearcher searcher;

    //public static String fileName = "C:\\Documents and Settings\\admin\\Application Data\\Macromedia\\Flash Player\\Logs\\flashlog.txt";
    public static String fileName = "flashlog.txt";

    private JScrollBar vbar;
    
    private String traceContent;

    private Properties props;

    private File settingsFile = new File("tracer.properties");

    private int traceLinesCount;

    private TracerForm jMainFrame;
    
}
