package com.clase.tarea.controller;

import com.clase.tarea.model.Request;
import com.clase.tarea.model.UserInfo;
import com.clase.tarea.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    public final AuthService authService;
    @PostMapping
    @Operation(summary = "Get token")
    public ResponseEntity<Object> login(@RequestBody Request request){
        return authService.login(request);
    }

    @PostMapping("/addUser")
    @Operation(summary = "Add user")
    public ResponseEntity<Object> addUser(@RequestBody UserInfo user){
        return authService.addUser(user);
    }

    @GetMapping("/getAllUser")
    @Operation(summary = "Get all user")
    @ResponseStatus(HttpStatus.OK)
    public List<UserInfo> getAllUser(){
            return authService.getAllUser();
    }
}
