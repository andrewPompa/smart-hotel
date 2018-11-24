package com.dynamics.andrzej.smart.hotel.configurations;

import com.dynamics.andrzej.smart.hotel.entities.Receptionist;
import com.dynamics.andrzej.smart.hotel.respositories.ReceptionistRepository;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AdminUserDetailsService implements UserDetailsService {
    private final ReceptionistRepository receptionistRepository;

    public AdminUserDetailsService(ReceptionistRepository receptionistRepository) {
        this.receptionistRepository = receptionistRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Receptionist receptionist = receptionistRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        return map(receptionist);
    }

    private UserDetails map(Receptionist receptionist) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Stream.of(new SimpleGrantedAuthority("ADMIN")).collect(Collectors.toList());
            }

            @Override
            public String getPassword() {
                return receptionist.getPassword();
            }

            @Override
            public String getUsername() {
                return receptionist.getLogin();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }
}
