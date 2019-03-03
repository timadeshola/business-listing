package com.business.services.impl;

import com.business.core.exceptions.CustomException;
import com.business.jpa.entity.Address;
import com.business.jpa.entity.Business;
import com.business.jpa.entity.Category;
import com.business.jpa.repository.AddressRepository;
import com.business.jpa.repository.BusinessRepository;
import com.business.jpa.repository.CategoryRepository;
import com.business.model.request.BusinessRequest;
import com.business.services.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {

    private BusinessRepository businessRepository;
    private CategoryRepository categoryRepository;
    private AddressRepository addressRepository;

    public BusinessServiceImpl(BusinessRepository businessRepository, CategoryRepository categoryRepository, AddressRepository addressRepository) {
        this.businessRepository = businessRepository;
        this.categoryRepository = categoryRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Business createBusiness(BusinessRequest request) {
        Optional<Business> countryOptional = businessRepository.findByName(request.getName());
        if(countryOptional.isPresent()) {
            throw new CustomException("Business already exist", HttpStatus.CONFLICT);
        }
        Business business = new Business();
        business.setName(request.getName());
        business.setDescription(request.getDescription());

        if(request.getAddressId() != null) {
            Optional<Address> addressOptional = addressRepository.findById(request.getAddressId());
            addressOptional.ifPresent(business::setAddress);
        }else {
            throw new CustomException("Address cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if(request.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
            if(!categories.isEmpty()) {
                business.setCategories(new HashSet<>(categories));
            }else {
                throw new CustomException("Business category cannot be empty", HttpStatus.BAD_REQUEST);
            }
        }

        return businessRepository.save(business);
    }

    @Override
    public Page<Business> findAllBusiness(Pageable pageable) {
        return businessRepository.findAll(pageable);
    }

    @Override
    public Business updateBusiness(BusinessRequest request, Long businessId) {
        Optional<Business> countryOptional = businessRepository.findById(businessId);
        if(countryOptional.isPresent()) {
            Business business = countryOptional.get();
            if(request.getName() != null) {
                business.setName(request.getName());
            }
            if(request.getDescription() != null) {
                business.setDescription(request.getDescription());
            }

            if(request.getAddressId() != null) {
                Optional<Address> addressOptional = addressRepository.findById(request.getAddressId());
                addressOptional.ifPresent(business::setAddress);
            }

            if(request.getCategoryIds() != null) {
                List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
                if(!categories.isEmpty()) {
                    business.setCategories(new HashSet<>(categories));
                }
            }

            return businessRepository.save(business);
        }
        throw new CustomException("Business does not exist", HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Business> findBusinessByName(String name) {
        return businessRepository.findByName(name);
    }

    @Override
    @Transactional
    public void deleteBusinessById(Long businessId) {
        businessRepository.deleteById(businessId);
    }
}
