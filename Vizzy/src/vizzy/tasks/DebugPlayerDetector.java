/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizzy.tasks;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author sergeil
 */
public class DebugPlayerDetector {

    public DebugPlayerDetector() {
    }

    public void offerDetection() {
        Object[] options = {"Yes",
            "No",};
        int reply = JOptionPane.showOptionDialog(null, "Debug Flash Player detection has not been\n"
                + "performed yet. Would you like to perform it now?",
                "Debug Flash Player Detection",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (reply == JOptionPane.YES_OPTION) {
            detect();
        }
    }

    private void detect() {
        try {
            String p = getHtmlFile();
            new BrowserLauncher().openURLinBrowser(p);
        } catch (BrowserLaunchingInitializingException ex1) {
            Logger.getLogger(CheckUpdates.class.getName()).log(Level.SEVERE, null, ex1);
        } catch (UnsupportedOperatingSystemException ex1) {
            Logger.getLogger(CheckUpdates.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    private String getHtmlFile() {
        String cp = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if (cp.lastIndexOf('/') != -1) {
            cp = cp.substring(0, cp.lastIndexOf('/') + 1);
        } else {
            cp = "";
        }
        File f = new File(cp + "fp-detect/fp-detect.html");
        String p = f.getAbsolutePath();
        p = "file://" + p.replaceAll("\\\\", "/");
        return p;
    }
}
