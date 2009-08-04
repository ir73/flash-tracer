/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizzy.tasks;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergeil
 */
public class MMCFGInitializer {

    private boolean mmcfgCreated;
    private String traceFileLocation;

    public void init() {

        String ls = System.getProperty("line.separator");
        String logPath = "";
        String osName = System.getProperty("os.name");
        String home = System.getProperty("user.home", "");

        if (osName == null) {
            return;
        }

        if (osName.indexOf("Vista") > -1) {
            logPath = home + File.separator + "AppData" + File.separator + "Roaming" + File.separator + "Macromedia" + File.separator + "Flash Player" + File.separator + "Logs" + File.separator + "flashlog.txt";
        } else if (osName.indexOf("Windows") > -1) {
            logPath = home + File.separator + "Application Data" + File.separator + "Macromedia" + File.separator + "Flash Player" + File.separator + "Logs" + File.separator + "flashlog.txt";
        } else if (osName.indexOf("Mac") > -1) {
            logPath = home + "/Library/Preferences/Macromedia/Flash Player/Logs/flashlog.txt";
        } else if (osName.indexOf("Linux") > -1) {
            logPath = home + "/.macromedia/Flash_Player/Logs/flashlog.txt";
        }

        if (!logPath.equals("")) {
            setTraceFileLocation(logPath);
        }

        if (home.equals("")) {
            return;
        }

        try {
            String mmcfgPath = "";
            if (osName.indexOf("Vista") > -1) {
                mmcfgPath = home + File.separator + "mm.cfg";
            } else if (osName.indexOf("Windows") > -1) {
                mmcfgPath = home + File.separator + "mm.cfg";
            } else if (osName.indexOf("Mac") > -1) {
                mmcfgPath = "/Library/Application Support/Macromedia/mm.cfg";
            } else if (osName.indexOf("Linux") > -1) {
                mmcfgPath = home + File.separator + "mm.cfg";
            }

            File mmcfg = new File(mmcfgPath);
            
            if (mmcfg.exists()) {

                FileInputStream fis = null;
                ByteArrayOutputStream bo = null;
                try {
                    fis = new FileInputStream(mmcfg);

                    bo = new ByteArrayOutputStream();
                    byte[] b = new byte[1024];
                    int count = 0;
                    while ((count = fis.read(b)) != -1) {
                        bo.write(b, 0, count);
                    }
                    String s = new String(bo.toByteArray(), "UTF-8");

                    String[] lines = s.split(ls);
                    for (String line : lines) {
                        String[] keys = line.split("=");
                        if (keys[0].equals("TraceOutputFileName")) {
                            setTraceFileLocation(keys[1]);
                            break;
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MMCFGInitializer.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    fis.close();
                    bo.close();
                }

            } else {
                OutputStreamWriter osr = null;
                try {
                    osr = new OutputStreamWriter(new FileOutputStream(mmcfg), "UTF-8");

                    osr.write("TraceOutputFileEnable=1" + ls);
                    osr.write("ErrorReportingEnable=1" + ls);
                    osr.write("PolicyFileLog=1" + ls);
                    osr.write("PolicyFileLogAppend=1");

                    mmcfgCreated = true;
                } catch (IOException ex) {
                    Logger.getLogger(MMCFGInitializer.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    osr.close();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(MMCFGInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the traceFileLocation
     */
    public String getTraceFileLocation() {
        return traceFileLocation;
    }

    /**
     * @param traceFileLocation the traceFileLocation to set
     */
    public void setTraceFileLocation(String traceFileLocation) {
        this.traceFileLocation = traceFileLocation;
    }

    /**
     * @return the mmcfgCreated
     */
    public boolean isMmcfgCreated() {
        return mmcfgCreated;
    }

    /**
     * @param mmcfgCreated the mmcfgCreated to set
     */
    public void setMmcfgCreated(boolean mmcfgCreated) {
        this.mmcfgCreated = mmcfgCreated;
    }
}
