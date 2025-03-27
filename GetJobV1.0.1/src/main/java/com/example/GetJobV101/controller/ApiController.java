package com.example.GetJobV101.controller;

import com.example.GetJobV101.dto.PortfolioDto;
import com.example.GetJobV101.entity.Portfolio;
import com.example.GetJobV101.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final PortfolioService portfolioService;

    public ApiController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @Operation(summary = "포트폴리오 저장", description = "프로젝트 데이터를 저장합니다.")
    @PostMapping("/portfolios")
    public Portfolio savePortfolio(@RequestBody PortfolioDto portfolioDto) {
        return portfolioService.savePortfolio(portfolioDto);
    }

    /*@Operation(summary = "포트폴리오 전체 조회", description = "저장된 모든 포트폴리오 데이터를 반환합니다.")
    @GetMapping("/portfolios")
    public List<Portfolio> getAllPortfolios() {
        return portfolioService.getAllPortfolios();
    }*/

    @Operation(summary = "메인 페이지 데이터 조회", description = "메인 페이지의 데이터를 반환합니다.")
    @GetMapping("/mainpage")
    public String getMainPageData() {
        return "This is API response for mainpage";
    }

    @Operation(summary = "포트폴리오 데이터 조회", description = "포트폴리오 데이터를 반환합니다.")
    @GetMapping("/portfolio")
    public String getPortfolioData() {
        return "This is API response for portfolio";
    }

    @Operation(summary = "AI 커버레터 데이터 조회", description = "AI 커버레터 데이터를 반환합니다.")
    @GetMapping("/ai_coverletter")
    public String getAiCoverLetterData() {
        return "This is API response for AI coverletter";
    }

    @Operation(summary = "AI 면접 데이터 조회", description = "AI 면접 데이터를 반환합니다.")
    @GetMapping("/ai_interview")
    public String getAiInterviewData() {
        return "This is API response for AI interview";
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃 처리(아직 구현 전이어서 임의로 해두었습니다)")
    @GetMapping("/logout")
    public String logout() {
        return "Logout successful";
    }

    @Operation(summary = "입력 페이지 데이터 조회", description = "입력 페이지의 데이터를 반환합니다.")
    @GetMapping("/input")
    public String getInputPageData() {
        return "This is API response for input page";
    }
}
