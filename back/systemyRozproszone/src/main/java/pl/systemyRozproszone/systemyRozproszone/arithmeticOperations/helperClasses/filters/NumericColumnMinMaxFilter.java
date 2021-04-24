package pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.filters;

import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.UsefulInfoFromDatasetReturner;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.discretization.DiscretizerResponseEnum;

import java.util.ArrayList;
import java.util.List;

public class NumericColumnMinMaxFilter {

    List<List<String>> createdList;


    public DiscretizerResponseEnum filterByMinAndMaxVal(List<List<String>> data, String columnName, String min, String max){

        double minimum = Double.valueOf(min);
        double maximum = Double.valueOf(max);
        List<List<String>> filteredList;
        boolean doesColumnExist = UsefulInfoFromDatasetReturner.ifColumnWithGivenNameExists(data, columnName);
        int columnIndex = UsefulInfoFromDatasetReturner.getColumnId(data, columnName);
        boolean doesColumnContainOnlyNumericValues = UsefulInfoFromDatasetReturner.doesColumnContainOnlyNumericValues(data, columnName );

        if(!doesColumnExist | columnIndex==-1 | !doesColumnContainOnlyNumericValues){
            return DiscretizerResponseEnum.CANNOT_SELECT_MIN_AND_MAX;
        }

        int amountOfElements = data.get(0).size();

        filteredList = data;
        for(int i = amountOfElements-1; i>=1; i--){

            double currentElementInColumnValue = Double.valueOf(data.get(columnIndex).get(i));
            if(currentElementInColumnValue < minimum ||
                    currentElementInColumnValue > maximum){

                for(int j=0;j<filteredList.size();j++){
                    filteredList.remove(filteredList.get(j).get(i));
                }
            }

        }

        createdList = filteredList;
        return DiscretizerResponseEnum.SUCCESS;
    }

    public List<List<String>> returnList(){
        return createdList;
    }
}
