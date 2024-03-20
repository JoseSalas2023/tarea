package com.clase.tarea.service;

import com.clase.tarea.model.Request;
import com.clase.tarea.model.Response;
import com.clase.tarea.model.UserInfo;
import com.clase.tarea.repository.IuserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class AuthService {

    private IuserRepository iuserRepository;

    private JwtService jwtService;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;


    public ResponseEntity<Object> login(Request request) {

        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUser(), request.getPassword()));
            UserDetails user = iuserRepository.finByUsername(request.getUser()).orElse(null);

            if (user != null) {
                return ResponseEntity.status(200).body(Response
                        .builder()
                        .code(200)
                        .message(jwtService.getToken(user))
                        .build()
                );
            }

            return ResponseEntity.status(401)
                    .body(Response
                            .builder()
                            .code(401)
                            .message("User no found")
                            .build()
                    );

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401)
                    .body(Response
                            .builder()
                            .code(401)
                            .message("Authenticate error: " + e.getMessage())
                            .build()
                    );
        }

    }

    public ResponseEntity<Object> addUser(UserInfo userInfo) {

        AtomicReference<String> message = new AtomicReference<>();
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<UserInfo>> violations = validate.validate(userInfo);

        try {

            if (!violations.isEmpty()) {
                violations.forEach(violation -> message.set(violation.getMessage()));
                return ResponseEntity.status(401).body(Response
                        .builder()
                        .code(401)
                        .message(message.toString())
                        .build()
                );
            } else {
                userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
                iuserRepository.save(userInfo);

                return ResponseEntity.status(200)
                        .body(Response
                                .builder()
                                .code(200)
                                .message("User added succesfully")
                                .build()
                        );
            }


        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(Response
                            .builder()
                            .code(401)
                            .message("Duplicate key. this user is registerd: " + e.getMessage())
                            .build()
                    );
        }
    }


    public List<UserInfo> getAllUser(){
        return iuserRepository.findAll();
    }
}
