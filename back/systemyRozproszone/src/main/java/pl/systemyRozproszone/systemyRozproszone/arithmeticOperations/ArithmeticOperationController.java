package pl.systemyRozproszone.systemyRozproszone.arithmeticOperations;

import org.springframework.web.bind.annotation.*;
import pl.systemyRozproszone.systemyRozproszone.CSVHandle.CSVParser;

import java.io.File;
import java.util.List;

@RestController
public class ArithmeticOperationController {

    public static String PATH = "/Users/tomaszkoltun/Documents/uploadedSpringFiles/";


    @GetMapping("/discretization/get")
    public String returnString(@RequestParam("fileName") String fileName){

        File newFile = new File(PATH + fileName);

        List<List<String>> testFile = CSVParser.parseCSVtoListArray(newFile);

        System.out.println("te");

        File file = CSVParser.parseListOfListsToCSV(testFile);

        System.out.println("te");


        return "get test";
    }

    @PostMapping("/post")
    public String postString(){
        return "post test";
    }
}
