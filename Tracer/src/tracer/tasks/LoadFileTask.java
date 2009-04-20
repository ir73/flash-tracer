/*
 * LoadFileTask.java
 *
 * Created on 21 March 2007, 19:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tracer.tasks;

import java.io.File;
import java.io.FileReader;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    /** Creates a new instance of LoadFileTask */
    public LoadFileTask(String fileName, long maxNumLines, boolean numMaxLinesEnabled, TracerForm tracerForm) {
        this.fileName = fileName;
        this.maxNumLines = maxNumLines;
        this.numMaxLinesEnabled = numMaxLinesEnabled;
        this.tracerForm = tracerForm;
    }
 

    public void run() {


        
        try {
            //System.out.println(fileName);
            File inputFile = new File(fileName);
            FileReader fr;
            fr = new FileReader(inputFile);

            char[] c = null;
            int l1 = (int)inputFile.length();
            if (numMaxLinesEnabled && l1 > maxNumLines) {
                c = new char[(int)maxNumLines];
                //System.out.println(l1 + ", " + maxNumLines);
                fr.skip(l1 - maxNumLines);
            } else {
                c = new char[(int)inputFile.length()];
            }

            fr.read(c);
            fr.close();

            StringBuffer sbuf = new StringBuffer();
            sbuf.append(c);

            //System.out.println(sbuf.substring(0, 10).toString() + "...");

            tracerForm.onFileRead(sbuf);

        } catch (Exception ex) {
            Logger.getLogger(LoadFileTask.class.getName()).log(Level.SEVERE, null, ex);
        }
/*
        try {

                // read input file contents
               // BufferedReader in = new BufferedReader(new FileReader(inputFile));
                Reader in2 = new InputStreamReader(new FileInputStream(inputFile), "UTF-8");
                BufferedReader in = new BufferedReader(in2);

                String str;
                StringBuffer inputFileBuff = new StringBuffer("");

                while ((str = in.readLine()) != null) {
                    inputFileBuff.append(str);
                    inputFileBuff.append(System.getProperty("line.separator"));
                }
                in.close();
                    
               tracerForm.onFileRead(inputFileBuff);
                

        } catch (SecurityException ex) {
                System.out.println("Cannot read file: " + getFileName());
        } catch (FileNotFoundException e) {
                System.out.println("File not found: " + getFileName());

        } catch (IOException e) {
                System.out.println("Cannot read file!");
        }*/
    }

    
    
}
