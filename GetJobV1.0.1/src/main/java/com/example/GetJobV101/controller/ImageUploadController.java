/*
package com.example.GetJobV101.controller;

import com.example.GetJobV101.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ImageUploadController {

    @Value("${spring.file.upload-dir}")
    private String uploadDir;

    @Autowired
    private PortfolioService portfolioService;

    @PostMapping("/portfolios/{portfolioId}/upload-images")
    public ResponseEntity<String> uploadImages(
            @PathVariable Long portfolioId,
            @RequestParam("images") MultipartFile[] files) {

        List<String> uploadedFileNames = new ArrayList<>();

        try {
            Path dirPath = Paths.get(uploadDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

                    if (!allowedExtensions.contains(extension)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("지원하지 않는 파일 형식입니다: " + originalFilename);
                    }

                    String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;
                    Path filePath = dirPath.resolve(uniqueFileName);
                    file.transferTo(filePath.toFile());

                    uploadedFileNames.add(uniqueFileName);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("빈 파일이 포함되어 있습니다.");
                }
            }

            // ✅ PortfolioService의 새 메소드 호출로 DB 업데이트
            portfolioService.addImagePaths(portfolioId, uploadedFileNames);

            return ResponseEntity.ok("이미지 업로드 및 포트폴리오 이미지 경로 업데이트 성공!");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 내부 오류: " + e.getMessage());
        }
    }
}

*/
