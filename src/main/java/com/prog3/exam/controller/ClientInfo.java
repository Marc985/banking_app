package com.prog3.exam.controller;

import com.prog3.exam.entity.Client;
import com.prog3.exam.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientInfo {
    @Autowired
    ClientRepository clientRepository;

@GetMapping("/info")
    public  Client getUserInfo(@AuthenticationPrincipal OAuth2User principal){
        String name=principal.getAttribute("name");
        String email=principal.getAttribute("email");
        String id=principal.getAttribute("sub");
        String pic=principal.getAttribute("picture");
      Client client=new Client();
      client.setIdClient(id);
      client.setEmail(email);
      client.setName(name);
      client.setPic(pic);
        return client;
}


}
