package com.save_help.Save_Help.user.oauth2;

import com.save_help.Save_Help.user.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public OAuth2SuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = extractEmail(oAuth2User);

        // JWT 발급
        String token = jwtUtil.generateToken(email);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"token\":\"" + token + "\"}");
    }

    private String extractEmail(OAuth2User oAuth2User) {
        if (oAuth2User.getAttributes().containsKey("kakao_account")) {
            return (String) ((java.util.Map)oAuth2User.getAttributes().get("kakao_account")).get("email");
        } else if (oAuth2User.getAttributes().containsKey("response")) {
            return (String) ((java.util.Map)oAuth2User.getAttributes().get("response")).get("email");
        } else {
            return (String) oAuth2User.getAttributes().get("email");
        }
    }
}
