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

    private boolean isDebugOCX;

    public DebugPlayerDetector() {
    }

    public void start() {
        Object[] options = {"Yes",
                        "No",};
        int reply = JOptionPane.showOptionDialog(null, "Debug Flash Player detection has not been performed yet. " +
                "Would you like to perform it now?",
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


    /**
     * @return the isDebugOCX
     */
    public boolean isIsDebugOCX() {
        return isDebugOCX;
    }

    /**
     * @param isDebugOCX the isDebugOCX to set
     */
    public void setIsDebugOCX(boolean isDebugOCX) {
        this.isDebugOCX = isDebugOCX;
    }

    private void detect() {
         try {
            File f = new File("fp-detect/fp-detect.html");
            String p = f.getAbsolutePath();
            p = p.replaceAll("\\\\", "/");
            new BrowserLauncher().openURLinBrowser("file://" + p);
        } catch (BrowserLaunchingInitializingException ex1) {
            Logger.getLogger(CheckUpdates.class.getName()).log(Level.SEVERE, null, ex1);
        } catch (UnsupportedOperatingSystemException ex1) {
            Logger.getLogger(CheckUpdates.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }
}
