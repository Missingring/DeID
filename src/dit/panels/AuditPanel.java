package dit.panels;

import dit.AuditJTable;
import dit.DeidData;
import dit.FileUtils;
import dit.NiftiDisplayPanel;
import dit.OpenImagewithMRIcron;
import dit.TextviewFrame;
import dit.ReSkullStrippingFrame;
import java.awt.Cursor;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.util.Hashtable;
import java.util.Iterator;


/**
 *
 * @author christianprescott
 */
public class AuditPanel extends javax.swing.JPanel implements WizardPanel {
     private class Point{
        public float x;
        public float y;
        public Point(float x,float y)
        {
            this.x=x;
            this.y=y;
        }
    }
    
    Point startPoint;
    Point endPoint;
    /**
     * Creates new form AuditPanel
     */
    private ReSkullStrippingFrame redo;
    public AuditPanel() {
        initComponents();
        // if(DeidData.demographicData != DemographicTableModel.dummyModel)
        createFakenames();
        jButtonViewMontage.setVisible(false);
        DEIDGUI.title = "Auditing";
        DEIDGUI.helpButton.setEnabled(true);
        if(DeidData.includeFileInTar == null){
            DeidData.includeFileInTar = new Boolean[DeidData.deidentifiedFiles.size()];
            // Select all images by default
            for (int ndx = 0; ndx < DeidData.includeFileInTar.length; ndx++) {
                DeidData.includeFileInTar[ndx] = true;
            }
        }
        
        // Define the AuditJTable model
        imagesTable.setModel(new AbstractTableModel() {
            // <editor-fold defaultstate="collapsed" desc="AuditTableModel">
            
            private String[] columnNames = new String[]{"Selected", "Image"};
            
            @Override
            public int getRowCount() {
                return DeidData.deidentifiedFiles.size();
            }
            
            @Override
            public int getColumnCount() {
                return columnNames.length;
            }
            
            @Override
            public String getColumnName(int i) {
                return columnNames[i];
            }
            
            @Override
            public Class getColumnClass(int i) {
                Class colClass;
                switch (i) {
                    case 0:
                        colClass = Boolean.class;
                        break;
                    case 1:
                        colClass = File.class;
                        break;
                    default:
                        colClass = Object.class;
                }
                return colClass;
            }
            
            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == 0 ? true : false);
            }
            
            @Override
            public Object getValueAt(int row, int col) {
                Object value;
                switch (col) {
                    case 0:
                        value = DeidData.includeFileInTar[row];
                        break;
                    case 1:
                        value = DeidData.deidentifiedFiles.get(row);
                        break;
                    default:
                        value = "Error";
                        break;
                }
                return value;
            }
            
            @Override
            public void setValueAt(Object o, int row, int col) {
                if (col == 0) {
                    DeidData.includeFileInTar[row] = (Boolean) o;
                }
            }
            // </editor-fold>
        });
        // Add a selection listener for enabling/disabling the "View Header" button
        imagesTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    // <editor-fold defaultstate="collapsed" desc="AuditTableSelectionListener">
                    
                    @Override
                    public void valueChanged(ListSelectionEvent lse) {
                        if (lse.getValueIsAdjusting()) {
                            File selectedFile = DeidData.deidentifiedFiles.get(imagesTable.getSelectedRow());
                            jButtonViewHeader.setEnabled(
                                    DeidData.ConvertedDicomHeaderTable.containsKey(selectedFile)
                                    ? true : false);
                            ((NiftiDisplayPanel)imagePanel).setImage(selectedFile);
                           // ((NiftiDisplayPanel)imagePanel).reset();
                        }
                    }
                    // </editor-fold>
                });
        
        DEIDGUI.log("AuditPanel initialized");
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButtonViewDemo = new javax.swing.JButton();
        jButtonViewImage = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        imagesTable = new AuditJTable();
        jButtonViewHeader = new javax.swing.JButton();
        imagePanel = new NiftiDisplayPanel();
        sliceBar = new javax.swing.JSlider();
        jButtonViewMontage = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        leftRotateBtn = new javax.swing.JButton();
        rightRotateBtn = new javax.swing.JButton();
        resetRotateBtn = new javax.swing.JButton();
        orientationLbl = new javax.swing.JLabel();

        jLabel1.setText("<html><p>Ensure the data has been de-identified. Deselect images that have not had identifying information properly removed or will not be transferred.</p><p>&nbsp;</p></html>");

        jButtonViewDemo.setText("View data file");
        jButtonViewDemo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewDemoActionPerformed(evt);
            }
        });

        jButtonViewImage.setText("View image");
        jButtonViewImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewImageActionPerformed(evt);
            }
        });

        imagesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                { new Boolean(true), "File1"},
                {null, "File2"},
                { new Boolean(true), "File3"},
                {null, "File4"}
            },
            new String [] {
                "Selected", "File"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(imagesTable);

        jButtonViewHeader.setText("View DICOM header");
        jButtonViewHeader.setEnabled(false);
        jButtonViewHeader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewHeaderActionPerformed(evt);
            }
        });

        imagePanel.setMinimumSize(new java.awt.Dimension(0, 32));
        imagePanel.setPreferredSize(new java.awt.Dimension(0, 0));
        imagePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                imagePanelMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                imagePanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                imagePanelMouseReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout imagePanelLayout = new org.jdesktop.layout.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 228, Short.MAX_VALUE)
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 52, Short.MAX_VALUE)
        );

        sliceBar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliceBarStateChanged(evt);
            }
        });

        jButtonViewMontage.setText("View image montage");
        jButtonViewMontage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewMontageActionPerformed(evt);
            }
        });

        jButton1.setText("Redo skull-stripping");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        leftRotateBtn.setText("Left");
        leftRotateBtn.setActionCommand("");
        leftRotateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftRotateBtnActionPerformed(evt);
            }
        });

        rightRotateBtn.setText("Right");
        rightRotateBtn.setActionCommand("");
        rightRotateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightRotateBtnActionPerformed(evt);
            }
        });

        resetRotateBtn.setText("Reset");
        resetRotateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetRotateBtnActionPerformed(evt);
            }
        });

        orientationLbl.setText("<html>Drag the image to adjust orientation. <br> Click buttons to rotate image. </html>");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 403, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(layout.createSequentialGroup()
                                    .add(45, 45, 45)
                                    .add(leftRotateBtn)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(resetRotateBtn)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(rightRotateBtn))
                                .add(layout.createSequentialGroup()
                                    .add(jButtonViewImage)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(jButtonViewHeader))
                                .add(sliceBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(imagePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 228, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(9, 9, 9))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jButtonViewDemo)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonViewMontage)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(orientationLbl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jButtonViewDemo)
                        .add(jButtonViewMontage)
                        .add(jButton1))
                    .add(orientationLbl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(19, 19, 19)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(leftRotateBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(rightRotateBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(resetRotateBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(imagePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(sliceBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jButtonViewImage)
                            .add(jButtonViewHeader))))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private boolean OpenFile(File file) {
        boolean openSucceeded;
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(file);
                openSucceeded = true;
            } catch (IOException ex) {
                DEIDGUI.log("The system couldn't open " + file.toString() + ": "
                        + ex.getMessage(), DEIDGUI.LOG_LEVEL.ERROR);
                openSucceeded = false;
            } catch (IllegalArgumentException e) {
                DEIDGUI.log("The system couldn't open " + file.toString() + ": "
                        + e.getMessage(), DEIDGUI.LOG_LEVEL.ERROR);
                openSucceeded = false;
            }
        } else {
            DEIDGUI.log("Unable to open file, desktop operations not supported", DEIDGUI.LOG_LEVEL.ERROR);
            openSucceeded = false;
        }
        return openSucceeded;
    }
    
    private void createFakenames(){
        Iterator curFile = DeidData.deidentifiedFiles.iterator();
        while (curFile.hasNext()){
            String x = FileUtils.getName((File)curFile.next()).toString();
            String y="";
            
           x=x.replace((DeidData.outputPath+"betOut/").replace(System.getProperty("file.separator").toString(), "_"), "");
          //  System.out.println("X:"+x);
            //System.out.println(DeidData.IdFilename.keys().nextElement());
            if(DeidData.isNoData)
                y=DeidData.IdTable.get(x);
            else
                y = DeidData.IdTable.get(DeidData.IdFilename.get(x));
            
           
            if (!DeidData.multinameSol.containsKey(y)){
                DeidData.multinameSol.put(y,1);
                DeidData.multinameSolFile.put(x, y + "_1"+".nii");
            }
            else
            {
                int value =  DeidData.multinameSol.get(y);
                value = value + 1;
                DeidData.multinameSolFile.put(x,y +"_"+ value+".nii");
                DeidData.multinameSol.remove(y);
                DeidData.multinameSol.put(y, value);
            }
            
            //}
        }
        
        
    }
    private boolean multiImages(Hashtable<String, String> ht, String val ){
        boolean ismulti = false;
        int count = 0;
        for (Iterator it = ht.keySet().iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            String value = ht.get(key);
            if (value.equals(val)) count++;
            
        }
        if (count > 1) ismulti = true;
        return ismulti;
    }
    
    private void jButtonViewImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewImageActionPerformed
        /* if(jTableImages.getSelectedRow() >= 0){
         * File selectedFile = (File) DeidData.deidentifiedFiles.get(jTableImages.getSelectedRow());
         * OpenFile(selectedFile);
         * }*/
        if(imagesTable.getSelectedRow() >= 0){
            File selectedFile = (File) DeidData.deidentifiedFiles.get(imagesTable.getSelectedRow());
            OpenImagewithMRIcron openImage = new OpenImagewithMRIcron(selectedFile);
            openImage.run();
        }
    }//GEN-LAST:event_jButtonViewImageActionPerformed
    
    private void jButtonViewDemoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewDemoActionPerformed
        // OpenFile(DeidData.deidentifiedDemoFile);
        TextviewFrame viewtext = new TextviewFrame(DeidData.deidentifiedDemoFile);
        //try {
        viewtext.pack();
        viewtext.setVisible(true);
        //} catch(IOException ex)
        //{
        //      DEIDGUI.log("Unable to open demographic file.", DEIDGUI.LOG_LEVEL.ERROR);
        //}
        
    }//GEN-LAST:event_jButtonViewDemoActionPerformed
    
    private void jButtonViewHeaderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewHeaderActionPerformed
        // Open matching header var file
        if(imagesTable.getSelectedRow() >= 0){
            File selectedFile = (File) DeidData.deidentifiedFiles.get(imagesTable.getSelectedRow());
            if (DeidData.ConvertedDicomHeaderTable.containsKey(selectedFile)) {
                // OpenFile(DeidData.ConvertedDicomHeaderTable.get(selectedFile));
                TextviewFrame viewtext = new TextviewFrame(DeidData.ConvertedDicomHeaderTable.get(selectedFile));
                viewtext.pack();
                viewtext.setVisible(true);
            }
        }
    }//GEN-LAST:event_jButtonViewHeaderActionPerformed
    
    private void sliceBarStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliceBarStateChanged
        if(imagesTable.getSelectedRow() >= 0){
            File selectedFile = (File) DeidData.deidentifiedFiles.get(imagesTable.getSelectedRow());
            ((NiftiDisplayPanel)imagePanel).setSlice((float)sliceBar.getValue()/100f);
        }
    }//GEN-LAST:event_sliceBarStateChanged
    
    private void jButtonViewMontageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewMontageActionPerformed
        File montageFile = new File(DeidData.outputPath + "montage.jpg");
        if(montageFile.exists()){
            // Programatically create a JFrame to view the image
            // TODO: place image within a scrollviewer?
            JFrame imageFrame = new JFrame("DeID image montage");
            imageFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            imageFrame.setResizable(false);
            JLabel imageLabel;
            try {
                imageLabel = new JLabel(new ImageIcon(ImageIO.read(montageFile)));
                imageFrame.add(imageLabel);
                imageFrame.pack();
                imageFrame.setVisible(true);
            } catch (IOException ex) {
                DEIDGUI.log("Unable to read image montage", DEIDGUI.LOG_LEVEL.ERROR);
            }
        } else {
            DEIDGUI.log("No montage image was created", DEIDGUI.LOG_LEVEL.ERROR);
        }
    }//GEN-LAST:event_jButtonViewMontageActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        redo = new ReSkullStrippingFrame();
        redo.pack();
        redo.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void rightRotateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightRotateBtnActionPerformed

        ((NiftiDisplayPanel)imagePanel).setOrientationState( ((NiftiDisplayPanel)imagePanel).rotateClockwise());
         ((NiftiDisplayPanel)imagePanel).setSlice(0.5f);

    }//GEN-LAST:event_rightRotateBtnActionPerformed

    private void leftRotateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftRotateBtnActionPerformed
         ((NiftiDisplayPanel)imagePanel).setOrientationState( ((NiftiDisplayPanel)imagePanel).rotateAntiClockwise());
         ((NiftiDisplayPanel)imagePanel).setSlice(0.5f);
    }//GEN-LAST:event_leftRotateBtnActionPerformed

    private void resetRotateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetRotateBtnActionPerformed
         ((NiftiDisplayPanel)imagePanel).reset();
    }//GEN-LAST:event_resetRotateBtnActionPerformed

    private void imagePanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imagePanelMouseEntered
           Cursor cursor=Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR); 
     //change cursor appearance to HAND_CURSOR when the mouse pointed on images
           imagePanel.setCursor(cursor);    
    }//GEN-LAST:event_imagePanelMouseEntered

    private void imagePanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imagePanelMousePressed
         startPoint=new Point(evt.getX(),evt.getY());
         //System.out.println("Mouse entered at ("+evt.getX()+","+evt.getY()+")");
    }//GEN-LAST:event_imagePanelMousePressed

    private void imagePanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imagePanelMouseReleased
         endPoint=new Point(evt.getX(),evt.getY());
         System.out.println("Mouse exited at ("+evt.getX()+","+evt.getY()+")");
         
         float diffX=endPoint.x-startPoint.x;
         float diffY=endPoint.y-startPoint.y;
         
         if(diffX>0)
         {
             if(Math.abs(diffY)<diffX)
                  ((NiftiDisplayPanel)imagePanel).setOrientationState(((NiftiDisplayPanel)imagePanel).getOrientationState().toRight());
             else if(diffY>0)
                    ((NiftiDisplayPanel)imagePanel).setOrientationState(((NiftiDisplayPanel)imagePanel).getOrientationState().toTop());
             else
                 ((NiftiDisplayPanel)imagePanel).setOrientationState(((NiftiDisplayPanel)imagePanel).getOrientationState().toBottom());
                 
         }
         if(diffX<0)
         {
              if(Math.abs(diffY)<(-diffX))
                  ((NiftiDisplayPanel)imagePanel).setOrientationState(((NiftiDisplayPanel)imagePanel).getOrientationState().toLeft());
              else if(diffY>0)
                    ((NiftiDisplayPanel)imagePanel).setOrientationState(((NiftiDisplayPanel)imagePanel).getOrientationState().toTop());
             else
                 ((NiftiDisplayPanel)imagePanel).setOrientationState(((NiftiDisplayPanel)imagePanel).getOrientationState().toBottom());
         }
             
        ((NiftiDisplayPanel)imagePanel).setSlice(0.5f);
    }//GEN-LAST:event_imagePanelMouseReleased
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel imagePanel;
    private javax.swing.JTable imagesTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonViewDemo;
    private javax.swing.JButton jButtonViewHeader;
    private javax.swing.JButton jButtonViewImage;
    private javax.swing.JButton jButtonViewMontage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton leftRotateBtn;
    private javax.swing.JLabel orientationLbl;
    private javax.swing.JButton resetRotateBtn;
    private javax.swing.JButton rightRotateBtn;
    private javax.swing.JSlider sliceBar;
    // End of variables declaration//GEN-END:variables
    
    @Override
    public WizardPanel getNextPanel() {
        try{
            redo.setVisible(false);
        }catch (Exception e){
            
            return new TransferPanel();
            
        }
        return new TransferPanel();
    }
    
    @Override
    public WizardPanel getPreviousPanel() {
        DeidData.includeFileInTar = null;
        return new DeIdentifyPanel();
    }
}
