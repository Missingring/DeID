/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

import java.io.File;
import java.io.IOException;

import com.smartxls.WorkBook;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
/**
 *
 * @author angelo
 */
public class XlsxFile {
   private String inputFile;

  public void setInputFile(String inputFile) {
    this.inputFile = inputFile;
  }
  
  public String[] readHeadings() throws IOException{
          
      
      boolean flag = true;
      String[] headings = null;
      try {
      WorkBook inputworkBook = new WorkBook();
      inputworkBook.readXLSX(inputFile); 
      inputworkBook.setSheet(0);
      int sheetNum = inputworkBook.getSheet();
      String cellcont = inputworkBook.getText(sheetNum, 0, 0);
      int colcount = 0;
      while (!cellcont.equals("")){
      colcount ++;
      cellcont = inputworkBook.getText(sheetNum, 0, colcount);
      }
      String[] fields;
      fields = new String[colcount];
      for (int i = 0; i< colcount; i++){
      
      cellcont = inputworkBook.getText(sheetNum, 0, i);
       
          fields[i]=cellcont;
      }      
      
      headings = fields;
      } catch (Exception e) {
      e.printStackTrace();
      flag = false;
    }
      if (flag) 
   return headings;
      else return null;
  }
  
  public ArrayList<Object[]> read() throws IOException  {
    
    
    ArrayList<Object[]> rowList = new ArrayList<Object[]>();
    try {
      WorkBook inputworkBook = new WorkBook();
      inputworkBook.readXLSX(inputFile); 
      inputworkBook.setSheet(0);
      int sheetNum = inputworkBook.getSheet();      
      String[] fields;
      fields = readHeadings();
      if (fields != null){
      
        for (int i = 1; i < inputworkBook.getLastRow(); i++){
            Object[] rowData;
            rowData = new Object[fields.length];
          for(int k = 0; k < fields.length; k++){
          
          String cellcont = inputworkBook.getText(sheetNum, i, k);
          if (StringFilter(cellcont).equals("")) {
          cellcont = "misV";
          DeidData.errorlog.addElement("Missing value in column " + fields[k] +" in line " + i);  
                      
                    
          }          
          rowData[k] =  cellcont; 
          
          }
          
          rowList.add(rowData);
          
      
      
        }
      }
      else return null;
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return rowList; 
  }
  private   static   String StringFilter(String   str)   throws   PatternSyntaxException   {     
                 
          String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~@#%……&*——+|{}‘”“’]";  
          Pattern   p   =   Pattern.compile(regEx);     
          Matcher   m   =   p.matcher(str);     
          return   m.replaceAll("").trim();     
          } 
}

