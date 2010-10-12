/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AboutPanel.java
 *
 * Created on 14.10.2009, 16:17:17
 */

package vizzy.forms.panels;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.net.URI;
import vizzy.controller.VizzyController;
import vizzy.model.SettingsModel;

/**
 *
 * @author sergeil
 */
public class AboutPanel extends javax.swing.JFrame {

    private VizzyController controller;
    private SettingsModel settings;

    /** Creates new form AboutPanel */
    public AboutPanel(Rectangle rect, VizzyController controller, SettingsModel settings) {
        this.controller = controller;
        this.settings = settings;
        initComponents();
        initSizeAndPosition(rect);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("About Vizzy");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                onWIndowClose(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("<html>Vizzy Flash Tracer<br>2007 - 2010  ©<br>Developed by Sergei Ledvanov</html>");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel2.setForeground(new java.awt.Color(0, 51, 204));
        jLabel2.setText("sergei.ledvanov@gmail.com");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                emailClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                onMouseEntered(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(0, 51, 204));
        jLabel3.setText("Product website");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                websiteClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                onMouseEntered(evt);
            }
        });

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("<html>Please consider donating $5 so I could drink more beer this weekend</html>");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                donateClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                onMouseEntered(evt);
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
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 237, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 253, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(109, 109, 109)
                        .add(jButton1))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 253, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .add(5, 5, 5)
                .add(jLabel3)
                .add(18, 18, 18)
                .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jButton1)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void websiteClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_websiteClicked
        try {
            if (Desktop.isDesktopSupported())
                Desktop.getDesktop().browse(new URI("http://code.google.com/p/flash-tracer"));
        } catch (Exception ex1) {

        }
    }//GEN-LAST:event_websiteClicked

    private void emailClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emailClicked
        try {
            if (Desktop.isDesktopSupported())
                Desktop.getDesktop().mail(new URI("mailto:sergei.ledvanov@gmail.com"));
        } catch (Exception ex1) {
        }
    }//GEN-LAST:event_emailClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        controller.aboutOKClick();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void onMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onMouseEntered
        evt.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_onMouseEntered

    private void donateClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_donateClicked
        try {
            if (Desktop.isDesktopSupported())
                Desktop.getDesktop().browse(new URI("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=JPVRL8G9JYAVL&lc=EE&item_name=Vizzy%20Flash%20Tracer&currency_code=EUR&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted"));
        } catch (Exception ex1) {
        }
    }//GEN-LAST:event_donateClicked

    private void onWIndowClose(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onWIndowClose
        controller.aboutOKClick();
    }//GEN-LAST:event_onWIndowClose

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables

    private void initSizeAndPosition(Rectangle rect) {
        try {
            this.setIconImage(settings.getAppIcon());
        } catch (Exception e) {
        }
        int x = (int)(rect.getX() + rect.getWidth() / 2 - getWidth( )/ 2);
        int y = (int)(rect.getY() + rect.getHeight() / 2 - getHeight()/ 2);
        setLocation(x, y);
    }

    @Override
    public void dispose() {
        controller = null;
        settings = null;
        super.dispose();
    }

}
