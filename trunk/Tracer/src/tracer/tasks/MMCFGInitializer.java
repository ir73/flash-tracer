/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tracer.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
                    try {
                      BufferedReader input =  new BufferedReader(new FileReader(mmcfg));
                      try {
                        String line = null; //not declared within while loop
                        while (( line = input.readLine()) != null){
                            String[] keys = line.split("=");
                            if (keys[0].equals("TraceOutputFileName")) {
                                setTraceFileLocation(keys[1]);
                                break;
                            }
                        }
                      }
                      finally {
                        input.close();
                      }
                    }
                    catch (IOException ex){
                      ex.printStackTrace();
                    }
                } else {
                    Properties props = new Properties();
                    props.setProperty("TraceOutputFileEnable", "1");
                    props.setProperty("ErrorReportingEnable", "1");
                    props.store(new FileOutputStream(mmcfg), "");
                }
            } catch (IOException ex) {
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
