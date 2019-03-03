package com.business.resources;

import com.business.core.security.JwtAuthenticationRequest;
import com.business.core.security.UserTokenState;
import com.business.core.utils.AppUtils;
import com.business.model.request.BusinessRequest;
import com.business.services.AuthenticationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class BusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationService authenticationService;

    public String accessToken = "";

    /**
     * Enter a valid username and password to be able to test
     */
    @PostConstruct
    public void authenticateTestUser() {
        UserTokenState authenticationToken = authenticationService.createAuthenticationToken(new JwtAuthenticationRequest("timadeshola", "Password@123"));
        accessToken = "Bearer " + authenticationToken.getAccess_token();
    }

    @Test
    public void createBusinessEndpointTest() throws Exception {
        BusinessRequest request = BusinessRequest.builder().name("Airtel Nigeria").description("Airtel Nigeria").addressId(1L).categoryIds(Collections.singletonList(1L)).build();
        String requestJson = AppUtils.toJSON(request);
        mockMvc.perform(post("/api/v1/business")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateBusinessEndpointTest() throws Exception {
        BusinessRequest request = BusinessRequest.builder().name("MTN Nigeria").description("MTN Nigeria").addressId(1L).categoryIds(Collections.singletonList(1L)).build();
        String requestJson = AppUtils.toJSON(request);
        mockMvc.perform(put("/api/v1/business/{businessId}", "1")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteBusinessEndpointTest() throws Exception {
        mockMvc.perform(delete("/api/v1/business/{businessId}", "1")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findAllBusinessesEndpointTest() throws Exception {
        mockMvc.perform(get("/api/v1/business")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findBusinessByNameEndpointTest() throws Exception {
        mockMvc.perform(get("/api/v1/business/{name}", "MTN Nigeria")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
