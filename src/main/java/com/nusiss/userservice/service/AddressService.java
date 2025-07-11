package com.nusiss.userservice.service;

import com.nusiss.userservice.entity.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    public Optional<Address> getAddressById(Integer addressId);

    public List<Address> getAddressesByUserId(Integer userId);

    public Address createAddress(Address address);

    public Address updateAddress(Integer addressId, Address newAddressData);

    public void deleteAddress(Integer addressId);
}
