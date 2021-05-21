package pl.systemyRozproszone.systemyRozproszone.graphs;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.systemyRozproszone.systemyRozproszone.CSVHandle.CSVParser;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.UsefulInfoFromDatasetReturner;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.digitization.Digitizer;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.discretization.DiscretizerResponseEnum;
import pl.systemyRozproszone.systemyRozproszone.graphs.corellation.ColorsObject;
import pl.systemyRozproszone.systemyRozproszone.graphs.corellation.CorellationColumn;
import pl.systemyRozproszone.systemyRozproszone.graphs.corellation.CorrelationColor;
import pl.systemyRozproszone.systemyRozproszone.graphs.corellation.PearsonCorellationDataGenerator;
import pl.systemyRozproszone.systemyRozproszone.graphs.histogram.HistogramDataGenerator;

import java.util.ArrayList;
import java.util.List;


@RestController
public class GraphsController {


    @Value("${fileUploadDirectory}")
    public String PATH;

    public  static String TAG = "GraphsController";


    @GetMapping("/createHistogram")
    public String createdHistogram(
            @RequestParam("amountOfSections") int amountOfSectors,
            @RequestParam("decissionColumn") String decissionColumn,
            @RequestParam("columnToCheck") String columnToCheck,
            @RequestParam("fileName") String fileName){


        boolean ifFileExists = UsefulInfoFromDatasetReturner.checkIfFileExists(fileName, PATH);
        if(!ifFileExists){
            return DiscretizerResponseEnum.FILE_DOESNT_EXIST.toString();
        }
        List<List<String>> data = CSVParser.parseCSVtoListArray(fileName, PATH);
        int columnToCheckIndex = UsefulInfoFromDatasetReturner.getColumnId(data, columnToCheck);
        int decissionColumnIndex = UsefulInfoFromDatasetReturner.getColumnId(data, decissionColumn);

        if(columnToCheckIndex == -1 || decissionColumnIndex == -1){
            return  DiscretizerResponseEnum.COLUMN_DOESNT_EXIST.toString();
        }

        if(!UsefulInfoFromDatasetReturner.doesColumnContainOnlyNumericValues(data, columnToCheck)){
            return DiscretizerResponseEnum.COLUMN_DOESNT_CONTAIN_ONLY_NUMERIC_VALUES.toString();
        }

        String histogramData = HistogramDataGenerator.generate(data.get(columnToCheckIndex), data.get(decissionColumnIndex), amountOfSectors);


        return histogramData;
    }

