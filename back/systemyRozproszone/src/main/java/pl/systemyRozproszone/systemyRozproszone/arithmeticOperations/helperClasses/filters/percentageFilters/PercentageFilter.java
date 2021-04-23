package pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.filters.percentageFilters;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class PercentageFilter {

    public List<List<String>> filter(List<List<String>> dataset, int columnId, int percentage) {
        return null;
    }

    public List<ValueWrapperToSort> createWrapperList(List<String> column){
        List<ValueWrapperToSort> list = new ArrayList<>();

        for(int i=1 ;i<column.size(); i++){
            list.add(new ValueWrapperToSort(i, Double.valueOf(column.get(i))));
        }

        list.sort(new ValueWrapperToSortComparator());
        return list;
    }

    public int findPercentage(int size, int percentage) {
        int result = (int)(size*(percentage*0.01));
        return result;
    }

    private class ValueWrapperToSortComparator implements Comparator<ValueWrapperToSort> {

        @Override
        public int compare(ValueWrapperToSort arg0, ValueWrapperToSort arg1) {
            if(arg0.getValue() > arg1.getValue()) {
                return 1;
            }

            else if(arg0.getValue() < arg1.getValue()) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
}
