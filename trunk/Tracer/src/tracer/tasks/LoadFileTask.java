/*
 * LoadFileTask.java
 *
 * Created on 21 March 2007, 19:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tracer.tasks;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import tracer.forms.TracerForm;

/**
 *
 * @author Admin
 */
public class LoadFileTask extends TimerTask {

    private TracerForm tracerForm;
    private String fileName;
    private long maxNumLines;
    private boolean numMaxLinesEnabled;
    private boolean isUTF;

    /** Creates a new instance of LoadFileTask */
    public LoadFileTask(String fileName, long maxNumLines, boolean numMaxLinesEnabled, boolean isUTF, TracerForm tracerForm) {
        this.fileName = fileName;
        this.maxNumLines = maxNumLines;
        this.numMaxLinesEnabled = numMaxLinesEnabled;
        this.tracerForm = tracerForm;
        this.isUTF = isUTF;
    }

    public void run() {

        ByteArrayOutputStream bo = null;
        RandomAccessFile raf = null;
        try {
            File inputFile = new File(fileName);
            raf = new RandomAccessFile(inputFile, "r");
            bo = new ByteArrayOutputStream();

            int len = (int) raf.length();
            if (numMaxLinesEnabled && len > maxNumLines) {
                int v = (int) (len - maxNumLines);
                raf.seek(v);
            }

            byte[] b = new byte[4096];
            int count = 0;
            while ((count = raf.read(b)) != -1) {
                bo.write(b, 0, count);
            }

            String s = null;
            if (isUTF) {
                s = new String(bo.toByteArray(), "UTF-8");
            } else {
                s = new String(bo.toByteArray());
            }

            tracerForm.onFileRead(s);

        } catch (OutOfMemoryError ex) {
            tracerForm.onOutOfMemory();
            Logger.getLogger(LoadFileTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            //Logger.getLogger(LoadFileTask.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bo.close();
                raf.close();
            } catch (Exception ex) {
                //Logger.getLogger(LoadFileTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
