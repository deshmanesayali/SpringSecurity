package com.sayali.SpringSecurity.service;

import com.sayali.SpringSecurity.model.UserPrincipal;
import com.sayali.SpringSecurity.model.Users;
import com.sayali.SpringSecurity.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    //In essence, this code is responsible for loading a user's details from a repository (e.g., a database) based on their username,
    // and returning those details in a format that can be used by Spring Security for authentication.
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUsername(username);
        if(user == null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("User you are looking for is not here.");
        }
        return new UserPrincipal (user);
    }
}
