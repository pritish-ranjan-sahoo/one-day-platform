package com.oneday.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String credential;
    private String password;
}
