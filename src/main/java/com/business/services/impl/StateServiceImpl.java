package com.business.services.impl;

import com.business.core.exceptions.CustomException;
import com.business.jpa.entity.States;
import com.business.jpa.repository.StateRepository;
import com.business.model.request.StateRequest;
import com.business.services.StateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class StateServiceImpl implements StateService {

    private StateRepository stateRepository;

    public StateServiceImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public States createState(StateRequest request) {
        Optional<States> countryOptional = stateRepository.findByName(request.getName());
        if (countryOptional.isPresent()) {
            throw new CustomException("State already exist", HttpStatus.CONFLICT);
        }
        States state = new States();
        state.setName(request.getName());
        state.setStateCode(request.getStateCode());

        return stateRepository.save(state);
    }

    @Override
    public Page<States> findAllStates(Pageable pageable) {
        return stateRepository.findAll(pageable);
    }

    @Override
    public States updateState(StateRequest request, Long stateId) {
        Optional<States> countryOptional = stateRepository.findById(stateId);
        if (countryOptional.isPresent()) {
            States state = countryOptional.get();
            if (request.getName() != null) {
                state.setName(request.getName());
            }
            if (request.getStateCode() != null) {
                state.setName(request.getStateCode());
            }
            return stateRepository.save(state);
        }
        throw new CustomException("State does not exist", HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<States> findStateByName(String name) {
        return stateRepository.findByName(name);
    }

    @Override
    @Transactional
    public void deleteStateById(Long stateId) {
        stateRepository.deleteById(stateId);
    }
}
