package dit;

import com.sun.org.apache.bcel.internal.generic.ISHL;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import niftijlib.Nifti1Dataset;

/**
 *
 * @author brain This class hold a image object, which contains all the
 * attribute of an image.
 */
public class NIHImage {

    private File _storedPotistion; //where images are stored
    private File _tempPotision;   //where images are put during the program
    private File _montageFile;
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
    private boolean _isCorrectedOrientation;
    private String _longitudinalSuject = "";
    private String _longitudinalNumber = "";
    private OrientationState orientationState;
    private Nifti1Dataset _set;
    double[][][] _data; // where pixels are stored
    private NiftiPara _para;

    public NIHImage(File file) {
        _storedPotistion = file;
        _tempPotision = null;

        String fileName = file.getAbsolutePath();
        if (fileName.endsWith("nii.gz")) {
            fileName = fileName.replace("nii.gz", "");
        } else {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        _imageName = fileName;
        _imageNewName = "";
        _imageFormalName = FileUtils.getName(file);
        _imageFormat = FileUtils.getExtension(file);
        _parentPath = "";
        _idInDataFile = "";

        _imageDisplayName = _storedPotistion.getAbsolutePath();       

        if (_imageName.matches("^.*#\\d+$")) {
            _isLongitudinal = true;
            _longitudinalSuject = _imageName.substring(0, _imageName.lastIndexOf('#'));
            _longitudinalNumber = _imageName.substring(_imageName.lastIndexOf('#'));
        } else {
            _isLongitudinal = false;
        }
        _needDefaced = true;
        _needRedefaced = false;
        _isDefaced = false;
        _seletedInJarFile = true;
        _isCorrectedOrientation=false;
        
        orientationState = new OrientationState(0, true, 0, 5, 1, 2, 3, 4);
    }

    /**
     * @return the _storedPotistion
     */
    public File getStoredPotistion() {
        return _storedPotistion;
    }
    
    public void initNifti(){
        _set = new Nifti1Dataset(_tempPotision.getAbsolutePath());
        short ttt = 0;
        try {
            _set.readHeader();
            _data = _set.readDoubleVol(ttt);
            _para=new NiftiPara(_set);
        } catch (IOException ex) {
            Logger.getLogger(NIHImage.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public String toString() {
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

    /**
     * @return the orientationState
     */
    public OrientationState getOrientationState() {
        return orientationState;
    }

    /**
     * @param orientationState the orientationState to set
     */
    public void setOrientationState(OrientationState orientationState) {
        this.orientationState = orientationState;
    }

    public BufferedImage imageAt(float sliceFactor, OrientationState orientationState) throws IOException {

        short[] dims = _para.dims;
        float calMax2 = _para.calMax2;
        float calMin2 = _para.calMin2;


        BufferedImage i = new BufferedImage(dims[orientationState.widthAxis], dims[orientationState.heightAxis], BufferedImage.TYPE_INT_RGB);
        int depth;
        if (orientationState.isForward) {
            depth = (int) ((dims[orientationState.currentAxis] - 1) * Math.max(0, Math.min(sliceFactor, 1.0)));
        } else {
            depth = (int) ((dims[orientationState.currentAxis] - 1) * Math.max(0, Math.min(1.0 - sliceFactor, 1.0)));
        }
        int widthStart, heightStart, heightStep;
        int widthEnd, heightEnd, widthStep;

        if (orientationState.widthPostive) {
            widthStart = 0;
            widthEnd = dims[orientationState.widthAxis] - 1;
            widthStep = 1;
        } else {
            widthStart = dims[orientationState.widthAxis] - 1;
            widthEnd = 0;
            widthStep = -1;
        }

        if (orientationState.heightPostive) {
            heightStart = 0;
            heightEnd = dims[orientationState.heightAxis] - 1;
            heightStep = 1;
        } else {
            heightStart = dims[orientationState.heightAxis] - 1;
            heightEnd = 0;
            heightStep = -1;
        }
        for (int ii = heightStart; ii != heightEnd; ii += heightStep) {
            for (int j = widthStart; j != widthEnd; j += widthStep) {

                float density = 0;

                if (orientationState.currentAxis == orientationState.Z_AXIS) {
                    if (orientationState.widthAxis == orientationState.X_AXIS) {
                        density = (float) _data[depth][ii][j];
                    } else {
                        density = (float) _data[depth][j][ii];
                    }
                } else if (orientationState.currentAxis == orientationState.Y_AXIS) {
                    if (orientationState.widthAxis == orientationState.X_AXIS) {
                        density = (float) _data[ii][depth][j];
                    } else {
                        density = (float) _data[j][depth][ii];
                    }
                } else // currentAxis is X axis
                {
                    if (orientationState.widthAxis == orientationState.Y_AXIS) {
                        density = (float) _data[ii][j][depth];
                    } else {
                        density = (float) _data[j][ii][depth];
                    }
                }
                float colorFactor = Math.min((density - calMin2) / (calMax2 - calMin2), 1f);
                int argb = new Color(colorFactor, colorFactor, colorFactor).getRGB();

                int targetX, targetY;

                if (orientationState.widthPostive) {
                    targetX = j;
                } else {
                    targetX = widthStart - j;
                }

                if (orientationState.heightPostive) {
                    targetY = ii;
                } else {
                    targetY = heightStart - ii;
                }
                //System.out.println("Width:"+i.getWidth()+"  Height:"+i.getHeight());
                //System.out.println("X:"+targetX+"  Y:"+targetY);
                i.setRGB(targetX, targetY, argb);
            }
        }
        return i;
    }

    public float getMax(double[][][] triDarr, short[] dims) {
        float max = 0;
        for (int i = 0; i < dims[2]; i++) {
            for (int j = 0; j < dims[1]; j++) {
                for (int k = 0; k < dims[0]; k++) {
                    if (triDarr[k][j][i] > max) {
                        max = (float) triDarr[k][j][i];
                    }
                }
            }
        }
        return max;
    }

    /**
     * @return the _montageFile
     */
    public File getMontageFile() {
        return _montageFile;
    }

    /**
     * @param montageFile the _montageFile to set
     */
    public void setMontageFile(File montageFile) {
        this._montageFile = montageFile;
    }

    /**
     * @return the _isCorrectedOrientation
     */
    public boolean isIsCorrectedOrientation() {
        return _isCorrectedOrientation;
    }

    /**
     * @param isCorrectedOrientation the _isCorrectedOrientation to set
     */
    public void setIsCorrectedOrientation(boolean isCorrectedOrientation) {
        this._isCorrectedOrientation = isCorrectedOrientation;
    }

    private class NiftiPara {

        short[] dims;
        float calMax2;
        float calMin2;

        public NiftiPara(Nifti1Dataset _set) {
            dims = new short[]{_set.ZDIM, _set.YDIM, _set.XDIM};
            if (calMax2 - calMin2 == 0) {
                calMax2 = getMax(_data, dims);
                calMin2 = 0;
            }
        }
    }
}
