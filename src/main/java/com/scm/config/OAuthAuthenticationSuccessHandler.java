package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.repositories.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    @Autowired
    private UserRepository userRepository;

    //OAuth handler.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
            String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            
            var oAuthUser = (DefaultOAuth2User)authentication.getPrincipal();

            //common attributes.
            User user = new User();
            user.setUserId(UUID.randomUUID().toString());
            user.setRoleList(List.of(AppConstants.ROLE_USER));
            user.setEmailVarified(true);
            user.setEnabled(true);
            user.setPassword("dummy");
            
            //google attributes
            if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
                user.setEmail(oAuthUser.getAttribute("email").toString());
                user.setName(oAuthUser.getAttribute("name").toString());
                user.setProfilePic(oAuthUser.getAttribute("picture").toString());
                user.setAbout("This user is signin using google.");
                user.setProvider(Providers.GOOGLE);
                user.setProviderUserId(oAuthUser.getName());
            } 
            //github attributes
            else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){
                String email = oAuthUser.getAttribute("email") != null ? oAuthUser.getAttribute("email").toString() : oAuthUser.getAttribute("login").toString()+"@gmail.com";
                String profile = oAuthUser.getAttribute("avatar_url").toString();
                String name = oAuthUser.getAttribute("login").toString();
                String providerUserId = oAuthUser.getName();

                user.setEmail(email);
                user.setProfilePic(profile);
                user.setName(name);
                user.setProviderUserId(providerUserId);
                user.setProvider(Providers.GITHUB);
                user.setAbout("This user is signin using github.");
            }
            else{
                log.info("OAuthAuthenticationSuccessHandler: unknown provider!");
            }

            User user2 = userRepository.findByEmail(user.getEmail()).orElse(null);
            if(user2==null){
                userRepository.save(user);
            }

            new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
        }
}
