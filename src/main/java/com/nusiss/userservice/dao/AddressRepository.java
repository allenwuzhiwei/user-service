package com.nusiss.userservice.dao;

import com.nusiss.userservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    List<Address> findByUserId(Integer userId);
}