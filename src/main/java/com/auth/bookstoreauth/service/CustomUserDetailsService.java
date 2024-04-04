package com.auth.bookstoreauth.service;

import com.auth.bookstoreauth.entity.model.CurrentUser;
import com.auth.bookstoreauth.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    public CurrentUser loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails currentUser = userDetailsService.loadUserByUsername(username);
        return new CurrentUser(currentUser.getUsername(), currentUser.getPassword(), getAuthorities("ROLE"));
    }


    public Collection<? extends GrantedAuthority> getAuthorities(String role) throws UsernameNotFoundException {
        List<String> grants = new ArrayList<>();
        grants.add(role);
        return getGrantedAuthorities(grants);
    }

    public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
