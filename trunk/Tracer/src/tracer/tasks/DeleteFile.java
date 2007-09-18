/*
 * DeleteFile.java
 *
 * Created on 21 March 2007, 20:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tracer.tasks;
 
import java.io.File;

/**
 *
 * @author Admin
 */
public class DeleteFile {
    
    /** Creates a new instance of DeleteFile */
    public DeleteFile(String file) {
        try {
            
            File bkup = new File(file);
            
            if (bkup.exists()) {
                bkup.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
