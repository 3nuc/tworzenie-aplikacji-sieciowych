package pl.systemyRozproszone.systemyRozproszone.fileUpload;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class FileUploadController {

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
            File newFile = new File("${fileUploadDirectory}"+file.getOriginalFilename());
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

    private boolean checkWhetherFileWithThisNameAlreadyExists(String fileName) {
        File dir = new File("${fileUploadDirectory}");
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
}
