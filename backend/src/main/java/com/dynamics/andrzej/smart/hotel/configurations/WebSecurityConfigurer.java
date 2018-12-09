package com.dynamics.andrzej.smart.hotel.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfigurer {
    private final AdminUserDetailsService adminUserDetailsService;
    private final ClientUserDetailsService clientUserDetailsService;

    @Autowired
    public WebSecurityConfigurer(AdminUserDetailsService adminUserDetailsService, ClientUserDetailsService clientUserDetailsService) {
        this.adminUserDetailsService = adminUserDetailsService;
        this.clientUserDetailsService = clientUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean("adminAuthProvider")
    public DaoAuthenticationProvider adminAuthProvider(PasswordEncoder encoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(adminUserDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }

    @Bean("clientAuthProvider")
    public DaoAuthenticationProvider clientAuthProvider(PasswordEncoder encoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(clientUserDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }

    @Order(1)
    @EnableWebSecurity
    public class ApiSecurity extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/**")
                    .authorizeRequests().anyRequest().authenticated()
                    .and().csrf().disable();
        }

    }

    @Order(2)
    @EnableWebSecurity
    public class AdminSecurity extends WebSecurityConfigurerAdapter {
        private final AuthenticationProvider authenticationProvider;
        AdminSecurity(@Qualifier("adminAuthProvider") AuthenticationProvider authenticationProvider1) {
            this.authenticationProvider = authenticationProvider1;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/admin/**")
                    .authorizeRequests()
                    .anyRequest()
                    .hasRole("ADMIN")
                    .and()
                    .authenticationProvider(authenticationProvider)
                    .headers().frameOptions().sameOrigin().and()
                    .formLogin()
                    .successForwardUrl("/admin/reservation")
                    .loginPage("/login/admin")
                    .loginProcessingUrl("/admin/check")
                    .and()
            .csrf().disable();
        }

    }

    @Order(3)
    @EnableWebSecurity
    public class ClientSecurity extends WebSecurityConfigurerAdapter {
        private final AuthenticationProvider authenticationProvider;

        public ClientSecurity(@Qualifier("clientAuthProvider") AuthenticationProvider authenticationProvider) {
            this.authenticationProvider = authenticationProvider;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/client/**")
                    .authorizeRequests()
                    .anyRequest()
                    .hasRole("CLIENT")
                    .and()
                    .authenticationProvider(authenticationProvider)
                    .headers().frameOptions().sameOrigin().and()
                    .formLogin()
                    .loginPage("/login/client")
                    .loginProcessingUrl("/client/check")
                    .and()
                    .csrf().disable();
        }
    }

    @Order(4)
    @EnableWebSecurity
    public class PermitAllSecurity extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/login", "/login/client", "/register/client", "/login/admin", "/webjars/**", "js/main.js", "/h2-console**", "/h2-console/**")
                    .permitAll().and()
                    .headers().frameOptions().sameOrigin().and()
                    .csrf().disable();
        }
    }
}
