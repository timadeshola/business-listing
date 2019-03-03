package com.business.repository;

import com.business.jpa.entity.Role;
import com.business.jpa.repository.RoleRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @Before
    public void setUp() {
        role = new Role();
    }

    @Test
    public void testCreateRoleRepository() {
        role = Role.builder().id(1L).name("Administrator").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        role = roleRepository.save(role);
        assertThat(role.getName()).isEqualTo("Administrator");
    }

    @Test
    public void testUpdateRoleRepository() {
        role = Role.builder().id(1L).name("Manager").dateCreated(new Timestamp(System.currentTimeMillis())).build();
        role = roleRepository.save(role);
        assertThat(role.getName()).isEqualTo("Manager");
        Optional<Role> roleOptional = roleRepository.findById(1L);
        role = roleOptional.get();
        role.setName("CJN");
        role = roleRepository.save(role);
        assertThat(role.getName()).isEqualTo("CJN");
    }

    @Test
    public void fetchFromRoleRepository() {
        Optional<Role> optionalRole = roleRepository.findRoleByName("ADMIN");
        if(optionalRole.isPresent()) {
            role = optionalRole.get();
            Java6Assertions.assertThat(role.getName()).isEqualTo("ADMIN");
            Java6Assertions.assertThat(role.getDateCreated()).isNotNull();
        }
    }

    @Test
    public void deleteRoleRepository() {
        Optional<Role> optionalRole = roleRepository.findRoleByName("ADMIN");
        if(optionalRole.isPresent()) {
            role = optionalRole.get();
            roleRepository.delete(role);
            Java6Assertions.assertThat(role).isNotNull();
        }
    }
}
