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
public class FlashAndPolicyLogInitializer {

    private String policyFileLocation;
    private String traceFileLocation;
    
    public void init() {
        
        String policyPath = null;
        String logPath = null;
        String osName = System.getProperty("os.name");
        String home = System.getProperty("user.home", "");

        if (osName == null || home == null) {
            return;
        }

        if (osName.indexOf("Vista") > -1) {
            logPath = home + File.separator + "AppData" + File.separator + "Roaming" + File.separator + "Macromedia" + File.separator + "Flash Player" + File.separator + "Logs" + File.separator + "flashlog.txt";
            policyPath = home + File.separator + "AppData" + File.separator + "Roaming" + File.separator + "Macromedia" + File.separator + "Flash Player" + File.separator + "Logs" + File.separator + "policyfiles.txt";
        } else if (osName.indexOf("Windows") > -1) {
            logPath = home + File.separator + "Application Data" + File.separator + "Macromedia" + File.separator + "Flash Player" + File.separator + "Logs" + File.separator + "flashlog.txt";
            policyPath = home + File.separator + "Application Data" + File.separator + "Macromedia" + File.separator + "Flash Player" + File.separator + "Logs" + File.separator + "policyfiles.txt";
        } else if (osName.indexOf("Mac") > -1) {
            logPath = home + "/Library/Preferences/Macromedia/Flash Player/Logs/flashlog.txt";
            policyPath = home + "/Library/Preferences/Macromedia/Flash Player/Logs/policyfiles.txt";
        } else if (osName.indexOf("Linux") > -1) {
            logPath = home + "/.macromedia/Flash_Player/Logs/flashlog.txt";
            policyPath = home + "/.macromedia/Flash_Player/Logs/policyfiles.txt";
        }

        if (logPath != null) {
            setTraceFileLocation(logPath);
        }

        if (policyPath != null) {
            setPolicyFileLocation(policyPath);
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
     * @return the policyFileLocation
     */
    public String getPolicyFileLocation() {
        return policyFileLocation;
    }

    /**
     * @param policyFileLocation the policyFileLocation to set
     */
    public void setPolicyFileLocation(String policyFileLocation) {
        this.policyFileLocation = policyFileLocation;
    }
}
