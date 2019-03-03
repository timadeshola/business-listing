package com.business.services;

import com.business.jpa.entity.States;
import com.business.model.request.StateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StateService {

    States createState(StateRequest request);

    Page<States> findAllStates(Pageable pageable);

    States updateState(StateRequest request, Long stateId);

    Optional<States> findStateByName(String name);

    void deleteStateById(Long stateId);

}
