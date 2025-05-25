package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.entity.User;
import com.nusiss.userservice.service.JwtTokenService;
import com.nusiss.userservice.service.LoginService;
import com.nusiss.userservice.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@CrossOrigin(origins = "http://localhost:5000")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<String>> login(HttpServletResponse response, @RequestBody Map<String, String> requestParams) {
        String username = requestParams.get("username");
        String password = requestParams.get("password");

        String token = loginService.login(username, password);
        //jwtTokenService.addTokenToCookie(response, token);

        return ResponseEntity.status(200).body(new ApiResponse<>(true, "Login successfully", token));
    }

    @RequestMapping(value = "/validateToken", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<Boolean>> validateToken(@RequestBody Map<String, String> requestParams) {
        String token = requestParams.get("Authorization");
        boolean isValidated = loginService.validateToken(token);

        return ResponseEntity.status(200).body(new ApiResponse<>(true, "valide successfully", isValidated));
    }

    @RequestMapping(value = "/getCurrentUserInfo", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<User>> getCurrentUserInfo(@RequestHeader("authToken") String authToken) {

        User user =  userService.getCurrentUserInfo(authToken);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, "Retrieve successfully", user));

    }

}
