package com.dynamics.andrzej.smart.hotel.configurations;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final DataSource dataSource;
    private final AdminUserDetailsService adminUserDetailsService;

    @Autowired
    public WebSecurityConfigurer(DataSource dataSource, AdminUserDetailsService adminUserDetailsService) {
        this.dataSource = dataSource;
        this.adminUserDetailsService = adminUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider adminAuthProvider(PasswordEncoder encoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(adminUserDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }

    @Order(1)
    @EnableWebSecurity
    public class ApiSecurity extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/**")
                    .authorizeRequests().anyRequest().authenticated();
        }

    }

    @Order(2)
    @EnableWebSecurity
    public class AdminSecurity extends WebSecurityConfigurerAdapter {
        private final AuthenticationProvider authenticationProvider;

        AdminSecurity(AuthenticationProvider authenticationProvider1) {
            this.authenticationProvider = authenticationProvider1;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().antMatchers("/admin", "/admin/**")
                    .hasRole("ADMIN")
                    .and()
                    .authenticationProvider(authenticationProvider)
                    .headers().frameOptions().sameOrigin().and()
                    .formLogin()
                    .loginPage("/login/admin")
                    .loginProcessingUrl("/admin")
                    .and()
            .csrf().disable();
        }

    }

    @Order(3)
    @EnableWebSecurity
    public class ClientSecurity extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/client/**")
                    .authorizeRequests().anyRequest().authenticated().and()
                    .headers().frameOptions().sameOrigin().and()
                    .formLogin();
//                    .loginPage("/login/client");
        }
    }

    @Order(4)
    @EnableWebSecurity
    public class PermitAllSecurity extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/login", "/login/client", "/login/admin", "/webjars/**", "js/main.js", "/h2-console**", "/h2-console/**")
                    .permitAll().and()
                    .headers().frameOptions().sameOrigin().and()
                    .csrf().disable();
        }
    }
}
