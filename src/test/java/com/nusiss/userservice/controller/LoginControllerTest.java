package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
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
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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


    /*@Test
    public void testValidateToken_ValidToken() throws Exception {
        when(loginService.validateToken("validToken")).thenReturn(true);

        mockMvc.perform(post("/validateToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"validToken\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        verify(loginService, times(1)).validateToken("validToken");
    }*/

    /*@Test
    public void testValidateToken_InvalidToken() throws Exception {
        when(loginService.validateToken("invalidToken")).thenReturn(false);

        mockMvc.perform(post("/validateToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"invalidToken\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));

        verify(loginService, times(1)).validateToken("invalidToken");
    }*/

    /*\@Test
    public void testGetCurrentUserInfo() throws Exception {
        User user = new User();
        user.setUsername("testUser");

        when(userService.getCurrentUserInfo("validAuthToken")).thenReturn(user);

        mockMvc.perform(post("/getCurrentUserInfo")
                        .header("Authorization", "validAuthToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Retrieve successfully"))
                .andExpect(jsonPath("$.data.username").value("testUser"));

        verify(userService, times(1)).getCurrentUserInfo("validAuthToken");
    }*/

    @Test
    public void testValidateToken_ValidToken() throws Exception {
        when(loginService.validateToken("validToken")).thenReturn(true);

        mockMvc.perform(post("/validateToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"Authorization\":\"validToken\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Validate token"))
                .andExpect(jsonPath("$.data").value(true));

        verify(loginService, times(1)).validateToken("validToken");
    }

    @Test
    public void testValidateToken_InvalidToken() throws Exception {
        when(loginService.validateToken("invalidToken")).thenReturn(false);

        mockMvc.perform(post("/validateToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"Authorization\":\"invalidToken\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Validate token"))
                .andExpect(jsonPath("$.data").value(false));

        verify(loginService, times(1)).validateToken("invalidToken");
    }

    @Test
    public void testGetCurrentUserInfo() throws Exception {
        User user = new User();
        user.setUsername("testUser");

        when(userService.getCurrentUserInfo("validAuthToken")).thenReturn(user);
        URI uri = URI.create("/getCurrentUserInfo");
        mockMvc.perform(get(uri)
                        .header("Authorization", "validAuthToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Retrieve successfully"))
                .andExpect(jsonPath("$.data.username").value("testUser"));

        verify(userService, times(1)).getCurrentUserInfo("validAuthToken");
    }

    @Test
    void testGetCurrentUserInfoWithTokenString_ReturnsUser() {
        String authToken = "valid-token";
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setUsername("john.doe");

        when(userService.getCurrentUserInfo(authToken)).thenReturn(mockUser);

        ResponseEntity<ApiResponse<User>> response = loginController.getCurrentUserInfoWithTokenString(authToken);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Retrieve successfully", response.getBody().getMessage());
        assertEquals(mockUser, response.getBody().getData());

        verify(userService, times(1)).getCurrentUserInfo(authToken);
    }
}