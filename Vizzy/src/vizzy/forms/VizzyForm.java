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

import java.awt.Desktop;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import org.apache.log4j.Logger;
import vizzy.comp.JScrollHighlightPanel;
import vizzy.comp.NewFeaturesPanel;
import vizzy.controller.VizzyController;
import vizzy.listeners.INewFeaturesListener;
import vizzy.listeners.IVizzyView;
import vizzy.model.Conf;
import vizzy.model.SettingsModel;

/**
 *
 * @author sergeil
 */
public class VizzyForm extends javax.swing.JFrame implements IVizzyView {

    private static final Logger log = Logger.getLogger(VizzyForm.class);

    private NewFeaturesPanel newFeaturesPanel;
    private Border searchComboboxBorder;

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jHighlightAllCheckbox = new javax.swing.JCheckBox();
        jFilterCheckbox = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jSearchComboBox = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTraceTextArea = new javax.swing.JTextArea();
        jScrollHighlight = new vizzy.comp.JScrollHighlightPanel();
        jPanel3 = new javax.swing.JPanel();
        jClearTraceButton = new javax.swing.JButton();
        logTypeCombo = new javax.swing.JComboBox();
        jWordWrapCheckbox = new javax.swing.JCheckBox();
        jOnTopCheckbox = new javax.swing.JCheckBox();
        jAutorefreshCheckBox = new javax.swing.JCheckBox();
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
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jLayeredPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));
        jLayeredPane1.setMaximumSize(new java.awt.Dimension(32767, 84));
        jLayeredPane1.setPreferredSize(new java.awt.Dimension(0, 68));

        jHighlightAllCheckbox.setText("Highlight All");
        jHighlightAllCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jHighlightAllCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jHighlightAllCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jHighlightAllCheckboxChecked(evt);
            }
        });
        jHighlightAllCheckbox.setBounds(10, 18, 120, 15);
        jLayeredPane1.add(jHighlightAllCheckbox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jFilterCheckbox.setText("Filter");
        jFilterCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jFilterCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jFilterCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFilterCheckboxChecked(evt);
            }
        });
        jFilterCheckbox.setBounds(130, 18, 80, 15);
        jLayeredPane1.add(jFilterCheckbox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton1.setText("Clear");
        jButton1.setPreferredSize(new java.awt.Dimension(70, 23));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jClearActionPerformed(evt);
            }
        });
        jButton1.setBounds(255, 37, 70, 23);
        jLayeredPane1.add(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jSearchComboBox.setEditable(true);
        jSearchComboBox.setPreferredSize(new java.awt.Dimension(240, 24));
        jSearchComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSearchComboBoxActionPerformed(evt);
            }
        });
        jSearchComboBox.setBounds(9, 37, 240, 24);
        jLayeredPane1.add(jSearchComboBox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jPanel1.add(jLayeredPane1);

        jPanel2.setPreferredSize(new java.awt.Dimension(0, 0));

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
            .add(0, 234, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollHighlight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollHighlight, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2);

        jPanel3.setPreferredSize(new java.awt.Dimension(762, 26));

        jClearTraceButton.setText("Clear Log");
        jClearTraceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jClearTraceButtondeleteActionPerformed(evt);
            }
        });

        logTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Flash Log", "Policy File" }));
        logTypeCombo.setPreferredSize(new java.awt.Dimension(72, 23));
        logTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logTypeComboActionPerformed(evt);
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

        jOnTopCheckbox.setText("Always on Top");
        jOnTopCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jOnTopCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jOnTopCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOnTopCheckboxChecked(evt);
            }
        });

        jAutorefreshCheckBox.setSelected(true);
        jAutorefreshCheckBox.setText("Auto Refresh");
        jAutorefreshCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jAutorefreshCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jAutorefreshCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAutorefreshCheckBoxjAutorefreshCheckboxChecked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .add(jAutorefreshCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 106, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jWordWrapCheckbox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 103, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jOnTopCheckbox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 124, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 68, Short.MAX_VALUE)
                .add(jClearTraceButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(logTypeCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                .add(jAutorefreshCheckBox)
                .add(logTypeCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jClearTraceButton)
                .add(jWordWrapCheckbox)
                .add(jOnTopCheckbox))
        );

        jPanel1.add(jPanel3);

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
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jHighlightAllCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jHighlightAllCheckboxChecked
        if (!settings.isUIActionsAvailable()) return;
        controller.highlightAllClicked(jHighlightAllCheckbox.isSelected(), jSearchComboBox.getSelectedItem());
}//GEN-LAST:event_jHighlightAllCheckboxChecked

    private void jFilterCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFilterCheckboxChecked
        if (!settings.isUIActionsAvailable()) return;
        controller.filterClicked(jFilterCheckbox.isSelected(), jSearchComboBox.getSelectedItem());
}//GEN-LAST:event_jFilterCheckboxChecked

    private void jTraceTextAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTraceTextAreaKeyPressed
        if (!settings.isUIActionsAvailable()) return;
        controller.textAreaKeyPressed(jTraceTextArea.getSelectedText(), evt);
}//GEN-LAST:event_jTraceTextAreaKeyPressed

    private void jAutorefreshCheckBoxjAutorefreshCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAutorefreshCheckBoxjAutorefreshCheckboxChecked
        if (!settings.isUIActionsAvailable()) return;
        controller.autoRefreshClicked(jAutorefreshCheckBox.isSelected());
}//GEN-LAST:event_jAutorefreshCheckBoxjAutorefreshCheckboxChecked

    private void jWordWrapCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jWordWrapCheckboxChecked
        if (!settings.isUIActionsAvailable()) return;
        controller.wordWrapClicked(jWordWrapCheckbox.isSelected());
}//GEN-LAST:event_jWordWrapCheckboxChecked

    private void jOnTopCheckboxChecked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOnTopCheckboxChecked
        if (!settings.isUIActionsAvailable()) return;
        controller.alwaysOnTopClicked(jOnTopCheckbox.isSelected());
}//GEN-LAST:event_jOnTopCheckboxChecked

    private void jClearTraceButtondeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jClearTraceButtondeleteActionPerformed
        if (!settings.isUIActionsAvailable()) return;
        controller.clearTraceClicked();
}//GEN-LAST:event_jClearTraceButtondeleteActionPerformed

    private void jTraceTextAreaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTraceTextAreaMousePressed
        if (!settings.isUIActionsAvailable()) return;
        controller.textAreaMousePressed();
    }//GEN-LAST:event_jTraceTextAreaMousePressed

    private void jTraceTextAreaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTraceTextAreaMouseReleased
        if (!settings.isUIActionsAvailable()) return;
        String selection = jTraceTextArea.getSelectedText();
        controller.textAreaMouseReleased(selection);
    }//GEN-LAST:event_jTraceTextAreaMouseReleased

    private void jClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jClearActionPerformed
        if (!settings.isUIActionsAvailable()) return;
        controller.clearSearchClicked();
}//GEN-LAST:event_jClearActionPerformed

    private void logTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logTypeComboActionPerformed
        if (!settings.isUIActionsAvailable()) return;
        controller.setLogTypeClicked(String.valueOf(logTypeCombo.getSelectedIndex()));
    }//GEN-LAST:event_logTypeComboActionPerformed

    private void menuCopyAllClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCopyAllClicked
        if (!settings.isUIActionsAvailable()) return;
        controller.copyAllClicked(jTraceTextArea.getText());
    }//GEN-LAST:event_menuCopyAllClicked

    private void menuOptionsClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOptionsClicked
        if (!settings.isUIActionsAvailable()) return;
        controller.openOptionsClicked();
    }//GEN-LAST:event_menuOptionsClicked

    private void menuSnapshotClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSnapshotClicked
        if (!settings.isUIActionsAvailable()) return;
        controller.snapshotClicked(jTraceTextArea.getText());
    }//GEN-LAST:event_menuSnapshotClicked

    private void aboutClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutClicked
        if (!settings.isUIActionsAvailable()) return;
        controller.aboutOpenClicked();
    }//GEN-LAST:event_aboutClicked

    private void jTraceTextAreaMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTraceTextAreaMouseMoved
        if (!settings.isUIActionsAvailable()) return;
        controller.textAreaMouseMoved(evt);
    }//GEN-LAST:event_jTraceTextAreaMouseMoved

    private void jTraceTextAreaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTraceTextAreaMouseExited
        if (!settings.isUIActionsAvailable()) return;
        controller.textAreaMouseExited(evt);
    }//GEN-LAST:event_jTraceTextAreaMouseExited

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        if (!settings.isUIActionsAvailable()) return;
        controller.formWindowDeactivated();
    }//GEN-LAST:event_formWindowDeactivated

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (!settings.isUIActionsAvailable()) return;
        controller.onClose();
    }//GEN-LAST:event_formWindowClosing

    private void jTraceTextAreaMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jTraceTextAreaMouseWheelMoved
        if (!settings.isUIActionsAvailable()) return;
        traceAreaScrollBar.dispatchEvent(evt);
        checkNeedScrollDown();
        controller.traceAreaMouseWheel(evt);
    }//GEN-LAST:event_jTraceTextAreaMouseWheelMoved

    private void formWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowLostFocus
        if (!settings.isUIActionsAvailable()) return;
        controller.formWindowDeactivated();
    }//GEN-LAST:event_formWindowLostFocus

    private void jSearchComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSearchComboBoxActionPerformed
        controller.searchComboboxChanged((String)jSearchComboBox.getSelectedItem());
    }//GEN-LAST:event_jSearchComboBoxActionPerformed

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
    public javax.swing.JCheckBox jOnTopCheckbox;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jScrollHighlight;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox jSearchComboBox;
    private javax.swing.JSeparator jSeparator1;
    public javax.swing.JTextArea jTraceTextArea;
    public javax.swing.JCheckBox jWordWrapCheckbox;
    public javax.swing.JComboBox logTypeCombo;
    // End of variables declaration//GEN-END:variables

    private boolean needToScrolldown = true;
    private boolean isCapturingScroll = false;

    private JScrollBar traceAreaScrollBar;
    private SettingsModel settings;
    private VizzyController controller;

    public VizzyForm(VizzyController controller, SettingsModel settings) {
        super("Vizzy Flash Tracer");
        this.controller = controller;
        this.settings = settings;
    }
    @Override
    public void onInit() {
        initComponents();
        searchComboboxBorder = jSearchComboBox.getBorder();
    }
    @Override
    public void onAfterInit() {
        initVars();
        initListeners();
        setVisible(true);
    }

    private void initListeners() {
        traceAreaScrollBar.addMouseListener(new MouseListener() {
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

        traceAreaScrollBar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (isCapturingScroll) {
                    checkNeedScrollDown();
                }
            }
        });
    }

    private void initVars() {
        traceAreaScrollBar = jScrollPane1.getVerticalScrollBar();
        jSearchComboBox.getEditor().getEditorComponent().addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyPressed(KeyEvent e) {
            }
            public void keyReleased(KeyEvent e) {
                if (!settings.isUIActionsAvailable()) return;
                controller.searchKeyReleased((String) jSearchComboBox.getSelectedItem(), e);
            }
        });
        getHighLightScroll().setTa(jTraceTextArea);
    }

    private void checkNeedScrollDown() {
        int sum = traceAreaScrollBar.getValue() + traceAreaScrollBar.getVisibleAmount();
        int am = traceAreaScrollBar.getMaximum();
        if (sum >= am) {
            needToScrolldown = true;
        } else {
            needToScrolldown = false;
        }
    }









    
    @Override
    public void onCustomASEditorChanged(String customASEditor) {
        
    }
    @Override
    public void onRefreshFreqChanged(long refreshFreq) {
        
    }
    @Override
    public void onLogTypeChanged(int logType) {
        logTypeCombo.setSelectedIndex(settings.getLogType());
    }
    @Override
    public void onHightlightAllChanged(boolean hightlightAll) {
        jHighlightAllCheckbox.setSelected(settings.isHightlightAll());
    }
    @Override
    public void onDefaultASEditorChanged(boolean defaultASEditor) {
        
    }
    @Override
    public void onUTFChanged(boolean uTF) {

    }
    @Override
    public void onHighlightStackTraceErrorsChanged(boolean highlightKeywords) {

    }
    @Override
    public void onPolicyLogFileNameChanged(String policyLogFileName) {

    }
    @Override
    public void onFlashLogFileNameChanged(String flashLogFileName) {

    }
    @Override
    public void onFontsChanged(Font[] fonts) {
    }
    @Override
    public void onMainWindowLocationChanged(Rectangle window) {
        setBounds(settings.getMainWindowLocation());
    }
    @Override
    public void onCurrentLogFileChanged(String currentLogFile) {

    }
    @Override
    public void onTraceFontChanged(Font traceFont) {
        jTraceTextArea.setFont(settings.getTraceFont());
    }
    @Override
    public void onSearchKeywordsChanged(String[] searchKeywords, DefaultComboBoxModel searchKeywordsModel) {
        jSearchComboBox.setModel(settings.getSearchKeywordsModel());
    }
    @Override
    public void onFilterChanged(boolean filter) {
        if (!filter) {
            jTraceTextArea.setText(settings.getTraceContent());
        }
        jFilterCheckbox.setSelected(settings.isFilter());
        updateCommaTooltipCheckBox();
        jHighlightAllCheckbox.setEnabled(!settings.isFilter());
    }
    @Override
    public void onAutoRefreshChanged(boolean autoRefresh) {
        jAutorefreshCheckBox.setSelected(settings.isAutoRefresh());
    }
    @Override
    public void onWordWrapChanged(boolean wordWrap) {
        jWordWrapCheckbox.setSelected(settings.isWordWrap());
        jTraceTextArea.setLineWrap(settings.isWordWrap());
    }
    @Override
    public void onCheckUpdatesChanged(boolean checkUpdates) {

    }
    @Override
    public void onMaxNumLinesEnabledChanged(boolean maxNumLinesEnabled) {

    }
    @Override
    public void onMaxNumLinesChanged(long maxNumLines) {

    }
    @Override
    public void onRestoreOnUpdateChanged(boolean restoreOnUpdate) {

    }
    @Override
    public void onAlwaysOnTopChanged(boolean alwaysONTop) {
        jOnTopCheckbox.setSelected(settings.isAlwaysOnTop());
        setAlwaysOnTop(settings.isAlwaysOnTop());
    }
    @Override
    public void onLastUpdateDateChanged(Date lastUpdateDate) {

    }
    @Override
    public void onDetectPlayerChanged(boolean detectPlayer) {

    }
    @Override
    public void onDefaultFontChanged(String defaultFont) {

    }
    @Override
    public void onSetFontNamesChanged(String[] fontNames) {

    }
    @Override
    public void onRecentHashChanged(String recentHash) {

    }
    @Override
    public void onTraceContentChanged(String traceContent) {
        if ("".equals(traceContent) || traceContent == null) {
            jTraceTextArea.setText(settings.getTraceContent());
            needToScrolldown = true;
            ((JScrollHighlightPanel) jScrollHighlight).setIndexes(null);
        } else {
            boolean filteringOff = !(settings.getSearcher().isWasSearching() && settings.getSearcher().isFilter());
            if (filteringOff) {
                jTraceTextArea.setText(traceContent);
            }
            if (needToScrolldown) {
                jTraceTextArea.setCaretPosition(jTraceTextArea.getDocument().getLength());
            }
            if (settings.isRestoreOnUpdate()) {
                setExtendedState(JFrame.NORMAL);
                repaint();
            }
        }
    }
    @Override
    public JTextArea getTextArea() {
        return jTraceTextArea;
    }
    
    @Override
    public void onSearch(String word, int offset, boolean scrollToSearchResult) {
        if (settings.getSearcher().isFilter()) {
            try {
                jTraceTextArea.setText(settings.getSearcher().filter(settings.getTraceContent(), word));
            } catch (Exception ex) {
                log.warn("onSearch()", ex);
            }
            updateSearchResults(false, false);
        } else {
            if (offset != -1) {
                updateSearchResults(true, true);
                if (scrollToSearchResult) {
                    needToScrolldown = false;
                    try {
                        jTraceTextArea.scrollRectToVisible(jTraceTextArea
                                .modelToView(offset));
                    } catch (BadLocationException e) {
                    }
                }
            } else {
                updateSearchResults(true, false);
            }
        }
    }
    @Override
    public void onOptionsClosed() {
//        jTraceTextArea.setText("");
    }
    @Override
    public void onHighlightTraceKeyword(String text) {
        jSearchComboBox.setSelectedItem(text);
    }

    @Override
    public void onSearchCleared() {
        jTraceTextArea.setText(settings.getTraceContent());
        jSearchComboBox.setSelectedItem("");
        updateSearchResults(false, false);
        ((JScrollHighlightPanel)jScrollHighlight).setIndexes(null);
    }

    @Override
    public void onAlwaysOnTopUIChanged(boolean alwaysOnTop) {
        setAlwaysOnTop(alwaysOnTop);
    }

    @Override
    public JScrollHighlightPanel getHighLightScroll() {
        return (JScrollHighlightPanel)jScrollHighlight;
    }
    @Override
    public void onEnableTraceClickChanged(boolean enableStackTraceClick) {
    }
    @Override
    public void onEnableCodePopupChanged(boolean enableCodePopup) {
    }
    @Override
    public void onShowNewFeaturesPanel() {
        if (newFeaturesPanel == null) {
            newFeaturesPanel = new NewFeaturesPanel("<html>Did you know that it is possible to explore stack"
                    + " trace source files directly from Vizzy? "
                    + "<a href=\"" + Conf.URL_PROJECT_HOME + "wiki/Features\">Read more...</a></html>",
                    new INewFeaturesListener() {
                public void click() {
                    removeNewFeaturesPanel();
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(new URI(Conf.URL_PROJECT_HOME + "wiki/Features"));
                        } catch (Exception ex) {
//                            log.warn("browse()", ex);
                        }
                    }
                }
                public void close() {
                    removeNewFeaturesPanel();
                }
            });
            jPanel1.add(newFeaturesPanel, 1);
        }
        jPanel1.validate();
        jPanel1.repaint();
    }
    @Override
    public void onNewFeaturesPanelShownChanged(boolean wasNewFeaturesPanelShown) {
    }


    private void removeNewFeaturesPanel() {
        controller.newFeaturesPanelClosed();
        newFeaturesPanel.dispose();
        jPanel1.remove(newFeaturesPanel);
        newFeaturesPanel = null;
        jPanel1.validate();
        jPanel1.repaint();
    }

    private void updateCommaTooltipCheckBox() {
        if (settings.isFilter()) {
            jSearchComboBox.setToolTipText("Use comma to separate multiple keywords");
        } else {
            jSearchComboBox.setToolTipText(null);
        }
    }

    private void updateSearchResults(boolean show, boolean found) {
        if (!show) {
            jSearchComboBox.setBorder(searchComboboxBorder);
        } else {
            if (found) {
                jSearchComboBox.setBorder(BorderFactory.createLineBorder(Conf.FOUND_SEARCH_COMBO_COLOR, 2));
            } else {
                jSearchComboBox.setBorder(BorderFactory.createLineBorder(Conf.NOTFOUND_SEARCH_COMBO_COLOR, 2));
            }
        }
    }

}
