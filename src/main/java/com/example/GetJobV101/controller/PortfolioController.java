package com.example.GetJobV101.controller;

import com.example.GetJobV101.dto.PortfolioDto;
import com.example.GetJobV101.entity.Portfolio;
import com.example.GetJobV101.service.PortfolioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    @Value("${spring.file.upload-dir}")
    private String uploadDir;

    @Autowired
    private PortfolioService portfolioService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createPortfolio(
            @RequestPart("title") String title,
            @RequestPart("startDate") String startDate,
            @RequestPart("endDate") String endDate,
            @RequestPart("teamSize") int teamSize,
            @RequestPart("skills") String skills,
            @RequestPart("role") String role,
            @RequestPart("descriptions") List<String> descriptions,
            @RequestPart("images") MultipartFile[] images) {

        try {
            // 이미지 업로드
            List<String> imagePaths = saveImages(images);

            // DTO로 데이터 구성
            PortfolioDto dto = new PortfolioDto();
            dto.setTitle(title);
            dto.setStartDate(startDate);
            dto.setEndDate(endDate);
            dto.setTeamSize(teamSize);
            dto.setSkills(skills);
            dto.setRole(role);
            dto.setDescriptions(descriptions);
            dto.setImagePaths(imagePaths);

            // 포트폴리오 저장
            Portfolio savedPortfolio = portfolioService.savePortfolio(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedPortfolio);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 업로드 실패: " + e.getMessage());
        }
    }

    // 이미지 업로드 로직을 별도 메소드로 분리
    private List<String> saveImages(MultipartFile[] images) throws IOException {
        List<String> uploadedFileNames = new ArrayList<>();

        Path dirPath = Paths.get(uploadDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");

        for (MultipartFile file : images) {
            if (!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

                if (!allowedExtensions.contains(extension)) {
                    throw new IOException("지원하지 않는 파일 형식: " + originalFilename);
                }

                String uniqueFileName = UUID.randomUUID() + "_" + originalFilename;
                Path filePath = dirPath.resolve(uniqueFileName);
                file.transferTo(filePath.toFile());

                uploadedFileNames.add(uniqueFileName);
            }
        }

        return uploadedFileNames;
    }

}



