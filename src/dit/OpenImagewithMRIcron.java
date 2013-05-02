/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author angelo
 */
public class OpenImagewithMRIcron implements Runnable{
    private String[] command;
    private String imageName;
    public OpenImagewithMRIcron(File imagefile) throws RuntimeException {
        super();
        if(FileUtils.OS.isMac()){
            //System.out.println("I'm a mac");
        } else if(FileUtils.OS.isWindows()){
        } else if(FileUtils.OS.isUnix()){
        } else {
            throw new RuntimeException("Platform (" + FileUtils.OS.getOS() + ") not "
                    + "supported by MRIcron.");
        }
        
        imageName = imagefile.getAbsolutePath();
        if (FileUtils.OS.isOS64bit()){
        command = new String[]{
        DeidData.unpackedFileLocation.get("mricron_64").getAbsolutePath(),
        imageName, "-r", DeidData.unpackedFileLocation.get("mricron_64").getParent().toString()+"/default.ini",
        };       
        } 
        else {
        command = new String[]{
        DeidData.unpackedFileLocation.get("mricron").getAbsolutePath(),
        imageName, "-r ", DeidData.unpackedFileLocation.get("mricron").getParent().toString()+"/default.ini",
        }; 
        }
        
    }
    @Override
    public void run() {
        java.lang.ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process openImageProc = null;
        try {
                openImageProc = pb.start();
            } catch (IOException ex) {
                
                DEIDGUI.log("Open image file failed: " + ex.getMessage(), DEIDGUI.LOG_LEVEL.ERROR);
                
       }
                
       }
    }
   
    

