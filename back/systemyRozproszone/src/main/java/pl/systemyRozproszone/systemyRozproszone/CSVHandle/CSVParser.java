package pl.systemyRozproszone.systemyRozproszone.CSVHandle;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVParser {

    public static List<List<String>> parseCSVtoListArray(File file){

        List<List<String>> parsedList = new ArrayList<>();
        List<List<String>> reversedList = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(file))){

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

    public static File parseListOfListsToCSV(List<List<String>> list){

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
        System.out.println("test");
        return null;
    }

}
