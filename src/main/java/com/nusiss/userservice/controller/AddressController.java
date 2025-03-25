package com.nusiss.userservice.controller;

import com.nusiss.userservice.entity.Address;
import com.nusiss.userservice.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:5000")
@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    @Operation(summary = "create address")
    public ResponseEntity<Address> createAddress(@RequestBody Address address) {
        Address createdAddress = addressService.createAddress(address);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "get address by id")
    public ResponseEntity<Address> getAddressById(@PathVariable("id") Long addressId) {
        Optional<Address> address = addressService.getAddressById(addressId);
        return address.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/user/{userId}")
    @Operation(summary = "get addresses by user id")
    public ResponseEntity<List<Address>> getAddressesByUserId(@PathVariable("userId") Long userId) {
        List<Address> addresses = addressService.getAddressesByUserId(userId);
        // 如果地址列表为空，返回 404
        if (addresses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update address")
    public ResponseEntity<Address> updateAddress(@PathVariable("id") Long addressId,
                                                 @RequestBody Address address) {
        Address updatedAddress = addressService.updateAddress(addressId, address);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete address")
    public ResponseEntity<Void> deleteAddress(@PathVariable("id") Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}