package com.nusiss.userservice.service;

import com.nusiss.userservice.dao.AddressRepository;
import com.nusiss.userservice.entity.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAddressById_Success() {
        Address mockAddress = new Address();
        mockAddress.setId(1);
        mockAddress.setStreet("123 Main St");
        when(addressRepository.findById(1)).thenReturn(Optional.of(mockAddress));

        Optional<Address> result = addressService.getAddressById(1);

        assertTrue(result.isPresent());
        assertEquals("123 Main St", result.get().getStreet());
        verify(addressRepository, times(1)).findById(1);
    }

    @Test
    void testGetAddressesByUserId_Success() {
        Address address1 = new Address();
        address1.setId(1);
        address1.setStreet("123 Main St");

        Address address2 = new Address();
        address2.setId(2);
        address2.setStreet("456 Elm St");

        when(addressRepository.findByUserId(1)).thenReturn(Arrays.asList(address1, address2));

        List<Address> result = addressService.getAddressesByUserId(1);

        assertEquals(2, result.size());
        verify(addressRepository, times(1)).findByUserId(1);
    }

    @Test
    void testCreateAddress_Success() {
        Address newAddress = new Address();
        newAddress.setStreet("789 Oak St");
        when(addressRepository.save(any(Address.class))).thenReturn(newAddress);

        Address result = addressService.createAddress(newAddress);

        assertNotNull(result);
        assertEquals("789 Oak St", result.getStreet());
        verify(addressRepository, times(1)).save(newAddress);
    }

    @Test
    void testUpdateAddress_NotFound() {
        when(addressRepository.findById(1)).thenReturn(Optional.empty());

        Address updatedData = new Address();
        updatedData.setStreet("New St");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                addressService.updateAddress(1, updatedData));

        assertEquals("Address with ID 1 not found", exception.getMessage());
        verify(addressRepository, times(1)).findById(1);
        verify(addressRepository, times(0)).save(any(Address.class));
    }

    @Test
    void testDeleteAddress_Success() {
        when(addressRepository.existsById(1)).thenReturn(true);

        addressService.deleteAddress(1);

        verify(addressRepository, times(1)).existsById(1);
        verify(addressRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAddress_NotFound() {
        when(addressRepository.existsById(1)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                addressService.deleteAddress(1));

        assertEquals("Address with ID 1 not found", exception.getMessage());
        verify(addressRepository, times(1)).existsById(1);
        verify(addressRepository, times(0)).deleteById(anyInt());
    }
}