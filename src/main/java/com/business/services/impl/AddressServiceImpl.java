package com.business.services.impl;

import com.business.core.exceptions.CustomException;
import com.business.jpa.entity.Address;
import com.business.jpa.entity.Country;
import com.business.jpa.entity.States;
import com.business.jpa.repository.AddressRepository;
import com.business.jpa.repository.CountryRepository;
import com.business.jpa.repository.StateRepository;
import com.business.model.request.AddressRequest;
import com.business.services.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    private AddressRepository addressRepository;
    private CountryRepository countryRepository;
    private StateRepository stateRepository;

    public AddressServiceImpl(AddressRepository addressRepository, CountryRepository countryRepository, StateRepository stateRepository) {
        this.addressRepository = addressRepository;
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
    }

    @Override
    public Address createAddress(AddressRequest request) {
        Optional<Address> addressOptional = addressRepository.findByHouseNo(request.getHouseNo());
        if(addressOptional.isPresent()) {
            Address fetchAddress = addressOptional.get();
            if(fetchAddress.getHouseNo().equals(request.getHouseNo()) && fetchAddress.getStreet().equals(request.getStreet()) && fetchAddress.getCity().equals(request.getCity())) {
                throw new CustomException("Address already exist", HttpStatus.IM_USED);
            }
        }
        Address address = new Address();
        address.setHouseNo(request.getHouseNo());
        address.setCity(request.getCity());
        address.setStreet(request.getStreet());
        Optional<States> states = stateRepository.findById(request.getStateId());
        states.ifPresent(address::setState);
        Optional<Country> countries = countryRepository.findById(request.getCountryId());
        countries.ifPresent(address::setCountry);

        return addressRepository.save(address);
    }

    @Override
    public Page<Address> findAllAddress(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    @Override
    public Address updateAddress(AddressRequest request, Long addressId) {
        Optional<Address> countryOptional = addressRepository.findById(addressId);
        if (countryOptional.isPresent()) {
            Address address = countryOptional.get();
            if (request.getHouseNo() != null) {
                address.setHouseNo(request.getHouseNo());
            }
            if (request.getCity() != null) {
                address.setCity(request.getCity());
            }
            if (request.getStreet() != null) {
                address.setStreet(request.getStreet());
            }

            Optional<States> states = stateRepository.findById(request.getStateId());
            states.ifPresent(address::setState);
            Optional<Country> countries = countryRepository.findById(request.getCountryId());
            countries.ifPresent(address::setCountry);
            return addressRepository.save(address);
        }
        throw new CustomException("Address does not exist", HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Address> findAddressByName(String houseNo) {
        return addressRepository.findByHouseNo(houseNo);
    }

    @Override
    @Transactional
    public void deleteAddressById(Long addressId) {
        addressRepository.deleteById(addressId);
    }
}
