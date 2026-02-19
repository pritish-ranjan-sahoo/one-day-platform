package com.oneday.service.impl;

import com.oneday.entity.AppUser;
import com.oneday.reposiratory.AppUserRepository;
import com.oneday.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AppUserRepository appUserRepository;

    @Override
    public void createNew() {
        appUserRepository.save(AppUser.builder().UserId(2L).build());
    }
}
