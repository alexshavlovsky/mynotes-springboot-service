package com.ctzn.mynotesservice.services;

import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.TimeSource;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.repositories.UserRepository;
import com.ctzn.mynotesservice.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserEntity getUser(Principal principal) throws ApiException {
        return userRepository.findByUserId(principal.getName()).orElseThrow(ApiException::getAccessDenied);
    }

    public void assertEmailNotUsed(String email) throws ApiException {
        if (userRepository.findByEmail(email).isPresent()) throw ApiException.getAlreadyInUse("Email address", email);
    }

    public UserEntity getUserByEmail(String email) throws ApiException {
        return userRepository.findByEmail(email).orElseThrow(ApiException::getInvalidEmailPassword);
    }

    public void updateLastSeenOn(UserEntity user) {
        user.setLastSeenOn(TimeSource.now());
        saveUser(user);
    }

    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public String createToken(UserEntity userEntity) {
        return jwtTokenProvider.createToken(userEntity.getUserId(), userEntity.getRoles());
    }

}
