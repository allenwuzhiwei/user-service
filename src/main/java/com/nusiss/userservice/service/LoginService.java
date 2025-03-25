package com.nusiss.userservice.service;

public interface LoginService {

    public String login(String username, String password);

    public boolean validateToken(String token);

}