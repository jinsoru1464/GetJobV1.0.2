package com.example.GetJobV101.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
