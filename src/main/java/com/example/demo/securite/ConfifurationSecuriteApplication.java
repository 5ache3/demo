// package com.example.demo.securite;

// import com.example.demo.services.UserService;
// import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
// import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.servlet.config.annotation.EnableWebMvc;

// import java.io.PipedOutputStream;

// @Configuration
// @EnableWebSecurity
// public class ConfifurationSecuriteApplication  {
//     private final JwtFilter jwtFilter;
//     private  final  CryptageMotDePasse cryptageMotDePasse;
//     private final UserService userService;
//     public ConfifurationSecuriteApplication(JwtFilter jwtFilter, CryptageMotDePasse cryptageMotDePasse, UserService userService){
//         this.jwtFilter=jwtFilter;
//         this.cryptageMotDePasse=cryptageMotDePasse;
//         this.userService=userService;
//     }
//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws  Exception{
//         return httpSecurity
//                 .csrf(AbstractHttpConfigurer::disable)
//                 .authorizeHttpRequests(
//                         authorize->
//                                 authorize.anyRequest().permitAll()
//                 )
//                 .sessionManagement(
//                         httpSecuritySessionManagementConfigurer ->
//                                 httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                 )
//                 .build();
//     }


//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws  Exception{
//         return  authenticationConfiguration.getAuthenticationManager();

//     }
//     @Bean
//     public  UserDetailsService userDetailsService()  {
//         return  userService;
//     }

//     @Bean
//     public AuthenticationProvider authenticationProvider(){
//         DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
//         daoAuthenticationProvider.setUserDetailsService(this.userDetailsService());
//         daoAuthenticationProvider.setPasswordEncoder(this.cryptageMotDePasse.bCryptPasswordEncoder());
//         return daoAuthenticationProvider;
//     }

    



// }
