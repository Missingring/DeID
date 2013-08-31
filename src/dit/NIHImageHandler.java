package dit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brain This class provide all the operation to NIHImages such like
 * look up by name, return files as list
 */
public class NIHImageHandler {

    private Vector<NIHImage> _inputFiles;

    public NIHImageHandler() {
        _inputFiles = new Vector<NIHImage>();
    }

    /**
     * @return the _inputFiles
     */
    public Vector<NIHImage> getInputFiles() {
        return _inputFiles;
    }

    public void addFile(File file) {
        if (!isExistInputFile(file)) {
            _inputFiles.add(new NIHImage(file));
        }
    }

    public int getInputFilesSize() {
        return _inputFiles.size();
    }

    private boolean isExistInputFile(File file) {
        String fileName = file.getAbsolutePath();
        if (fileName.endsWith("nii.gz")) {
            fileName = fileName.replace("nii.gz", "");
        } else {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        for (NIHImage image : _inputFiles) {
            // System.out.println(image.getImageName()+  "  "+fileName);
            if (image.getImageName().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param inputFiles the _inputFiles to set
     */
    public void setInputFiles(Vector<NIHImage> inputFiles) {
        this._inputFiles = inputFiles;
    }

    public void removeAll(List<File> asList) {
        _inputFiles.removeAll(asList);
    }

    public NIHImage findImageByDisplayName(String displayName) {
        for (NIHImage image : _inputFiles) {
            if (image.getImageDisplayName().equals(displayName)) {
                return image;
            }
        }
        return null;
    }

    public NIHImage findImageByLongitudinalSubject(String subject) {
        for (NIHImage image : _inputFiles) {
            if (image.getLongitudinalSuject().equals(subject) && !image.getImageNewName().equals("")) {
                return image;
            }
        }
        return null;
    }

    public Vector<NIHImage> findImageByIdInDataFile(String id) {
        Vector<NIHImage> images=new Vector<>();
        for (NIHImage image : _inputFiles) {
            if (image.getIdInDataFile().equals(id)) {
                images.add(image);
            }
        }
        return images;
    }

    public Collection<String> getAllIDs() {
        Collection<String> ids = new Vector<>();
        for (NIHImage image : _inputFiles) {
            if (!image.getIdInDataFile().equals("")) {
                ids.add(image.getIdInDataFile());
            }
        }
        return ids;
    }

    public void assignID(NIHImage image, String baseId) {

        if (image.isIsLongitudinal()) {
            NIHImage sameSubjectImage = findImageByLongitudinalSubject(image.getLongitudinalSuject());

            if (sameSubjectImage != null && !sameSubjectImage.getImageNewName().equals("")) {
                System.out.println("Hit");
                String prefix = sameSubjectImage.getImageNewName().substring(0,
                        sameSubjectImage.getImageNewName().lastIndexOf("#"));
                image.setImageNewName(prefix + image.getLongitudinalNumber());
                return;
            }

        }

        String newId;
        do {
            newId = baseId;
            for (int strNdx = 0; strNdx < 4; strNdx++) {
                newId += Integer.toString(new Random().nextInt(9));
            }
        } while (!uniqueID(newId));

        if (image.isIsLongitudinal()) {
            image.setImageNewName(newId + image.getLongitudinalNumber());
        } else {
            image.setImageNewName(newId);
        }

    }

    private boolean uniqueID(String newId) {
        for (NIHImage image : _inputFiles) {
            if (image.getImageNewName().equals(newId)) {
                return false;
            }
        }
        return true;
    }

    public void moveImages() {
        for (NIHImage image : _inputFiles) {
            if (!image.getImageNewName().equals("")) {
                String newFileDir = DeidData.outputPath + "betOut/";
                String newFileName = DeidData.outputPath + "betOut/" + image.getImageNewName() + ".nii";
                File newFile = new File(newFileName);
                File newDir = new File(newFileDir);
                try {
                    if (!newDir.exists()) {
                        newDir.mkdir();
                    }
                    FileUtils.copyFile(image.getStoredPotistion(), newFile);
                    image.setTempPotision(newFile);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(NIHImageHandler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(NIHImageHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}