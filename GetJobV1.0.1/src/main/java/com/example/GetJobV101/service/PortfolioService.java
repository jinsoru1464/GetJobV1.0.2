package com.example.GetJobV101.service;

import com.example.GetJobV101.dto.PortfolioDto;
import com.example.GetJobV101.entity.Portfolio;
import com.example.GetJobV101.repository.PortfolioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    // 🚩 수정된 부분
    public Portfolio savePortfolio(PortfolioDto dto) {
        Portfolio portfolio = new Portfolio();
        portfolio.setTitle(dto.getTitle());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        portfolio.setStartDate(LocalDate.parse(dto.getStartDate(), formatter));
        portfolio.setEndDate(LocalDate.parse(dto.getEndDate(), formatter));

        portfolio.setTeamSize(dto.getTeamSize());
        portfolio.setSkills(dto.getSkills());
        portfolio.setRole(dto.getRole());
        portfolio.setDescriptions(dto.getDescriptions());

        // ✅ 누락된 부분 (이게 없어서 DB에 저장 안됨!)
        portfolio.setImagePaths(dto.getImagePaths());

        return portfolioRepository.save(portfolio);
    }

    // 이미지 추가 저장 메소드는 유지 가능 (선택)
    public void addImagePaths(Long portfolioId, List<String> imagePaths) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("포트폴리오를 찾을 수 없습니다."));

        portfolio.getImagePaths().addAll(imagePaths);
        portfolioRepository.save(portfolio);
    }
}

