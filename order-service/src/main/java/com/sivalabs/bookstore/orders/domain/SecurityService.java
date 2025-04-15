package com.sivalabs.bookstore.orders.domain;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public String getLoginUserName() {
        // return "user";
//        OAuth2AuthenticationToken authentication =
//                (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        JwtAuthenticationToken jwt = (JwtAuthenticationToken) authentication.getPrincipal();
        JwtAuthenticationToken authentication =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();

        /*
        var username = jwt.getClaimAsString("preferred_username");
        var email = jwt.getClaimAsString("email");
        var name = jwt.getClaimAsString("name");
        var token = jwt.getTokenValue();
        var authorities = authentication.getAuthorities();
        */
        //return jwt.getName();
        return jwt.getClaimAsString("username");
    }
}
