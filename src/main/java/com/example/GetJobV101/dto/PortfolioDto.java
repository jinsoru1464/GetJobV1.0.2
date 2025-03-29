package com.example.GetJobV101.dto;

import lombok.Data;

import java.util.List;

@Data
public class PortfolioDto {
    private String title;
    private String startDate;  // HTML에서 넘어오는 형식은 문자열
    private String endDate;    // HTML에서 넘어오는 형식은 문자열
    private int teamSize;
    private String skills;
    private String role;
    private List<String> descriptions;
    private List<String> imagePaths;


}
