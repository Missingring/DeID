/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JPanel;
import niftijlib.Nifti1Dataset;

/**
 *
 * @author christianprescott & angelo
 */
public class NiftiDisplayPanel extends JPanel {

    private class ImageLoader implements Runnable {

        private File image;

        public ImageLoader(File srcImage) {
            image = srcImage;
        }

        @Override
        public void run() {
            i = null;
            curImage = null;
            data = null;

            Nifti1Dataset set = new Nifti1Dataset(image.getAbsolutePath());

            if (set.exists()) {
                try {
                    set.readHeader();

                    dims = new short[]{set.ZDIM, set.YDIM, set.XDIM};
                    calMax = set.cal_max;
                    if (calMax <= 0) {
                        calMax = 255f;
                    }
                    calMin = set.cal_min;
                    if (calMin < 0) {
                        calMin = 0;
                    }
                    calMin2 = set.cal_min;
                    calMax2 = set.cal_max;
                    sform = set.sform_code;
                    qform = set.qform_code;
                    quat_x = set.srow_x[3];
                    quat_y = set.srow_y[3];
                    quat_z = set.srow_z[3];
                    //System.out.println(calMin2);
                    //System.out.println(calMax2);
                    System.out.println("sform code:" + sform);
                    System.out.println("qform code:" + qform);
                    System.out.println("Dimesion Info:" + set.xyz_unit_code);
                    for (int srowi = 0; srowi < set.srow_x.length; srowi++) {
                        System.out.println("Dimesion Info:" + set.srow_x[srowi]);
                    }
                    //  set.printHeader();


                    try {
                        short ttt = 0;
                        data = set.readDoubleVol(ttt);
                        correctOrieatation();
                        curImage = image;
                        if (calMax2 - calMin2 == 0) {
                            calMax2 = getMax(data);
                            calMin2 = 0;
                        }
                    } catch (IOException ex) {
                        DEIDGUI.log("Unable to render image, data could not be read: "
                                + ex.getMessage(), DEIDGUI.LOG_LEVEL.ERROR);
                    } catch (OutOfMemoryError ex) {
                        DEIDGUI.log("Out of memory, image could not be displayed. "
                                + "Increase memory available to DeID with the -Xmx "
                                + "option. -Xmx256m is recommended",
                                DEIDGUI.LOG_LEVEL.ERROR);
                    }
                } catch (FileNotFoundException ex) {
                    DEIDGUI.log("Unable to render image, file does not exist: "
                            + ex.getMessage(), DEIDGUI.LOG_LEVEL.ERROR);
                } catch (IOException ex) {
                    DEIDGUI.log("Unable to render image, file read error: "
                            + ex.getMessage(), DEIDGUI.LOG_LEVEL.ERROR);
                }
            } else {
                DEIDGUI.log("No image data found in " + image.getAbsolutePath(),
                        DEIDGUI.LOG_LEVEL.ERROR);
            }

            synchronized (this) {
                this.notify();
            }
        }

        private void correctOrieatation() {
            
        }
    }
    private BufferedImage i;
    private double[][][] data;
    private float calMin, calMax, calMin2, calMax2, sform, qform;
    private short[] dims;
    private File curImage;
    private float quat_x, quat_y, quat_z;

    public NiftiDisplayPanel() {
    }

    public void setImage(final File image) {
        if (curImage != image) {
            // Set busy cursor
            DEIDGUI.getFrames()[0].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            Thread loadThread = new Thread(new ImageLoader(image));

            synchronized (loadThread) {
                try {
                    loadThread.start();
                    loadThread.wait();
                    setSlice(.5f);
                } catch (InterruptedException ex) {
                    DEIDGUI.log("Failed to load image", DEIDGUI.LOG_LEVEL.ERROR);
                } finally {
                    // Set normal cursor
                    DEIDGUI.getFrames()[0].setCursor(Cursor.getDefaultCursor());
                }
            }
        }
    }

