package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.*;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonBusinessService {
    @Autowired
    UserDao userDao;

    @Autowired
    UserAuthDao userAuthDao;

    public UsersEntity getUserDetails(String userUuid, String authorizationToken) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuthEntity = userAuthDao.getAuthToken(authorizationToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        UsersEntity usersEntity = userDao.getUser(userUuid);
        if (usersEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }

        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        }

        return usersEntity;
    }
}
