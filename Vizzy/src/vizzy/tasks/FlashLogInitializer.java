/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vizzy.tasks;

import java.io.File;

/**
 *
 * @author sergeil
 */
public class FlashLogInitializer {

    private String traceFileLocation;
    
    public void init() {
        
        String logPath = null;
        String osName = System.getProperty("os.name");
        String home = System.getProperty("user.home", "");

        if (osName == null || home == null) {
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

        if (logPath != null) {
            setTraceFileLocation(logPath);
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
