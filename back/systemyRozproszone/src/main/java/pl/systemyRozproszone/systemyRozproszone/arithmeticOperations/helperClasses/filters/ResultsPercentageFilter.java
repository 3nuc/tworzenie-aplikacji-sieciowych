package pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.filters;

import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.UsefulInfoFromDatasetReturner;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.discretization.DiscretizerResponseEnum;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.filters.percentageFilters.PercentageAsc;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.filters.percentageFilters.PercentageDesc;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.filters.percentageFilters.PercentageFilter;

import java.util.ArrayList;
import java.util.List;

public class ResultsPercentageFilter{

    List<List<String>> listToReturn;

    public DiscretizerResponseEnum filterData(List<List<String>> data, String columnName,
                                             String percentage, boolean isDesc) {

        List<List<String>> filteredList;
        boolean doesColumnExist = UsefulInfoFromDatasetReturner.ifColumnWithGivenNameExists(data, columnName);
        int columnIndex = UsefulInfoFromDatasetReturner.getColumnId(data, columnName);
        boolean doesColumnContainOnlyNumericValues = UsefulInfoFromDatasetReturner.doesColumnContainOnlyNumericValues(data, columnName );

        //if cant proceed
        if(!doesColumnExist | columnIndex==-1 | !doesColumnContainOnlyNumericValues){
            return DiscretizerResponseEnum.COLUMN_DOESNT_EXIST;
        }

        PercentageFilter filter = ( (isDesc) ? new PercentageDesc() : new PercentageAsc() );
        filteredList = filter.filter(data,columnIndex,Integer.parseInt(percentage));

        listToReturn = filteredList;
        System.out.println(filteredList);


        return DiscretizerResponseEnum.SUCCESS;
    }

    public List<List<String>> returnList(){
        return listToReturn;
    }

}
