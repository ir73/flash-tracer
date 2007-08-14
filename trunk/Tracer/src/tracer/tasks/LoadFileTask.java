/*
 * LoadFileTask.java
 *
 * Created on 21 March 2007, 19:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tracer.tasks;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.TimerTask;
import tracer.forms.TracerForm;

/**
 *
 * @author Admin
 */
public class LoadFileTask extends TimerTask {

    private TracerForm tracerForm;

    private String fileName;

    private EventQueue eventDispather;
    
    
    /** Creates a new instance of LoadFileTask */
    public LoadFileTask(String fileName) {
        this.fileName = fileName;
    }
 

    public void run() {
        File inputFile = new File(fileName);
       // System.out.println("getAbsolutePath = " + inputFile.getAbsolutePath());
        //System.out.println("getPath = " + inputFile.getPath());
        //System.out.println("getCanonicalPath = " + inputFile.getCanonicalPath());
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
                System.out.println("Cannot read file: " + fileName);
        } catch (FileNotFoundException e) {
                System.out.println("File not found: " + fileName);
                //this.cancel();
        } catch (IOException e) {
                System.out.println("Cannot read file!");
        }
    }

    public void setActionListener(TracerForm tracerForm) {
        this.tracerForm = tracerForm;
    }
    
}
