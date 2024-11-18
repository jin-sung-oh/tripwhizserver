package com.example.demo.util.file.controller;

import com.example.demo.util.file.exception.UploadException;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@Log4j2
public class UploadController {

    @Value("${com.example.upload.path}")
    private String uploadFolder;

    @PostMapping("")
    public ResponseEntity<List<String>> uploadFiles (MultipartFile[] files) {

        if(files == null || files.length == 0){
            return ResponseEntity.noContent().build();
        }

        List<String> uploadedFiles = new ArrayList<>();

        for(MultipartFile file : files){

            String fileName = UUID.randomUUID().toString()+"_"+ file.getOriginalFilename();

            try {

                File savedFile = new File(uploadFolder, fileName);
                FileCopyUtils.copy(file.getBytes(), savedFile);

                if(file.getContentType().startsWith("image")){

                    String thumbnailFileName = "s_" + fileName;

                    @Cleanup
                    InputStream inputStream = new FileInputStream(new File(uploadFolder, fileName));
                    @Cleanup
                    OutputStream outputStream = new FileOutputStream(new File(uploadFolder, thumbnailFileName));

                    Thumbnailator.createThumbnail(inputStream, outputStream, 200, 200);

                }

                uploadedFiles.add(fileName);

            } catch (IOException e) {

                e.printStackTrace();
                throw new UploadException(e.getMessage());
            }


        }

        return ResponseEntity.ok(uploadedFiles);
    }

}
