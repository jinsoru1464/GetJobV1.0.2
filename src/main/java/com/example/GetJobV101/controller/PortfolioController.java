package com.example.GetJobV101.controller;

import com.example.GetJobV101.PresignedUrlRequest;
import com.example.GetJobV101.dto.PortfolioDto;
import com.example.GetJobV101.entity.Portfolio;
import com.example.GetJobV101.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    @Value("${spring.file.upload-dir}")
    private String uploadDir;

    @Autowired
    private PortfolioService portfolioService;

    // 포트폴리오 생성
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createPortfolio(
            @RequestParam("title") String title,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("teamSize") int teamSize,
            @RequestParam("skills") String skills,
            @RequestParam("role") String role,
            @RequestParam("descriptions") List<String> descriptions,
            @RequestPart("images") MultipartFile[] images) {

        try {
            Path dirPath = Paths.get(uploadDir);
            System.out.println("✅ 업로드 경로 (절대경로): " + dirPath.toAbsolutePath());

            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                System.out.println("✅ 업로드 디렉터리를 새로 생성했습니다.");
            }

            List<String> uploadedFileNames = new ArrayList<>();
            List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");

            for (MultipartFile file : images) {
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || originalFilename.isEmpty()) {
                    continue;
                }

                String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
                if (!allowedExtensions.contains(extension)) {
                    throw new IOException("지원하지 않는 파일 형식: " + originalFilename);
                }

                String uniqueFileName = UUID.randomUUID() + "_" + originalFilename;
                Path filePath = dirPath.resolve(uniqueFileName);
                file.transferTo(filePath.toFile());
                uploadedFileNames.add(uniqueFileName);
            }

            PortfolioDto dto = new PortfolioDto(title, startDate, endDate, teamSize, skills, role, descriptions, uploadedFileNames);
            Portfolio savedPortfolio = portfolioService.savePortfolio(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPortfolio);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("🚩 실패 이유: [" + e.getClass().getSimpleName() + "] " + e.getMessage());
        }
    }

    // 포트폴리오 목록 조회
    @GetMapping
    public ResponseEntity<List<Portfolio>> getAllPortfolios() {
        try {
            List<Portfolio> portfolios = portfolioService.getAllPortfolios();
            return ResponseEntity.ok(portfolios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    // 단일 포트폴리오 조회
    @GetMapping("/{id}")
    public ResponseEntity<Portfolio> getPortfolioById(@PathVariable Long id) {
        try {
            Optional<Portfolio> portfolio = portfolioService.getPortfolioById(id);
            return portfolio.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 포트폴리오 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePortfolio(@PathVariable Long id) {
        try {
            portfolioService.deletePortfolio(id);
            return ResponseEntity.ok("✅ 포트폴리오가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("🚩 삭제 실패: [" + e.getClass().getSimpleName() + "] " + e.getMessage());
        }
    }

    // 포트폴리오 수정
    // 포트폴리오 수정
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> updatePortfolio(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("teamSize") int teamSize,
            @RequestParam("skills") String skills,
            @RequestParam("role") String role,
            @RequestParam("descriptions") List<String> descriptions,
            @RequestPart(value = "images", required = false) MultipartFile[] images) {

        try {
            // 기존 포트폴리오 가져오기
            Optional<Portfolio> existingPortfolioOpt = portfolioService.getPortfolioById(id);
            if (existingPortfolioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("🚩 포트폴리오를 찾을 수 없습니다.");
            }

            Portfolio existingPortfolio = existingPortfolioOpt.get();

            // 기존 이미지 경로 가져오기
            List<String> existingImagePaths = existingPortfolio.getImagePaths();
            if (existingImagePaths == null) {
                existingImagePaths = new ArrayList<>();
            }

            // 이미지 업로드 처리
            Path dirPath = Paths.get(uploadDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            List<String> uploadedFileNames = new ArrayList<>(existingImagePaths); // 기존 이미지 경로 유지
            List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");

            // 새로운 이미지가 있는 경우 추가 처리
            if (images != null && images.length > 0) {
                for (MultipartFile file : images) {
                    String originalFilename = file.getOriginalFilename();
                    if (originalFilename == null || originalFilename.isEmpty()) {
                        continue;
                    }

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

            // DTO 생성 및 업데이트
            PortfolioDto dto = new PortfolioDto(title, startDate, endDate, teamSize, skills, role, descriptions, uploadedFileNames);
            Portfolio updatedPortfolio = portfolioService.updatePortfolio(id, dto);

            return ResponseEntity.ok(updatedPortfolio);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("🚩 수정 실패: [" + e.getClass().getSimpleName() + "] " + e.getMessage());
        }
    }

    // Presigned URL 요청 API
    @PostMapping("/preSignedUrl")
    @Operation(summary = "Presigned URL 요청 API", description = "S3에 이미지를 업로드하기 위한 Presigned URL을 요청합니다.")
    public ResponseEntity<Map<String, String>> getPresignedUrl(@Valid @RequestBody PresignedUrlRequest request) {
        Map<String, String> preSignedUrl = portfolioService.getPresignedUrl("image", request.getImageName());
        return ResponseEntity.status(HttpStatus.OK).body(preSignedUrl);
    }


}
