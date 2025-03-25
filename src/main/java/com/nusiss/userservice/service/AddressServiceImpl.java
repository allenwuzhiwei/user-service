package com.nusiss.userservice.service;

import com.nusiss.userservice.entity.Address;
import com.nusiss.userservice.dao.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    // 根据 addressId 查询 Address
    public Optional<Address> getAddressById(Long addressId) {
        return addressRepository.findById(addressId);
    }

    // 根据 userId 查询 Address 列表
    public List<Address> getAddressesByUserId(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    // 新增 Address
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    // 更新 Address
    @Transactional
    public Address updateAddress(Long addressId, Address newAddressData) {
        return addressRepository.findById(addressId)
                .map(address -> {
                    address.setStreet(newAddressData.getStreet());
                    address.setCity(newAddressData.getCity());
                    address.setState(newAddressData.getState());
                    address.setUpdateUser(newAddressData.getUpdateUser());
                    address.setUpdateDatetime(newAddressData.getUpdateDatetime());
                    return addressRepository.save(address);
                })
                .orElseThrow(() -> new IllegalArgumentException("Address with ID " + addressId + " not found"));
    }

    // 根据 addressId 删除 Address
    public void deleteAddress(Long addressId) {
        if (addressRepository.existsById(addressId)) {
            addressRepository.deleteById(addressId);
        } else {
            throw new IllegalArgumentException("Address with ID " + addressId + " not found");
        }
    }
}