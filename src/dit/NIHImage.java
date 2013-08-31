package dit;

import com.sun.org.apache.bcel.internal.generic.ISHL;
import java.io.File;

/**
 *
 * @author brain This class hold a image object, which contains all the
 * attribute of an image.
 */
public class NIHImage {

    private File _storedPotistion; //where images are stored
    private File _tempPotision;   //where images are put during the program
    private String _imageName;      //images' name without extensiont
    private String _imageNewName;   //new id generated for images
    private String _imageDisplayName;  //name shown in the Matching GUI
    private String _imageFormalName;   //name that replace '/' with '_', this make sure in temp dir no filename collision.
    private String _imageFormat;  //images' extension
    private String _idInDataFile;  // the id in data file when matching 
    private String _parentPath;  // if images is chosen from multiple directies, this indicate it's parent file path
   
    private boolean _needDefaced;  
    private boolean _isDefaced;
    private boolean _isLongitudinal;
    private boolean _seletedInJarFile;
    private boolean _needRedefaced;

    private String _longitudinalSuject="";
    private String _longitudinalNumber="";
    
    public NIHImage(File file) {
        _storedPotistion = file;
        _tempPotision = null;
        
        String fileName = file.getAbsolutePath();
        if (fileName.endsWith("nii.gz")) {
            fileName = fileName.replace("nii.gz", "");
        } else {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        _imageName=fileName;
        _imageNewName="";
        _imageFormalName = FileUtils.getName(file);
        _imageFormat = FileUtils.getExtension(file);
        _parentPath="";
        _idInDataFile="";
        
        _imageDisplayName=_storedPotistion.getAbsolutePath();
        
        if(_imageName.matches("^.*#\\d+$")){
            _isLongitudinal=true;
            _longitudinalSuject=_imageName.substring(0,_imageName.lastIndexOf('#'));
            _longitudinalNumber=_imageName.substring(_imageName.lastIndexOf('#'));
        }
        else
            _isLongitudinal=false;
        _needDefaced = true;
        _needRedefaced=false;
        _isDefaced = false;
        _seletedInJarFile=true;
    }

    /**
     * @return the _storedPotistion
     */
    public File getStoredPotistion() {
        return _storedPotistion;
    }

    /**
     * @param storedPotistion the _storedPotistion to set
     */
    public void setStoredPotistion(File storedPotistion) {
        this._storedPotistion = storedPotistion;
    }

    /**
     * @return the _tempPotision
     */
    public File getTempPotision() {
        return _tempPotision;
    }

    /**
     * @param tempPotision the _tempPotision to set
     */
    public void setTempPotision(File tempPotision) {
        this._tempPotision = tempPotision;
    }

    /**
     * @return the _imageName
     */
    public String getImageName() {
        return _imageName;
    }

    /**
     * @param imageName the _imageName to set
     */
    public void setImageName(String imageName) {
        this._imageName = imageName;
    }

    /**
     * @return the _imageFormat
     */
    public String getImageFormat() {
        return _imageFormat;
    }

    /**
     * @param imageFormat the _imageFormat to set
     */
    public void setImageFormat(String imageFormat) {
        this._imageFormat = imageFormat;
    }

    /**
     * @return the _idInDataFile
     */
    public String getIdInDataFile() {
        return _idInDataFile;
    }

    /**
     * @param idInDataFile the _idInDataFile to set
     */
    public void setIdInDataFile(String idInDataFile) {
        this._idInDataFile = idInDataFile;
    }

    /**
     * @return the needDefaced
     */
    public boolean isNeedDefaced() {
        return _needDefaced;
    }

    /**
     * @param needDefaced the needDefaced to set
     */
    public void setNeedDefaced(boolean needDefaced) {
        this._needDefaced = needDefaced;
    }

    /**
     * @return the isDefaced
     */
    public boolean isIsDefaced() {
        return _isDefaced;
    }

    /**
     * @param isDefaced the isDefaced to set
     */
    public void setIsDefaced(boolean isDefaced) {
        this._isDefaced = isDefaced;
    }

    /**
     * @return the _imageFormalName
     */
    public String getImageFormalName() {
        return _imageFormalName;
    }

    /**
     * @param imageFormalName the _imageFormalName to set
     */
    public void setImageFormalName(String imageFormalName) {
        this._imageFormalName = imageFormalName;
    }
    
    public String toString(){
        return _storedPotistion.getAbsolutePath().toString();
    }

    /**
     * @return the _imageNewName
     */
    public String getImageNewName() {
        return _imageNewName;
    }

    /**
     * @param imageNewName the _imageNewName to set
     */
    public void setImageNewName(String imageNewName) {
        this._imageNewName = imageNewName;
    }

    /**
     * @return the _parentPath
     */
    public String getParentPath() {
        return _parentPath;
    }

    /**
     * @param parentPath the _parentPath to set
     */
    public void setParentPath(String parentPath) {
        this._parentPath = parentPath;
    }

    /**
     * @return the _imageDisplayName
     */
    public String getImageDisplayName() {
        return _imageDisplayName;
    }

    /**
     * @param imageDisplayName the _imageDisplayName to set
     */
    public void setImageDisplayName(String imageDisplayName) {
        this._imageDisplayName = imageDisplayName;
    }

    /**
     * @return the isLongitudinal
     */
    public boolean isIsLongitudinal() {
        return _isLongitudinal;
    }

    /**
     * @param isLongitudinal the isLongitudinal to set
     */
    public void setIsLongitudinal(boolean isLongitudinal) {
        this._isLongitudinal = isLongitudinal;
    }

    /**
     * @return the _longitudinalSuject
     */
    public String getLongitudinalSuject() {
        return _longitudinalSuject;
    }

    /**
     * @param longitudinalSuject the _longitudinalSuject to set
     */
    public void setLongitudinalSuject(String longitudinalSuject) {
        this._longitudinalSuject = longitudinalSuject;
    }

    /**
     * @return the _longitudinalNumber
     */
    public String getLongitudinalNumber() {
        return _longitudinalNumber;
    }

    /**
     * @param longitudinalNumber the _longitudinalNumber to set
     */
    public void setLongitudinalNumber(String longitudinalNumber) {
        this._longitudinalNumber = longitudinalNumber;
    }

    /**
     * @return the _seletecInJarFile
     */
    public boolean isSeletecInJarFile() {
        return _seletedInJarFile;
    }

    /**
     * @param seletecInJarFile the _seletecInJarFile to set
     */
    public void setSeletecInJarFile(boolean seletecInJarFile) {
        this._seletedInJarFile = seletecInJarFile;
    }

    /**
     * @return the _needRedefaced
     */
    public boolean isNeedRedefaced() {
        return _needRedefaced;
    }

    /**
     * @param needRedefaced the _needRedefaced to set
     */
    public void setNeedRedefaced(boolean needRedefaced) {
        this._needRedefaced = needRedefaced;
    }
}
