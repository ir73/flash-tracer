/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vizzy.tasks;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import vizzy.util.DialogUtils;
import vizzy.util.FileUtil;

/**
 *
 * @author sergeil
 */
public class DebugPlayerDetector {

    private boolean isDebugOCX;

    public DebugPlayerDetector() {
    }

    public void start() {
        String osName = System.getProperty("os.name");
        String windir = System.getenv("windir");

        if (osName == null) {
            return;
        }

        if (osName.indexOf("Windows") > -1) {
            File dir = new File(windir + "\\system32\\Macromed\\Flash");
            File[] ocxFiles = dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".ocx") && name.startsWith("FlDbg");
                }
            });
            setIsDebugOCX(ocxFiles.length > 0);
        } else {
            return;
        }

        if (!isDebugOCX) {
            Object[] options = {"Yes",
                "No",};

            int reply = JOptionPane.showOptionDialog(null, "No debug player detected for " +
                    "Internet Explorer. Would you like Vizzy to download it for you?\n" +
                    "After download, please run 'uninstall_flash_player.exe' first to remove previously installed" +
                    " versions.",
                    "Debug Flash Player",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (reply == JOptionPane.YES_OPTION) {
                try {
                    DialogUtils.showDialog("Downloading uninstall_flash_player.exe...");
                    downloadDebugFlash("http://download.macromedia.com/pub/flashplayer/current/", "uninstall_flash_player.exe");
                    DialogUtils.showDialog("Downloading flashplayer_10_ax_debug.exe...");
                    downloadDebugFlash("http://download.macromedia.com/pub/flashplayer/updaters/10/", "flashplayer_10_ax_debug.exe");
                    DialogUtils.showDialog("Downloading flashplayer_10_plugin_debug.exe...");
                    downloadDebugFlash("http://download.macromedia.com/pub/flashplayer/updaters/10/", "flashplayer_10_plugin_debug.exe");
                    
                } catch (Exception ex) {
                    Logger.getLogger(CheckUpdates.class.getName()).log(Level.SEVERE, null, ex);

                    JOptionPane.showMessageDialog(null, "Failed to download flash players. Press OK to go to " +
                            "http://www.adobe.com/support/flashplayer/downloads.html" +
                            " and to download debug flash players manually.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    
                    try {
                        new BrowserLauncher().openURLinBrowser("http://www.adobe.com/support/flashplayer/downloads.html");
                    } catch (BrowserLaunchingInitializingException ex1) {
                        Logger.getLogger(CheckUpdates.class.getName()).log(Level.SEVERE, null, ex1);
                    } catch (UnsupportedOperatingSystemException ex1) {
                        Logger.getLogger(CheckUpdates.class.getName()).log(Level.SEVERE, null, ex1);
                    }

                } finally {
                    DialogUtils.closeDialog();
                }
            }
        }
    }



    private void downloadDebugFlash(String url, String filename) throws Exception {
        String fileUrl = url + filename;
        System.out.println("f " + fileUrl);


        byte[] bytes = new byte[1024];
        URL u = new URL(fileUrl);
        URLConnection openConnection = u.openConnection();
        InputStream isr = openConnection.getInputStream();

        File tmpFile = File.createTempFile(filename, "");
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
        DialogUtils.setVisible(false);
        while (true) {
            int choice = fileChooser.showSaveDialog(null);
            if (choice == JFileChooser.APPROVE_OPTION) {
                File chosen = fileChooser.getSelectedFile();
                saveFile = chosen;
                break;
            } else {
                break;
            }
        }
        DialogUtils.setVisible(true);

        FileUtil.copyfile(tmpFile.getAbsolutePath(), saveFile.getAbsolutePath());
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

    

}
