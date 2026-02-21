package com.oneday.util;

import com.oneday.entity.type.OAuthProviderType;
import com.oneday.entity.type.RoleType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.util.Set;

@Component
public class AuthUtil {

    public String generateOtp(){
        SecureRandom secureRandom = new SecureRandom();
        int min = 100000;
        int max = 999999;
        int otp = secureRandom.nextInt(max - min + 1) + min;

        return Integer.toString(otp);
    }

    public RoleType convertToRoleType(String userType){
        switch (userType) {
            case "customer" :
                return RoleType.CUSTOMER;
            case "provider" :
                return RoleType.PROVIDER;
            default:
                throw new InvalidParameterException("Invalid user modification request");
        }
    }

    public String findRole(Set<RoleType> roles){
        if(roles.contains(RoleType.ADMIN)){
            return RoleType.ADMIN.name();
        } else if(roles.contains(RoleType.PROVIDER)){
            return RoleType.PROVIDER.name();
        } else if(roles.contains(RoleType.CUSTOMER)){
            return RoleType.CUSTOMER.name();
        } else {
            return RoleType.USER.name();
        }
    }

    public String getProviderId(OAuth2User oAuth2User, String registrationId){
        switch (registrationId.toLowerCase()) {
            case "github" :
                return oAuth2User.getAttribute("id").toString();
            default:
                throw new OAuth2AuthenticationException("Unknown registration id found");
        }
    }

    public OAuthProviderType getProviderName(String registrationId){
        switch (registrationId.toLowerCase()) {
            case "github" :
                return OAuthProviderType.GITHUB;
            default:
                throw new OAuth2AuthenticationException("Unknown registration id found");
        }
    }
}
