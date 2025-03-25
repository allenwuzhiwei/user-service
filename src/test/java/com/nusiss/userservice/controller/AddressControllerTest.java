package com.nusiss.userservice.controller;

import com.nusiss.userservice.entity.Address;
import com.nusiss.userservice.service.AddressService;
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

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(addressController).build();
    }

    @Test
    public void testCreateAddress() throws Exception {
        Address address = new Address();
        address.setId(1L);
        address.setStreet("123 Main St");
        address.setCity("City");
        address.setState("State");
        address.setUserId(1L);
        address.setCreateUser("user1");

        when(addressService.createAddress(any(Address.class))).thenReturn(address);

        mockMvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"street\":\"123 Main St\",\"city\":\"City\",\"state\":\"State\",\"createUser\":\"user1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.street").value("123 Main St"));

        verify(addressService, times(1)).createAddress(any(Address.class));
    }

    @Test
    public void testGetAddressById() throws Exception {
        Address address = new Address();
        address.setId(1L);
        address.setStreet("123 Main St");

        when(addressService.getAddressById(1L)).thenReturn(Optional.of(address));

        mockMvc.perform(get("/api/addresses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.street").value("123 Main St"));

        verify(addressService, times(1)).getAddressById(1L);
    }

    @Test
    public void testGetAddressById_NotFound() throws Exception {
        when(addressService.getAddressById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/addresses/1"))
                .andExpect(status().isNotFound());

        verify(addressService, times(1)).getAddressById(1L);
    }

    @Test
    public void testGetAddressesByUserId() throws Exception {
        Address address1 = new Address();
        address1.setId(1L);
        address1.setStreet("123 Main St");

        Address address2 = new Address();
        address2.setId(2L);
        address2.setStreet("456 Main St");

        when(addressService.getAddressesByUserId(1L)).thenReturn(Arrays.asList(address1, address2));

        mockMvc.perform(get("/api/addresses/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(addressService, times(1)).getAddressesByUserId(1L);
    }

    @Test
    public void testGetAddressesByUserId_NotFound() throws Exception {
        when(addressService.getAddressesByUserId(1L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/addresses/user/1"))
                .andExpect(status().isNotFound());

        verify(addressService, times(1)).getAddressesByUserId(1L);
    }

    @Test
    public void testUpdateAddress() throws Exception {
        Address updatedAddress = new Address();
        updatedAddress.setId(1L);
        updatedAddress.setStreet("123 Main St Updated");

        when(addressService.updateAddress(eq(1L), any(Address.class))).thenReturn(updatedAddress);

        mockMvc.perform(put("/api/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"street\":\"123 Main St Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.street").value("123 Main St Updated"));

        verify(addressService, times(1)).updateAddress(eq(1L), any(Address.class));
    }

    @Test
    public void testDeleteAddress() throws Exception {
        doNothing().when(addressService).deleteAddress(1L);

        mockMvc.perform(delete("/api/addresses/1"))
                .andExpect(status().isNoContent());

        verify(addressService, times(1)).deleteAddress(1L);
    }
}