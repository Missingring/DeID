package dit;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.util.*;

/**
 *
 * @author Christian Prescott
 */
public class AuditJTableFileRenderer extends JLabel implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, 
        boolean isSelected, boolean cellHasFocus, int i, int i1) {
        // Displaying the image name this way is sort of cheating - at this stage
        // the image file still has its original name. It will be renamed to an
        // anonymous ID when it is added to the tar.
       /* int countID = 0;
        Iterator   it   =   DeidData.IdFilename.entrySet().iterator(); 
                     
                     while(it.hasNext()){
                         Map.Entry   entry   =   (Map.Entry)   it.next(); 
                         if (entry.getValue() == DeidData.IdFilename.get(FileUtils.getName((File)o))) countID++;                         
                     }
                     if (countID == 1)*/
       /* String x = DeidData.IdTable.get(DeidData.IdFilename.get(FileUtils.getName((File)o)));
        String y = FileUtils.getName((File)o);
        if(!multiImages(DeidData.IdFilename,DeidData.IdFilename.get(FileUtils.getName((File)o)))){
        setText(DeidData.IdTable.get(DeidData.IdFilename.get(FileUtils.getName((File)o))) + ".nii");
        }*/
        String y = FileUtils.getName((File)o);
        setText(DeidData.multinameSolFile.get(y).toString());
                 /*    else if (countID > 1)
                     {
                         DeidData.multimatchingFlag = countID;
                         if (DeidData.curFlag == 0) {
                            DeidData.curFlag = 1;
                           setText(DeidData.IdTable.get(DeidData.IdFilename.get(FileUtils.getName((File)o))) + "_"+Integer.toString(DeidData.curFlag) +".nii"); 
                           DeidData.curFlag++;
                         }
                         else if (DeidData.curFlag > 0 && DeidData.curFlag < DeidData.multimatchingFlag)
                         {
                           setText(DeidData.IdTable.get(DeidData.IdFilename.get(FileUtils.getName((File)o))) + "_"+Integer.toString(DeidData.curFlag) +".nii");  
                           DeidData.curFlag++;
                         }
                         else if (DeidData.curFlag == DeidData.multimatchingFlag)
                         {
                             setText(DeidData.IdTable.get(DeidData.IdFilename.get(FileUtils.getName((File)o))) + "_"+Integer.toString(DeidData.curFlag) +".nii");  
                           DeidData.curFlag = 0;
                           DeidData.multimatchingFlag = 0;
                         }
                     }*/
                         
        if(isSelected){
            if(cellHasFocus){
                setBackground(new Color(51, 102, 203));
                setForeground(Color.white);
            } else {
                setBackground(new Color(202, 202, 202));
                setForeground(Color.black);
            }
        } else {
            setBackground(jtable.getBackground());
            setForeground(Color.black);
        }
        setOpaque(true);
        return this;
    }
  /*  private boolean multiImages(Hashtable<String, String> ht, String val ){
    boolean ismulti = false;
    int count = 0;
    for (Iterator it = ht.keySet().iterator(); it.hasNext(); ) {
    String key = (String) it.next();
    String value = ht.get(key);
    if (value.equals(val)) count++;   
    
    }
    if (count > 1) ismulti = true;
    return ismulti;
    }*/
}
