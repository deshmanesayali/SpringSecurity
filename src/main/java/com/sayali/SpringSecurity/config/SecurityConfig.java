package com.sayali.SpringSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//overrides the basic Spring Security configuration and allows you to configure your own security settings
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.csrf(customizer -> customizer.disable())
                //This line of code configures Spring Security to require authentication for all incoming HTTP requests.
                // In other words, every request must be authenticated in order to be processed by the application.
                .authorizeHttpRequests(request -> request
                        .requestMatchers("register", "jwt-login").permitAll().anyRequest().authenticated())
                //This line of code configures Spring Security to use the default login form for authentication.
                //Customizer is an interface that provides a way to customize the configuration of various components in Spring Security.
                //In the given code snippet, Customizer.withDefaults() is being used to configure the form login with default settings.
                // This means that the form login will be enabled with the default settings provided by Spring Security.
                //httpSecurity.formLogin(Customizer.withDefaults());
                //Allows you to make Postman calls.This method sets up the basic authentication with default settings, such as the realm and the authentication manager.
                .httpBasic(Customizer.withDefaults())
                //This line of code configures Spring Security to not store any sessions.
                // This is in contrast to a traditional web application where the server stores session information to track the user's interactions across multiple requests.
                //However, because form login is enabled, the session is not stored and the login form is always shown to the user after every login attempt.
                //To make the requet stateless, we have to comment the form login.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                //returns the object that allows you to configure your security settings
                .build();
    }

    //@Bean

    //UserDetailsService: This is an interface that provides a way to retrieve user details for authentication purposes.
    // It has a single method loadUserByUsername(String username) that returns a UserDetails object.
    //UserDetails: This is an interface that represents a user's details, including their username, password, and roles.
    // It has several methods that provide information about the user, such as getUsername(), getPassword(), and getAuthorities().
    //User: This is a class that implements the UserDetails interface. It provides a builder-style API for creating UserDetails objects.
    //InMemoryUserDetailsManager: This is a class that implements the UserDetailsService interface.
    // It provides an in-memory implementation of user details management, allowing you to create and manage users in memory.
    //In summary, the code snippet uses the following interfaces and classes:
    //
    //UserDetailsService (interface)
    //Implemented by InMemoryUserDetailsManager (class)
    //UserDetails (interface)
    //Implemented by User (class)
    //The User class implements the UserDetails interface, and the InMemoryUserDetailsManager class implements the UserDetailsService interface.
//    public UserDetailsService userDetailsService() {
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()
//                .username("bruno")
//                .password("deshmane")
//                .roles("USER")
//                .build();
//
//        UserDetails user2 = User
//                .withDefaultPasswordEncoder()
//                .username("aditya")
//                .password("jadhav")
//                .build();
//        return new InMemoryUserDetailsManager(user1, user2);
    // }


    //Implementing DaoAuthenticatorProvider used for db based authentication
    @Bean
    public AuthenticationProvider authenticationProvider() {
        //DaoAuthenticationProvider is a subclass of AuthenticationProvider in Spring Security.
        // AuthenticationProvider is an interface that defines the contract for authenticating users,
        // and DaoAuthenticationProvider is a specific implementation of that interface that uses a DAO to retrieve user data.
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //BCryptPasswordEncoder is a Java class that implements password encoding using the bcrypt algorithm,
        // a strong and widely-used password hashing algorithm.
        // It's used to securely store passwords by converting them into a fixed-length string of characters,
        // making it difficult for attackers to reverse-engineer the original password.
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }
}
