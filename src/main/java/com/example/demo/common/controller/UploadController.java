package com.example.demo.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.common.dto.SampleDTO;
import com.example.demo.util.CustomFileUtil;

import java.util.Arrays;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class UploadController {

    private final CustomFileUtil customFileUtil;

    @PostMapping("upload")
    public List<String> upload(SampleDTO sampleDTO){

        log.info(sampleDTO);
        log.info("------------------");

        List<String> fileNames =
                customFileUtil.saveFiles(Arrays.asList(sampleDTO.getFiles()));

        log.info(fileNames);

        return fileNames;

    }

}
