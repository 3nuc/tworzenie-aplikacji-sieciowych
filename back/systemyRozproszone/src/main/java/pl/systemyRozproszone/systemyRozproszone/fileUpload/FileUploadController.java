package pl.systemyRozproszone.systemyRozproszone.fileUpload;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.FileInfo;
import pl.systemyRozproszone.systemyRozproszone.arithmeticOperations.helperClasses.discretization.DiscretizerResponseEnum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FileUploadController {

    @Value("${fileUploadDirectory}")
    public String PATH;

    /**
     *
     * @param file - file from the request
     * @return http status
     *      if file exists in given directory it returns 406 NOT_ACCEPTABLE
     *      if file doesnt exist - it returns 200 OK
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file){
        
        boolean doesFileAlreadyExist = checkWhetherFileWithThisNameAlreadyExists(file.getOriginalFilename());

        if(!doesFileAlreadyExist){
            File newFile = new File(PATH + file.getOriginalFilename());
            try {
                newFile.createNewFile();
                FileOutputStream fout = new FileOutputStream(newFile);
                fout.write(file.getBytes());
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new ResponseEntity<>("file uploaded", HttpStatus.OK);
        }

        return new ResponseEntity<>("file already exists", HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     *
     * @param fileName filename
     * @return status with file or status alone
     */
    @GetMapping(value = "/download/")
    public ResponseEntity<Resource> getFile(@RequestParam("fileName") String fileName){


        //kurwa zwraca jako tablice bajtow czyli bezuzyteczny jebaniec
        // do plikow .csv trza bedzie dodac superscv by sam budowal z pliku jakiegos
        System.out.println("filePath: "+PATH+fileName);
        File newFile = new File(PATH + fileName);
        Path path = Paths.get(newFile.getAbsolutePath());
        try {
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            return ResponseEntity.ok()
                    .contentLength(newFile.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);

        } catch (IOException e) {
            return new ResponseEntity("file not found", HttpStatus.CONFLICT);
        }

    }

    /**
     *
     * @param fileName name of file to be uploaded
     * @return true if this file already exists, false if doesnt exist and can be uploaded
     */
    private boolean checkWhetherFileWithThisNameAlreadyExists(String fileName) {
        File dir = new File(PATH);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if(child.getName().equals(fileName)){
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }


    /**
     *
     * @return every uploaded file name
     */
    @GetMapping("/listOfFiles")
    public List<String> listOfFiles(){
        List<String> fileNames = new ArrayList<>();

        File dir = new File(PATH);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                fileNames.add(child.getName());
            }
        }
        return fileNames;
    }

    @GetMapping("/getFileInfo")
    public String fileData(@RequestParam("fileName") String fileName){
        File dir = new File(PATH);
        String result;
        File fileToCheck = new File("null");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if(child.getName().equals(fileName)){
                    fileToCheck = child;
                    break;
                }
            }
        }
        if(fileToCheck!=null){
            FileInfo fileInfo = new FileInfo(fileToCheck);
            int amountOfRows = fileInfo.getRowCount();
            int amountOfColumns = fileInfo.getColumnCount();
            List<String> titles = fileInfo.getColumnNames();

            JsonObject response = new JsonObject();
            response.addProperty("columnCount", amountOfColumns);
            response.addProperty("rowCount", amountOfRows);

            JsonArray columnNames = new JsonArray();
            for(String title: titles){
                columnNames.add(title);
            }
            response.add("columnNames", columnNames);

            return response.toString();

        }
        else{
            return DiscretizerResponseEnum.FILE_DOESNT_EXIST.toString();
        }
    }
}
