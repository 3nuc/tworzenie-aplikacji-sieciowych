package pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.filters.percentageFilters;

import java.util.ArrayList;
import java.util.List;

import static pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.UsefulInfoFromDatasetReturner.removeQuotes;

public class PercentageDesc extends PercentageFilter{


    @Override
    public List<List<String>> filter(List<List<String>> dataset, int columnId, int percentage) {

        List<ValueWrapperToSort> wrapValues = super.createWrapperList(dataset.get(columnId));
        int amountOfElements = super.findPercentage(wrapValues.size()-1, percentage);

        int [] indexesOfElements = new int[amountOfElements];

        for(int i=0;i<amountOfElements;i++) {
            indexesOfElements[i] = wrapValues.get(wrapValues.size()-1-i).getPosition();
        }

        List<List<String>> filteredDataset = new ArrayList<>();
        for(int i=0; i<dataset.size(); i++){

            filteredDataset.add(new ArrayList<>());
            //set title
            filteredDataset.get(i).add(removeQuotes(dataset.get(i).get(0)));

            for(int j=0; j<amountOfElements; j++){
                String itemToInsert = removeQuotes(dataset.get(i).get(indexesOfElements[j]));
                filteredDataset.get(i).add(itemToInsert);
            }

        }

        System.out.println(filteredDataset.toString());



        return filteredDataset;
    }
}
