package com.oneday.service.impl;

import com.oneday.entity.AppUser;
import com.oneday.reposiratory.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class verifyUserImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String credential) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmailOrUsername(credential,credential).orElseThrow(
                () -> new UsernameNotFoundException("User not found!!")
        );

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user
                        .getRoles()
                        .stream()
                        .map((x)-> x.name())
                        .collect(Collectors.toList())
                        .toArray(new String[0])
                )
                .authorities(new HashSet<GrantedAuthority>())
                .build();
    }
}
