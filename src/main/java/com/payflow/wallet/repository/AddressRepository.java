package com.payflow.wallet.repository;


import com.payflow.wallet.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
