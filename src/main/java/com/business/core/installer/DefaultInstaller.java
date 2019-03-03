package com.business.core.installer;

import com.business.jpa.entity.*;
import com.business.jpa.repository.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
//@Profile("dev")
public class DefaultInstaller implements ApplicationListener<ContextRefreshedEvent> {

    private Boolean alreadySetup = Boolean.FALSE;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private BusinessRepository businessRepository;
    private CategoryRepository categoryRepository;
    private AddressRepository addressRepository;
    private CountryRepository countryRepository;
    private StateRepository stateRepository;

    public DefaultInstaller(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, BusinessRepository businessRepository, CategoryRepository categoryRepository, AddressRepository addressRepository, CountryRepository countryRepository, StateRepository stateRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.businessRepository = businessRepository;
        this.categoryRepository = categoryRepository;
        this.addressRepository = addressRepository;
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
    }


    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        final Role admin = createRoleIfNotFound("ADMIN");
        final Role user = createRoleIfNotFound("USER");
        final Role guest = createRoleIfNotFound("GUEST");

        final Set<Role> adminRole = new HashSet<>(Arrays.asList(admin, user, guest));
        final Set<Role> userRole = new HashSet<>(Collections.singletonList(user));
        final Set<Role> guestRole = new HashSet<>(Collections.singletonList(guest));

        createUserIfNotFound("timadeshola", "timadeshola@gmail.com", "John", "Adeshola", "Password@123",  adminRole);
        createUserIfNotFound("user", "user@example.com", "Paul", "Essien", "Password@123", userRole);
        Category category01 = new Category("COMMUNICATION");
        Category category02 = new Category("REVENUE");
        Category category03 = new Category("FINANCE");
        List<Category> categories = Arrays.asList(category01, category02, category03);
        categoryRepository.saveAll(categories);

        States lagos = new States("Lagos", "LG");
        States portHarcourt = new States("Rivers", "PH");
        States abuja = new States("Abuja", "ABJ");
        List<States> states = Arrays.asList(lagos, portHarcourt, abuja);
        stateRepository.saveAll(states);


        Country nigeria = new Country("Nigeria", "NGR", new HashSet<>(states) );
        countryRepository.save(nigeria);


        Address address01 = new Address("No 1", "Adeola Odeku Street", "Victoria Island", lagos, nigeria );
        Address address02 = new Address("Plot 202", "Trans-Amadi Rd, Trans Amadi Street", "Port Harcourt", lagos, nigeria);
        Address address03 = new Address("Zone 5", "Mississippi Street Mississippi St", "Maitama", lagos, nigeria);
        List<Address> addresses = Arrays.asList(address01, address02, address03);
        addressRepository.saveAll(addresses);

        createBusinessIfNotExist("MTN Nigeria", "MTN Nigeria", address01, Collections.singleton(category01));
        createBusinessIfNotExist("Federal Inland Revenue", "Nigeria Federal Inland Revenue Service", address02, Collections.singleton(category02));
        createBusinessIfNotExist("Stanbic Bank Plc", "Stanbic Bank Pls", address01, Collections.singleton(category03));

        alreadySetup = Boolean.TRUE;
    }

    @Transactional
    protected Role createRoleIfNotFound(final String name) {
        Optional<Role> roleCheck = roleRepository.findRoleByName(name);
        if (roleCheck.isPresent()) {
            return roleCheck.get();
        }
        Role role = new Role();
        role.setName(name);
        role.setStatus(true);
        roleRepository.save(role);
        return role;
    }

    @Transactional
    protected void createUserIfNotFound(final String username, final String email, final String firstName, final String lastName, final String password, final Set<Role> roles) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isPresent()) {
            return;
        }
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setStatus(true);
        user.setRoles(roles);
        user.setStatus(true);
        userRepository.save(user);
    }

    @Transactional
    protected void createBusinessIfNotExist(String name, String description, Address address, Set<Category> categories) {
        Optional<Business> businessOptional = businessRepository.findByName(name);
        if(businessOptional.isPresent()) {
            return;
        }
        Business business = new Business();
        business.setName(name);
        business.setDescription(description);
        business.setAddress(address);
        business.setCategories(categories);
        businessRepository.save(business);
    }
}
