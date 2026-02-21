package com.oneday.service.impl;

import com.oneday.dto.VerifyUserRequestDto;
import com.oneday.entity.AppUser;
import com.oneday.entity.OtpList;
import com.oneday.entity.type.AccountStatusType;
import com.oneday.entity.type.RoleType;
import com.oneday.reposiratory.AppUserRepository;
import com.oneday.reposiratory.OtpListRepository;
import com.oneday.service.UserService;
import com.oneday.util.AuthUtil;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AppUserRepository appUserRepository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;
    private final JavaMailSender jms;
    private final OtpListRepository otpListRepository;

    @Override
    public void sendOtp(String email) {
        String otpValue = authUtil.generateOtp();
        String body = "Your OneDay account verification code is: "+otpValue;

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Verification mail");
        msg.setText(body);
        jms.send(msg);

        OtpList newOtpData = otpListRepository.findByEmail(email).orElse(null);

        if(newOtpData!=null){
            newOtpData.setOtp(otpValue);
            newOtpData.setCreationDate(LocalDateTime.now());
            newOtpData.setExpiryDate(LocalDateTime.now().plusMinutes(2));
        } else {
            newOtpData = OtpList.builder()
                    .email(email)
                    .otp(otpValue)
                    .creationDate(LocalDateTime.now())
                    .expiryDate(LocalDateTime.now().plusMinutes(2))
                    .build();
        }

        otpListRepository.save(newOtpData);
    }

    @Transactional
    @Override
    public void verifyUser(VerifyUserRequestDto request) {
        String requestEmail = request.getEmail().toLowerCase();
        otpListRepository.findAll()
                .forEach(o -> log.info("DB contains: " + o.getEmail()));
        OtpList otpInDB = otpListRepository.findByEmail(requestEmail).orElseThrow(
                () -> new BadCredentialsException("Otp expired or invalid!!")
        );
        if(otpInDB.getExpiryDate().isBefore(LocalDateTime.now())){
            otpListRepository.deleteByEmail(requestEmail);
            throw new BadCredentialsException("Otp expired!!");
        }

        if(!otpInDB.getOtp().equals(request.getOtp())){
            throw new BadCredentialsException("Wrong otp!!");
        }

        String userType = request.getVerifyAs().toLowerCase();
        RoleType roleType = authUtil.convertToRoleType(userType);

        AppUser appUser = appUserRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("Username not found during verification")
        );

        if(appUser.getRoles().size()<2){
            appUser.getRoles().add(roleType);
        } else {
            String currentRole = authUtil.findRole(appUser.getRoles());
            throw new DuplicateRequestException("The user is already registered as "+currentRole);
        }
        appUser.setAccountStatus(AccountStatusType.VERIFIED);
        if(appUser.getEmail()==null){
            appUser.setEmail(requestEmail);
        }
        appUserRepository.save(appUser);
        otpListRepository.deleteByEmail(requestEmail);
    }
}
