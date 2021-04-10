package pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.digitization;

import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.discretization.DiscretizerResponseEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Digitizer {

    List<List<String>> createdList = new ArrayList<>();

    public DiscretizerResponseEnum digitize(List<List<String>> testFile, String columnName){

        String title = columnName+"_digitized";

        if(doesColumnLikeThisExist(testFile, title)){
            return DiscretizerResponseEnum.COLUMN_EXIST;
        }

        List<String> listToDigitize = new ArrayList<>(fillTheList(testFile, columnName));
        Set<String> set = new HashSet(listToDigitize);


        List<String> digitizedColumn = new ArrayList<>();
        digitizedColumn.add(title);

        //iterate through the set to change strings into 0 1 2 3 etc in desc order
        for(int i=0; i<listToDigitize.size();i++) {

            int indexInSet=0;
            for(String name: set) {
                if(listToDigitize.get(i).equals(name)) {
                    digitizedColumn.add(String.valueOf(indexInSet));
                }
                indexInSet++;
            }
        }

        createdList = testFile;
        createdList.add(digitizedColumn);



        return DiscretizerResponseEnum.SUCCESS;
    }

    private boolean doesColumnLikeThisExist(List<List<String>> testFile, String title) {

        for(int i=0; i< testFile.size(); i++){

            if(testFile.get(i).get(0).equals(title)){
                return true;
            }
        }
        return false;
    }

    public List<List<String>> returnList(){
        return createdList;
    }

    private List<String> fillTheList(List<List<String>> testFile, String columnName) {

        List<String> list = new ArrayList<>();
        int correctColumnIndex = -1;

        //find the index
        for(int i=0; i<testFile.size(); i++){
            if(testFile.get(i).get(0).equals(columnName)){
                correctColumnIndex = i;
                break;
            }
        }

        //go through that column (without title) and parse to dobule
        for(int i=1 ; i<testFile.get(correctColumnIndex).size(); i++){
            list.add(testFile.get(correctColumnIndex).get(i));
        }

        return list;
    }
}
