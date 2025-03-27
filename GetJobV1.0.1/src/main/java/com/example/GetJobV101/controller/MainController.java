package com.example.GetJobV101.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Controller
public class MainController {

    @GetMapping("/mainpage")
    public String mainPage() {
        return "mainpage";
    }

    @GetMapping("/portfolio")
    public String portfolioPage() {
        return "portfoliopage";
    }

    @GetMapping("/ai_coverletter")
    public String aiServicePage() {
        return "aiservice";
    }

    @GetMapping("/ai_interview")
    public String interviewPage() {
        return "interview";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

    @GetMapping("/input")
    public String inputPage() {
        return "inputpage";
    }



}
