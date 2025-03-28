/*
package com.example.GetJobV101.interfacefolder;

import com.example.GetJobV101.helper.constants.SocialLoginType;
import com.example.GetJobV101.service.social.GoogleOauth;

public interface SocialOauth {
    String getOauthRedirectURL();
    String requestAccessToken(String code);

    default SocialLoginType type() {
        if (this instanceof GoogleOauth) {
            return SocialLoginType.GOOGLE;
        } else if (this instanceof NaverOauth) {
            return SocialLoginType.NAVER;
        } else if (this instanceof KakaoOauth) {
            return SocialLoginType.KAKAO;
        } else {
            return null;
        }
    }
}
*/
