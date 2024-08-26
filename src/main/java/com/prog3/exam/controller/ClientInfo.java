package com.prog3.exam.controller;

import com.prog3.exam.entity.Client;
import com.prog3.exam.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

@RestController
public class ClientInfo {
    @Autowired
    ClientRepository clientRepository;

    @GetMapping("/info")
    public Client getUserInfo(Principal principal) {
        // Assuming principal is an instance of OAuth2User or JwtAuthenticationToken
       if (principal instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) principal;
           String id=jwtToken.getToken().getClaim("sub");
           String name= jwtToken.getToken().getClaim("name");
           String email= jwtToken.getToken().getClaim("email");
           String pic= jwtToken.getToken().getClaim("picture");
           Client client = new Client();
           client.setIdClient(id);
           client.setEmail(email);
           client.setName(name);
           client.setPic(pic);

           clientRepository.saveOrUpdate(client);
           return client;
        }
       return null;
    }



}
