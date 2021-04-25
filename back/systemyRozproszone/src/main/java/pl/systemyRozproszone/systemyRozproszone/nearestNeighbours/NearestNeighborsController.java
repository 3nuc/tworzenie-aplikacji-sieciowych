package pl.systemyRozproszone.systemyRozproszone.nearestNeighbours;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.systemyRozproszone.systemyRozproszone.CSVHandle.CSVParser;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.UsefulInfoFromDatasetReturner;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.discretization.DiscretizerResponseEnum;
import pl.systemyRozproszone.systemyRozproszone.nearestNeighbours.distanceMetrics.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class NearestNeighborsController {

    @Value("${fileUploadDirectory}")
    public String PATH;

    public static String TAG = "NearestNeighborsController";

    @GetMapping("/returnNearestNeighbours/")
    public String returnString(
            @RequestParam("fileName") String fileName,
            @RequestParam("columns") String [] columns,
            @RequestParam("decissionColumn") String decissionColumn,
            @RequestParam("pointCoordinates") String [] coordinates,
            @RequestParam("findType") String findType,
            @RequestParam("neighbours") Integer amountOfNeighbours){

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

        List<String> pointCoords = new ArrayList<>(Arrays.asList(coordinates));
//        createDoubleArrayFromString(coordinates, pointCoords);
        if(pointCoords.size() != coordinates.length){
            return DiscretizerResponseEnum.PROVIDED_UNPARSABLE_VALUES_IN_POINT_COORDINATES.toString();
        }
        //if its here it means that i need to create new data list - made only from selected columns
        List<List<String>> fileFromColumnsWhichUserWants = createUserSelectedDataset(data, columnNamesDigitized);
        int newDecissionColumnIndex = UsefulInfoFromDatasetReturner.getColumnId(data, decissionColumn);

        Distance distance = null;

        if(findType.equals("Euklidean")){
            distance = new EuklideanDistance(fileFromColumnsWhichUserWants,pointCoords,decissionColumn);
        }else if(findType.equals("Chebyshew")){
            distance = new ChebyshewDistance(fileFromColumnsWhichUserWants,pointCoords,decissionColumn);
        }else if(findType.equals("Mahalanobis")){
            distance = new MahalanobisDistance(fileFromColumnsWhichUserWants,pointCoords,decissionColumn);
        }else if(findType.equals("Manhattan")){
            distance = new ManhattanDistance(fileFromColumnsWhichUserWants,pointCoords,decissionColumn);
        }

        distance.calculateDistance();
        List<List<String>> neighbours = distance.returnNearestNeighbours(amountOfNeighbours);


//        JsonObject response = new JsonObject();
//        response.addProperty("columnCount", amountOfColumns);
//        response.addProperty("rowCount", amountOfRows);
//
//        JsonArray columnNames = new JsonArray();
//        for(String title: titles){
//            columnNames.add(title);
//        }
//        response.add("columnNames", columnNames);
//
//        return response.toString();



        return neighbours.toString();
    }

    private List<Double> createDoubleArrayFromString(String[] coordinates, List<Double> pointCoords) {
        List<String> input = new ArrayList<>(Arrays.asList(coordinates));
        String toReturn = "ok";
        List<Double> inputDoubled = new ArrayList<>();
        for(int i=0; i< input.size(); i++){

            double value;

            try{
                value = Double.valueOf(input.get(i));
                inputDoubled.add(value);
            }
            catch (NumberFormatException e){
                toReturn = DiscretizerResponseEnum.PROVIDED_UNPARSABLE_VALUES_IN_POINT_COORDINATES.toString();
                break;
            }

        }
        pointCoords = inputDoubled;
        return pointCoords;
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
