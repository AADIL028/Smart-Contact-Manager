package com.scm.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {
    public static String getEmailOfLoggedinUser(Authentication authentication) {

        if (authentication instanceof OAuth2AuthenticationToken) {

            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            var clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oAuthUser = (OAuth2User) authentication.getPrincipal();

            String username = "";
            if (clientId.equalsIgnoreCase("google")) {
                // google
                username = oAuthUser.getAttribute("email").toString();

            } else if (clientId.equalsIgnoreCase("github")) {
                // github
                username = oAuthUser.getAttribute("email") != null ? oAuthUser.getAttribute("email").toString()
                        : oAuthUser.getAttribute("login").toString() + "@gmail.com";

            }
            return username;

        } else {
            return authentication.getName();
        }
    }

    public static String getLinkForEmailVarification(String emailToken) {
        return "http://localhost:8081/auth/verify-email?token=" + emailToken;
    }
}
