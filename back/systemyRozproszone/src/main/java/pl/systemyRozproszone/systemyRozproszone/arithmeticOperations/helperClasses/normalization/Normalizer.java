package pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.normalization;

import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.discretization.DiscretizerResponseEnum;

import java.util.ArrayList;
import java.util.List;

public class Normalizer {


    List<List<String>> createdList = new ArrayList<>();
    double deviation =0;
    double mean = 0 ;
    String title;
    double size;

    public DiscretizerResponseEnum normalize(List<List<String>> testFile, String fileName,
                                              String columnToDiscretize) {

        if(doesColumnLikeThisExist(testFile, columnToDiscretize)){
            return DiscretizerResponseEnum.COLUMN_EXIST;
        }

        //if cannot be normalized;
        if(!canBeNormalized(testFile, columnToDiscretize)){
            return DiscretizerResponseEnum.CANNOT_BE_DISCRETIZED;
        }

        title = columnToDiscretize+"_normalized_";
        size=testFile.get(0).size()-1;
        String columnName = columnToDiscretize;
        int columnId=-1;
        columnId = getSearchedColumnIndex(columnName, testFile);

        //create double column from string values stored inside
        List<Double> values = new ArrayList<>(parseColumnToDoubleValues(testFile, columnId));


        //calculateSumOfSquares
        mean = calculateMean(values);
        deviation = calculateDeviation(values, mean);

        //calculate normalized values
//        ------------------------------- here skonczylem ---------------------------------

        List<String> finalList = new ArrayList<>(createFinalList(values, columnId));


        createdList = testFile;
        createdList.add(finalList);
        return DiscretizerResponseEnum.SUCCESS;
    }

    private boolean canBeNormalized(List<List<String>> testFile, String columnToDiscretize) {
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

    private boolean doesColumnLikeThisExist(List<List<String>> testFile, String columnToDiscretize) {

        for(int i=0; i< testFile.size(); i++){

            if(testFile.get(i).get(0).equals(columnToDiscretize+"_normalized_")){
                return true;
            }
        }
        return false;
    }

    private List<String> createFinalList(List<Double> values, int columnId) {

        List<String> finalList = new ArrayList<>();

        finalList.add(title);
        for(Double value : values){
            double result = (value-mean)/deviation;
            finalList.add(String.valueOf(value));
        }

        return finalList;
    }

    private List<Double> parseColumnToDoubleValues(List<List<String>> testFile, int columnId) {

        List<Double> doubles = new ArrayList<>();

        //dont use first val cuz its the title
        for(int i=1; i<testFile.get(0).size(); i++){

            Double currentDouble = Double.parseDouble(testFile.get(columnId).get(i));
            doubles.add(currentDouble);
        }

        return doubles;
    }

    private int getSearchedColumnIndex(String columnName, List<List<String>> testFile) {
        //get columnId

        int iterator=0;
        int columnId =-1;

        for(int i=0; i<testFile.size();i++){

            String currentColumnName = testFile.get(i).get(0);
            if(currentColumnName.equals(columnName)){
                columnId=iterator;
            }
            iterator++;
        }
        return columnId;
    }

    private double calculateMean(List<Double> values) {
        double sum=0;
        double size=0;
        for(Double d : values) {
            sum+=sum;
            size++;
        }
        return sum/size;
    }

    private double calculateDeviation(List<Double> values, double mean) {
        double sumOfSquares = 0;
        double size = values.size();
        for(Double val: values) {
            double square = Math.pow((val-mean), 2);
            sumOfSquares+=square;
        }
        double result = Math.sqrt(sumOfSquares/size);
        return result;
    }

    public List<List<String>> returnList(){
        return createdList;
    }
}
