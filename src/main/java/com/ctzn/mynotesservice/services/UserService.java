package com.ctzn.mynotesservice.services;

import com.ctzn.mynotesservice.model.apimessage.ApiException;
import com.ctzn.mynotesservice.model.apimessage.TimeSource;
import com.ctzn.mynotesservice.model.user.UserEntity;
import com.ctzn.mynotesservice.model.user.UserRole;
import com.ctzn.mynotesservice.repositories.UserRepository;
import com.ctzn.mynotesservice.security.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ctzn.mynotesservice.model.user.UserRole.checkUserHasRole;

@Service
public class UserService {

    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserEntity getUser(Authentication auth) throws ApiException {
        UserEntity user = userRepository.findByUserId(auth.getName()).orElseThrow(ApiException::getAccessDenied);
        if (!user.getEnabled()) throw ApiException.getAccessDenied();
        if (!user.getRoles().equals(auth.getCredentials())) throw ApiException.getAccessDenied();
        return user;
    }

    public UserEntity getUserAssertRole(Authentication auth, UserRole role) throws ApiException {
        UserEntity user = getUser(auth);
        if (!checkUserHasRole(user, role)) throw ApiException.getAccessDenied();
        return user;
    }

    public UserEntity getUserByPublicId(String userId) throws ApiException {
        return userRepository.findByUserId(userId).orElseThrow(
                () -> ApiException.getNotFoundByName("User with public id", userId)
        );
    }

    public void assertEmailNotUsed(String email) throws ApiException {
        if (userRepository.findByEmail(email).isPresent()) throw ApiException.getAlreadyInUse("Email address", email);
    }

    public UserEntity getUserByEmail(String email) throws ApiException {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(ApiException::getInvalidEmailPassword);
        if (!user.getEnabled()) throw ApiException.getAccessDenied();
        return user;
    }

    public void updateLastSeenOn(UserEntity user) {
        user.setLastSeenOn(TimeSource.now());
        saveUser(user);
    }

    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    public void deleteUser(UserEntity user) {
        userRepository.delete(user);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public String createToken(UserEntity userEntity) {
        return jwtTokenProvider.createToken(userEntity.getUserId(), userEntity.getRoles());
    }

}
