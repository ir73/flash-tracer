/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizzy.tasks;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;

/**
 *
 * @author sergeil
 */
public class CheckUpdates extends Thread {

    public static final String VERSION = "1.23";
    private static final String WEBSITE_UPDATE_PHRASE = "Current version is: ";
    private Component cmp;
    private boolean reportIfOk;

    public CheckUpdates(Component cmp, boolean reportIfOk) {
        this.cmp = cmp;
        this.reportIfOk = reportIfOk;
    }

    @Override
    public void run() {
        try {
            URL u = new URL("http://code.google.com/p/flash-tracer/");
            URLConnection openConnection = u.openConnection();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    openConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
//                System.out.println(inputLine);
            }
            in.close();

            String r = response.toString();

            int i = r.indexOf(WEBSITE_UPDATE_PHRASE);
            int i2 = r.indexOf(";", i);
            if (i > -1) {
                String newVer = r.substring(i + WEBSITE_UPDATE_PHRASE.length(), i2);
                double newVerd = Double.parseDouble(newVer);
                double verd = Double.parseDouble(VERSION);
                if (newVerd > verd) {
                    Object[] options = {"Yes",
                        "No",};

                    int reply = JOptionPane.showOptionDialog(cmp, "New version is availible (" + newVer + "). " +
                            "Would you like to open your browser and update now? \n\n " +
                            "(You can turn off automatic updates in Options menu)",
                            "Info",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
                    if (reply == JOptionPane.YES_OPTION) {
                        try {
                            new BrowserLauncher().openURLinBrowser("http://code.google.com/p/flash-tracer/downloads/list");
                        } catch (BrowserLaunchingInitializingException ex) {
                            JOptionPane.showMessageDialog(cmp, "Cannot open browser! Please go manually to " +
                                    " http://code.google.com/p/flash-tracer/downloads/list", "Error", JOptionPane.ERROR_MESSAGE);
                        } catch (UnsupportedOperatingSystemException ex) {
                            JOptionPane.showMessageDialog(cmp, "Cannot open browser! Please go manually to " +
                                    " http://code.google.com/p/flash-tracer/downloads/list", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    if (reportIfOk) {
                        JOptionPane.showMessageDialog(cmp, "Your version is up-to-date!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } catch (Exception ex) {
//            Logger.getLogger(VizzyForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
