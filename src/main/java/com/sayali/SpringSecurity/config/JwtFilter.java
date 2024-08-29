package com.sayali.SpringSecurity.config;

import com.sayali.SpringSecurity.service.JWTService;
import com.sayali.SpringSecurity.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//OncePerRequestFilter is a Spring Framework class that ensures a filter is executed only once per request,
// even if the request is dispatched multiple times (e.g., to a servlet, then to a JSP, and so on).
@Component
public class JwtFilter extends OncePerRequestFilter {


    @Autowired
    private JWTService jwtService;

    //ApplicationContext is an interface in the Spring Framework that represents the Spring IoC (Inversion of Control) container.
    // It provides access to the application's beans, resources, and other configuration information.
    // Think of it as the central registry of the application's components and configuration.
    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYW52ZWVyIiwiaWF0IjoxNzI0Nzk3NDM1LCJleHAiOjE3MjQ3OTc1NDN9.z02XsgC4Ope13zPZN1EQBISd394TbhBhJRe-Pk31ObA
        //The bearer token has to be in the header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            //Bearer is a String and we should get the token from position 7
            token = authHeader.substring(7);
            username = jwtService.extractusername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //if token is valid
            //while validating token, the token should be valid and user should exist in the database
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }
        filterChain.doFilter(request, response);
    }
}
