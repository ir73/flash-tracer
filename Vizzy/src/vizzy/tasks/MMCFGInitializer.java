/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizzy.tasks;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergeil
 */
public class MMCFGInitializer {

    private String traceFileLocation;

    public void init() {

        String logPath = "";
        String osName = System.getProperty("os.name");
        String home = System.getProperty("user.home", "");

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

        if (!home.equals("")) {
            try {
                File mmcfg = new File(home + File.separator + "mm.cfg");
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
                        
                        String[] lines = s.split(System.getProperty("line.separator"));
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

                        osr.write("TraceOutputFileEnable=1" + System.getProperty("line.separator"));
                        osr.write("ErrorReportingEnable=1");

                    } catch (IOException ex) {
                        Logger.getLogger(MMCFGInitializer.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        osr.close();
                    }
//                    Properties props = new Properties();
//                    props.setProperty("TraceOutputFileEnable", "1");
//                    props.setProperty("ErrorReportingEnable", "1");
//                    props.store(new FileOutputStream(mmcfg), "");
                }
            } catch (Exception ex) {
                Logger.getLogger(MMCFGInitializer.class.getName()).log(Level.SEVERE, null, ex);
            }
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
}
