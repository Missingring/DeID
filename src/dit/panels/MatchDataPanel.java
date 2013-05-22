package dit.panels;

import dit.*;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author christianprescott and angelo
 */
public class MatchDataPanel extends javax.swing.JPanel implements WizardPanel {

    private ManualMatchTableModel mmodel;
    private DummyFileTableModel dmodel;
    private ManuallyCorrectFrame mcp;
    private removeImageFrame rif;
    private ManualCorrectTableModel cmodel;

    public static HashMap<String,String> displayTofile;
    
    
    /**
     * Creates new form MatchDataPanel
     */
    public MatchDataPanel() {
        initComponents();
        DEIDGUI.title = "Data Matching";
        DEIDGUI.helpButton.setEnabled(true);
        DEIDGUI.jButtonMisHelp.setVisible(false);
        lblDummy.setVisible(false);
        boolean isSearchByPath = cbxSearchByPath.isSelected();
        boolean isMultipleLink = cbxMultiMatch.isSelected();
        
        displayTofile=new HashMap<>();
        
        if(DeidData.isNoData)
        {
            lblDummy.setVisible(true);
            btnMatch.setEnabled(false);
            dmodel=new DummyFileTableModel(DeidData.niftiFiles);
            jTable2.setModel(dmodel);
            jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
            cbxMultiMatch.setEnabled(false);
            cbxSearchByPath.setEnabled(false);
            DEIDGUI.continueButton.setEnabled(true);
        }
        else
        {
            String  matchingkey = txtMatchpattern.getText();
            mmodel = new ManualMatchTableModel(DeidData.niftiFiles, DeidData.demographicData.getColumn(DeidData.IdColumn),matchingkey,isSearchByPath,isMultipleLink);
            jTable2.setModel(mmodel);
            jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
            //System.out.println(mmodel.getMismatchedImageCount()+" and " + mmodel.getMatchedImageCount());
            findUnmatchCount();
            if(mmodel.getMismatchedImageCount() > 0 || mmodel.getMatchedImageCount() == 0){
                // Ensure that there is at least one matched image, and that
                // there are no unmatched images, otherwise OK
                //wjd = new WarningJdialog(new JFrame(), "Warning", "Mismatch for each image must be removed.");
                
                DEIDGUI.continueButton.setEnabled(false);
            } else {
                DEIDGUI.continueButton.setEnabled(true);
            }
            
            
            
            cmodel = new ManualCorrectTableModel();
            
            jTable2.setModel(cmodel);
            jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
            //        final JComboBox comboBox = new JComboBox();
            //        Object[] demoIDs = DeidData.demographicData.getColumn(DeidData.IdColumn);
            //        int demoIDNdx = 0;
            //        while(demoIDNdx < demoIDs.length){
            //            comboBox.addItem((String)demoIDs[demoIDNdx]);
            //            demoIDNdx++;
            //
            //        }
            //        comboBox.addItem("None");
            jTable2.getColumnModel().getColumn(1).setCellEditor(new ComboBoxCellEditor(lblMatchStat));
            jTable2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        }
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
        txtMatchpattern = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnMatch = new javax.swing.JButton();
        cbxSearchByPath = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        cbxMultiMatch = new javax.swing.JCheckBox();
        lblMatchStat = new javax.swing.JLabel();
        lblDummy = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(840, 400));

        jLabel4.setText("<html><p>Please scroll below to ensure that image files and data IDs have been correctly matched.  Mismatched data should be corrected before selecting Continue>.</p><p>&nbsp;</p></html>");

        jTable2.setModel(new MatchTableModel());
        jScrollPane3.setViewportView(jTable2);

        txtMatchpattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMatchpatternActionPerformed(evt);
            }
        });

        jLabel1.setText("Matching pattern");

        btnMatch.setText("Match");
        btnMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMatchActionPerformed(evt);
            }
        });

        cbxSearchByPath.setText("Search the path for matching information");
        cbxSearchByPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSearchByPathActionPerformed(evt);
            }
        });

        jButton2.setText("Remove selected image");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        cbxMultiMatch.setText("Multiple images linking to one subject");
        cbxMultiMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxMultiMatchActionPerformed(evt);
            }
        });

        lblDummy.setForeground(new java.awt.Color(255, 0, 0));
        lblDummy.setText("Some functions are not available since no data file is selected.");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .add(110, 110, 110))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(lblMatchStat, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane3)
                            .add(layout.createSequentialGroup()
                                .add(15, 15, 15)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createSequentialGroup()
                                        .add(cbxSearchByPath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 301, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jLabel1))
                                    .add(cbxMultiMatch))
                                .add(8, 8, 8)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(layout.createSequentialGroup()
                                        .add(txtMatchpattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 121, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(btnMatch, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 66, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(231, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(lblDummy)
                        .add(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblDummy)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cbxSearchByPath)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtMatchpattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnMatch))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cbxMultiMatch)
                    .add(jButton2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblMatchStat))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMatchActionPerformed
        // TODO add your handling code here:
        String  matchingkey = txtMatchpattern.getText();
        boolean isSearchByPath = cbxSearchByPath.isSelected();
        boolean isMultipleLink = cbxMultiMatch.isSelected();
        match(matchingkey, isSearchByPath, isMultipleLink);
         findUnmatchCount();
    
    }//GEN-LAST:event_btnMatchActionPerformed
    
    private void txtMatchpatternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMatchpatternActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMatchpatternActionPerformed
        
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
        
        String  matchingkey = txtMatchpattern.getText();
        boolean isSearchByPath = cbxSearchByPath.isSelected();
        boolean isMultipleLink = cbxMultiMatch.isSelected();
     
        
        mmodel = new ManualMatchTableModel(DeidData.niftiFiles, DeidData.demographicData.getColumn(DeidData.IdColumn),matchingkey,isSearchByPath,isMultipleLink);
        jTable2.setModel(mmodel);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
        findUnmatchCount();
        if(mmodel.getMismatchedImageCount() > 0 || mmodel.getMatchedImageCount() == 0){
            // Ensure that there is at least one matched image, and that
            // there are no unmatched images, otherwise OK
            //System.out.println(mmodel.getMismatchedImageCount()+" and " + mmodel.getMatchedImageCount());
            // wjd = new WarningJdialog(new JFrame(), "Warning", "Mismatch for each image must be removed.");
            DEIDGUI.continueButton.setEnabled(false);
        } else {
            DEIDGUI.continueButton.setEnabled(true);
        }

        
        cmodel = new ManualCorrectTableModel();
        
        jTable2.setModel(cmodel);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
       
        jTable2.getColumnModel().getColumn(1).setCellEditor(new ComboBoxCellEditor(lblMatchStat));
        jTable2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
       
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void cbxSearchByPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSearchByPathActionPerformed
        String  matchingkey = txtMatchpattern.getText();
        boolean isSearchByPath = cbxSearchByPath.isSelected();
        boolean isMultipleLink = cbxMultiMatch.isSelected();
        match(matchingkey, isSearchByPath, isMultipleLink);
        findUnmatchCount();
    }//GEN-LAST:event_cbxSearchByPathActionPerformed

    private void cbxMultiMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxMultiMatchActionPerformed
        String  matchingkey = txtMatchpattern.getText();
        boolean isSearchByPath = cbxSearchByPath.isSelected();
        boolean isMultipleLink = cbxMultiMatch.isSelected();
        match(matchingkey, isSearchByPath, isMultipleLink);
        findUnmatchCount();
    }//GEN-LAST:event_cbxMultiMatchActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMatch;
    private javax.swing.JCheckBox cbxMultiMatch;
    private javax.swing.JCheckBox cbxSearchByPath;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lblDummy;
    private javax.swing.JLabel lblMatchStat;
    private javax.swing.JTextField txtMatchpattern;
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
        //DeidData.demographicData = null;
        DEIDGUI.jButtonMisHelp.setVisible(false);
        return new LoadDemoPanel();
    }
    
    private void match(String key,boolean isPath,boolean isMultiSearch){
      String  matchingkey = key;
        boolean isSearchByPath = isPath;
        boolean isMultipleLink = isMultiSearch;
            
        mmodel = new ManualMatchTableModel(DeidData.niftiFiles, DeidData.demographicData.getColumn(DeidData.IdColumn),matchingkey,isSearchByPath,isMultipleLink);
        jTable2.setModel(mmodel);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
        
        if(mmodel.getMismatchedImageCount() > 0 || mmodel.getMatchedImageCount() == 0){
           
            DEIDGUI.continueButton.setEnabled(false);
        } else {
            DEIDGUI.continueButton.setEnabled(true);
        }
   
        
        cmodel = new ManualCorrectTableModel();
        
        jTable2.setModel(cmodel);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
       
        jTable2.getColumnModel().getColumn(1).setCellEditor((new ComboBoxCellEditor(lblMatchStat)));
        jTable2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //System.out.println(model.getMismatchedImageCount());
       
        
    }
    
    private void findUnmatchCount(){
        Collection<String> ids= DeidData.IdFilename.values();
        int totalID=DeidData.demographicData.getColumn(DeidData.IdColumn).length;
        for(Object obj : DeidData.demographicData.getColumn(DeidData.IdColumn))
        {
            String id=(String)obj;
            if(ids.contains(id))
                totalID--;
        }
        lblMatchStat.setText(totalID+ " cases have no images");
    }
}
