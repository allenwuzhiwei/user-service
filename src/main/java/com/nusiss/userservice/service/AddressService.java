package com.nusiss.userservice.service;

import com.nusiss.userservice.entity.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    public Optional<Address> getAddressById(Long addressId);

    public List<Address> getAddressesByUserId(Long userId);

    public Address createAddress(Address address);

    public Address updateAddress(Long addressId, Address newAddressData);

    public void deleteAddress(Long addressId);
}
