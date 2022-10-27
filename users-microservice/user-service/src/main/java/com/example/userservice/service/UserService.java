package com.example.userservice.service;

import com.example.userservice.domain.User;
import com.example.userservice.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(String userId);

    List<User> getUserByAll();

}
