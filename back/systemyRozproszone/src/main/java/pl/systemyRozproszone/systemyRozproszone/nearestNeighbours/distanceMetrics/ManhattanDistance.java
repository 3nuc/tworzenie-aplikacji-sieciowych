package pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.distanceMetrics;

import pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.DistanceHolder;

import java.util.ArrayList;
import java.util.List;

public class ManhattanDistance extends Distance {

    public ManhattanDistance(List<List<String>> dataset, List<String> pointCoordinates, String decissionColumnName) {
        super(dataset, pointCoordinates, decissionColumnName);
    }

    @Override
    public void calculateDistance() {
        super.calculateDistance();
        //iterate through every row
        for(int i=1; i<data.get(0).size();i++){
            distances.add(calculateManhattanDistance(pointCoordinates,getRow(data,i),  i, columnId));
        }
    }

    private DistanceHolder calculateManhattanDistance(List<Double> pointCoordinates, List<String> row, int index, int columnId) {
        List<Double> values = new ArrayList<>();
        for(int i =0 ; i< row.size(); i++) {
            if (i != columnId) {
                values.add(Double.parseDouble(row.get(i)));
            }
        }

        double distance = 0;
        for(int x=0; x<pointCoordinates.size(); x++) {
            distance+= Math.abs(values.get(x)-pointCoordinates.get(x));
        }
        return new DistanceHolder(distance, index);
    }
}
