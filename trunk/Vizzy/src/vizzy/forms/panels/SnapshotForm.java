/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SnapshotForm.java
 *
 * Created on 14.10.2009, 11:46:52
 */

package vizzy.forms.panels;

import org.apache.log4j.Logger;
import vizzy.controller.VizzyController;
import vizzy.model.SettingsModel;

/**
 *
 * @author sergeil
 */
public class SnapshotForm extends javax.swing.JFrame {
    private static final Logger log = Logger.getLogger(SnapshotForm.class);

    private VizzyController controller;
    private SettingsModel settings;

    /** Creates new form OptionsForm */
    public SnapshotForm(VizzyController controller, SettingsModel settings) {
        this.controller = controller;
        this.settings = settings;
        initComponents();

        try {
            this.setIconImage(settings.getAppIcon());
        } catch (Exception e) {
//            log.warn("SnapshotForm() ", e);
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Log snapshot");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                onWindowClose(evt);
            }
        });

        jTextArea.setColumns(20);
        jTextArea.setRows(5);
        jScrollPane1.setViewportView(jTextArea);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onWindowClose(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onWindowClose
        controller.snapshotFormsClose(this);
        dispose();
    }//GEN-LAST:event_onWindowClose

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea;
    // End of variables declaration//GEN-END:variables

    public void setWordWrap(boolean b) {
        jTextArea.setLineWrap(b);
    }

    public void init(String text) {
        jTextArea.setText(text);
        jTextArea.setFont(settings.getTraceFont());
        jTextArea.setLineWrap(settings.isWordWrap());
        setVisible(true);
    }

    @Override
    public void dispose() {
        controller = null;
        settings = null;
        super.dispose();
    }


}
