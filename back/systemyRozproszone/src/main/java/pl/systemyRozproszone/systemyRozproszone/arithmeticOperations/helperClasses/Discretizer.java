package pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses;

import java.util.ArrayList;
import java.util.List;

public class Discretizer {

    List<List<String>> createdList = new ArrayList<>();

    public DiscretizerResponseEnum discretize(List<List<String>> testFile, String fileName,
                                                     String columnToDiscretize, Integer amountOfSections){


        String title = columnToDiscretize+"_discretized_"+amountOfSections+"_sectors";
        double min,max =0;
        double sectorSize;
        double [] minAndMax;

        //If column already exist
        if(doesColumnLikeThisExist(testFile, columnToDiscretize, amountOfSections)){
            return DiscretizerResponseEnum.COLUMN_EXIST;
        }
        
        //if cannot be discretized;
        if(!canBeDiscretized(testFile, columnToDiscretize, amountOfSections)){
            return DiscretizerResponseEnum.CANNOT_BE_DISCRETIZED;
        }
        
        List<Double> columnToBeDiscretized = fillTheList(testFile, columnToDiscretize);
        
        minAndMax = getMinMax(columnToBeDiscretized);
        min = minAndMax[0];
        max = minAndMax[1];
        sectorSize=(max-min)/amountOfSections;


        double tinyFraction = 0.000001d;
        double currentSectorMin=min;
        double currentSectorMax=min+sectorSize;

        List<String> sectorsColumn = new ArrayList<>();
        sectorsColumn.add(title);

        for(int i=0;i<amountOfSections; i++) {


            for(int j=0; j<columnToBeDiscretized.size();j++) {
                if(columnToBeDiscretized.get(j)>=currentSectorMin && columnToBeDiscretized.get(j)<=currentSectorMax) {
                    sectorsColumn.add(String.valueOf(i));
                }

            }
            currentSectorMin=currentSectorMax+tinyFraction;
            currentSectorMax=currentSectorMin+sectorSize;
        }

        createdList = testFile;
        createdList.add(sectorsColumn);
        return DiscretizerResponseEnum.SUCCESS;
    }

    private double[] getMinMax(List<Double> columnToBeDiscretized) {
        double [] minAndMax = new double [2];
        double max = Integer.MIN_VALUE;
        double min = Integer.MAX_VALUE;
        for(int i=0 ; i< columnToBeDiscretized.size(); i++) {
            if(columnToBeDiscretized.get(i)> max)
                max = columnToBeDiscretized.get(i);
            if(columnToBeDiscretized.get(i)<min)
                min=columnToBeDiscretized.get(i);
        }
        minAndMax[0]=min;
        minAndMax[1]=max;

        return minAndMax;
    }

    public List<List<String>> returnList(){
        return createdList;
    }

    private List<Double> fillTheList(List<List<String>> testFile, String columnToDiscretize) {
        
        List<Double> list = new ArrayList<>();
        int correctColumnIndex = -1;
        
        //find the index
        for(int i=0; i<testFile.size(); i++){
            if(testFile.get(i).get(0).equals(columnToDiscretize)){
                correctColumnIndex = i;
                break;
            }
        }
        
        //go through that column (without title) and parse to dobule
        for(int i=1 ; i<testFile.get(correctColumnIndex).size(); i++){
            list.add(Double.parseDouble(testFile.get(correctColumnIndex).get(i)));
        }
        
        return list;
    }

    private boolean canBeDiscretized(List<List<String>> testFile, String columnToDiscretize, Integer amountOfSections) {
        
        for(int i=0; i< testFile.size(); i++){

            //try to cast it to double, if fails it means that it cannot be like that
            String currentColumnName = testFile.get(i).get(0);
            if(currentColumnName.equals(columnToDiscretize)){
                
                try{
                    Double test = Double.parseDouble(testFile.get(i).get(1));
                    return true;
                }
                catch (NumberFormatException nne){
                    return false;
                }
            }
            
        }
        
        return true;
    }

    private boolean doesColumnLikeThisExist(List<List<String>> testFile, String columnToDiscretize, Integer amountOfSections) {

        for(int i=0; i< testFile.size(); i++){

            if(testFile.get(i).get(0).equals(columnToDiscretize+"_discretized_"+amountOfSections+"_sectors")){
                return true;
            }
        }
        return false;
    }
}
