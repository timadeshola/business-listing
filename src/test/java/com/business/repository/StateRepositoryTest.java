package com.business.repository;

import com.business.jpa.entity.States;
import com.business.jpa.repository.StateRepository;
import org.assertj.core.api.Java6Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class StateRepositoryTest {

    @Autowired
    private StateRepository stateRepository;

    private States states;

    @Before
    public void setUp() {
        states = new States();
    }

    @Test
    public void testCreateStatesRepository() {
        states = States.builder().id(1L).name("Lagos").stateCode("234").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        states = stateRepository.save(states);
        assertThat(states.getName()).isEqualTo("Lagos");
    }

    @Test
    public void testUpdateStatesRepository() {
        states = States.builder().id(1L).name("Lagos").stateCode("234").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        states = stateRepository.save(states);
        assertThat(states.getName()).isEqualTo("Lagos");
        Optional<States> statesOptional = stateRepository.findByName("Lagos");
        states = statesOptional.get();
        states.setName("Kano");
        states = stateRepository.save(states);
        assertThat(states.getName()).isEqualTo("Kano");
    }

    @Test
    public void fetchFromStatesRepository() {
        Optional<States> statesOptional = stateRepository.findById(1L);
        if (statesOptional.isPresent()) {
            states = statesOptional.get();
            assertThat(states.getName()).isEqualTo("Lagos");
            assertThat(states.getDateCreated()).isNotNull();
        }
    }

    @Test
    public void deleteStatesRepository() {
        Optional<States> optionalStates = stateRepository.findById(1L);
        if (optionalStates.isPresent()) {
            states = optionalStates.get();
            stateRepository.delete(states);
            assertThat(states).isNotNull();
        }
    }
}
