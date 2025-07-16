package com.jeffjackson.controller;

import com.jeffjackson.model.LoginResponse;
import com.jeffjackson.model.MessageModel;
import com.jeffjackson.request.LoginRequest;
import com.jeffjackson.request.User;
import com.jeffjackson.security.service.JwtService;
import com.jeffjackson.service.EmailService;
import com.jeffjackson.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class LoginController {

    private final EmailService emailService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${jwt.clientId}")
    private String clientId;

    public LoginController(EmailService emailService, LoginService loginService) {
        this.emailService = emailService;
        this.loginService = loginService;
    }

    /*@PostMapping("/api/signup")
    public ResponseEntity<Object> signup(@RequestBody User user){
        try {
            user.setCreatedAt(LocalDateTime.now(ZoneId.of("America/New_York")));
            this.loginService.save(user);
            MessageModel messageModel = new MessageModel("Success", "User registered!!");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("Error saving user: " +e.getMessage());
        }
    }*/

    @PostMapping("/api/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest){
        if(null == loginRequest.getUsername() || null == loginRequest.getPassword()){
            MessageModel messageModel = new MessageModel("Fail", "Bad Request, Please pass username and password !");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }
        Optional<User> data = this.loginService.findById(loginRequest.getUsername());
        if(data.isEmpty()){
            MessageModel messageModel = new MessageModel("Fail", "Incorrect Username and password!");
            return ResponseEntity.status(HttpStatus.OK).body(messageModel);
        }
        if(data.isPresent()){
            if(data.get().getUsername().equals(loginRequest.getUsername()) && data.get().getPassword().equals(loginRequest.getPassword())){
                UserDetails user = userDetailsService.loadUserByUsername(clientId);
                String token = jwtService.generateToken(user);
                LoginResponse loginResponse = new LoginResponse("Success", "Login Successful!", token);
                return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
            }
        }
        MessageModel messageModel = new MessageModel("Fail", "Please enter correct username and password !");
        return ResponseEntity.status(HttpStatus.OK).body(messageModel);
    }
}