    public void setSlice(float sliceFactor) {
        if (curImage != null) {
            if (sform == 4.0 || sform == 0.0) {
               i = new BufferedImage(dims[2], dims[0], BufferedImage.TYPE_INT_RGB);

                int y = (int) ((dims[1] - 1) * Math.max(0, Math.min(sliceFactor, 1.0)));
                for (int x = dims[2]-1; x >=0 ; x--) {
                    for (int z = 0; z < dims[0]; z++) {
                       // System.out.println("hh");
                        //float colorFactor = Math.min(((float)data[z][y][x] - calMin)/ (calMax - calMin), 1f);
                        float colorFactor = Math.min(((float) data[z][y][x] - calMin2) / (calMax2 - calMin2), 1f);
                        colorFactor=Math.max(0, colorFactor);
                        int argb = new Color(colorFactor, colorFactor, colorFactor).getRGB();
                        i.setRGB(x, dims[0] - 1 - z, argb);
                    }
                }
            } else if (sform == 1.0 && quat_x < 0 && quat_y > 0 && quat_z < 0) {
                /* i = new BufferedImage(dims[1], dims[0], BufferedImage.TYPE_INT_RGB);*/
                i = new BufferedImage(dims[0], dims[1], BufferedImage.TYPE_INT_RGB);
                int x = (int) ((dims[2] - 1) * Math.max(0, Math.min(sliceFactor, 1.0)));
                for (int z = 0; z < dims[0]; z++) {
                    for (int y = 0; y < dims[1]; y++) {
                        //  float colorFactor = Math.min(((float)data[z][y][x] - calMin2)/ (calMax - calMin), 1f);
                        //System.out.println((float)data[z][y][x]);
                        float colorFactor = Math.min(((float) data[z][y][x] - calMin2) / (calMax2 - calMin2), 1f);
                        int argb = new Color(colorFactor, colorFactor, colorFactor).getRGB();
                        //float cF =  Math.abs((float)data[z][y][x] - calMin2) * 255f / Math.abs(calMax2 - calMin2) ;
                        //int argb = new Color(cF, cF, cF).getRGB();
                        i.setRGB(dims[0] - 1 - z, dims[1] - 1 - y, argb);
                        // i.setRGB(y, z, argb);
                    }
                }
            } else if (sform == 1.0 && quat_x > 0 && quat_y > 0 && quat_z < 0) {
                /* i = new BufferedImage(dims[1], dims[0], BufferedImage.TYPE_INT_RGB);*/
                i = new BufferedImage(dims[2], dims[0], BufferedImage.TYPE_INT_RGB);

                int y = (int) ((dims[1] - 1) * Math.max(0, Math.min(sliceFactor, 1.0)));
                for (int x = 0; x < dims[2]; x++) {
                    for (int z = 0; z < dims[0]; z++) {
                        //float colorFactor = Math.min(((float)data[z][y][x] - calMin)/ (calMax - calMin), 1f);
                        float colorFactor = Math.min(((float) data[z][y][x] - calMin2) / (calMax2 - calMin2), 1f);
                        int argb = new Color(colorFactor, colorFactor, colorFactor).getRGB();
                        i.setRGB(dims[2] - 1 - x, dims[0] - 1 - z, argb);
                    }
                }
            } else {
                i = new BufferedImage(dims[0], dims[1], BufferedImage.TYPE_INT_RGB);
                int x = (int) ((dims[2] - 1) * Math.max(0, Math.min(sliceFactor, 1.0)));
                for (int z = 0; z < dims[0]; z++) {
                    for (int y = 0; y < dims[1]; y++) {
                        //  float colorFactor = Math.min(((float)data[z][y][x] - calMin2)/ (calMax - calMin), 1f);
                        //System.out.println((float)data[z][y][x]);
                        float colorFactor = Math.min(((float) data[z][y][x] - calMin2) / (calMax2 - calMin2), 1f);
                        int argb = new Color(colorFactor, colorFactor, colorFactor).getRGB();
                        //float cF =  Math.abs((float)data[z][y][x] - calMin2) * 255f / Math.abs(calMax2 - calMin2) ;
                        //int argb = new Color(cF, cF, cF).getRGB();
                        i.setRGB(dims[0] - 1 - z, dims[1] - 1 - y, argb);
                        // i.setRGB(y, z, argb);
                    }
                }
            }
            /*else if (sform == 0.0 ) {
             i = new BufferedImage(dims[1], dims[0], BufferedImage.TYPE_INT_RGB);
             int x = (int) ((dims[2] - 1) * Math.max(0, Math.min(sliceFactor, 1.0)));
             for (int y = 0; y < dims[1]; y++) {
             for (int z = 0; z < dims[0]; z++) {
             //  float colorFactor = Math.min(((float)data[z][y][x] - calMin2)/ (calMax - calMin), 1f);
             //System.out.println((float)data[z][y][x]);
             float colorFactor = Math.min(((float)data[z][y][x] - calMin2)/ (calMax2 - calMin2), 1f);
             int argb = new Color(colorFactor, colorFactor, colorFactor).getRGB();
             //float cF =  Math.abs((float)data[z][y][x] - calMin2) * 255f / Math.abs(calMax2 - calMin2) ;
             //int argb = new Color(cF, cF, cF).getRGB();
             i.setRGB(y, dims[0] - 1 - z, argb);
             // i.setRGB(y, z, argb);
             }
             }
            
             }*/
        }
        this.repaint();
    }

    public float getMax(double[][][] triDarr) {
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

    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        if (i != null) {
            int width = getWidth(), height = width * i.getHeight() / i.getWidth();
            if (height > getHeight()) {
                height = getHeight();
                width = height * i.getWidth() / i.getHeight();
            }

            g.drawImage(i, (getWidth() - width) / 2, (getHeight() - height), width, height, this);
        }
    }
}
