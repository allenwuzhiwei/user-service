package com.nusiss.userservice.controller;

import com.nusiss.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:5000")
@RestController
public class PermissionController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    public Boolean checkIfHasPermission(@RequestHeader("authToken") String authToken,
                                        @RequestParam(value = "url") String url,
                                        @RequestParam(value = "method") String method){

        return userService.hasPermission(authToken, url, method);
    }
}
