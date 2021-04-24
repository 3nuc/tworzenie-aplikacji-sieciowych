package pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.distanceMetrics;

import pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.DistanceHolder;

import java.util.ArrayList;
import java.util.List;

public class EuklideanDistance extends Distance{

    public EuklideanDistance(List<List<String>> dataset, List<String> pointCoordinates, String decissionColumnName) {
        super(dataset, pointCoordinates, decissionColumnName);
    }

    @Override
    public void calculateDistance() {
        super.calculateDistance();

        //iterate through every row
        for(int i=1; i<data.get(0).size();i++){
            distances.add(calculateEuklideanDistance(pointCoordinates,data.get(i), i));
        }
    }

    private DistanceHolder calculateEuklideanDistance(List<Double> pointCoordinates, List<String> row, int index) {

        List<Double> values = new ArrayList<>();
        for(int i =0 ; i< row.size(); i++){
            if(i!=index){
                values.add(Double.parseDouble(row.get(i)));
            }
        }

        double distance =0;

        for(int x=0; x<row.size(); x++) {
            distance+= Math.pow((values.get(x)-pointCoordinates.get(x)), 2);
        }
        distance=Math.sqrt(distance);


        return new DistanceHolder(distance, index);

    }
}
