package com.ctzn.mynotesservice.model.login;

import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.model.user.UserPasswordEncoder;
import com.ctzn.mynotesservice.repositories.UserRepository;
import com.ctzn.mynotesservice.security.JwtTokenProvider;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(LoginController.BASE_PATH)
public class LoginController {

    public static final String BASE_PATH = "/api/login";

    private static final ApiException BAD_CREDENTIALS = ApiException.getBadCredentials("Invalid email/password supplied");

    private JwtTokenProvider jwtTokenProvider;

    private UserRepository userRepository;

    public LoginController(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping
    LoginResponse login(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) throws ApiException {
        if (result.hasErrors()) throw BAD_CREDENTIALS;
        UserEntity userEntity = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> BAD_CREDENTIALS);
        if (!UserPasswordEncoder.matches(loginRequest.getPassword(), userEntity.getEncodedPassword()))
            throw BAD_CREDENTIALS;
        String token = jwtTokenProvider.createToken(userEntity.getUserId(), userEntity.getRolesMask());
        return new LoginResponse(token);
    }
}
