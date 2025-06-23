package com.example.demo.securite;

import com.example.demo.models.Jwt;
import com.example.demo.models.User;
import com.example.demo.repositories.JwtRepository;
import com.example.demo.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {
     private UserService userService;
     private  JwtService jwtService;
     private JwtRepository jwtRepository;
     public JwtFilter(UserService userService,JwtService jwtService){
         this.jwtService=jwtService;
         this.userService=userService;
     }
     @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       Jwt jwt=null ;
       String email=null;
       String token=null;
       boolean expire=true;
       String authorization = request.getHeader("Authorization");
       if (authorization!=null && authorization.startsWith("Bearer ")){
           token = authorization.substring(7);

           email = jwtService.extraireEmail(token);
           jwt=jwtService.trouveJwt(token);
           expire=jwt.getExpire();

       }
       if(!expire
               && email !=null
               &&jwt.getUser().getEmail().equals(email)
               && SecurityContextHolder.getContext().getAuthentication()==null){
           User user  = (User)  userService.loadUserByUsername(email);
           UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
           SecurityContextHolder.getContext().setAuthentication(authenticationToken);
       }
       filterChain.doFilter(request,response);
    }



 }
