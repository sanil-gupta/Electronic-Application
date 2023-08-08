package com.sanil.electronic.store.controllers;

import com.sanil.electronic.store.dtos.JwtRequest;
import com.sanil.electronic.store.dtos.JwtResponse;
import com.sanil.electronic.store.dtos.UserDto;
import com.sanil.electronic.store.exception.BadApiRequestException;
import com.sanil.electronic.store.security.JwtHelper;
import com.sanil.electronic.store.services.UserService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
@Api(value = "AuthController",description = "APIs for Authentication!!")
public class AuthController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserDetailsService userDetailsService;  //preDefined class

    @Autowired
    private AuthenticationManager manager;   //preDefined class

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

        this.doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.jwtHelper.generateToken(userDetails);

        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        JwtResponse response = JwtResponse
                        .builder()
                        .jwtToken(token)
                        .user(userDto).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadApiRequestException("Invalid username or password !!");
        }
    }

    @GetMapping("/current")
    public ResponseEntity<String> getCurrentUser(Principal principal) {
        String name = principal.getName();
        return new ResponseEntity<>(name, HttpStatus.OK);
    }
}
