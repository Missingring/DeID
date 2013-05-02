/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

/**
 *
 * @author angelo
 */
public class HelpManualFrame extends javax.swing.JFrame {

    /**
     * Creates new form HelpManualFrame
     */
    public HelpManualFrame(String page) {
        initComponents();
        jTextArea1.setEditable(false);
        if (page.equals("User Information")){
        
        jTextArea1.setText("Enter your name and institution then click 'Continue' button to proceed"
                      +  "\nto next step 'load images'.");
        }
        else if(page.equals("Load Images")){
        jTextArea1.setText("In this step, you can click 'add images' button to load the images"  
                      +  "\nfrom the directory you specify. You can either select image files "
                      +  "\nor the directory contains the files. When you finish selecting image"
                      +  "\nfiles, they will be listed. You can select items from the list and "
                      +  "\nclick 'Remove Selected' button to remove the images you don't want to."
                      +  "\nprocess."
                      +  "\nClick 'Continue' button to proceed to next step 'Load demographic file'"
                      +  "\nand click 'Back' button to previous step 'User information'.");
        
        }
        else if (page.equals("Load Demographic Data")){
        jTextArea1.setText("In this step, you can click 'choose data file' button to select the"
                      +   "\ndemographic file related to the image files in the previous step. "
                      +   "\nWe support .txt, .xls and .xlsx data files. You can change the file"
                      +   "\nby simply click 'choose data file' button again and re-select a file."
                      +   "\nAfter you finish selecting, the data in the file will be display in"
                      +   "\nthe table below. DeID will remind you the problems(e.g.missing value) "
                      +   "\nof your data. You can click 'Edit' button to modify the data but "
                      +   "\nthis modification will not affect the original data file. A new window"
                      +   "\nwill be created after you click 'Edit' button. You can double-click the"
                      +   "\ntable cell to set a new value."
                      +   "\nOnce you finish modifying the data, click 'Done' to get back to the main"
                      +   "\nwindow and click 'Refresh' button and view the modified data in the table." 
                      +   "\nThen you can select a variable(column name) in this table to be used in "
                      +   "\nnext data matching step."
                      +  "\nClick 'Continue' button to proceed to next step 'Data Matching'."
                      +  "\nClick 'Back' button to previous step 'Load images'.");
        }
        else if (page.equals("Data Matching")){
        jTextArea1.setText("This step mainly focuses on the data matching(match the image files "
                      +   "\nwith the record in demographic file). There will be an initial matching"
                      +   "\ndone by DeID automatically. "
                      +   "\nIf it doesn't work, you may help match the data and files."
                      +   "\nProvide a string with regulation between the demographic matching variable "
                      +   "\nand the image file name, then click'Auto Match' to re-match the data. "
                      +   "\n'#' will represent a digit of number and '%' will represent a string of any length. "
                      +   "\nIf you input 'IXI###':"
                      +   "\nsystem will map the variable in demographic file to that format(e.g. "
                      +   "\none sample's ID is '12', then its id will be mapped to 'IXI012'.) Then"
                      +   "\nDeId will make use of the new substring to match image files."
                      +   "\nIf you input 'IXI%':"
                      +   "\nsystem will map the variable in demographic file to that format(e.g. "
                      +   "\none sample's ID is 'ab12', then its id will be mapped to 'IXIab12'.) Then"
                      +   "\nDeId will make use of the new substring to match image files."
                      +   "\nYou can check the checkbox to search the images' path to find the part matches."
                      +   "\nIf none of the above matching methods works or some of the matching result are" 
                      +   "\nnot correct in your view, you can manually correct the mistakes. "
                      +   "\nClick 'Manually Correct' button, a new window will show up to help correct the"
                      +   "\nmatching mistakes. You can click the cells of id column and select a id from"
                      +   "\nthe dropdown list of id. After you finish correcting, click 'Done' button."
                      +   "\nClick 'Refresh' button to view the new matching result in the main window. If"
                      +   "\neverything is OK, the 'Continue' button will be enabled to click. Otherwise, there"
                      +   "\nwill be warnings to remind you the problem in matching."
                      +   "\nClick 'Continue' button to proceed to next step 'Deidentification Options'."
                      +   "\nClick 'Back' button to previous step 'Load demographic file'.");
        
        }
        else if (page.equals("Deidentification Options")){
        jTextArea1.setText("In this step, you can select the variables to be de-identified in demographic"
                      +   "\ndata. And you can also select the check box to de-face the images."
                      +   "\nJust select one item in the list on the left and click 'remove' button to"
                      +   "\nadd itinto the list on the right. You can also undo it by select one item"
                      +   "\nin the list on the right and click 'add' button."                      
                      +   "\nClick 'Continue' button to start de-identification process and proceed to "
                      +   "\nnext step 'Auditing'."
                      +   "\nClick 'Back' button to previous step 'Data Matching'.");
        
        }
        else if (page.equals("Auditing")){
        jTextArea1.setText("This step mainly focuses on auditing the de-identification results."
                      +   "\nThere is a list of image file names with new subject ids. Click on the row to"
                      +   "\nto view the image after skull-stripping on the right. "
                      +   "\nSelect the checkbox to determine whether one image will be included in final "
                      +   "\ndata tar file to submit."
                      +   "\nClick'View Image' to view image in MRIcron with a 3D rendering window. "
                      +   "\n'View head data' will be enabled when the image is converted from DICOM files."
                      +   "\nClick 'View Demographic Data' to view the de-identified demographic data."
                      +   "\nClick 'View image montage' to see the montage image generated by the system."
                      +   "\nClick 'Redo...' to open a new window to redo the defacing job of certain images."
                      +   "\nIn 'Redo...' window, there is a list of image files which you can select from to"
                      +   "\nbe re-skullstripped. Simply select the checkbox or use the buttons 'select all'"
                      +   "\nor 'unselect all' to help selecting. On the right, you can also view the current"
                      +   "\nimage."
                      +   "\nThen you can set new skullstripping threshold(0.0~1.0) to redo skull-stripping."
                      +   "\nClick 'Start' to start the re-skull-stripping job."
                      +   "\nClick 'Reset' to start another job." 
                      +   "\nClick 'Done' to finish the job and back to main window."                      
                      +   "\nClick 'Continue' button to proceed to next step 'Transfer Options'."
                      +   "\nClick 'Back' button to previous step 'Deidentification Options'.");
        
        }
        else if (page.equals("Transfer Options")){
        jTextArea1.setText("In this step, users can choose the sharing mode to determine whether to share"
                      +   "\nthe data to the public."
                      +   "\nThere are three options: "
                      +   "\nNo share: only you can reach and use the data. "
                      +   "\nEncalve: only the registered and authorized users can use the data."
                      +   "\nAll share: Everybody can  download and use the data."
                      +   "\nYou can save the result to local machine by choosing a path."
                      +   "\nYou can upload the result to a remote location by providing the server address,"
                      +   "\nport, user name and password."
                      +   "\nBy checking the checkbox 'I agree' to agree on the items to continue. "                                         
                      +   "\nClick 'Continue' button to process the data results and  proceed to last step"
                      +   "'Complete'."
                      +   "\nClick 'Back' button to previous step 'Auditing'.");
        
        }
        else if (page.equals("Complete")){
        jTextArea1.setText("This is the last page, you have completely finish the job. "
                      +   "\nYou can find your tar file in the path displayed."
                      +   "\nYou cannot get back to previous step because the temporary files are"
                      +   "\ncleaned up automatically."
                      +   "\nClick 'Done' to finish and exit."
                     );
        
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        CloseButton = new javax.swing.JButton();

        setTitle("Help Manual");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        CloseButton.setText("Close");
        CloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(CloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(CloseButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseButtonActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_CloseButtonActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
