package com.CollegeHub.Services;

import com.CollegeHub.Entity.Users;
import com.CollegeHub.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users existingUser = userRepository.findByEmail((username)).orElseThrow( ()->new UsernameNotFoundException("Email id is not found..try with existing email..") );
        return new User(existingUser.getEmail(),existingUser.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_" + existingUser.getRole())));
    }


}
