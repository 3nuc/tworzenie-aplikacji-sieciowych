package pl.systemyRozproszone.systemyRozproszone.arithmeticOperations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import pl.systemyRozproszone.systemyRozproszone.CSVHandle.CSVParser;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.digitization.Digitizer;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.discretization.Discretizer;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.discretization.DiscretizerResponseEnum;
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

        List<List<String>> testFile = CSVParser.parseCSVtoListArray(fileName, PATH);
        Discretizer discretizer = new Discretizer();
        DiscretizerResponseEnum response = discretizer.discretize(testFile,fileName,
                columnToDiscretize,amountOfSections);
        if(response.equals(DiscretizerResponseEnum.SUCCESS)){
            testFile = discretizer.returnList();
            CSVParser.parseListOfListsToCSV(testFile, PATH+"twojStaryNajebanyXD.csv");
        }



        return response;
    }

    @GetMapping("/normalization/get")
    public DiscretizerResponseEnum returnString(
            @RequestParam("fileName") String fileName,
            @RequestParam("columnName") String columnToDiscretize){

        List<List<String>> testFile = CSVParser.parseCSVtoListArray(fileName, PATH);
        Normalizer normalizer = new Normalizer();
        DiscretizerResponseEnum response = normalizer.normalize(testFile,fileName,
                columnToDiscretize);
        if(response.equals(DiscretizerResponseEnum.SUCCESS)){
            testFile = normalizer.returnList();
            CSVParser.parseListOfListsToCSV(testFile, PATH+"twojStaryNajebanyXD.csv");
        }



        return response;
    }

    @GetMapping("/digitalization/get")
    public DiscretizerResponseEnum returnResult(
            @RequestParam("fileName") String fileName,
            @RequestParam("columnName") String columnName){

        List<List<String>> testFile = CSVParser.parseCSVtoListArray(fileName, PATH);
        Digitizer digitizer = new Digitizer();
        DiscretizerResponseEnum response = digitizer.digitize(testFile,columnName);
        if(response.equals(DiscretizerResponseEnum.SUCCESS)){
            testFile = digitizer.returnList();
            CSVParser.parseListOfListsToCSV(testFile, PATH+"twojStaryNajebanyXD.csv");
        }



        return response;

    }

    @PostMapping("/post")
    public String postString(){
        return "post test";
    }
}
