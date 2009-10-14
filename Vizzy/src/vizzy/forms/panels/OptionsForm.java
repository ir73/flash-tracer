/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * OptionsForm.java
 *
 * Created on 14.10.2009, 10:51:10
 */

package vizzy.forms.panels;

import java.io.File;
import javax.swing.JFileChooser;
import vizzy.forms.VizzyForm;
import vizzy.tasks.CheckUpdates;
import vizzy.util.DialogUtils;

/**
 *
 * @author sergeil
 */
public class OptionsForm extends javax.swing.JFrame {

    private final VizzyForm vf;

    /** Creates new form OptionsForm */
    public OptionsForm(VizzyForm vf) {
        this.vf = vf;
        initComponents();

        try {
            this.setIconImage(vf.getIconImage());
        } catch (Exception e) {
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jOKButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jFontSizeTextField = new javax.swing.JTextField();
        jFontComboBox = new javax.swing.JComboBox();
        jRestoreCheckBox = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jLayeredPane3 = new javax.swing.JLayeredPane();
        jUTFCheckBox = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jFlashLogTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jFreqTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLayeredPane4 = new javax.swing.JLayeredPane();
        jNumLinesTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jNumLinesEnabledCheckBox = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jVersionLabel = new javax.swing.JLabel();
        jCheckNowButton = new javax.swing.JButton();
        jUpdatesCheckBox = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Options");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jOKButton.setText("OK");
        jOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOKButtonActionPerformed(evt);
            }
        });

        jLayeredPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Font"));

        jLabel3.setText("Font size:");
        jLabel3.setBounds(290, 20, 80, 14);
        jLayeredPane2.add(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel2.setText("Font:");
        jLabel2.setBounds(10, 20, 260, 14);
        jLayeredPane2.add(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jFontSizeTextField.setBounds(290, 40, 80, 20);
        jLayeredPane2.add(jFontSizeTextField, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jFontComboBox.setModel(vf.fontsModel);
        jFontComboBox.setBounds(10, 40, 270, 22);
        jLayeredPane2.add(jFontComboBox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jRestoreCheckBox.setText("Restore window on trace update if window minimized");
        jRestoreCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRestoreCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLayeredPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(27, 27, 27)
                        .add(jRestoreCheckBox)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(30, 30, 30)
                .add(jRestoreCheckBox)
                .add(18, 18, 18)
                .add(jLayeredPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(188, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("General", jPanel2);

        jLayeredPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("Log file"));

        jUTFCheckBox.setText("read file as UTF-8");
        jUTFCheckBox.setBounds(200, 16, 170, 20);
        jLayeredPane3.add(jUTFCheckBox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel4.setText("flashlog.txt location:");
        jLabel4.setBounds(10, 20, 190, 14);
        jLayeredPane3.add(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jFlashLogTextField.setBounds(10, 40, 250, 20);
        jLayeredPane3.add(jFlashLogTextField, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel1.setText("Log file read frequency (in milliseconds):");
        jLabel1.setBounds(10, 70, 360, 14);
        jLayeredPane3.add(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jFreqTextField.setText("1000");
        jFreqTextField.setBounds(10, 90, 160, 20);
        jLayeredPane3.add(jFreqTextField, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton1.setText("Browse...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonClicked(evt);
            }
        });
        jButton1.setBounds(271, 38, 100, 23);
        jLayeredPane3.add(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLayeredPane4.setBorder(javax.swing.BorderFactory.createTitledBorder("Limit"));
        jNumLinesTextField.setBounds(10, 150, 160, 20);
        jLayeredPane4.add(jNumLinesTextField, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel8.setText("Max amount of bytes to load:");
        jLabel8.setBounds(10, 130, 360, 14);
        jLayeredPane4.add(jLabel8, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel11.setText("<html>This is usually required when the log file gets too big and that might cause slower performance. Setting this limit is not mandatory because Vizzy will set this automatically if runs out of memory.</html>");
        jLabel11.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel11.setBounds(10, 40, 360, 80);
        jLayeredPane4.add(jLabel11, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jNumLinesEnabledCheckBox.setText("Load limited amount of bytes from the end of file only");
        jNumLinesEnabledCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jNumLinesEnabledCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jNumLinesEnabledCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumLinesEnabledCheckBoxActionPerformed(evt);
            }
        });
        jNumLinesEnabledCheckBox.setBounds(10, 20, 360, 15);
        jLayeredPane4.add(jNumLinesEnabledCheckBox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLayeredPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLayeredPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLayeredPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 123, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLayeredPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 184, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Log File", jPanel3);

        jVersionLabel.setText("Current version is: 1.19");

        jCheckNowButton.setText("Check now!");
        jCheckNowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckNowButtonActionPerformed(evt);
            }
        });

        jUpdatesCheckBox.setSelected(true);
        jUpdatesCheckBox.setText("Check for updates at startup");
        jUpdatesCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jUpdatesCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(21, 21, 21)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jUpdatesCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 362, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jCheckNowButton)
                    .add(jVersionLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 207, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(22, 22, 22)
                .add(jUpdatesCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jVersionLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckNowButton)
                .addContainerGap(236, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Updates", jPanel4);

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jTabbedPane1))
                    .add(layout.createSequentialGroup()
                        .add(138, 138, 138)
                        .add(jOKButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 359, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jOKButton)
                    .add(jButton2))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jNumLinesEnabledCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumLinesEnabledCheckBoxActionPerformed
        if (jNumLinesEnabledCheckBox.isSelected()) {
            long n = Long.parseLong(jNumLinesTextField.getText());
            if (!(n > 0)) {
                jNumLinesTextField.setText("1000");
            }
        }
        jNumLinesTextField.setEnabled(jNumLinesEnabledCheckBox.isSelected());
}//GEN-LAST:event_jNumLinesEnabledCheckBoxActionPerformed

    private void jCheckNowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckNowButtonActionPerformed
        vf.checkUpdates(true);
}//GEN-LAST:event_jCheckNowButtonActionPerformed

    private void jOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOKButtonActionPerformed
        closeOptions();
    }//GEN-LAST:event_jOKButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        dispose();
    }//GEN-LAST:event_formWindowClosing

    private void browseButtonClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonClicked
        File openFile = new File(jFlashLogTextField.getText());
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(openFile);
        while (true) {
            int choice = fileChooser.showOpenDialog(this);
            if (choice == JFileChooser.APPROVE_OPTION) {
                openFile = fileChooser.getSelectedFile();
                jFlashLogTextField.setText(openFile.getAbsolutePath());
                break;
            } else {
                break;
            }
        }
    }//GEN-LAST:event_browseButtonClicked

    private void cancelClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelClicked
        dispose();
    }//GEN-LAST:event_cancelClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jCheckNowButton;
    private javax.swing.JTextField jFlashLogTextField;
    private javax.swing.JComboBox jFontComboBox;
    private javax.swing.JTextField jFontSizeTextField;
    private javax.swing.JTextField jFreqTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JLayeredPane jLayeredPane3;
    private javax.swing.JLayeredPane jLayeredPane4;
    private javax.swing.JCheckBox jNumLinesEnabledCheckBox;
    private javax.swing.JTextField jNumLinesTextField;
    private javax.swing.JButton jOKButton;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JCheckBox jRestoreCheckBox;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JCheckBox jUTFCheckBox;
    private javax.swing.JCheckBox jUpdatesCheckBox;
    private javax.swing.JLabel jVersionLabel;
    // End of variables declaration//GEN-END:variables

    private void closeOptions() {
        vf.setTraceFont((String)jFontComboBox.getSelectedItem(), jFontSizeTextField.getText());

        vf.jTraceTextArea.setText("");
        vf.setFlashLogFile(jFlashLogTextField.getText());
        if (vf.logType == 0) {
            vf.setCurrentLogFile(vf.flashLogFileName);
        }
        vf.setCheckUpdates(jUpdatesCheckBox.isSelected());
        vf.setMaxNumLinesEnabled(jNumLinesEnabledCheckBox.isSelected());
        vf.setMaxNumLines(jNumLinesTextField.getText());
        vf.setUTF(jUTFCheckBox.isSelected());
        vf.setRefreshFreq(jFreqTextField.getText());
        vf.setAutoRefresh(vf.jAutorefreshCheckBox.isSelected());
        vf.setAlwaysOnTop(vf.jOnTopCheckbox.isSelected());
        vf.setRestoreOnUpdate(jRestoreCheckBox.isSelected());


        vf.recentHash = null;
        vf.createLoadTimerTask().run();
        if (vf.isAutoRefresh) {
            vf.startTimer();
        } else {
            vf.stopTimer();
        }

        dispose();
    }

    private void initVars() {
        jUTFCheckBox.setSelected(vf.isUTF);
        jUpdatesCheckBox.setSelected(vf.isCheckUpdates);
        jNumLinesEnabledCheckBox.setSelected(vf.maxNumLinesEnabled);
        jNumLinesTextField.setText(String.valueOf(vf.maxNumLines));
        jRestoreCheckBox.setSelected(vf.restoreOnUpdate);

        jFontComboBox.setSelectedItem(vf.jTraceTextArea.getFont().getName());
        jFontSizeTextField.setText(String.valueOf(vf.jTraceTextArea.getFont().getSize()));
        jFlashLogTextField.setText(vf.flashLogFileName);
        jFreqTextField.setText(String.valueOf(vf.refreshFreq));
        jVersionLabel.setText("Current version is: " + CheckUpdates.VERSION);
        jNumLinesTextField.setEnabled(jNumLinesEnabledCheckBox.isSelected());
    }

    @Override
    public void setVisible(boolean val) {
        if (val) {
            initVars();
        }
        super.setVisible(val);
    }

}
