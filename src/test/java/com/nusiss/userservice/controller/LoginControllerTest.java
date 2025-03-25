package com.nusiss.userservice.controller;

import com.nusiss.userservice.entity.User;
import com.nusiss.userservice.service.JwtTokenService;
import com.nusiss.userservice.service.LoginService;
import com.nusiss.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LoginService loginService;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    public void testLogin_Success() throws Exception {
        String token = "dummyToken";

        when(loginService.login("testUser", "testPassword")).thenReturn(token);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"testPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successfully"))
                .andExpect(jsonPath("$.data").value(token));

        verify(loginService, times(1)).login("testUser", "testPassword");
    }


    @Test
    public void testValidateToken_ValidToken() throws Exception {
        when(loginService.validateToken("validToken")).thenReturn(true);

        mockMvc.perform(post("/validateToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"validToken\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(loginService, times(1)).validateToken("validToken");
    }

    @Test
    public void testValidateToken_InvalidToken() throws Exception {
        when(loginService.validateToken("invalidToken")).thenReturn(false);

        mockMvc.perform(post("/validateToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"invalidToken\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));

        verify(loginService, times(1)).validateToken("invalidToken");
    }

    @Test
    public void testGetCurrentUserInfo() throws Exception {
        User user = new User();
        user.setUsername("testUser");

        when(userService.getCurrentUserInfo("validAuthToken")).thenReturn(user);

        mockMvc.perform(post("/getCurrentUserInfo")
                        .header("authToken", "validAuthToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Retrieve successfully"))
                .andExpect(jsonPath("$.data.username").value("testUser"));

        verify(userService, times(1)).getCurrentUserInfo("validAuthToken");
    }
}