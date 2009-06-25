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
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import vizzy.util.FileUtil;

/**
 *
 * @author sergeil
 */
public class CheckUpdates extends Thread {

    public static final String VERSION = "1.24";
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
                            "Would you like to download it to your machine?\n" +
                            "Note: if you do not want to receive update notifications anymore, you can turn " +
                            "it off in Options menu.",
                            "Info",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
                    if (reply == JOptionPane.YES_OPTION) {
                        downloadNewVersion(newVer);
//                        try {
//
//                            new BrowserLauncher().openURLinBrowser("http://code.google.com/p/flash-tracer/downloads/list");
//                        } catch (BrowserLaunchingInitializingException ex) {
//                            JOptionPane.showMessageDialog(cmp, "Cannot open browser! Please go manually to " +
//                                    " http://code.google.com/p/flash-tracer/downloads/list", "Error", JOptionPane.ERROR_MESSAGE);
//                        } catch (UnsupportedOperatingSystemException ex) {
//                            JOptionPane.showMessageDialog(cmp, "Cannot open browser! Please go manually to " +
//                                    " http://code.google.com/p/flash-tracer/downloads/list", "Error", JOptionPane.ERROR_MESSAGE);
//                        }
                    }
                } else {
                    if (reportIfOk) {
                        JOptionPane.showMessageDialog(cmp, "You have the latest version installed!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } catch (Exception ex) {
//            Logger.getLogger(VizzyForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void downloadNewVersion() {
        try {
            URL u = new URL("http://code.google.com/p/flash-tracer/downloads/list");
            URLConnection openConnection = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();
            String r = response.toString();
        } catch (Exception ex) {
            Logger.getLogger(CheckUpdates.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void downloadNewVersion(String newVer) {
        String filename = "Vizzy_v" + newVer + ".zip";
        String fileUrl = "http://flash-tracer.googlecode.com/files/" + filename;
        System.out.println("f " + fileUrl);
        try {

            byte[] bytes = new byte[1024];
            URL u = new URL(fileUrl);
            URLConnection openConnection = u.openConnection();
            InputStream isr = openConnection.getInputStream();

            File tmpFile = File.createTempFile("vizzy", ".zip");
            FileOutputStream fos = new FileOutputStream(tmpFile);
            int len = 0;
            while ((len = isr.read(bytes)) != -1) {
                fos.write(bytes, 0, len);
            }

            fos.close();
            isr.close();

            File saveFile = new File(filename);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(saveFile);
            while (true) {
                int choice = fileChooser.showSaveDialog(cmp);
                if (choice == JFileChooser.APPROVE_OPTION) {
                    File chosen = fileChooser.getSelectedFile();
                    saveFile = chosen;
                    break;
                } else {
                    break;
                }
            }

            FileUtil.copyfile(tmpFile.getAbsolutePath(), saveFile.getAbsolutePath());

//            JOptionPane.showMessageDialog(cmp, "Vizzy update has beed downloaded to this folder: " +
//                    f.getAbsolutePath() + ".\nClose Vizzy, unpack it and replace current version!", "Error", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            Logger.getLogger(CheckUpdates.class.getName()).log(Level.SEVERE, null, ex);

            JOptionPane.showMessageDialog(cmp, "Failed to download update. Press OK to go to the web-site" +
                    " and to download new version manually!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            try {
                new BrowserLauncher().openURLinBrowser("http://code.google.com/p/flash-tracer/downloads/list");
            } catch (BrowserLaunchingInitializingException ex1) {
                Logger.getLogger(CheckUpdates.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (UnsupportedOperatingSystemException ex1) {
                Logger.getLogger(CheckUpdates.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }

    }
}
