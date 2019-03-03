package com.business.services;

import com.business.jpa.entity.Address;
import com.business.model.request.AddressRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AddressService {

    Address createAddress(AddressRequest request);

    Page<Address> findAllAddress(Pageable pageable);

    Address updateAddress(AddressRequest request, Long addressId);

    Optional<Address> findAddressByName(String houseNo);

    void deleteAddressById(Long addressId);

}
