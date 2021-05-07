package pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.kmeans;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.systemyRozproszone.systemyRozproszone.CSVHandle.CSVParser;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.UsefulInfoFromDatasetReturner;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.discretization.DiscretizerResponseEnum;
import pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.distanceMetrics.Distance;
import pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.distanceMetrics.ManhattanDistance;
import pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.kmeans.helpers.DataColumn;
import pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.kmeans.helpers.DataRowWithCentroidID;
import pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.kmeans.helpers.Dot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@RestController
public class KmeansController {

    @Value("${fileUploadDirectory}")
    public String PATH;

    int amountOfDecisionClasses = -1;
    List<DataRowWithCentroidID> centroids;
    List<DataRowWithCentroidID> rows;
    List <DataColumn> listOfCols;

    public static final String TAG = "KMeansController";


    @GetMapping("/predictDecision")
    public String returnString(
            @RequestParam("fileName") String fileName,
            @RequestParam("columns") String [] columns,
            @RequestParam("decissionColumn") String decissionColumn,
            @RequestParam("pointCoordinates") String [] coordinates,
            @RequestParam("findType") String findType,
            @RequestParam("returnAllColumns") Boolean returnAll,
            @RequestParam("neighbours") Integer amountOfNeighbours){

        centroids = new ArrayList<>();
        rows = new ArrayList<>();
        boolean ifFileExists = UsefulInfoFromDatasetReturner.checkIfFileExists(fileName, PATH);
        if(!ifFileExists){
            return DiscretizerResponseEnum.FILE_DOESNT_EXIST.toString();
        }
        List<List<String>> data = CSVParser.parseCSVtoListArray(fileName, PATH);
        //create new indexes list eg - get indexes of elements which user wants to use
        List<Integer> columnNamesDigitized = digitizeColumnNames(data, columns, decissionColumn);
        if(columnNamesDigitized.size() == 0){
            return DiscretizerResponseEnum.COLUMN_DOESNT_EXIST.toString();
        }

        List<List<String>> fileFromColumnsWhichUserWants = createUserSelectedDataset(data, columnNamesDigitized);
        amountOfDecisionClasses = findDecisionClasses(fileFromColumnsWhichUserWants);
        listOfCols = createDataColumnList(fileFromColumnsWhichUserWants);

        createRows();
        createCentroids();

        boolean isChanging = true;
        int counter = 0;

        while(isChanging) {
            System.out.println("relocate "+counter);
            assignDistanceFromCentroids(1);
            isChanging =relocateCentroid();
            counter++;
        }

        DataColumn dc = new DataColumn();


    // porownac z pierwotnym datasetem




        return "1";
    }

    private boolean relocateCentroid() {
        boolean hasAnythingChanged=false;
        for(int k=0;k<centroids.size();k++) {

            List<Double> relocatedCentroidData = new ArrayList<>();
            for(int i=0; i<rows.get(0).getData().size(); i++) {

                double sum=0;
                int elementsInsideCentroid=0;
                for(int j=0;j<rows.size();j++) {
                    DataRowWithCentroidID temp = rows.get(j);
                    if(rows.get(j).getCentroidId()==k) {
                        sum+=rows.get(j).getData().get(i);
                        elementsInsideCentroid++;
                    }

                }
                double newPosition = sum/elementsInsideCentroid;
                relocatedCentroidData.add(newPosition);
            }

            for(int x=0;x<centroids.get(k).getData().size();x++) {

                double change = Math.abs(centroids.get(k).getData().get(x)-relocatedCentroidData.get(x));
                if(change>0.01d) {
                    hasAnythingChanged=true;
                }
            }
            centroids.get(k).setData(relocatedCentroidData);
        }

        return hasAnythingChanged;

    }

    private void createRows() {
        for(int i=0 ; i<listOfCols.get(0).getContents().size(); i++){
            List<String> values = new ArrayList<>();
            for(int j=0;j<listOfCols.size()-1; j++){
                values.add(listOfCols.get(j).getContents().get(i));
            }
            rows.add(new DataRowWithCentroidID(values));
        }
    }

    private void assignDistanceFromCentroids(int decission) {
        int changes=0;
        for(int i=0; i<rows.size(); i++) {


            int minDistance = -1;
            DataRowWithCentroidID currentRow = rows.get(i);
            double [] distances = new double[amountOfDecisionClasses];
            //tyle ile jest centroidow
            //obliczam dystans od kazdej z centroidy
            //mahalanobis zwraca dystans od sredniej danego punktu
            //wiec musze go odpalic 2 razy - 1 raz dla centroidy 2 raz dla punktu i wartosci odjac z ABS
            for(int j=0; j<amountOfDecisionClasses; j++) {
                DataRowWithCentroidID currentCentroid = centroids.get(j);
                Distance distance;

                if(decission==1)
                    distances[j] = manhattanDistance(currentRow, currentCentroid);
                if(decission==3)
                    distances[j] = euklideanDistance(currentRow, currentCentroid);
//                if(decission==2) {
//                    double centroidFromMean = mahalanobisDistance(currentCentroid);
//                    double rowFromMean = mahalanobisDistance(currentRow);
//                    distances[j] = Math.abs(centroidFromMean-rowFromMean);
//                }
            }
            int minimalIndex = findMinimalDistanceIndex(distances);
            rows.get(i).setDistanceFromCentroid(distances[minimalIndex]);
            if(rows.get(i).getCentroidId()==-1 || rows.get(i).getCentroidId()!=minimalIndex)
                changes++;
            rows.get(i).setCentroidId(minimalIndex);


        }
        System.out.println("amountOfChanges: "+changes);

    }

