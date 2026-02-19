package com.oneday.dto;


import com.oneday.entity.type.AccountStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDataDto {
    private String accessToken;
    private String username;
    private String email;
    private AccountStatusType accountStatus;
    private String role;
}
