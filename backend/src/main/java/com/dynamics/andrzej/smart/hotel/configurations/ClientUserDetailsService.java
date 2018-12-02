package com.dynamics.andrzej.smart.hotel.configurations;

import com.dynamics.andrzej.smart.hotel.entities.Client;
import com.dynamics.andrzej.smart.hotel.entities.Receptionist;
import com.dynamics.andrzej.smart.hotel.respositories.ClientRepository;
import com.dynamics.andrzej.smart.hotel.respositories.ReceptionistRepository;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ClientUserDetailsService implements UserDetailsService {
    private final ClientRepository clientRepository;

    public ClientUserDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Client client = clientRepository.findByLoginEquals(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        return map(client);
    }

    private UserDetails map(Client receptionist) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Stream.of(new SimpleGrantedAuthority("ROLE_USER")).collect(Collectors.toList());
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
