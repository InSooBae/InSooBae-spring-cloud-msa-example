package com.example.userservice.controller;

import com.example.userservice.domain.User;
import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final Environment env;

    private final Greeting greeting;

    private final UserService userService;

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's working in user service" +
                " on local server PORT " + env.getProperty("local.server.port") +
                " on server PORT " + env.getProperty("server.port") +
                " on token secret " + env.getProperty("token.secret") +
                " on token expiration time " + env.getProperty("token.expiration_time")

        );
    }

    @GetMapping("/welcome")
    public String welcome() {
//        return env.getProperty("greeting.message");
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser requestUser) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(requestUser, UserDto.class);

        userService.createUser(userDto);

        ResponseUser responseUser = modelMapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        List<ResponseUser> responseUsers = new ArrayList<>();
        List<User> userByAll = userService.getUserByAll();

        userByAll.forEach(user -> {
            responseUsers.add(new ModelMapper().map(user, ResponseUser.class));
        });

        return ResponseEntity.ok(responseUsers);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable String userId) {
        UserDto user = userService.getUserById(userId);

        ResponseUser responseUser = new ModelMapper().map(user, ResponseUser.class);

        return ResponseEntity.ok(responseUser);
    }
}
