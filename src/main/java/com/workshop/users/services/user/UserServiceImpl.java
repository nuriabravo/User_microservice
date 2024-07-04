package com.workshop.users.services.user;

import com.workshop.users.api.dto.CountryDto;
import com.workshop.users.api.dto.Login;
import com.workshop.users.api.dto.AddressDto;
import com.workshop.users.api.dto.UserDto;
import com.workshop.users.exceptions.*;
import com.workshop.users.model.UserEntity;
import com.workshop.users.repositories.CartRepository;
import com.workshop.users.repositories.UserDAORepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAORepository userDAORepository;
    private final CartRepository cartRepository;



    @Override
    public UserDto addUser(UserDto user) throws AuthenticateException, RegisterException,CantCreateCartException {

        String encryptedPassword = Login.BCRYPT.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setFidelityPoints(0);

        if (user.getId() != null && userDAORepository.findById(user.getId()).isPresent()) {
            throw new RegisterException("There's an error registering the user");
        }
        UserDto userSaved = UserEntity.fromEntity(userDAORepository.save(UserDto.toEntity(user)));
        cartRepository.createCart(userSaved.getId());
        return userSaved;

    }


    @Override
    public UserDto getUserById(Long id) throws NotFoundUserException {
        return UserEntity.fromEntity(userDAORepository.findById(id)
                .orElseThrow(() -> new NotFoundUserException("Not found user")));

    }

    @Override
    public UserDto getUserByEmail(String email) throws AuthenticateException {
        return UserEntity.fromEntity(userDAORepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticateException("Can't authenticate")));
    }

    @Override
    public Optional<UserEntity> getUserByEmailOptional(String email) {
        return userDAORepository.findByEmail(email);
    }

    @Override
    public UserDto updateFidelityPoints(Long id, Integer points) throws NotFoundUserException {
        UserEntity userToUpdate = userDAORepository.findById(id)
                .orElseThrow(() -> new NotFoundUserException("The user with the id " + id  + " was not found."));

        userToUpdate.setFidelityPoints(UserDto.setSaveFidelityPoints(userToUpdate.getFidelityPoints(), points));

        return UserEntity.fromEntity(userDAORepository.save(userToUpdate));
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) throws NotFoundUserException{
        UserEntity userEntity = userDAORepository.findById(id).orElseThrow(()->new NotFoundUserException("The user with the id " + id  + " was not found."));
        userEntity.setName(userDto.getName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setBirthDate(UserDto.convertDateToLocalDate(userDto.getBirthDate()));
        userEntity.setPassword(Login.BCRYPT.encode(userDto.getPassword()));
        userEntity.setPhone(userDto.getPhone());
        userEntity.setAddress(AddressDto.toEntity(userDto.getAddress()));
        userEntity.setCountry(CountryDto.toEntity(userDto.getCountry()));
        return UserEntity.fromEntity(userDAORepository.save(userEntity));

    }


}

