package com.business.services;

import com.business.jpa.entity.Business;
import com.business.model.request.BusinessRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BusinessService {

    Business createBusiness(BusinessRequest request);

    Page<Business> findAllBusiness(Pageable pageable);

    Business updateBusiness(BusinessRequest request, Long countryId);

    Optional<Business> findBusinessByName(String name);

    void deleteBusinessById(Long countryId);

}
