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
        
        jTextArea1.setText("Enter your name and institution then click 'Continue' button to proceedto next step 'load images'.");
        }
        else if(page.equals("Load Images")){
        jTextArea1.setText("Click one of the buttons to add images or add directories where the images are located."  
                      +  "\nOnce the images are selected, remove images by clicking on the path and then clicking on the Remove selected button. "
                      +  "\nThe Select All button can be used to highlight all of the images for removal.\n"
                      +  "\nThe red check-box should be checked if your images are already skull-stripped.\n"
                      +  "\nClick the 'Continue' button to proceed to next 'Load data file' step.");
        
        }
        else if (page.equals("Load Demographic Data")){
        jTextArea1.setText("Click the 'Choose Data File' button to select the data file related to the image files in the previous step."
                      +   "\nDeID can read .txt, .xls and .xlsx data files. You can change the file by clicking the 'Choose Data File' button again and selecting a different file. \n"
                      +   "\nDeID will highlight any problems in the data file (e.g., missing value). You can edit each cell to correct any problems.\n"
                      +   "\nBy default, the first column is used for matching cases in the data file with the image files. "
                      +   "\nIf this is not your matching variable, click on the variable name at the top of the column for your matching variable.\n"
                      +   "\nClick the 'Continue' button to proceed to next 'Data Matching' step. ");
                    
        }
        else if (page.equals("Data Matching")){
        jTextArea1.setText("DeID automatically tries to match the image file/path names with the ID variable in your data file. DeID will report \"MISMATCH\" when it cannot identify matching values.  "
                      +   "\nIn the case where your path contains the subject's ID rather than the image, select 'Search the Path for Matching Information'. "
                      +   "\nIf you have more than one image for each subject, select 'Multiple Images Linking to One Subject' . "
                      +   "\nThese two options address most matching problems. If you still have mismatches, use the Matching Pattern function.\n"
                      +   "\nMatching Pattern function:  You can type in a string of values that appear in your image file name that may precede "
                      +   "\nor follow the matching values in a file and that is consistent across subjects."
                      +   "\nIf you type IXI### in the Matching Pattern box, DeID  will map the variable in demographic file to that format (e.g., one subject's ID is '12', then its ID will be mapped to an image with 'IXI012' in the filename.).  "
                      +   "\nOr, if you input IXI%#, DeID will map the variable in demographic file to that format (e.g., one subject's ID is 'ab12', then its ID will be mapped to an image with 'IXIab12' in the filename.)\n"
                      +   "\nNote that you cannot Continue until the mismatches are resolved.  You can select a mismatched case and remove it. \n"
                      +   "\nAlso note that there may be additional cases in your data file that are not matching, but these mismatches are not displayed because there is not matching image file."
                      +   "\nThese data are given new IDs and included in the file tarball.\n"
                      +   "\nClick the 'Continue' button to proceed to next 'Deidentification Options' step.");
        
        }
        else if (page.equals("Deidentification Options")){
        jTextArea1.setText("Select identifying variables that need to be removed from your data file. "
                      +   "\nYour ID variable should be removed.  Names, addresses, contact information, birth dates, test dates, medical record numbers, and social security numbers should be removed. "
                      +   "\nBelow is the HIPAA PHI list of 18 identifiers that should not be included in your de-identified data file.\n"
                      +   "\nClick the 'Continue' button to start de-identification process and proceed to next step 'Auditing'.\n"
                      +   "\n1. Names;"                      
                      +   "\n2. All geographical subdivisions smaller than a State, including street address, city, county, precinct, zip code, and their equivalent geocodes, except for the initial three digits of a zip code"
                      +   "\n3. All elements of dates (except year) for dates directly related to an individual, including birth date, admission date, discharge date, date of death; "
                      +   "\nand all ages over 89 and all elements of dates (including year) indicative of such age, except that such ages and elements may be aggregated into a single category of age 90 or older;"
                      +   "\n4. Phone numbers;"
                      +   "\n5. Fax numbers;"
                      +   "\n6. Electronic mail addresses;"
                      +   "\n7. Social Security numbers;"
                      +   "\n8. Medical record numbers;"
                      +   "\n9. Health plan beneficiary numbers;"
                      +   "\n10. Account numbers;"
                      +   "\n11. Certificate/license numbers;"
                      +   "\n12. Vehicle identifiers and serial numbers, including license plate numbers;"
                      +   "\n13. Device identifiers and serial numbers;"
                      +   "\n14. Web Universal Resource Locators (URLs);"
                      +   "\n15. Internet Protocol (IP) address numbers;"
                      +   "\n16. Biometric identifiers, including finger and voice prints;"
                      +   "\n17. Full face photographic images and any comparable images;"
                      +   "\n18. Any other unique identifying number, characteristic, or code (note this does not mean the unique code assigned by the investigator to code the data)");
        
        }
        else if (page.equals("Auditing")){
        jTextArea1.setText("Click on a file name to view the image after skull-stripping.\n"
                      +   "\nThe checkbox is used to select images that will be included in the final tarball.  Images that cannot be skull stripped should not be included in the final tarball.\n"
                      +   "\nClicking on 'View Image' will open the selected image in MRIcron with a 3D rendering window. "
                      +   "\nYou can use these function to evaluate the quality of skull-stripping and the extent to which  face can be rendered.\n"
                      +   "\nThe 'Redo' button allows you to skull-strip the images again with a different BET threshold.  Lower values will remove fewer voxels representing tissue.\n"
                      +   "\nThe 'View Demographic Data' allows you to inspect the de-identified data file.\n"
                      +   "\n'View DICOM Header' only works with DICOM files. \n"
                      +   "\nClick the 'Continue' button to proceed to next 'Transfer Options' step.");
        
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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
