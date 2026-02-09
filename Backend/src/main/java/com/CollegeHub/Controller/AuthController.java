package com.CollegeHub.Controller;


import com.CollegeHub.DTO.LoginDTO.LoginRequest;
import com.CollegeHub.DTO.LoginDTO.LoginResponse;
import com.CollegeHub.Services.AppUserDetailsService;
import com.CollegeHub.Util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService appUserDetailsService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        System.out.println("Inside the login() method...");

        try{
            authenticate(loginRequest.getEmail() , loginRequest.getPassword());

            final UserDetails userDetails = appUserDetailsService.loadUserByUsername(loginRequest.getEmail());
            System.out.println("Details recieved are - email = "+userDetails.getUsername()+" password = "+loginRequest.getPassword());


            final String jwtToken = jwtUtil.generateToken(userDetails);


            ResponseCookie cookie = ResponseCookie.from("jwt",jwtToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
                    .body(new LoginResponse("Account verification successfull...",loginRequest.getEmail(), jwtToken));


        }catch (BadCredentialsException e){
            Map<String , Object> error = new HashMap<>();
            error.put("error",true);
            error.put("message" , "Email or Password is incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        }catch (DisabledException e){
            Map<String , Object> error = new HashMap<>();
            error.put("error",true);
            error.put("message" , "Account is disabled");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);

        }catch (Exception e){
            Map<String , Object> error = new HashMap<>();
            error.put("error",true);
            System.out.println(e.getMessage());
            e.printStackTrace();
            error.put("message" , "Authentication failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

    }


    private void authenticate(String email , String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    @GetMapping("/api/v1/auth/me")
    public ResponseEntity<?> getLoggedInUser(Authentication authentication){

        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();

        if(! (principal instanceof UserDetails userDetails)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // false if not using https
        cookie.setPath("/");
        cookie.setMaxAge(0); // delete cookie

        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }
}
