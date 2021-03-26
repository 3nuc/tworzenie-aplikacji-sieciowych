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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file){
        File newFile = new File("/Users/tomaszkoltun/Documents/uploadedSpringFiles/"+file.getOriginalFilename());
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
}
