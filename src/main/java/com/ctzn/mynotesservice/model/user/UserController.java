package com.ctzn.mynotesservice.model.user;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.BindingResultAdapter;
import com.ctzn.mynotesservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(UserController.BASE_PATH)
public class UserController {

    public static final String BASE_PATH = "/api/users";
    public static final String LOGIN_RES = "login";
    public static final String CURRENT_RES = "current";
    public static final String LOGIN_PATH = BASE_PATH + '/' + LOGIN_RES;

    private static final List<UserRole> DEFAULT_USER_ROLES = Collections.singletonList(UserRole.USER);

    private UserService userService;
    private DomainMapper domainMapper;

    public UserController(UserService userService, DomainMapper domainMapper) {
        this.userService = userService;
        this.domainMapper = domainMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse register(@Valid @RequestBody UserRequest userRequest, BindingResult result) throws ApiException {
        if (result.hasErrors()) throw ApiException.getBadRequest(BindingResultAdapter.adapt(result));
        userService.assertEmailNotUsed(userRequest.getEmail());
        UserEntity user = domainMapper.map(userRequest, UserEntity.class);
        user.setRoles(DEFAULT_USER_ROLES);
        return domainMapper.map(userService.saveUser(user), UserResponse.class);
    }

    @PostMapping(LOGIN_RES)
    UserLoginResponse login(@Valid @RequestBody UserLoginRequest userLoginRequest, BindingResult result) throws ApiException {
        if (result.hasErrors()) throw ApiException.INVALID_EMAIL_PASSWORD;
        UserEntity userEntity = userService.getUserByEmail(userLoginRequest.getEmail());
        if (!UserPasswordEncoder.matches(userLoginRequest.getPassword(), userEntity.getEncodedPassword()))
            throw ApiException.INVALID_EMAIL_PASSWORD;
        String token = userService.createToken(userEntity);
        return new UserLoginResponse(token, domainMapper.map(userEntity, UserResponse.class));
    }

    @GetMapping(CURRENT_RES)
    public UserResponse getCurrentUser(Principal principal) throws ApiException {
        UserEntity user = userService.getUser(principal);
        return domainMapper.map(user, UserResponse.class);
    }

    @GetMapping() // ADMIN only
    public List<UserResponse> getAllUsers() {
        return domainMapper.mapAll(userService.getAllUsers(), UserResponse.class);
    }

}
