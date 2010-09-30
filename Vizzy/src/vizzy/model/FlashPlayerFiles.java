/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vizzy.model;

/**
 *
 * @author sergeil
 */
public class FlashPlayerFiles {

    private String logPath;
    private String policyPath;
    
    public FlashPlayerFiles(String logPath, String policyPath) {
        this.logPath = logPath;
        this.policyPath = policyPath;
    }

    public FlashPlayerFiles() {
        super();
    }

    /**
     * @return the logPath
     */
    public String getLogPath() {
        return logPath;
    }

    /**
     * @param logPath the logPath to set
     */
    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    /**
     * @return the policyPath
     */
    public String getPolicyPath() {
        return policyPath;
    }

    /**
     * @param policyPath the policyPath to set
     */
    public void setPolicyPath(String policyPath) {
        this.policyPath = policyPath;
    }

}
