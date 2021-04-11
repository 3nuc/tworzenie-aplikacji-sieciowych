package pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileInfo {

    File file;


    public FileInfo(File file) {
        this.file = file;
    }

    public int getColumnCount(){
        int columnCount=0;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){

            String line=br.readLine();
            if(line != null){
                String [] values = line.split(",");
                columnCount = values.length;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return columnCount;
    }

    public int getRowCount(){
        boolean first = true;
        int rowCount=0;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){

            String line;
            while ((line = br.readLine())!=null){
                if(first){
                    first = false;
                }else{
                    rowCount++;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    public List<String> getColumnNames(){

        List<String> columnNames = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))){

            String line=br.readLine();
            if(line != null){
                String [] values = line.split(",");
                columnNames = new ArrayList<>(Arrays.asList(values));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return columnNames;
    }
}
