package com.auth.bookstoreauth.controller;

import com.auth.bookstoreauth.entity.User;
import com.auth.bookstoreauth.payload.request.LoginRequest;
import com.auth.bookstoreauth.payload.response.LoginResponse;
import com.auth.bookstoreauth.repository.UserRepository;
import com.auth.bookstoreauth.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        User currentUser = userRepository.findByUsername(loginRequest.getUsername());

        if(currentUser == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(new LoginResponse(jwtUtils.generateToken(currentUser),
                currentUser.getUserId(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getRole()));
    }

    /* SPRING SECURITY */
    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException ex) {
            throw new DisabledException("USER_DISABLED", ex);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
        } catch (InternalAuthenticationServiceException ex) {
            throw new InternalAuthenticationServiceException("INVALID_CREDENTIALS", ex);
        }
    }
}
