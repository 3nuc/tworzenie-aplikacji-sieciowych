package pl.systemyRozproszone.systemyRozproszone.arithmeticOperations;

import org.apache.juli.logging.Log;
import org.springframework.web.bind.annotation.*;
import pl.systemyRozproszone.systemyRozproszone.CSVHandle.CSVParser;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.Discretizer;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.DiscretizerResponseEnum;

import java.io.File;
import java.io.IOException;
import java.util.List;


@RestController
public class ArithmeticOperationController {

    public static String PATH = "/Users/tomaszkoltun/Documents/uploadedSpringFiles/";
    public static String TAG = "ArithmeticOperationController";


    @GetMapping("/discretization/get")
    public String returnString(
            @RequestParam("fileName") String fileName,
            @RequestParam("columnName") String columnToDiscretize,
            @RequestParam("amountOfSections") Integer amountOfSections){

        List<List<String>> testFile = CSVParser.parseCSVtoListArray(fileName);
        Discretizer discretizer = new Discretizer();
        DiscretizerResponseEnum response = discretizer.discretize(testFile,fileName,
                columnToDiscretize,amountOfSections);
        if(response.equals(DiscretizerResponseEnum.SUCCESS)){
            testFile = discretizer.returnList();
            CSVParser.parseListOfListsToCSV(testFile, PATH+"twojStaryNajebanyXD.csv");
        }



        return "get test";
    }

    @PostMapping("/post")
    public String postString(){
        return "post test";
    }
}
