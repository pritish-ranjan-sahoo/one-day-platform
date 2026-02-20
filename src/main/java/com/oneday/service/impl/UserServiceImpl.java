package com.oneday.service.impl;

import com.oneday.reposiratory.AppUserRepository;
import com.oneday.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AppUserRepository appUserRepository;
    private final ModelMapper modelMapper;
}
