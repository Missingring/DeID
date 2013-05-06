package dit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

/**
 *
 * @author Christian Prescott
 */
public class DefaceTaskinWindows  implements Runnable, IDefaceTask {

    private String outputDir = DeidData.outputPath + "betOut/";
    private JTextField detailText=null;
    private JProgressBar progressBar = null;
    private ArrayList<File> inputImages;
    private String[] command;

    public DefaceTaskinWindows() throws RuntimeException{
      //  super();
        
        // Establish OS dependent paths
        /*if(FileUtils.OS.isMac()){
            //System.out.println("I'm a mac");
        } else {
            throw new RuntimeException("Platform (" + FileUtils.OS.getOS() + ") not "
                    + "supported by bet.");
        }
        */
        File outDir = new File(outputDir);
        if(!outDir.exists()){
            outDir.mkdirs();
        }
        
        inputImages = new ArrayList<>();        
        command = new String[]{
            DeidData.unpackedFileLocation.get("robex.exe").getAbsolutePath(),
            // Depends on imtest and 
            // Requires fsl config file, change default output to NIFTI
            //Usage: 
            //bet2 <input_fileroot> <output_fileroot> [options]
            //Optional arguments (You may optionally specify one or more of):
            //-o,--outline	generate brain surface outline overlaid onto original image
            //-m,--mask	generate binary brain mask
            //-s,--skull	generate approximate skull image
            //-n,--nooutput	don't generate segmented brain image output
            //-f <f>		fractional intensity threshold (0->1); default=0.5; smaller values give larger brain outline estimates
            //-g <g>		vertical gradient in fractional intensity threshold (-1->1); default=0; positive values give larger brain outline at bottom, smaller at top
            //-r,--radius <r>	head radius (mm not voxels); initial surface sphere is set to half of this
            //-w,--smooth <r>	smoothness factor; default=1; values smaller than 1 produce more detailed brain surface, values larger than one produce smoother, less detailed surface
            //-c <x y z>	centre-of-gravity (voxels not mm) of initial mesh surface.
            //-t,--threshold	-apply thresholding to segmented brain image and mask
            //-e,--mesh	generates brain surface as mesh in vtk format
            //-v,--verbose	switch on diagnostic messages
            //-h,--help	displays this help, then exits
            "input", outputDir + "filename"};
    }

    /**
     * Assign a progress bar to be updated by this conversion task.
     *
     * @param bar The JProgressBar that will display the conversion's progress
     */
    public void setProgressBar(JProgressBar bar) {
        progressBar = bar;
    }

    /**
     * Add directories with DICOM images to be converted by this task.
     *
     * @param file File object representing a directory that contains .dcm
     * images
     */
    public void addInputImage(File file) {
        inputImages.add(file);
    }

     public void setTextfield(JTextField field)
    {
        detailText=field;
    }
    
    @Override
    public void run() {
        ArrayList<File> newFiles = new ArrayList<>();
        String errorPatternStr = "Error";
        Pattern errorPattern = Pattern.compile(errorPatternStr);

        for (int ndx = 0; ndx < inputImages.size(); ndx++) {
            // Set input directory for bet
            
            command[1] = inputImages.get(ndx).getAbsolutePath();
            String outFilename = FileUtils.getName(inputImages.get(ndx));
//            if(DeidData.IdTable.containsKey(outFilename)){
//                outFilename = DeidData.IdTable.get(outFilename);
//            }
         //   outFilename = outputDir + outFilename + ".nii";
            String abParent =  inputImages.get(ndx).getParent();
            if (!DeidData.parentPath.equals("none"))
            outFilename = outputDir + abParent.replaceFirst(DeidData.parentPath, "").replaceFirst(DeidData.anaPath, "").replaceFirst(DeidData.dicomPath, "").replaceAll("/", "") + outFilename + ".nii" ;
            else outFilename = outputDir + outFilename + ".nii";
            System.out.println(outFilename);
            command[2] = outFilename.replace("/", "\\"); //windows use \ as path seperator
            // Overwrites existing files
            
             System.out.println("Command:");
            for(String string:command)
            {
                 System.out.print(string+" ");
            }
             System.out.println();
            
            // Capture output of the bet process
            java.lang.ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            pb.directory(DeidData.unpackedFileLocation.get("robex.exe").getParentFile());
            
            Process defaceProc = null;
            boolean fileValid = true;
            try {
                defaceProc = pb.start();
                detailText.setText(detailText.getText()+"Begin defacing #"+ndx+ " image:\n");
            } catch (IOException ex) {
                DEIDGUI.log("Robex couldn't be started: " + ex.getMessage(), DEIDGUI.LOG_LEVEL.ERROR);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(defaceProc.getInputStream()));
            String line;
            int lineNumber=0;
            try {
                while ((line = br.readLine()) != null && !line.isEmpty()) {
                    detailText.setText(detailText.getText()+"\t"+line+"\n");
                    lineNumber++;
                    float processProgress = (float) (ndx*9+lineNumber) / (float) (inputImages.size()*9);
                    if (progressBar != null) {
                        progressBar.setValue((int) (100 * processProgress));
                    }
                    
                }
            } catch (IOException ex) {}
            
            if(fileValid){
                File newFile = new File(outFilename);
                File defaceSource = inputImages.get(ndx);
                
                // If the image was associated with a dicom, pair the 
                // new file with that source.
                if(DeidData.NiftiConversionSourceTable.containsKey(defaceSource)){
                    DeidData.NiftiConversionSourceTable.put(newFile,
                            DeidData.NiftiConversionSourceTable.get(defaceSource));
                    DeidData.NiftiConversionSourceTable.remove(defaceSource);
                }
                
                // Add the image to deientified files list.
                DeidData.deidentifiedFiles.add(newFile);
            }
           
        }

        // Advance to next panel
        synchronized (this) {
            this.notify();
        }
    }
}
