package dit.panels;

import dit.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author christianprescott and angelo
 */
public class MatchDataPanel extends javax.swing.JPanel implements WizardPanel {
    private MatchTableModel model;
    private ManualMatchTableModel mmodel;
    private WarningJdialog wjd;
    private ManuallyCorrectFrame mcp;
    private removeImageFrame rif;
    private ManualCorrectTableModel cmodel;
    
    /**
     * Creates new form MatchDataPanel
     */
    public MatchDataPanel() {
        initComponents();
        DEIDGUI.title = "Data Matching";
        DEIDGUI.helpButton.setEnabled(true);
        DEIDGUI.jButtonMisHelp.setVisible(true);
        boolean isSearchByPath = jCheckBox1.isSelected();
        boolean isMultipleLink = jCheckBox2.isSelected();
        
        /*model = new MatchTableModel(DeidData.niftiFiles, DeidData.demographicData.getColumn(DeidData.IdColumn), isSearchByPath,isMultipleLink);
        jTable2.setModel(model);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
        
        if(model.getMismatchedImageCount() > 0 || model.getMatchedImageCount() == 0){
            // Ensure that there is at least one matched image, and that
            // there are no unmatched images, otherwise OK
            DEIDGUI.continueButton.setEnabled(false);
        } else {
            DEIDGUI.continueButton.setEnabled(true);
        }*/
        String  matchingkey = jTextField1.getText();
        mmodel = new ManualMatchTableModel(DeidData.niftiFiles, DeidData.demographicData.getColumn(DeidData.IdColumn),matchingkey,isSearchByPath,isMultipleLink);
        jTable2.setModel(mmodel);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
        //System.out.println(mmodel.getMismatchedImageCount()+" and " + mmodel.getMatchedImageCount());
          
        if(mmodel.getMismatchedImageCount() > 0 || mmodel.getMatchedImageCount() == 0){
            // Ensure that there is at least one matched image, and that
            // there are no unmatched images, otherwise OK
            //wjd = new WarningJdialog(new JFrame(), "Warning", "Mismatch for each image must be removed.");
            
            DEIDGUI.continueButton.setEnabled(false);
        } else {
            DEIDGUI.continueButton.setEnabled(true);
        }
        
       /* mmodel = new ManualMatchTableModel(DeidData.niftiFiles, DeidData.demographicData.getColumn(DeidData.IdColumn),"",false,false);
        jTable2.setModel(mmodel);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
        
        if(mmodel.getMismatchedImageCount() > 0 || mmodel.getMatchedImageCount() == 0){
            // Ensure that there is at least one matched image, and that
            // there are no unmatched images, otherwise OK
            wjd = new WarningJdialog(new JFrame(), "Warning", "Mismatch for each image must be removed.");
            DEIDGUI.continueButton.setEnabled(false);
        } else {
            DEIDGUI.continueButton.setEnabled(true);
        }
        */
        cmodel = new ManualCorrectTableModel();       
       
        jTable2.setModel(cmodel);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
        final JComboBox comboBox = new JComboBox();
        Object[] demoIDs = DeidData.demographicData.getColumn(DeidData.IdColumn);
        int demoIDNdx = 0;
        while(demoIDNdx < demoIDs.length){
            comboBox.addItem((String)demoIDs[demoIDNdx]);
            demoIDNdx++;
        
        }        
        comboBox.addItem("None");
        jTable2.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));
        jTable2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //System.out.println(model.getMismatchedImageCount());
        comboBox.setEditable(true);
         comboBox.addItemListener(new ItemListener(){
        @Override
        public void itemStateChanged(ItemEvent e){
            int i = jTable2.getSelectedRow();
            if (i>=0){
            JComboBox cb = (JComboBox)e.getSource();
            Object item = e.getItem();
            String filename = (String)DeidData.data[i][0];
        System.out.println(i);
        if (e.getStateChange() == ItemEvent.SELECTED) {
            // Item was just selected
            if (DeidData.data[i][0]!=null)
            {
                if (cb.getSelectedItem().toString().equals("None")) {
                    DeidData.data[i][1] = null; 
                    DeidData.data[i][2] = new Boolean(false);
                }
                else {
                    DeidData.data[i][1] = (cb.getSelectedItem()); 
                    DeidData.data[i][2] = new Boolean(true);
                   /* filename = filename.replace(".nii", "");
                    try {filename = filename.replace(".gz","");}
                    catch (Exception ex){
                    System.out.println("Fail to add filename and id pair.");
                    }*/
                    DeidData.IdFilename.put(filename, cb.getSelectedItem().toString() );
                   
            
                }
                Object[][] data = DeidData.data;
                int checkFlag = 0;
                for(int ii = 0; ii < data.length; ii++)
                {
                     if (ii != i && (Boolean)data[ii][2] == true && data[ii][1].toString().equals(cb.getSelectedItem().toString())) 
                    { 
               // wjd = new WarningJdialog(new JFrame(), "Warning", "There exists another row where has the same ID matched with a different image.");                
               
                //break;
                    }
                if (!(Boolean)data[ii][2]){ checkFlag = 1;}
                    }
                    if (checkFlag == 0) {DEIDGUI.continueButton.setEnabled(true);}
                    else {DEIDGUI.continueButton.setEnabled(false);}
            
            
               // System.out.println(comboBox.getSelectedItem().toString());
            jTable2.setValueAt(cb.getSelectedItem().toString(), i, 1);    
            jTable2.setValueAt("true", i, 2);
            jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
            //cb.setSelectedItem(cb.getSelectedItem());
            jTable2.clearSelection();
            }   
        }
           
       
            
        
        
        }
        
        }
        });
        DEIDGUI.log("MatchDataPanel initialized");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jCheckBox2 = new javax.swing.JCheckBox();

        setPreferredSize(new java.awt.Dimension(840, 400));

        jLabel4.setText("<html><p>Please scroll below to ensure that image files and data IDs have been correctly matched.  Mismatched data should be corrected before selecting Continue>.</p><p>&nbsp;</p></html>");

        jTable2.setModel(new MatchTableModel());
        jScrollPane3.setViewportView(jTable2);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Matching Pattern");

        jButton1.setText("Match");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Search the Path for Matching Information");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton2.setText("Remove Selected Image");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("Multiple Images linking to one Subject");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jScrollPane3)
                        .addContainerGap())
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
                        .add(110, 110, 110))
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 166, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(layout.createSequentialGroup()
                        .add(jCheckBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 301, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jCheckBox2)
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCheckBox1)
                    .add(jCheckBox2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton1)
                    .add(jButton2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String  matchingkey = jTextField1.getText();
        boolean isSearchByPath = jCheckBox1.isSelected();
        boolean isMultipleLink = jCheckBox2.isSelected();
        //System.out.println(matchingkey);
        //if (!matchingkey.trim().equals("")) {
        
        
        
        
        
        
        mmodel = new ManualMatchTableModel(DeidData.niftiFiles, DeidData.demographicData.getColumn(DeidData.IdColumn),matchingkey,isSearchByPath,isMultipleLink);
        jTable2.setModel(mmodel);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
        
        if(mmodel.getMismatchedImageCount() > 0 || mmodel.getMatchedImageCount() == 0){
            // Ensure that there is at least one matched image, and that
            // there are no unmatched images, otherwise OK
            //System.out.println(mmodel.getMismatchedImageCount()+" and " + mmodel.getMatchedImageCount());
           // wjd = new WarningJdialog(new JFrame(), "Warning", "Mismatch for each image must be removed.");
            DEIDGUI.continueButton.setEnabled(false);
        } else {
            DEIDGUI.continueButton.setEnabled(true);
        }
      //  }
       /* else {
        
        
        model = new MatchTableModel(DeidData.niftiFiles, DeidData.demographicData.getColumn(DeidData.IdColumn), isSearchByPath,isMultipleLink);
       
       
        jTable2.setModel(model);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
        System.out.println(model.getMismatchedImageCount());
        if(model.getMismatchedImageCount() > 0 || model.getMatchedImageCount() == 0){
            // Ensure that there is at least one matched image, and that
            // there are no unmatched images, otherwise OK
            wjd = new WarningJdialog(new JFrame(), "Warning", "Mismatch for each image must be removed.");
            DEIDGUI.continueButton.setEnabled(false);
        } else {
           
            DEIDGUI.continueButton.setEnabled(true);
        }
        
        }*/
        
        cmodel = new ManualCorrectTableModel();       
       
        jTable2.setModel(cmodel);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
        final JComboBox comboBox = new JComboBox();
        Object[] demoIDs = DeidData.demographicData.getColumn(DeidData.IdColumn);
        int demoIDNdx = 0;
        while(demoIDNdx < demoIDs.length){
            comboBox.addItem((String)demoIDs[demoIDNdx]);
            demoIDNdx++;
        
        }        
        comboBox.addItem("None");
        jTable2.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));
        jTable2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //System.out.println(model.getMismatchedImageCount());
        comboBox.setEditable(true);
         comboBox.addItemListener(new ItemListener(){
        @Override
        public void itemStateChanged(ItemEvent e){
            int i = jTable2.getSelectedRow();
            if (i>=0){
            JComboBox cb = (JComboBox)e.getSource();
            Object item = e.getItem();
            String filename = (String)DeidData.data[i][0];
        System.out.println(i);
        if (e.getStateChange() == ItemEvent.SELECTED) {
            // Item was just selected
            if (DeidData.data[i][0]!=null)
            {
                if (cb.getSelectedItem().toString().equals("None")) {
                    DeidData.data[i][1] = null; 
                    DeidData.data[i][2] = new Boolean(false);
                }
                else {
                    DeidData.data[i][1] = (cb.getSelectedItem()); 
                    DeidData.data[i][2] = new Boolean(true);
                   /* filename = filename.replace(".nii", "");
                    try {filename = filename.replace(".gz","");}
                    catch (Exception ex){
                    System.out.println("Fail to add filename and id pair.");
                    }*/
                    DeidData.IdFilename.put(filename, cb.getSelectedItem().toString() );
                  
                }
            
            Object[][] data = DeidData.data;
                int checkFlag = 0;
                for(int ii = 0; ii < data.length; ii++)
                {
                     if (ii != i && (Boolean)data[ii][2] == true && data[ii][1].toString().equals(cb.getSelectedItem().toString())) 
                    { 
               // wjd = new WarningJdialog(new JFrame(), "Warning", "There exists another row where has the same ID matched with a different image.");                
               
                //break;
                    }
                if (!(Boolean)data[ii][2]){ checkFlag = 1;}
                    }
                    if (checkFlag == 0) {DEIDGUI.continueButton.setEnabled(true);}
                    else {DEIDGUI.continueButton.setEnabled(false);}
               // System.out.println(comboBox.getSelectedItem().toString());
            jTable2.setValueAt(cb.getSelectedItem().toString(), i, 1);    
            jTable2.setValueAt("true", i, 2);
            jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
            //cb.setSelectedItem(cb.getSelectedItem());
            jTable2.clearSelection();
            }   
        }
           
       
            
        
        
        }
        
        }
        });
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
        int[] selection = jTable2.getSelectedRows();
        int selectNum = selection.length;
        if(selectNum > 0)
        {
            int ii = JOptionPane.showConfirmDialog(null,
            "Are you sure to remove selected images?", "Confirmation",
            JOptionPane.YES_NO_OPTION);
            if (ii == JOptionPane.OK_OPTION){
            for(int s = 0; s < selectNum;s++){
            DeidData.niftiFiles.remove(selection[s] - s);
            }
            }
        
        }
        
        String  matchingkey = jTextField1.getText();
        boolean isSearchByPath = jCheckBox1.isSelected();
        boolean isMultipleLink = jCheckBox2.isSelected();
        //System.out.println(matchingkey);
        //if (!matchingkey.trim().equals("")) {
        
        
        
        
        
        
        mmodel = new ManualMatchTableModel(DeidData.niftiFiles, DeidData.demographicData.getColumn(DeidData.IdColumn),matchingkey,isSearchByPath,isMultipleLink);
        jTable2.setModel(mmodel);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
        
        if(mmodel.getMismatchedImageCount() > 0 || mmodel.getMatchedImageCount() == 0){
            // Ensure that there is at least one matched image, and that
            // there are no unmatched images, otherwise OK
            //System.out.println(mmodel.getMismatchedImageCount()+" and " + mmodel.getMatchedImageCount());
           // wjd = new WarningJdialog(new JFrame(), "Warning", "Mismatch for each image must be removed.");
            DEIDGUI.continueButton.setEnabled(false);
        } else {
            DEIDGUI.continueButton.setEnabled(true);
        }
      //  }
       /* else {
        
        
        model = new MatchTableModel(DeidData.niftiFiles, DeidData.demographicData.getColumn(DeidData.IdColumn), isSearchByPath,isMultipleLink);
       
       
        jTable2.setModel(model);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
        System.out.println(model.getMismatchedImageCount());
        if(model.getMismatchedImageCount() > 0 || model.getMatchedImageCount() == 0){
            // Ensure that there is at least one matched image, and that
            // there are no unmatched images, otherwise OK
            wjd = new WarningJdialog(new JFrame(), "Warning", "Mismatch for each image must be removed.");
            DEIDGUI.continueButton.setEnabled(false);
        } else {
           
            DEIDGUI.continueButton.setEnabled(true);
        }
        
        }*/
        
        cmodel = new ManualCorrectTableModel();       
       
        jTable2.setModel(cmodel);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
        final JComboBox comboBox = new JComboBox();
        Object[] demoIDs = DeidData.demographicData.getColumn(DeidData.IdColumn);
        int demoIDNdx = 0;
        while(demoIDNdx < demoIDs.length){
            comboBox.addItem((String)demoIDs[demoIDNdx]);
            demoIDNdx++;
        
        }        
        comboBox.addItem("None");
        jTable2.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));
        jTable2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //System.out.println(model.getMismatchedImageCount());
        comboBox.setEditable(true);
         comboBox.addItemListener(new ItemListener(){
        @Override
        public void itemStateChanged(ItemEvent e){
            int i = jTable2.getSelectedRow();
            if (i>=0){
            JComboBox cb = (JComboBox)e.getSource();
            Object item = e.getItem();
            String filename = (String)DeidData.data[i][0];
        System.out.println(i);
        if (e.getStateChange() == ItemEvent.SELECTED) {
            // Item was just selected
            if (DeidData.data[i][0]!=null)
            {
                if (cb.getSelectedItem().toString().equals("None")) {
                    DeidData.data[i][1] = null; 
                    DeidData.data[i][2] = new Boolean(false);
                }
                else {
                    DeidData.data[i][1] = (cb.getSelectedItem()); 
                    DeidData.data[i][2] = new Boolean(true);
                   /* filename = filename.replace(".nii", "");
                    try {filename = filename.replace(".gz","");}
                    catch (Exception ex){
                    System.out.println("Fail to add filename and id pair.");
                    }*/
                    DeidData.IdFilename.put(filename, cb.getSelectedItem().toString() );
                  
                }
            
            Object[][] data = DeidData.data;
                int checkFlag = 0;
                for(int ii = 0; ii < data.length; ii++)
                {
                     if (ii != i && (Boolean)data[ii][2] == true && data[ii][1].toString().equals(cb.getSelectedItem().toString())) 
                    { 
               // wjd = new WarningJdialog(new JFrame(), "Warning", "There exists another row where has the same ID matched with a different image.");                
               
                //break;
                    }
                if (!(Boolean)data[ii][2]){ checkFlag = 1;}
                    }
                    if (checkFlag == 0) {DEIDGUI.continueButton.setEnabled(true);}
                    else {DEIDGUI.continueButton.setEnabled(false);}
               // System.out.println(comboBox.getSelectedItem().toString());
            jTable2.setValueAt(cb.getSelectedItem().toString(), i, 1);    
            jTable2.setValueAt("true", i, 2);
            jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
            //cb.setSelectedItem(cb.getSelectedItem());
            jTable2.clearSelection();
            }   
        }
           
       
            
        
        
        }
        
        }
        });
        //rif = new removeImageFrame();	
		
        //rif.pack();
        //rif.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    @Override
    public WizardPanel getNextPanel() {
                
        DeidData.demographicDataforBack = DeidData.demographicData;
        try{
        DEIDGUI.jButtonMisHelp.setVisible(false);
        mcp.setVisible(false);
        rif.setVisible(false);
        }catch(Exception e){
        return new DeIdentifyPanel();
        }
        return new DeIdentifyPanel();
    }

    @Override
    public WizardPanel getPreviousPanel() {
        DeidData.demographicData = null;
        DEIDGUI.jButtonMisHelp.setVisible(false);
        return new LoadDemoPanel();
    }
}
