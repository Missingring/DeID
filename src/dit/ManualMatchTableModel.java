/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;
import java.io.File;
import java.util.*;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author angelo
 */
public class ManualMatchTableModel extends AbstractTableModel{
    private String[] columnNames = new String[]{"Image File", "Demographic Data", "Status"};
    private Object[][] data;
    private int mismatchCount, matchCount;

    public ManualMatchTableModel() {
        mismatchCount = 0;
        matchCount = 0;
        data = new Object[0][0];
    }

    public ManualMatchTableModel(List<File> images, Object[] demoIDs, final String key, final boolean isbyPath,final boolean ismulti) {
        ArrayList<Object[]> dataList = new ArrayList<Object[]>();

        // The demoIDs will ALWAYS be sorted, images probably will not be.
        // The standard string compareTo should be very reliable.
        Collections.sort(images, new Comparator<File>(){
            @Override
            public int compare(File f1, File f2) {
                if (key != null)
                {    
                    String s1 = filenameconvertor(key, FileUtils.getName(f1));
                    String s2 = filenameconvertor(key, FileUtils.getName(f2));
                    return s1.compareToIgnoreCase(s2);
               }
                else
                    return FileUtils.getName(f1).compareToIgnoreCase(FileUtils.getName(f2));
            }
        });
        
        DeidData.IdFilename = new Hashtable<String, String>();
        Iterator<File> imageIt = images.iterator();
        File curFile = null;
        try {
            curFile = imageIt.next();
        } catch (NoSuchElementException e) {
            curFile = null;
        }
        
       // int demoIDNdx = 0;
        if (!isbyPath)
        {
            Hashtable<String, String> id_exist = new Hashtable<String, String>();
            while (curFile != null){
                int demoIDNdx = 0;
                while(demoIDNdx < demoIDs.length){
                
                    String filename = FileUtils.getName(curFile);
                    String convertedID;
                    if (key != null)
                    { 
                        convertedID = keyconvertor(key,(String)demoIDs[demoIDNdx]);
                    }
                    else 
                    {
                        convertedID = (String)demoIDs[demoIDNdx];
                    }
                    if (filename.indexOf(convertedID) >= 0) {
                        int properidx = getProperindex(filename.toString(),demoIDs, key);
                        if(ismulti){
                        dataList.add(new Object[]{fileintoTable(curFile), demoIDs[properidx], new Boolean(true)});
                        DeidData.IdFilename.put(fileintoTable(curFile), (String)demoIDs[properidx] );
                        matchCount++;
                        break;
                        }
                        else {
                            if(!id_exist.containsKey((String)demoIDs[properidx]))
                            {
                                dataList.add(new Object[]{FileUtils.getName(curFile).toString(), demoIDs[properidx], new Boolean(true)});
                                DeidData.IdFilename.put(fileintoTable(curFile), (String)demoIDs[properidx] );
                                id_exist.put((String)demoIDs[properidx], "True");
                                matchCount++;
                                break;
                            }                            
                        
                        }
                    }
                    demoIDNdx++;
                
                }
                
                if (demoIDNdx == demoIDs.length)
                {
                    //dataList.add(new Object[]{fileintoTable(curFile), null, new Boolean(false)});
                  dataList.add(new Object[]{FileUtils.getName(curFile).toString(), null, new Boolean(false)}); 
                    mismatchCount++;
                }
                
                
                
                
                try {
                    curFile = imageIt.next();
                } catch (NoSuchElementException e) {
                    curFile = null;
                }
            
            
            }
            /*
        while (curFile != null && demoIDNdx < demoIDs.length) {
            
           
            //if(Arrays.binarySearch(demoIDs, "1952") >= 0){ This is how to use this
            //int searchResult = Arrays.binarySearch(demoIDs, FileUtils.getName(curFile));
            //if(searchResult >= 0){
            String filename = FileUtils.getName(curFile);
            String convertedID;
            if (key != null)
            convertedID = keyconvertor(key,(String)demoIDs[demoIDNdx]);
            else convertedID = (String)demoIDs[demoIDNdx];
            System.out.println(filename.indexOf(convertedID));
            System.out.println(convertedID);
            //System.out.println(filename);
            //System.out.println(filenameconvertor(key,filename));
            
            if (filename.indexOf(convertedID) >= 0) {
                dataList.add(new Object[]{fileintoTable(curFile), demoIDs[demoIDNdx], new Boolean(true)});
                //System.out.println("Found ID "+ demoIDs[searchResult] +" "+dataList.size());                
                DeidData.IdFilename.put(fileintoTable(curFile), (String)demoIDs[demoIDNdx] );
                try {
                    curFile = imageIt.next();
                } catch (NoSuchElementException e) {
                    curFile = null;
                }
                if (!ismulti){
                demoIDNdx++;
                }
                matchCount++;
            } else if (filenameconvertor(key,filename).indexOf((String) demoIDs[demoIDNdx]) < 0) {
                dataList.add(new Object[]{fileintoTable(curFile), null, new Boolean(false)});
                //System.out.println("Did not find " + FileUtils.getName(curFile));
                mismatchCount++;
                try {
                    curFile = imageIt.next();
                } catch (NoSuchElementException e) {
                    curFile = null;
                }
                
              
            } else {
                dataList.add(new Object[]{null, demoIDs[demoIDNdx], new Boolean(false)});
                //System.out.println("Did not find " + FileUtils.getName(curFile));
                demoIDNdx++;
            }
        }
            
        
       while (curFile != null) {
            dataList.add(new Object[]{fileintoTable(curFile), null, new Boolean(false)});
            //System.out.println("Did not find " + FileUtils.getName(curFile));
            mismatchCount++;
            try {
                curFile = imageIt.next();
            } catch (NoSuchElementException e) {
                curFile = null;
            }
        }
       while (demoIDNdx < demoIDs.length) {
            dataList.add(new Object[]{null, demoIDs[demoIDNdx], new Boolean(false)});
            //System.out.println("Did not find " + FileUtils.getName(curFile));
            demoIDNdx++;
        
       }*/
        }
        else
        {
          //if (!ismulti){
       /*   String convertedID;
          while (curFile != null){
          demoIDNdx = 0;
          String filepath = curFile.getAbsolutePath();
          int tmpmatchCount = 0;
          int mismatchflag = 0;
          int matchRecorder = 0;
          while(demoIDNdx < demoIDs.length)
          {
              if (key != null)
                convertedID = keyconvertor(key,(String)demoIDs[demoIDNdx]);
                else convertedID = (String)demoIDs[demoIDNdx];
              if(getCountIndex(filepath, convertedID) == 1)
              {
                  //dataList.add(new Object[]{curFile.getName(), demoIDs[demoIDNdx], new Boolean(true)});
                  
                  tmpmatchCount++;
                  matchRecorder = demoIDNdx;
                  
              }
              else if(getCountIndex(filepath, convertedID) > 1)
              {
                  //dataList.add(new Object[]{curFile.getName(), demoIDs[demoIDNdx], new Boolean(false)});
                  mismatchflag = 1;
                  break;
                  
              }
              
              
              demoIDNdx++;          
          
          }
          
          if (mismatchflag == 1 || tmpmatchCount > 1 || tmpmatchCount == 0 ) {
              
              mismatchCount++;
              dataList.add(new Object[]{fileintoTable(curFile), null, new Boolean(false)});
          
          } else if (tmpmatchCount == 1 && demoIDNdx == demoIDs.length)
          {
              dataList.add(new Object[]{fileintoTable(curFile), demoIDs[matchRecorder], new Boolean(true)});
             
              DeidData.IdFilename.put(fileintoTable(curFile), (String)demoIDs[matchRecorder] );
              matchCount++;
          }
         
        
            try {
                    curFile = imageIt.next();
                } catch (NoSuchElementException e) {
                    curFile = null;
                }
        }*/
        //}
          //else {
            
          /*
         String convertedID;
          while (curFile != null){
          demoIDNdx = 0;
          String filepath = curFile.getAbsolutePath();
         // int tmpmatchCount = 0;
         // int mismatchflag = 0;
          //int matchRecorder = 0;
          while(demoIDNdx < demoIDs.length)
          {
              if (key != null)
                convertedID = keyconvertor(key,(String)demoIDs[demoIDNdx]);
                else convertedID = (String)demoIDs[demoIDNdx];
              if(filepath.indexOf(convertedID) >= 0)
              {
                  
                  dataList.add(new Object[]{fileintoTable(curFile), demoIDs[demoIDNdx], new Boolean(true)});
             
                DeidData.IdFilename.put(fileintoTable(curFile), (String)demoIDs[demoIDNdx] );
                 matchCount++;
                 // tmpmatchCount++;
                 // matchRecorder = demoIDNdx;
                  
              }
              else 
              {
                  dataList.add(new Object[]{fileintoTable(curFile), null, new Boolean(false)});
                  mismatchCount++;
                //dataList.add(new Object[]{fileintoTable(curFile), null, new Boolean(false)});
                  
              }
              
              
              demoIDNdx++;          
          
          }
          
          
         
        
            try {
                    curFile = imageIt.next();
                } catch (NoSuchElementException e) {
                    curFile = null;
                }
        }     
           */
            Hashtable<String, String> id_exist = new Hashtable<String, String>();
            while (curFile != null){
                int demoIDNdx = 0;
                while(demoIDNdx < demoIDs.length){
                
                    String filepath = curFile.getAbsolutePath();
                    String filepp = curFile.getParentFile().getParentFile().getParentFile().getAbsolutePath();
                    String filenet = filepath.replaceAll(filepp, "");
                    System.out.println(filepp);
                    String convertedID;
                    if (key != null)
                    { 
                        convertedID = keyconvertor(key,(String)demoIDs[demoIDNdx]);
                    }
                    else 
                    {
                        convertedID = (String)demoIDs[demoIDNdx];
                    }
                    if (filenet.indexOf(convertedID) >= 0 ) {
                        if(ismulti){
                        dataList.add(new Object[]{fileintoTable(curFile), demoIDs[demoIDNdx], new Boolean(true)});
                        DeidData.IdFilename.put(fileintoTable(curFile), (String)demoIDs[demoIDNdx] );
                        matchCount++;
                        break;
                        }
                        else {
                            if(!id_exist.containsKey((String)demoIDs[demoIDNdx]))
                            {
                                dataList.add(new Object[]{fileintoTable(curFile), demoIDs[demoIDNdx], new Boolean(true)});
                                DeidData.IdFilename.put(fileintoTable(curFile), (String)demoIDs[demoIDNdx] );
                                id_exist.put((String)demoIDs[demoIDNdx], "True");
                                matchCount++;
                                break;
                            }                            
                        
                        }
                    }
                    demoIDNdx++;
                
                }
                
                if (demoIDNdx == demoIDs.length)
                {
                    dataList.add(new Object[]{fileintoTable(curFile), null, new Boolean(false)});
                    mismatchCount++;
                }                
                
                
                
                try {
                    curFile = imageIt.next();
                } catch (NoSuchElementException e) {
                    curFile = null;
                }
            
            
            }
            /*
            int demoIDNdx = 0;
          while (curFile != null && demoIDNdx < demoIDs.length) {
            
           
            //if(Arrays.binarySearch(demoIDs, "1952") >= 0){ This is how to use this
            //int searchResult = Arrays.binarySearch(demoIDs, FileUtils.getName(curFile));
            //if(searchResult >= 0){
           //String filename = FileUtils.getName(curFile);
            String filepath = curFile.getAbsolutePath();
            String convertedID;
            if (key != null)
            convertedID = keyconvertor(key,(String)demoIDs[demoIDNdx]);
            else convertedID = (String)demoIDs[demoIDNdx];
            //System.out.println(convertedID);
            //System.out.println(demoIDs[demoIDNdx]);
            if (filepath.indexOf(convertedID) >= 0) {
                dataList.add(new Object[]{fileintoTable(curFile), demoIDs[demoIDNdx], new Boolean(true)});
                //System.out.println("Found ID "+ demoIDs[searchResult] +" "+dataList.size());                
                DeidData.IdFilename.put(fileintoTable(curFile), (String)demoIDs[demoIDNdx] );
                try {
                    curFile = imageIt.next();
                } catch (NoSuchElementException e) {
                    curFile = null;
                }
                if (!ismulti){
                demoIDNdx++;
                }
                matchCount++;
            } else if (filenameconvertor(key,filepath).compareTo((String) demoIDs[demoIDNdx]) < 0) {
                dataList.add(new Object[]{fileintoTable(curFile), null, new Boolean(false)});
                //System.out.println("Did not find " + FileUtils.getName(curFile));
                mismatchCount++;
                try {
                    curFile = imageIt.next();
                } catch (NoSuchElementException e) {
                    curFile = null;
                }
                
              
            } else {
                dataList.add(new Object[]{null, demoIDs[demoIDNdx], new Boolean(false)});
                //System.out.println("Did not find " + FileUtils.getName(curFile));
                demoIDNdx++;
            }
        }
            
        
       while (curFile != null) {
            dataList.add(new Object[]{fileintoTable(curFile), null, new Boolean(false)});
            //System.out.println("Did not find " + FileUtils.getName(curFile));
            mismatchCount++;
            try {
                curFile = imageIt.next();
            } catch (NoSuchElementException e) {
                curFile = null;
            }
        }
       while (demoIDNdx < demoIDs.length) {
            dataList.add(new Object[]{null, demoIDs[demoIDNdx], new Boolean(false)});
            //System.out.println("Did not find " + FileUtils.getName(curFile));
            demoIDNdx++;
        
       }
              
        */  
          //}
        }
        Object[][] dataArray = new Object[dataList.size()][3];
        for (int ndx = 0; ndx < dataList.size(); ndx++) {
            dataArray[ndx] = dataList.get(ndx);
        }
        data = dataArray;
        DeidData.data = data;
    }
    //get a good match
private int getProperindex(String filename,Object[] demoIDs,String key){
    int index = 0;
    int min = filename.length();
    int demoIDNdx = 0;
          
          while(demoIDNdx < demoIDs.length)
          {
              String convertedID;
                    if (key != null)
                    { 
                        convertedID = keyconvertor(key,(String)demoIDs[demoIDNdx]);
                    }
                    else 
                    {
                        convertedID = (String)demoIDs[demoIDNdx];
                    }
              if(filename.indexOf(convertedID)>=0){
                  if(filename.indexOf(convertedID) < min){
                      min = filename.indexOf(convertedID);
                      index = demoIDNdx;
                  }
                  
              
              }
              demoIDNdx ++;
              
          }
    
    return index;


}    
    // get the number of one sub string appearing in a string
    private int getCountIndex(String str, String substr){
      int count=0;
        int index=0;
        while(true){
            index = str.indexOf(substr,index);
            if(index==-1){
            break;
            }
            index = index + substr.length();
            count++;
        }
    return count;
}
private String fileintoTable(File file){
    
    String abParent = file.getParent();
    String out = FileUtils.getName(file).toString();
    if (!DeidData.parentPath.equals("none"))    {       
       
       out = abParent.replaceFirst(DeidData.parentPath, "").replaceFirst(DeidData.anaPath, "").replaceFirst(DeidData.dicomPath, "").replaceAll("/", "") + out;
    }
    System.out.println(out);
    return out;

}
private String keyconvertor(String key, String IDstr){
    
    if (key.indexOf("#") >= 0){
    int idlength = key.lastIndexOf('#') - key.indexOf('#') + 1;
    
    String IDstr1 = IDstr;
    if (idlength > 0){
       while(IDstr1.length() < idlength){
           IDstr1 = "0" + IDstr1;
       }        
    }
    
    //System.out.println(IDstr1);
    IDstr1 = key.replaceFirst("#", IDstr1);
    //System.out.println(IDstr1);
    if (IDstr1.indexOf("#") >= 0)
    IDstr1 = IDstr1.replaceAll("#", "");
    //System.out.println(IDstr);
    return IDstr1;
    }
    else if (key.indexOf("%") >= 0){
    
    return key.replaceFirst("%", IDstr);
    }
    else return IDstr;
}

private String filenameconvertor(String key, String filename){
    
    if (key.indexOf("#") >= 0) {
    int idlength = key.lastIndexOf('#') - key.indexOf('#') + 1;
    String keywithoutid = key.substring(0, key.length() - idlength );
    //System.out.println(keywithoutid);
    int keyposition = filename.indexOf(keywithoutid);
    if (keyposition < 0) return filename;
    else{
    //System.out.println(keyposition);
    String convertedfilename; 
    try {
    convertedfilename = filename.substring(keyposition, keyposition + key.length());
    convertedfilename = convertedfilename.substring(keywithoutid.length());
    convertedfilename = Integer.toString(Integer.parseInt(convertedfilename));
    }
    catch(Exception e)
    {
        convertedfilename = filename;
    }
    
    //System.out.println(convertedfilename);
    //System.out.println(filename);
   
    return convertedfilename;
    }
    }
    else return filename;
}

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public int getMismatchedImageCount() {
        return mismatchCount;
    }
    
    public int getMatchedImageCount(){
        return matchCount;
    }
}
