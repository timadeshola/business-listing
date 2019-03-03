package com.business.jpa.repository;

import com.business.jpa.entity.States;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<States, Long> {

    Optional<States> findByName(String name);
}
