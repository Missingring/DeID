package dit.panels;

import dit.DEIDGUI;
import dit.DefaceTask;
import dit.DefaceTaskinWindows;
import dit.DeidData;
import dit.DemographicTableModel;
import dit.FileUtils;
import dit.IDefaceTask;
import dit.NIHImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map.Entry;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import niftijlib.Nifti1Dataset;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;
import org.dcm4che2.util.StringUtils;
import org.dcm4che2.util.TagUtils;

/**
 *
 * @author christianprescott & angelo
 */
public class DeidentifyProgressPanel extends javax.swing.JPanel implements WizardPanel {

    private String outputPath = DeidData.outputPath;
    private boolean doDeface;

    /**
     * Creates new form DeidentifyProgressPanel
     */
    public DeidentifyProgressPanel(boolean doDeface) {
        initComponents();
        DEIDGUI.helpButton.setEnabled(false);
        DEIDGUI.continueButton.setEnabled(false);
        DEIDGUI.backButton.setEnabled(false);
        this.doDeface = doDeface;
        //  DeidData.IdTable = new Hashtable<String, String>();
        DEIDGUI.log("DeidentifyProgressPanel initialized");
        startDeidentification();
    }

    private void startDeidentification() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                randomizeIds();
                initNiftidataset();
                if (doDeface) {
                    jLabel2.setText("<html><p>Defacing images...</p><p>&nbsp;</p></html>");
                    defaceImages();
                } else {
                    DeidData.imageHandler.moveImages();
                }
                jLabel2.setText("<html><p>Deidentifying demographic file...</p><p>&nbsp;</p></html>");
                createDemographicFile();
                if (DeidData.NiftiConversionSourceTable.size() > 0) {
                    jLabel2.setText("<html><p>Deidentifying DICOM header data...</p><p>&nbsp;</p></html>");
                    createHeaderDataFiles();
                }
                DeidData.imageHandler.correctOrientation();
                 createMontage();

