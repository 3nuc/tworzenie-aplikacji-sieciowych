package pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.distanceMetrics;

import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.UsefulInfoFromDatasetReturner;
import pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.DistanceHolder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class Distance {

    public List<Double> pointCoordinates;
    public List<DistanceHolder> distances;
    public List<List<String>> data;
    public List<List<String>> bareboneData;
    public int columnId;


    public Distance(List<List<String>> dataset, List<String> pointCoordinates, String decissionColumnName){

        this.data = dataset;
        this.pointCoordinates = setPointCoordinates(pointCoordinates);
        this.columnId = UsefulInfoFromDatasetReturner.getColumnId(dataset, decissionColumnName);
        this.data = dataset;
        this.distances = new ArrayList<>();

    }
    public List<Double> setPointCoordinates(List<String> coordinates){

        List<Double> coords = new ArrayList<>();
        for(String coordinate : coordinates){
            try {
                coords.add(Double.valueOf(coordinate));
            }
            catch (NumberFormatException e){
                System.out.println("Distance set distances parsing to double failed");
            }

        }

        return coords;

    }


    public void calculateDistance(){};

    public List<List<String>> returnNearestNeighbours(int amountOfNeighbours, List<List<String>> oldData){
        List<List<String>> nearestNeighbours = new ArrayList<>();
        distances.sort(new DistanceComparator());
        for(int i=0 ; i<amountOfNeighbours; i++){
//            nearestNeighbours.add(data.get(distances.get(i).getId()));
            nearestNeighbours.add(getRow(oldData, distances.get(i).getId()));
        }
        return nearestNeighbours;
    }

    private List<String> getRow(List<List<String>> dataset, int rowInd){
        List<String> row = new ArrayList<>();
        for(int i=0; i<dataset.size(); i++){
            row.add(dataset.get(i).get(rowInd));
        }
        return row;
    }


    public class DistanceComparator implements Comparator<DistanceHolder>{
        @Override
        public int compare(DistanceHolder o1, DistanceHolder o2) {

            if(o1.getDistance() > o2.getDistance()) {
                return 1;
            }

            else if(o1.getDistance() < o2.getDistance()) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
}
