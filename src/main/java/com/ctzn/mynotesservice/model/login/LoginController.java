package com.ctzn.mynotesservice.model.login;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.model.user.UserPasswordEncoder;
import com.ctzn.mynotesservice.model.user.UserResponse;
import com.ctzn.mynotesservice.repositories.UserRepository;
import com.ctzn.mynotesservice.security.JwtTokenProvider;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(LoginController.BASE_PATH)
public class LoginController {

    public static final String BASE_PATH = "/api/login";

    private static final ApiException BAD_CREDENTIALS = ApiException.getBadCredentials("Invalid email/password supplied");

    private JwtTokenProvider jwtTokenProvider;

    private UserRepository userRepository;

    private DomainMapper domainMapper;

    public LoginController(JwtTokenProvider jwtTokenProvider, UserRepository userRepository, DomainMapper domainMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.domainMapper = domainMapper;
    }

    @PostMapping
    LoginResponse login(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) throws ApiException {
        if (result.hasErrors()) throw BAD_CREDENTIALS;
        UserEntity userEntity = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> BAD_CREDENTIALS);
        if (!UserPasswordEncoder.matches(loginRequest.getPassword(), userEntity.getEncodedPassword()))
            throw BAD_CREDENTIALS;
        String token = jwtTokenProvider.createToken(userEntity.getUserId(), userEntity.getRoles());
        return new LoginResponse(token, domainMapper.map(userEntity, UserResponse.class));
    }
}
