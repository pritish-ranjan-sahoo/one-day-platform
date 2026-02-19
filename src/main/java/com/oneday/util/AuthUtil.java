package com.oneday.util;

import com.oneday.entity.type.RoleType;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AuthUtil {

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
}
