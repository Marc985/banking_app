package com.prog3.exam.service;

import com.prog3.exam.entity.Client;
import com.prog3.exam.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class ClientService extends  DefaultOAuth2UserService {
    @Autowired
    ClientRepository clientRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user= super.loadUser(userRequest);
        String name=user.getAttribute("name");
        String email=user.getAttribute("email");
        String pic=user.getAttribute("picture");
        String id=user.getAttribute("sub");
        Client client=new Client();
        client.setIdClient(id);
        client.setName(name);
        client.setPic(pic);
        client.setEmail(email);
        try {
            clientRepository.saveOrUpdate(client);
        } catch (Exception e) {

            throw new OAuth2AuthenticationException(e.getMessage());
        }

        return user;

    }
}
