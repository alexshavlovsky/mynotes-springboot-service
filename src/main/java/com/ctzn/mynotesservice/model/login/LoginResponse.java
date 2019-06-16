package com.ctzn.mynotesservice.model.login;

import com.ctzn.mynotesservice.model.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserResponse user;
}
