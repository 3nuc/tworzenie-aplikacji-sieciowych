package pl.systemyRozproszone.systemyRozproszone.arithmeticOperations;

import org.springframework.web.bind.annotation.*;
import pl.systemyRozproszone.systemyRozproszone.CSVHandle.CSVParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class ArithmeticOperationController {

    public static String PATH = "/Users/tomaszkoltun/Documents/uploadedSpringFiles/";


    @GetMapping("/discretization/get")
    public String returnString(@RequestParam("fileName") String fileName){

        File newFile = new File(PATH + fileName);

        List<List<String>> testFile = CSVParser.parseCSVtoListArray(newFile);


        CSVParser.parseListOfListsToCSV(testFile, PATH+"twojStaryNajebany.csv");
//        try {
//            file.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        return "get test";
    }

    @PostMapping("/post")
    public String postString(){
        return "post test";
    }
}
