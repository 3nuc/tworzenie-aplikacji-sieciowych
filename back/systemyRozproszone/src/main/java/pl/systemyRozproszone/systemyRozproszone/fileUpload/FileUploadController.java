package pl.systemyRozproszone.systemyRozproszone.fileUpload;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    public static String PATH = "/Users/tomaszkoltun/Documents/uploadedSpringFiles/";

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
    @GetMapping(value = "/download/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable("fileName") String fileName){


        //kurwa zwraca jako tablice bajtow czyli bezuzyteczny jebaniec
        // do plikow .csv trza bedzie dodac superscv by sam budowal z pliku jakiegos
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
}
