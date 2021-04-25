package pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.distanceMetrics;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.DistanceHolder;

import java.util.ArrayList;
import java.util.List;

public class MahalanobisDistance extends Distance{
    public MahalanobisDistance(List<List<String>> dataset, List<String> pointCoordinates, String decissionColumnName) {
        super(dataset, pointCoordinates, decissionColumnName);
    }

    @Override
    public void calculateDistance() {
        super.calculateDistance();
        //iterate through every row
        double distanceFromMean = calculateMahalanobisDistance(pointCoordinates);
        for(int i=1;i<data.get(0).size(); i++) {
            List<String> row = getRow(data,i);
            List<Double> pointCoords = new ArrayList<>();
            for(int j=0; j<row.size(); j++){
                if(j!=columnId){
                    pointCoords.add(Double.valueOf(row.get(j)));
                }
            }
            DistanceHolder d = new DistanceHolder();
            d.setId(i);
            d.setDistance(Math.sqrt(calculateMahalanobisDistance(pointCoords)));
            d.setDistance(Math.abs(distanceFromMean-d.getDistance()));
            distances.add(d);
        }
    }

    private double calculateMahalanobisDistance(List<Double> pointCoordinates) {

        List<Double> meanColumnValues = new ArrayList<>();
        List<Double> givenMinusMeanValues = new ArrayList<>();
        double[][] valuesMatrix = new double[data.get(0).size()-1][data.size()-1];

        //providedValues = pointCoordinates
        //rows - ilosc rzedow
        //rowsgetdata - ilosc kolumn


        //calculate mean for each column
        for(int i=0; i< pointCoordinates.size(); i++) {
            double sumInColumn =0;
            for(int j=0; j<data.get(0).size()-1; j++) {
                sumInColumn+=Double.valueOf(data.get(i).get(j+1));
                valuesMatrix[j][i]=Double.valueOf(data.get(i).get(j+1));
            }
            meanColumnValues.add(sumInColumn/(data.get(0).size()-1));
        }

        // calculate (x-m) given array minus mean array
        for(int x = 0 ; x< meanColumnValues.size() ; x++) {
            givenMinusMeanValues.add(pointCoordinates.get(x) - meanColumnValues.get(x));
        }

        //pionowa macierz
        double[][] verticalMatrixGiven = new double [1][givenMinusMeanValues.size()];
        for(int i=0;i<givenMinusMeanValues.size(); i++) {
            verticalMatrixGiven[0][i] = givenMinusMeanValues.get(i);
        }
        RealMatrix verticalMatrix = MatrixUtils.createRealMatrix(verticalMatrixGiven);

        //pozioma macierz
        double[][] horizontalMatrixGiven = new double [givenMinusMeanValues.size()][1];
        for(int i=0;i<givenMinusMeanValues.size(); i++) {
            horizontalMatrixGiven[i][0] = givenMinusMeanValues.get(i);
        }

        RealMatrix horizontalMatrix = MatrixUtils.createRealMatrix(horizontalMatrixGiven);

        //obliczam maciez kowariancji
        RealMatrix mx = MatrixUtils.createRealMatrix(valuesMatrix);
        RealMatrix cov = new Covariance(mx).getCovarianceMatrix();
        double [][] covarianceMatrix = cov.getData();
        RealMatrix inversed = MatrixUtils.inverse(cov);
        double [][] inversedCovarianceMatrix = inversed.getData();

        RealMatrix p1 = verticalMatrix.multiply(inversed);
        RealMatrix p2 = p1.multiply(horizontalMatrix);

        double [][] distance = p2.getData();
        double dist=0;
        for(int i=0;i<distance.length; i++) {
            for(int j=0; j<distance[i].length; j++) {
                dist+=distance[i][j];
            }
        }

        return dist;
    }
}
