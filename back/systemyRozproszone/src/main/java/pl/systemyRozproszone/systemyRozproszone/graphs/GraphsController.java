package pl.systemyRozproszone.systemyRozproszone.graphs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.systemyRozproszone.systemyRozproszone.CSVHandle.CSVParser;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.UsefulInfoFromDatasetReturner;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.discretization.DiscretizerResponseEnum;

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


//    @RestController
//    public class ArithmeticOperationController {
//
//        @Value("${fileUploadDirectory}")
//        public String PATH;
//
//        public  static String TAG = "ArithmeticOperationController";
//
//
//        @GetMapping("/discretization/get")
}
