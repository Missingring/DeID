/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

import java.io.File;
import java.util.Vector;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author QM
 */
public interface IDefaceTask {
    public void setProgressBar(JProgressBar bar);
    public void setTextfield(JTextField text);
    public void setInputImages(Vector<NIHImage> file);
    public void run();
    
}