                DEIDGUI.advance();
            }

            private void initNiftidataset() {
                  jLabel2.setText("<html><p>Analysing images header files...</p><p>&nbsp;</p></html>");
                  for(NIHImage image : DeidData.imageHandler.getInputFiles())
                  {
                      image.initNifti();
                  }
            }
        }).start();
    }
    /* Dr. Eckert's Image ID Cipher
     * image id = <site initials as numeric string>_<name initials as numeric string>_<random 4 digits>
     * i.e. 1234_5678_2301 or 1234_5678_4598
     * Utilizes an alphabet with consonants swapped around vowels and then the
     * transposed alphabet is numbered 01-26, and then used with the users site
     * and initials information */
    private final char[] CipherAlphabet = "bacfedgjihklmponqrsvutwxyz".toCharArray();

    private void randomizeIds() {
        // Only randomize if the ID column is selected
        for (NIHImage image : DeidData.imageHandler.getInputFiles()) {
            image.setImageNewName("");
        }

        // Build the base ID using the cipher
        String baseId = "";
        String[] institutionBits = DeidData.UserInstitution.split("\\s");
        for (String s : institutionBits) {
            if (s.length() > 0) {
                // The alphabet is not sorted, so a search will not work.
                char firstLetter = s.toLowerCase().charAt(0);
                for (int ndx = 0; ndx < CipherAlphabet.length; ndx++) {
                    if (CipherAlphabet[ndx] == firstLetter) {
                        baseId += String.format("%02d", ndx);
                    }
                }
            }
        }
        baseId += "_";
        String[] names = DeidData.UserFullName.split("\\s");
        for (String s : names) {
            if (s.length() > 0) {
                // The alphabet is not sorted, so a search will not work.
                char firstLetter = s.toLowerCase().charAt(0);
                for (int ndx = 0; ndx < CipherAlphabet.length; ndx++) {
                    if (CipherAlphabet[ndx] == firstLetter) {
                        baseId += String.format("%02d", ndx);
                    }
                }
            }
        }
        baseId += "_";

        if (DeidData.isNoData) {
            for (NIHImage file : DeidData.imageHandler.getInputFiles()) {
                DeidData.imageHandler.assignID(file, "r" + baseId);
            }
        } else {
            String idIdentifier = "Filename and "
                    + DeidData.demographicData.getColumnName(DeidData.IdColumn);
            String[] omissions = DeidData.selectedIdentifyingFields;
            Arrays.sort(omissions);
            boolean randomizeFilename = (Arrays.binarySearch(
                    omissions, idIdentifier) >= 0);

            if (randomizeFilename) {
                for (NIHImage image : DeidData.imageHandler.getInputFiles()) {
                    DeidData.imageHandler.assignID(image, baseId);
                }
            }

        }
        DEIDGUI.log("Randomized file IDs");
    }

    private void defaceImages() {
        try {
            IDefaceTask defaceTask = null;
            if (FileUtils.OS.isWindows()) {
                System.out.println("Windows Deface:");
                defaceTask = new DefaceTaskinWindows();
            } else {
                defaceTask = new DefaceTask();
            }
            defaceTask.setProgressBar(jProgressBar1);
            defaceTask.setTextfield(txtDetail);
            defaceTask.setInputImages(DeidData.imageHandler.getInputFiles());

            synchronized (defaceTask) {
                new Thread((Runnable) defaceTask).start();
                try {
                    defaceTask.wait();
                } catch (InterruptedException ex) {
                    DEIDGUI.log("bet was interrupted, the defacing result may "
                            + "be incorrect", DEIDGUI.LOG_LEVEL.WARNING);
                }
            }

            DEIDGUI.log("Defaced images");
        } catch (RuntimeException e) {
            e.printStackTrace();
            DEIDGUI.log("Defacing couldn't be started: " + e.getMessage(),
                    DEIDGUI.LOG_LEVEL.ERROR);
        }
    }

    private void createDemographicFile() {
        String[] headings = DeidData.demographicData.getDataFieldNames();
        String[] omissions = DeidData.selectedIdentifyingFields;
        Arrays.sort(omissions);
        boolean[] omit = new boolean[headings.length];

        // Find set of identifying columns to omit
        int omitCount = 0;
        for (int ndx = 0; ndx < headings.length; ndx++) {
            String fieldName = headings[ndx];
            if (ndx == DeidData.IdColumn) {
                fieldName = "Filename and "
                        + DeidData.demographicData.getColumnName(DeidData.IdColumn);
            }
            if (Arrays.binarySearch(omissions, fieldName) >= 0) {
                omit[ndx] = true;
                omitCount++;
            } else {
                omit[ndx] = false;
            }
        }

        if (omitCount != DeidData.selectedIdentifyingFields.length) {
            DEIDGUI.log("Some identifying fields weren't found (" + omitCount
                    + "/" + DeidData.selectedIdentifyingFields.length + "). "
                    + "The demographic data may not be deidentified properly",
                    DEIDGUI.LOG_LEVEL.WARNING);
        }

        File newDemoFile = new File(outputPath + "Demographics_Behavioral.txt");
        BufferedWriter writer = null;
        try {
            newDemoFile.createNewFile();
            writer = new BufferedWriter(new FileWriter(newDemoFile, false));

            // Write headings
            for (int colNdx = 0; colNdx < headings.length; colNdx++) {
                if (!omit[colNdx] || colNdx == DeidData.IdColumn) {
                    // The ID is a special case - heading must be included even
                    // when omitted (in randomized form)
                    writer.write(headings[colNdx] + "\t");
                }
            }
            writer.newLine();

            for (int ndx = 0; ndx < DeidData.demographicData.getRowCount(); ndx++) {

                Object[] row = DeidData.demographicData.getRow(ndx);
                String[] rowS = Arrays.copyOf(row, row.length, String[].class);

                Vector<NIHImage> images = DeidData.imageHandler.findImageByIdInDataFile(rowS[DeidData.IdColumn]);
                if (images.size() == 0) {
                    continue;
                }
                for (NIHImage image : images) {
                    for (int colNdx = 0; colNdx < row.length; colNdx++) {

                        if (!omit[colNdx]) {
                            writer.write(rowS[colNdx] + "\t");
                        } else if (colNdx == DeidData.IdColumn) {
                            // The ID is a special case - it must be included in its
                            // randomized form.
                            // writer.write(DeidData.IdTable.get(rowS[colNdx]) + "\t");

                            writer.write(image.getImageNewName() + "\t");
                        }
                    }
                    writer.newLine();
                }
            }
        } catch (IOException ex) {
            DEIDGUI.log("Couldn't write deidentified demographic file: "
                    + ex.getMessage(), DEIDGUI.LOG_LEVEL.WARNING);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                }
            }
        }
        DeidData.deidentifiedDemoFile = newDemoFile;

        DEIDGUI.log("Created anonymized demographic file");
    }

    private void createHeaderDataFiles() {
        Iterator<Entry<File, File>> it = DeidData.NiftiConversionSourceTable.entrySet().iterator();
        String newline = System.getProperty("line.separator");
        DEIDGUI.log("Creating " + DeidData.NiftiConversionSourceTable.size() + " header data files");
        while (it.hasNext()) {
            File hdrFile = null;
            try {
                Entry<File, File> curSet = it.next();
                // Create new file or overwrite existing
                String imageName = FileUtils.getName(curSet.getKey());
                if (DeidData.IdTable.containsKey(imageName)) {
                    imageName = DeidData.IdTable.get(imageName);
                }
                hdrFile = new File(outputPath + imageName + "_hdr_varNames.txt");
                hdrFile.createNewFile();
                DeidData.ConvertedDicomHeaderTable.put(curSet.getKey(), hdrFile);
                BufferedWriter writer = null;
                String[][] metadata = readDicomMetadata(curSet.getValue(), false);
                try {
                    writer = new BufferedWriter(new FileWriter(hdrFile, false));
                    for (int ndx = 0; ndx < metadata.length; ndx++) {
                        int tagNdx = Arrays.binarySearch(DeidData.dicomVarIds, metadata[ndx][0]);
                        if (tagNdx >= 0) {
                            String[] element = metadata[ndx];
                            String[] tagHalves = StringUtils.split(element[0], ',');
                            String formattedTag = "\"" + tagHalves[0] + ",\"\t" + tagHalves[1];
                            // element[1] contains the name as read from the
                            // DICOM. The name as provided by Dr. Mark Eckert
                            // is preferred.
                            String name = DeidData.dicomVarNames[tagNdx],
                                    vr = element[2],
                                    value = element[3];
                            String tab = "\t";
                            writer.write(formattedTag + tab + name + tab
                                    + vr + tab + value + newline);
                        }
                    }
                } catch (IOException e) {
                    DEIDGUI.log("Couldn't write header data to "
                            + hdrFile.getAbsolutePath(), DEIDGUI.LOG_LEVEL.WARNING);
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException ex) {
                        }
                    }
                }
            } catch (IOException ex) {
                if (hdrFile != null) {
                    DEIDGUI.log("Couldn't write header data to "
                            + hdrFile.getAbsolutePath(), DEIDGUI.LOG_LEVEL.WARNING);
                }
            }
        }
        DEIDGUI.log("Created header files");
    }

    //need modification in the future
    private void createMontage() {
        
        int imageHeight = 64, imageWidth = 64, textHeight = 12,
                rowHeight = imageHeight + textHeight;

        if (DeidData.imageHandler.getInputFiles().isEmpty()) {
            return;
        }

      
        for (NIHImage image : DeidData.imageHandler.getInputFiles()) {
            
         BufferedImage i = new BufferedImage(imageWidth * 16, rowHeight, BufferedImage.TYPE_INT_RGB);
            Nifti1Dataset set = new Nifti1Dataset(image.getTempPotision().getAbsolutePath());
            for (int idx = 0; idx < 16; idx++) {
                try {
                    BufferedImage ii = image.imageAt((float) (idx) / 16.0f, image.getOrientationState());
                    Graphics2D g = i.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.drawImage(ii, 64 * idx, textHeight, imageWidth, imageHeight, null);
                } catch (IOException ex) {
                    Logger.getLogger(DeidentifyProgressPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // Write image name to the image
            Font f = new Font(Font.MONOSPACED, Font.PLAIN, 12);
            Graphics2D g = i.createGraphics();
            g.setColor(Color.WHITE);
            g.setFont(f);
            //System.out.println(DeidData.IdTable.get("12"));
            g.drawString(image.getImageNewName() + ".nii("
                    + image.getImageName() + ")",
                    0, textHeight);           
           

            try {
                File targetFile=new File(DeidData.outputPath + image.getImageNewName()+"_montage.jpg");
                ImageIO.write(i, "jpg", targetFile);
                image.setMontageFile(targetFile);
            } catch (IOException ex) {
                DEIDGUI.log("Unable to write montage.png",
                        DEIDGUI.LOG_LEVEL.ERROR);
            }
        }

        /**
         * USE OF DCM4CHE HAS RUNTIME DEPENDENCIES ON log4j, slf4j-api,
         * slf4j-log4j12, and dcm4che-core.
         *
         * @param dicomFile source of metadata
         * @return A two-dimensional String array of metadata elements of the
         * form [tag, name, data type, value]
         */
    }

    private String[][] readDicomMetadata(File dicomFile, boolean anonymizeFile) {
        DicomInputStream dis = null;
        ArrayList<String[]> metadataList = new ArrayList<String[]>();
        try {
            // Open and read the DICOM image
            dis = new DicomInputStream(dicomFile);
            DicomObject dicomObject = dis.readDicomObject();

            // Iterate over metadata elements
            Iterator<DicomElement> metadataIt = dicomObject.iterator();
            while (metadataIt.hasNext()) {
                // Read element data
                DicomElement elem = metadataIt.next();
                String elemName = dicomObject.nameOf(elem.tag()),
                        elemTag = TagUtils.toString(elem.tag()),
                        elemVR = dicomObject.vrOf(elem.tag()).toString(),
                        elemValue = "";
                try {
                    if (dicomObject.vm(elem.tag()) != 1) {
                        String[] elemValues = dicomObject.getStrings(elem.tag());
                        elemValue = StringUtils.join(elemValues, '\\');
                    } else {
                        elemValue = dicomObject.getString(elem.tag());
                    }
                } catch (UnsupportedOperationException e) {
                    // Only alert if the element is one that we want to keep
                    if (Arrays.binarySearch(DeidData.dicomVarIds, elemTag) >= 0) {
                        DEIDGUI.log("Couldn't get value of desired DICOM element "
                                + elemTag + " \"" + elemName + "\". The image "
                                + "header file may be incomplete.", DEIDGUI.LOG_LEVEL.WARNING);
                    }
                }

                metadataList.add(new String[]{
                    elemTag,
                    elemName,
                    elemVR,
                    elemValue});
                if (anonymizeFile && Arrays.binarySearch(DeidData.dicomVarIds, elemTag) < 0) {
                    // Anonymize other elements
                    dicomObject.remove(elem.tag());
                }
            }

            if (anonymizeFile) {
                // Save anonymized file
                // TODO: Save this file in a temporary location - the original
                // file should be left intact.
                // TODO: Missing (0002,0010) Transfer Syntax UID, unable to
                // save file. Find out which data are required to save a DICOM.
                DicomOutputStream dos = new DicomOutputStream(new File("/Users/christianprescott/Desktop/anon.dcm"));//dicomFile);
                dos.writeDicomFile(dicomObject);
            }
        } catch (IOException ex) {
            DEIDGUI.log("Couldn't read DICOM object " + dicomFile.getAbsolutePath()
                    + ". The image header file may be incomplete.", DEIDGUI.LOG_LEVEL.WARNING);
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (IOException ex) {
            }
        }

        String[][] metadataArray = new String[metadataList.size()][metadataList.get(0).length];
        metadataList.toArray(metadataArray);
        return metadataArray;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        txtDetail = new javax.swing.JTextField();

        jLabel2.setText("<html><p>Deidentifying image IDs...</p><p>&nbsp;</p></html>");

        txtDetail.setEditable(false);
        txtDetail.setBackground(new java.awt.Color(255, 255, 255));
        txtDetail.setText("This process may take several minutes.");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(txtDetail)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jProgressBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(txtDetail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 188, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JTextField txtDetail;
    // End of variables declaration//GEN-END:variables

    @Override
    public WizardPanel getNextPanel() {
        return new AuditPanel();
    }

    @Override
    public WizardPanel getPreviousPanel() {

        return new DeIdentifyPanel();
    }
}
