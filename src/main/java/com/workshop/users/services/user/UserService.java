package com.workshop.users.services.user;

import com.workshop.users.api.dto.UserDto;
import com.workshop.users.exceptions.AuthenticateException;
import com.workshop.users.exceptions.CantCreateCartException;
import com.workshop.users.exceptions.NotFoundUserException;
import com.workshop.users.exceptions.RegisterException;
import com.workshop.users.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    UserDto addUser(UserDto user)  throws AuthenticateException, RegisterException,CantCreateCartException;
    UserDto getUserById(Long id) throws NotFoundUserException;
    UserDto getUserByEmail(String email) throws AuthenticateException;
    UserDto updateUser(Long id, UserDto userDto) throws NotFoundUserException;
    Optional<UserEntity> getUserByEmailOptional(String email);
    UserDto updateFidelityPoints(Long id, Integer points) throws NotFoundUserException;
}