    @GetMapping("/createCorellation")
    public String correlation(
            @RequestParam("xColumn") String xColumn,
            @RequestParam("yColumn") String yColumn,
            @RequestParam("fileName") String fileName){

        boolean ifFileExists = UsefulInfoFromDatasetReturner.checkIfFileExists(fileName, PATH);
        if(!ifFileExists){
            return DiscretizerResponseEnum.FILE_DOESNT_EXIST.toString();
        }
        List<List<String>> data = CSVParser.parseCSVtoListArray(fileName, PATH);
        List<String> xColumnData = new ArrayList<>();
        List<String> yColumnData = new ArrayList<>();
        int xColumnIndex = UsefulInfoFromDatasetReturner.getColumnId(data, xColumn);
        int yColumnIndex = UsefulInfoFromDatasetReturner.getColumnId(data, yColumn);
        if(xColumnIndex == -1 || yColumnIndex == -1){
            return  DiscretizerResponseEnum.COLUMN_DOESNT_EXIST.toString();
        }
        if(!UsefulInfoFromDatasetReturner.doesColumnContainOnlyNumericValues(data, xColumn)){
            xColumnData = Digitizer.digitizeColumn(data.get(xColumnIndex));
        }
        else{
            xColumnData = data.get(xColumnIndex);
        }

        if(!UsefulInfoFromDatasetReturner.doesColumnContainOnlyNumericValues(data, yColumn)){
            yColumnData = Digitizer.digitizeColumn(data.get(yColumnIndex));
        }
        else{
            yColumnData = data.get(yColumnIndex);
        }

        String correlationData = null;
        try {
            correlationData = PearsonCorellationDataGenerator.generateData(xColumnData, yColumnData);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return correlationData;
    }


    @GetMapping("/createCorellationTable")
    public String correlationTable(
            @RequestParam("fileName") String fileName) throws IllegalAccessException {

        boolean ifFileExists = UsefulInfoFromDatasetReturner.checkIfFileExists(fileName, PATH);
        if(!ifFileExists){
            return DiscretizerResponseEnum.FILE_DOESNT_EXIST.toString();
        }
        List<List<String>> data = CSVParser.parseCSVtoListArray(fileName, PATH);
        List<List<String>> dataDigitized = new ArrayList<>();

        for(int i=0; i<data.size(); i++){
            if(!UsefulInfoFromDatasetReturner.doesColumnContainOnlyNumericValues(data, data.get(i).get(0))){
                dataDigitized.add(Digitizer.digitizeColumn(data.get(i)));
            }else{
                dataDigitized.add(data.get(i));
            }
        }

        int tableSize = dataDigitized.size();
        CorrelationColor [][] resultTable = new CorrelationColor[tableSize][tableSize];

        for(int i=0 ; i<tableSize; i++){
            for(int j=0 ;j<tableSize; j++){
                String value = PearsonCorellationDataGenerator.generateCorrelationValue(dataDigitized.get(i),dataDigitized.get(j));
                CorrelationColor color = new CorrelationColor();
                color.setCorrelationValue(value);
                color.setColors(calculateColor(value));
                //tutaj ten kolor obliczyc
                resultTable[i][j]=color;
            }
        }

        JsonObject response = new JsonObject();
        response.addProperty("size", dataDigitized.size());

        JsonArray array = new JsonArray();

        for(int i=0; i<tableSize; i++){

            for(int j=0; j<tableSize; j++){
                JsonObject singleItem = new JsonObject();
                singleItem.addProperty("row", data.get(i).get(0));
                singleItem.addProperty("col", data.get(j).get(0));
                singleItem.addProperty("correlationValue", resultTable[i][j].getCorrelationValue());
                singleItem.addProperty("HcolorValue", resultTable[i][j].getH());
                singleItem.addProperty("ScolorValue", resultTable[i][j].getS());
                singleItem.addProperty("LcolorValue", resultTable[i][j].getL());
                array.add(singleItem);
            }
        }
        response.add("items", array);

        JsonObject namesObj = new JsonObject();
        JsonArray columns = new JsonArray();
        JsonArray rows = new JsonArray();
        for(int i=0; i<tableSize; i++){

            JsonObject item = new JsonObject();
            item.addProperty("row", i);
            item.addProperty("name", data.get(i).get(0));
            rows.add(item);

            JsonObject item2 = new JsonObject();
            item2.addProperty("col", i);
            item2.addProperty("name", data.get(i).get(0));
            columns.add(item2);
        }
        namesObj.add("columns", columns);
        namesObj.add("rows", rows);
        response.add("axis_names", namesObj);

//        for(int i=0; i<tableSize; i++){
//
//            JsonArray row = new JsonArray();
//            for(int j=0; j<tableSize; j++){
//                JsonObject singleItem = new JsonObject();
//                singleItem.addProperty("correlationValue", resultTable[i][j].getCorrelationValue());
//                singleItem.addProperty("HcolorValue", resultTable[i][j].getH());
//                singleItem.addProperty("ScolorValue", resultTable[i][j].getS());
//                singleItem.addProperty("LcolorValue", resultTable[i][j].getL());
//                row.add(singleItem);
//            }
//            array.add(row);
//        }
//        response.add("table", array);




        return response.toString();
    }

    private ColorsObject calculateColor(String value) {
        ColorsObject toReturn = new ColorsObject();
        double hue = 60;
        double val = Double.parseDouble(value);
        boolean isBelowZero = false;
        if(val<0){
            isBelowZero=true;
            val*=-1;
        }
        double valueToChange = 60*val;

        if(isBelowZero){
            hue=hue-valueToChange;
        }else{
            hue=hue+valueToChange;
        }

        toReturn.setH((int)hue);
        toReturn.setS(100);
        toReturn.setL(50);
        return toReturn;
    }

}
