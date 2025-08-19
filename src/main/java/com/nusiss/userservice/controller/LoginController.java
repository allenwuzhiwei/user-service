package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.entity.Role;
import com.nusiss.userservice.entity.User;
import com.nusiss.userservice.service.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

//@CrossOrigin(origins = "http://localhost:5000")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

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

        return ResponseEntity.status(200).body(new ApiResponse<>(true, "Validate token", isValidated));
    }

    @RequestMapping(value = "/getCurrentUserInfo", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<User>> getCurrentUserInfo(@RequestHeader("Authorization") String authToken) {

        User user =  userService.getCurrentUserInfo(authToken);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, "Retrieve successfully", user));

    }

    @RequestMapping(value = "/getCurrentUserInfoWithTokenString", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<User>> getCurrentUserInfoWithTokenString(@RequestParam("authToken") String authToken) {

        User user =  userService.getCurrentUserInfo(authToken);
        return ResponseEntity.status(200).body(new ApiResponse<>(true, "Retrieve successfully", user));

    }

    @RequestMapping(value = "/checkPermission", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<Boolean>> checkPermission(@RequestParam("authToken") String authToken
            ,@RequestParam("path") String path) {
        User user =  userService.getCurrentUserInfo(authToken);
        Integer roleId = roleService.getRoleByUserId(user.getUserId());
        // do check for admin user
        if (roleId == 5) {
            return ResponseEntity.status(200).body(new ApiResponse<>(true, "Retrieve successfully", true));

        }
        Set<String> permissions =  permissionService.findPermissionsByUserId(user.getUserId());
        boolean matchFound = permissions.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
        return ResponseEntity.status(200).body(new ApiResponse<>(true, "Retrieve successfully", matchFound));

    }

}
