package pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class UsefulInfoFromDatasetReturner {


    public static boolean ifColumnWithGivenNameExists(List<List<String>> dataset, String columnName){

        for(int i=0; i<dataset.size() ; i++){
            if(removeQuotes(dataset.get(i).get(0)).equals(columnName)){
                return true;
            }
        }
        return false;
    }

    public static String removeQuotes(String columnName) {
        String s;
        if(columnName.charAt(0)=='"' && columnName.charAt(columnName.length()-1)=='"'){
            s = columnName.substring(1, columnName.length()-1);
        }else{
            s = columnName;
        }

        return s;
    }

    public static boolean checkIfFileExists(String name, String path){

        try{
            File newFile = new File(path + name);
            BufferedReader br = new BufferedReader(new FileReader(newFile));

        } catch (FileNotFoundException e) {
            return false;
        }
        return true;


    }

    public static int getColumnId(List<List<String>> dataset, String columnName){
        int columnIndex = -1;
        for(int i=0; i<dataset.size() ; i++){
            if(removeQuotes(dataset.get(i).get(0)).equals(columnName)){
                columnIndex = i;
            }
        }
        return columnIndex;
    }

    public static boolean doesColumnContainOnlyNumericValues(List<List<String>>dataset, String columnName){

        int id = getColumnId(dataset, columnName);

        if(id!=-1){
            for(int i=1; i< dataset.get(id).size(); i++){

                try{
                    double val = Double.parseDouble(dataset.get(id).get(i));
                }
                catch (NumberFormatException e){
                    return false;
                }

            }
        }else{
            return false;
        }

        return true;
    }
}
