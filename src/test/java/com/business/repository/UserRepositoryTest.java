package com.business.repository;

import com.business.jpa.entity.User;
import com.business.jpa.repository.UserRepository;
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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void createUserTest() {
        user = User.builder().id(1L).firstName("Mayowa").lastName("Afolabi").username("mayor").password("password").email("mayor@example.com").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        user = userRepository.save(user);
        assertThat(user.getUsername()).isEqualTo("mayor");
    }

    @Test
    public void updateUserTest() {
        user = User.builder().id(1L).firstName("Marvin").lastName("Afolabi").username("mayor").password("password").email("mayor@example.com").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        user = userRepository.save(user);
        assertThat(user.getFirstName()).isEqualTo("Marvin");
        Optional<User> userOptional = userRepository.findByEmail("mayor@example.com");
        user = userOptional.get();
        user.setLastName("Gay");
        user = userRepository.save(user);
        assertThat(user.getLastName()).isEqualTo("Gay");
    }

    @Test
    public void fetchFromUserRepository() {
        Optional<User> optionalUser = userRepository.findUserByUsername("timadeshola");
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
            assertThat(user.getUsername()).isEqualTo("timadeshola");
            assertThat(user.getDateCreated()).isNotNull();
        }
    }

    @Test
    public void deleteUserRepository() {
        Optional<User> optionalUser = userRepository.findUserByUsername("timadeshola");
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
            userRepository.delete(user);
            assertThat(user).isNotNull();
        }
    }
}
