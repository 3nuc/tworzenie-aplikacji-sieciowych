package pl.systemyRozproszone.systemyRozproszone.CSVHandle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CSVParser {


    public static List<List<String>> parseCSVtoListArray(String fileName, String PATH){



        File newFile = new File(PATH + fileName);
        List<List<String>> parsedList = new ArrayList<>();
        List<List<String>> reversedList = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(newFile))){

            String line;
            while ((line = br.readLine())!=null){
                String [] values = line.split(",");
                parsedList.add(Arrays.asList(values));
            }
            reversedList = new ArrayList<>();

            int headersSize = parsedList.get(0).size();
            int rows = parsedList.size();

            for(int i=0 ; i< headersSize; i++){

                List<String> column = new ArrayList<>();
                for(int j=0; j<rows; j++){
                    column.add(parsedList.get(j).get(i));
                }
                reversedList.add(column);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return reversedList;
    }

    public static File parseListOfListsToCSV(List<List<String>> list, String fileName){

        int rows = list.get(0).size();
        int columns = list.size();

        List<List<String>> reversedList = new ArrayList<>();

        for(int i=0 ; i< rows; i++ ){

            List<String> row = new ArrayList<>();

            for(int j = 0 ; j<columns ; j++){
                row.add(list.get(j).get(i));
            }
            reversedList.add(row);

        }

        try {
            FileWriter csvWriter = new FileWriter(fileName);
            for(List<String> row : reversedList){
                csvWriter.append(String.join(",", row));
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("test");
        return null;
    }

}
