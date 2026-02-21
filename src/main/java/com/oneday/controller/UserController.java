package com.oneday.controller;


import com.oneday.dto.VerifyUserRequestDto;
import com.oneday.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/verify-user")
    public ResponseEntity<String> verifyUser(@RequestBody VerifyUserRequestDto request){
        userService.verifyUser(request);
        return new ResponseEntity<>("User verified as: "+request.getVerifyAs(), HttpStatus.OK);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody String email){
        userService.sendOtp(email);
        return new ResponseEntity<>("Otp sent!!!", HttpStatus.OK);
    }

    @GetMapping("/profile")
    public void getProfile(){

    }
}