    private double manhattanDistance(DataRowWithCentroidID centroid, DataRowWithCentroidID point) {
        List<Double> rowValues = new ArrayList<>();
        List<Double> providedValues = new ArrayList<>();
        double distance =0;

        for(int x=0; x<point.getData().size(); x++) {
            distance+= Math.abs(centroid.getData().get(x)-point.getData().get(x));
        }
        return distance;

    }

    private double euklideanDistance(DataRowWithCentroidID currentRow, DataRowWithCentroidID currentCentroid) {
        double distance =0;


        for(int x=0; x<currentRow.getData().size(); x++) {
            distance+= Math.pow((currentRow.getData().get(x)-currentCentroid.getData().get(x)), 2);
        }
        distance=Math.sqrt(distance);
//		System.out.println("distance");
        return distance;
    }

    private int findMinimalDistanceIndex(double[] distances) {
        double minimum = Double.MAX_VALUE;
        int index = -1;
        for(int i=0;i<distances.length; i++) {
            if(distances[i]<minimum) {
                minimum = distances[i];
                index=i;
            }
        }
        return index;
    }

    private List<DataColumn> createDataColumnList(List<List<String>> fileFromColumnsWhichUserWants) {

        int amountOfColumns = fileFromColumnsWhichUserWants.size();
        List<DataColumn> columnsList = new ArrayList<>();

        for(int i = 0; i<amountOfColumns; i++){
            String title = fileFromColumnsWhichUserWants.get(i).get(0);
            List<String> contents = new ArrayList<>();

            for(int j = 1 ; j<fileFromColumnsWhichUserWants.get(0).size(); j++){
                contents.add(fileFromColumnsWhichUserWants.get(i).get(j));
            }
            columnsList.add(new DataColumn(title, contents));
        }
        return columnsList;
    }


    private void createCentroids() {
        List<Dot> minMaxValuesInEachDimension = new ArrayList<>();

        for(int j=0;j<listOfCols.size()-1;j++)  {

            double min=Double.MAX_VALUE;
            double max=Double.MIN_VALUE;

            for(int i=0;i<listOfCols.get(0).getContents().size();i++) {

                if(Double.parseDouble(listOfCols.get(j).getContents().get(i))<min) {
                    min=Double.parseDouble(listOfCols.get(j).getContents().get(i));
                }
                if(Double.parseDouble(listOfCols.get(j).getContents().get(i))>max) {
                    max=Double.parseDouble(listOfCols.get(j).getContents().get(i));
                }
            }
            minMaxValuesInEachDimension.add(new Dot(min,max));

        }

        //create n centroids;

        for(int i=0; i<amountOfDecisionClasses;i++) {

            DataRowWithCentroidID newCentroid = new DataRowWithCentroidID();
            for(int j=0;j<minMaxValuesInEachDimension.size();j++) {

                Random r = new Random();
                double randomValue = minMaxValuesInEachDimension.get(j).getMin()
                        + (minMaxValuesInEachDimension.get(j).getMax() -
                        minMaxValuesInEachDimension.get(j).getMin())*r.nextDouble();
                newCentroid.addData(randomValue);
            }
            centroids.add(newCentroid);
        }


    }

    private int findDecisionClasses(List<List<String>> fileFromColumnsWhichUserWants) {

        int amount = 0;
        List<String> decisionElements = fileFromColumnsWhichUserWants.get(fileFromColumnsWhichUserWants.size()-1);
        //first element was title so thats why its being removed
        Set targetSet = Set.copyOf(decisionElements);
        return targetSet.size()-1;
    }


    private List<List<String>> createUserSelectedDataset(List<List<String>> data, List<Integer> columnNamesDigitized) {

        int amountOfRows = data.get(0).size();
        int amountOfColumns = data.size();

        List<List<String>> newDataset = new ArrayList<>();

        for(int i=0; i<amountOfRows; i++){

            List<String> currentRow = new ArrayList<>();
            for(int j=0; j<amountOfColumns; j++){

                boolean doesUserWantsIt = false;

                //if current column is wanted
                for(int x=0; x<columnNamesDigitized.size(); x++){
                    if(j == columnNamesDigitized.get(x)){
                        doesUserWantsIt = true;
                        break;
                    }
                }
                if(doesUserWantsIt){
                    currentRow.add(data.get(j).get(i));
                }

            }
            newDataset.add(currentRow);




        }
        newDataset = inverse(newDataset);
        return newDataset;
    }

    private List<List<String>> inverse(List<List<String>> newDataset) {

        List<List<String>> inversedDataset = new ArrayList<>();
        for(int i=0 ; i<newDataset.get(0).size(); i++){
            List<String> column = new ArrayList<>();

            for(int j=0; j<newDataset.size(); j++){
                column.add(newDataset.get(j).get(i));
            }
            inversedDataset.add(column);
        }
        return inversedDataset;
    }


    private List<Integer> digitizeColumnNames(List<List<String>> data, String[] columns, String decissionColumn) {
        int size = columns.length + 1;
        List<Integer> indexes = new ArrayList<>();
        List<String> names = new ArrayList<>();

        for(String c : columns){
            names.add(c);
        }
        names.add(decissionColumn);

        for(int i=0; i<names.size(); i++){

            boolean doesColumnExist = UsefulInfoFromDatasetReturner.ifColumnWithGivenNameExists(data, names.get(i));
            if(doesColumnExist){
                int columnIndex = UsefulInfoFromDatasetReturner.getColumnId(data, names.get(i));
                boolean doesColumnContainOnlyNumericValues = UsefulInfoFromDatasetReturner.doesColumnContainOnlyNumericValues(data, names.get(i));

                if(i != names.size()-2 | doesColumnContainOnlyNumericValues){
                    indexes.add(columnIndex);
                }
                else{
                    indexes.add(columnIndex);
                }

            }else{
                return new ArrayList<>();
            }

        }

        return indexes;
    }
}
