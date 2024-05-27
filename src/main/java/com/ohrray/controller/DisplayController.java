package com.ohrray.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;

@RestController
@Log4j2

public class DisplayController {
    @Value("${uploadPath}")
    private String uploadPath;

    @Value("${communityImgLocation}")
    private  String communityImgPath;
    @GetMapping("display")
    public ResponseEntity<byte[]> getFile(String fileName){
        ResponseEntity<byte[]> result = null;
        try {
            if (fileName == null) {
                // fileName이 null인 경우 적절한 예외 처리
                throw new IllegalArgumentException("File name cannot be null");
            }
            String srcFileName= URLDecoder.decode(fileName,"UTF-8");
            
            File file = new File(uploadPath+File.separator+fileName);
            HttpHeaders header = new HttpHeaders();
            header.add("Content-Type" , Files.probeContentType(file.toPath()));
            System.out.println("header = " + header);
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),header, HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    @GetMapping("display2")
    public ResponseEntity<byte[]> getCommunityFile(String fileName){
        ResponseEntity<byte[]> result = null;
        try {
            if (fileName == null) {
                // fileName이 null인 경우 적절한 예외 처리
                throw new IllegalArgumentException("File name cannot be null");
            }
            String srcFileName= URLDecoder.decode(fileName,"UTF-8");

            File file = new File(communityImgPath+File.separator+fileName);
            HttpHeaders header = new HttpHeaders();
            header.add("Content-Type" , Files.probeContentType(file.toPath()));
            System.out.println("header = " + header);
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),header, HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
