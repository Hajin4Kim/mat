package com.example.mat.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.mat.dto.UploadResultDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Log4j2
@RequestMapping("/dfup")
@Controller
public class DinerUploadController {

    @Value("${com.example.mat.upload.path}")
    private String uploadPath;

    @PostMapping("/upload")
    public ResponseEntity<List<UploadResultDto>> postDinerFileUpload(MultipartFile[] uploadFiles) {

        List<UploadResultDto> UploadResultDtos = new ArrayList<>();

        for (MultipartFile multipartFile : uploadFiles) {
            log.info("original file name : {}", multipartFile.getOriginalFilename());
            log.info("size : {}", multipartFile.getSize());
            log.info("content type : {}", multipartFile.getContentType());

            // 이미지 파일 여부 확인
            if (!multipartFile.getContentType().startsWith("image")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // 사용자가 올린 파일명
            String originName = multipartFile.getOriginalFilename();

            String saveFolderPath = makeFolder();

            // 파일 저장 - uuid(중복 해결)
            String uuid = UUID.randomUUID().toString();

            String saveName = uploadPath + File.separator + saveFolderPath + File.separator + uuid + "_" + originName;

            Path savePath = Paths.get(saveName);

            try {
                // 폴더 저장
                multipartFile.transferTo(savePath);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // 저장된 파일 정보 추가
            UploadResultDtos.add(new UploadResultDto(uuid, originName, saveFolderPath));
        }

        return new ResponseEntity<List<UploadResultDto>>(UploadResultDtos, HttpStatus.OK);
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName, String size) {
        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "utf-8");
            File file = new File(uploadPath + File.separator + srcFileName);

            if (size != null && size.equals("1")) {
                file = new File(file.getParent(), file.getName().substring(2));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content_Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers,
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    // @PostMapping("/remove")
    // public ResponseEntity<String> postUploadFileRemove(String filePath) {
    // log.info("삭제 요청 {}", filePath);
    // try {
    // String srcFileName = URLDecoder.decode(filePath, "utf-8");

    // // 원본 파일 삭제
    // File file = new File(uploadPath, srcFileName);
    // file.delete();

    // // 썸네일 파일 삭제
    // File thumbFile = new File(file.getParent(), "s_" + file.getName());
    // thumbFile.delete();

    // return new ResponseEntity<>("success", HttpStatus.OK);
    // } catch (UnsupportedEncodingException e) {
    // e.printStackTrace();
    // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }

    // }

    private String makeFolder() {
        // 오늘 날짜
        LocalDate today = LocalDate.now();
        log.info(today);
        String dateStr = today.format(DateTimeFormatter.ofPattern("YYYY/MM/dd"));

        File dirs = new File(uploadPath, dateStr);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }

        // 날짜/시간/숫자에 특정 포맷 지정
        // SimpleDateFormat sdf = new SimpleDateFormat("YYYY/mm/dd");
        // sdf.format(new Date());

        return dateStr;
    }

}
