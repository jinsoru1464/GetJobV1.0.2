package com.example.GetJobV101.service;

import com.example.GetJobV101.dto.PortfolioDto;
import com.example.GetJobV101.entity.Portfolio;
import com.example.GetJobV101.repository.PortfolioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    // ✅ 포트폴리오 저장 메소드
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

        // ✅ 이미지 경로 저장
        portfolio.setImagePaths(dto.getImagePaths());

        return portfolioRepository.save(portfolio);
    }

    // ✅ 이미지 추가 저장 메소드
    public void addImagePaths(Long portfolioId, List<String> imagePaths) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("포트폴리오를 찾을 수 없습니다."));
        portfolio.getImagePaths().addAll(imagePaths);
        portfolioRepository.save(portfolio);
    }

    // ✅ 포트폴리오 목록 조회 메소드
    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }

    // ✅ 단일 포트폴리오 조회 메소드
    public Optional<Portfolio> getPortfolioById(Long id) {
        return portfolioRepository.findById(id);
    }

    // ✅ 포트폴리오 삭제 메소드
    public void deletePortfolio(Long id) {
        if (portfolioRepository.existsById(id)) {
            portfolioRepository.deleteById(id);
        } else {
            throw new RuntimeException("삭제하려는 포트폴리오가 존재하지 않습니다.");
        }
    }
    // ✅ 포트폴리오 수정 메소드
    public Portfolio updatePortfolio(Long id, PortfolioDto dto) {
        // 기존 포트폴리오를 조회하여 존재 여부 확인
        Optional<Portfolio> existingPortfolioOpt = portfolioRepository.findById(id);
        if (existingPortfolioOpt.isPresent()) {
            Portfolio existingPortfolio = existingPortfolioOpt.get();

            // 제목, 날짜, 인원, 스킬, 역할 등 업데이트
            existingPortfolio.setTitle(dto.getTitle());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            existingPortfolio.setStartDate(LocalDate.parse(dto.getStartDate(), formatter));
            existingPortfolio.setEndDate(LocalDate.parse(dto.getEndDate(), formatter));

            existingPortfolio.setTeamSize(dto.getTeamSize());
            existingPortfolio.setSkills(dto.getSkills());
            existingPortfolio.setRole(dto.getRole());

            // 설명 업데이트
            existingPortfolio.setDescriptions(dto.getDescriptions());

            // 이미지 경로 업데이트
            existingPortfolio.setImagePaths(dto.getImagePaths());

            // 수정된 포트폴리오 저장
            return portfolioRepository.save(existingPortfolio);
        } else {
            throw new RuntimeException("수정하려는 포트폴리오가 존재하지 않습니다.");
        }
    }


}
