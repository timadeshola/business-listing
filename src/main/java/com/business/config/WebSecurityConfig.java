package com.business.config;

import com.business.core.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private MyUserDetails jwtUserDetailsService;
    private TokenHelper tokenHelper;
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private PasswordEncoder passwordEncoder;
    private CsrfSecurityRequestMatcher csrfSecurityRequestMatcher;

    @Autowired
    public WebSecurityConfig(MyUserDetails jwtUserDetailsService, TokenHelper tokenHelper, RestAuthenticationEntryPoint restAuthenticationEntryPoint, PasswordEncoder passwordEncoder, CsrfSecurityRequestMatcher csrfSecurityRequestMatcher) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.tokenHelper = tokenHelper;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.passwordEncoder = passwordEncoder;
        this.csrfSecurityRequestMatcher = csrfSecurityRequestMatcher;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<RequestMatcher> csrfMethods = new ArrayList<>();
        Arrays.asList("POST", "PUT", "PATCH", "DELETE")
                .forEach(method -> csrfMethods
                        .add(new AntPathRequestMatcher("/**", method)));
        http
                .authorizeRequests()
                .antMatchers("/api/v1/auth/login").permitAll()
                .antMatchers("/swagger-ui**").permitAll()
                .anyRequest().authenticated();

        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.apply(new JwtTokenFilterConfigurer(tokenHelper));

        http.headers().defaultsDisabled().cacheControl();

        http.headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable();

        http.headers().xssProtection().block(false);

        http.csrf().requireCsrfProtectionMatcher(csrfSecurityRequestMatcher).csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).disable();

        http.cors();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.POST, "/api/v1/auth/login");
        web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js");
        web.ignoring()
                .antMatchers("/v1/api-docs")
                .antMatchers("/v2/api-docs")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/configuration/**")
                .antMatchers("/webjars/**")
                .antMatchers("/actuator/**")
                .antMatchers("/public/**");
    }
}
