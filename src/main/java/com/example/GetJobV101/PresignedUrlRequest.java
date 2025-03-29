package com.example.GetJobV101;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresignedUrlRequest {
    @NotEmpty(message = "누락될 수 없습니다. 확장자까지 함께 입력해주세요")
    private String imageName;  // Presigned URL 요청 시 이미지 이름
}
