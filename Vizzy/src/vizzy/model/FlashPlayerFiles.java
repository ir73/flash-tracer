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
    
    public FlashPlayerFiles(String logPath) {
        this.logPath = logPath;
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

}
