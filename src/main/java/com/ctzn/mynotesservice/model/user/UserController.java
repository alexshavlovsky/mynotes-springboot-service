package com.ctzn.mynotesservice.model.user;

import com.ctzn.mynotesservice.model.DomainMapper;
import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.BindingResultAdapter;
import com.ctzn.mynotesservice.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(UserController.BASE_PATH)
public class UserController {

    public static final String BASE_PATH = "/api/users/";

    private static final List<UserRole> DEFAULT_USER_ROLES = Collections.singletonList(UserRole.USER);

    private UserRepository userRepository;

    private DomainMapper domainMapper;

    public UserController(UserRepository userRepository, DomainMapper domainMapper) {
        this.userRepository = userRepository;
        this.domainMapper = domainMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse createUser(@Valid @RequestBody UserRequest userRequest, BindingResult result) throws ApiException {
        if (result.hasErrors()) throw ApiException.getBadRequest(BindingResultAdapter.adapt(result));
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent())
            throw ApiException.getAlreadyInUse("Email address", userRequest.getEmail());
        UserEntity userEntity = domainMapper.map(userRequest, UserEntity.class);
        userEntity.setRolesMask(DEFAULT_USER_ROLES);
        return domainMapper.map(userRepository.save(userEntity), UserResponse.class);
    }
}
