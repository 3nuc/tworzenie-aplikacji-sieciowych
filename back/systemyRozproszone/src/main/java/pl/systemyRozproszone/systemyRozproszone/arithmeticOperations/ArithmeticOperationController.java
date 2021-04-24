package pl.systemyRozproszone.systemyRozproszone.arithmeticOperations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import pl.systemyRozproszone.systemyRozproszone.CSVHandle.CSVParser;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.UsefulInfoFromDatasetReturner;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.digitization.Digitizer;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.discretization.Discretizer;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.discretization.DiscretizerResponseEnum;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.filters.NumericColumnMinMaxFilter;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.filters.ResultsPercentageFilter;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.normalization.Normalizer;

import java.util.List;


@RestController
public class ArithmeticOperationController {

    @Value("${fileUploadDirectory}")
    public String PATH;

    public  static String TAG = "ArithmeticOperationController";


    @GetMapping("/discretization/get")
    public DiscretizerResponseEnum returnString(
            @RequestParam("fileName") String fileName,
            @RequestParam("columnName") String columnToDiscretize,
            @RequestParam("amountOfSections") Integer amountOfSections){

        boolean ifFileExists = UsefulInfoFromDatasetReturner.checkIfFileExists(fileName, PATH);
        if(!ifFileExists){
            return DiscretizerResponseEnum.FILE_DOESNT_EXIST;
        }
        List<List<String>> testFile = CSVParser.parseCSVtoListArray(fileName, PATH);
        Discretizer discretizer = new Discretizer();
        DiscretizerResponseEnum response = discretizer.discretize(testFile,fileName,
                columnToDiscretize,amountOfSections);
        if(response.equals(DiscretizerResponseEnum.SUCCESS)){
            testFile = discretizer.returnList();
            CSVParser.parseListOfListsToCSV(testFile, PATH+fileName);
        }



        return response;
    }

    @GetMapping("/normalization/get")
    public DiscretizerResponseEnum returnString(
            @RequestParam("fileName") String fileName,
            @RequestParam("columnName") String columnToDiscretize){

        boolean ifFileExists = UsefulInfoFromDatasetReturner.checkIfFileExists(fileName, PATH);
        if(!ifFileExists){
            return DiscretizerResponseEnum.FILE_DOESNT_EXIST;
        }
        List<List<String>> testFile = CSVParser.parseCSVtoListArray(fileName, PATH);
        Normalizer normalizer = new Normalizer();
        DiscretizerResponseEnum response = normalizer.normalize(testFile,fileName,
                columnToDiscretize);
        if(response.equals(DiscretizerResponseEnum.SUCCESS)){
            testFile = normalizer.returnList();
            CSVParser.parseListOfListsToCSV(testFile, PATH+fileName);
        }



        return response;
    }

    @GetMapping("/digitalization/get")
    public DiscretizerResponseEnum returnResult(
            @RequestParam("fileName") String fileName,
            @RequestParam("columnName") String columnName){

        boolean ifFileExists = UsefulInfoFromDatasetReturner.checkIfFileExists(fileName, PATH);
        if(!ifFileExists){
            return DiscretizerResponseEnum.FILE_DOESNT_EXIST;
        }
        List<List<String>> testFile = CSVParser.parseCSVtoListArray(fileName, PATH);
        Digitizer digitizer = new Digitizer();
        DiscretizerResponseEnum response = digitizer.digitize(testFile,columnName);
        if(response.equals(DiscretizerResponseEnum.SUCCESS)){
            testFile = digitizer.returnList();
            CSVParser.parseListOfListsToCSV(testFile, PATH+fileName);
        }
        return response;
    }

    @GetMapping("/percentageFilter")
    public DiscretizerResponseEnum returnPercentFiltered(
            @RequestParam("fileName") String fileName,
            @RequestParam("columnName") String columnName,
            @RequestParam("percentage") String percentage,
            @RequestParam("isDesc") Boolean isDesc){

        boolean ifFileExists = UsefulInfoFromDatasetReturner.checkIfFileExists(fileName, PATH);
        if(!ifFileExists){
            return DiscretizerResponseEnum.FILE_DOESNT_EXIST;
        }
        List<List<String>> testFile = CSVParser.parseCSVtoListArray(fileName, PATH);
        ResultsPercentageFilter percentageFilter = new ResultsPercentageFilter();
        DiscretizerResponseEnum response = percentageFilter.filterData(testFile,columnName, percentage, isDesc);
        if(response.equals(DiscretizerResponseEnum.SUCCESS)){
            testFile = percentageFilter.returnList();
            CSVParser.parseListOfListsToCSV(testFile, PATH+fileName);
        }

        return response;

    }

    @GetMapping("/filterMinAndMax")
    public DiscretizerResponseEnum returnMinMaxFiltered(
            @RequestParam("fileName") String fileName,
            @RequestParam("columnName") String columnName,
            @RequestParam("min") String min,
            @RequestParam("max") String max){

        boolean ifFileExists = UsefulInfoFromDatasetReturner.checkIfFileExists(fileName, PATH);
        if(!ifFileExists){
            return DiscretizerResponseEnum.FILE_DOESNT_EXIST;
        }
        List<List<String>> testFile = CSVParser.parseCSVtoListArray(fileName, PATH);
        NumericColumnMinMaxFilter numericColumnMinMaxFilter = new NumericColumnMinMaxFilter();
        DiscretizerResponseEnum response = numericColumnMinMaxFilter
                .filterByMinAndMaxVal(testFile, columnName, min, max);
        if(response.equals(DiscretizerResponseEnum.SUCCESS)){
            testFile = numericColumnMinMaxFilter.returnList();
            CSVParser.parseListOfListsToCSV(testFile, PATH+fileName);
        }
        return response;
    }

    @PostMapping("/post")
    public String postString(){
        return "post test";
    }
}
