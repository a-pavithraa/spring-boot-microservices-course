package com.sivalabs.bookstore.orders;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import static io.restassured.RestAssured.authentication;

public class MockOAuth2UserContextFactory implements WithSecurityContextFactory<WithCognitoMockUser> {

    public SecurityContext createSecurityContext(WithCognitoMockUser withAwsCognitoUser) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", withAwsCognitoUser.username());
        attributes.put("cognito:username", withAwsCognitoUser.username());


        List<GrantedAuthority> authorities = Collections.singletonList(
                new OAuth2UserAuthority("ROLE_USER", attributes));


        SecurityContext context = SecurityContextHolder.createEmptyContext();
        OAuth2User user = new DefaultOAuth2User(authorities, attributes, "sub");

        var authentication = new OAuth2AuthenticationToken( user,authorities, "client-registration-id");
        System.out.println("Authentication details");
        System.out.println(authentication.getPrincipal());

        context.setAuthentication(authentication);
        return context;

    }
}
