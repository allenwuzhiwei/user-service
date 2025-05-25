package com.nusiss.userservice.controller;

import com.nusiss.userservice.entity.Role;
import com.nusiss.userservice.service.UserRoleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRoleController.class)
public class UserRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /*@MockBean
    private UserRoleService userRoleService;*/

    /*@Test
    public void testGetUserRole_Success() throws Exception {
        // Mock the service response
        Role mockRole = new Role();
        mockRole.setId(1L);
        mockRole.setName("ADMIN");
        Mockito.when(userRoleService.getRoleByUserId(1L)).thenReturn(Optional.of(mockRole));

        // Perform the GET request
        mockMvc.perform(get("/user-roles/user/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    public void testGetUserRole_NotFound() throws Exception {
        // Mock the service response
        Mockito.when(userRoleService.getRoleByUserId(anyLong())).thenReturn(Optional.empty());

        // Perform the GET request
        mockMvc.perform(get("/user-roles/user/{userId}", 2L))
                .andExpect(status().isNotFound());
    }
*/
    /*@Test
    public void testCreateUserRole_Success() throws Exception {
        // No need to mock the service since it returns void

        // Perform the POST request
        mockMvc.perform(post("/user-roles/create")
                        .param("roleId", "1")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }*/
}