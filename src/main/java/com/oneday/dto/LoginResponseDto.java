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
public class LoginResponseDto {
    private String refreshToken;
    private UserDataDto userData;
}
