package com.example.demo.securite;

import com.example.demo.models.Jwt;
import com.example.demo.models.User;
import com.example.demo.repositories.JwtRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
@Builder
@RequiredArgsConstructor
@Service
public class JwtService {
    private static final  String ENCREPTION_KEY="168e23f2978a16ecd8f1618fc54b18ad2c0cc49a9ffb4a702a23687f72f6a057";
    private  final UserService userService ;
    private final UserRepository userRepository;
    private  final JwtRepository jwtRepository;


    public String generateToken(User user) {
    // Example using io.jsonwebtoken (jjwt) library
    return Jwts.builder()
            .setSubject(user.getId().toString())
            .claim("role", user.getRole())
            .setIssuedAt(new Date())
            .setExpiration(Date.from(Instant.now().plus(Duration.ofDays(5))))
            .signWith(SignatureAlgorithm.HS256, ENCREPTION_KEY)
            .compact();
}
    
public Map<String, String> generateJwt(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    if (user.getRole() == null) {
        throw new IllegalStateException("User role is missing for: " + username);
    }

    return Map.of(
        "token", generateToken(user),
        "role", String.valueOf(user.getRole()),
        "id", String.valueOf(user.getId())
    );
}

private static Key getKey() {
    byte[] decode = Decoders.BASE64.decode(ENCREPTION_KEY);
    return Keys.hmacShaKeyFor(decode);
}

    public  Map<String, String> generationJwt(User user) {
        Map<String, String> name = Map.of(
                "name", user.getName(),
                "email", user.getEmail()
        );
        final  long l = System.currentTimeMillis();
        final  long expiration=l + 30*60*1000;
        String jwtBuilder = Jwts.builder()
                .setIssuedAt(new Date(l))
                .setExpiration(new Date(expiration))
                .setSubject(user.getId().toString())
                .setClaims(name)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of("infos:",jwtBuilder);
    }

    public  String extraireEmail(String token){
        return  this.getClaims(token,Claims::getSubject) ;
    }
    
    public  boolean isTokenExprired(String token){
        Date expirationDate=this.getClaims(token,Claims::getExpiration);
        if(expirationDate==null){
            throw  new  IllegalArgumentException("date dexpiration est null");
        }
        return expirationDate.before(new Date());
    }

    private  <T> T getClaims(String token, Function<Claims,T> function){
        Claims claims=getAllClaims(token);
        return  function.apply(claims);
    }
    private  Claims getAllClaims(String token){
        return  Jwts.parserBuilder()
                      .setSigningKey(this.getKey())
                     .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Jwt trouveJwt(String token){
         return    jwtRepository.findByToken(token).orElseThrow(()-> new UsernameNotFoundException("aucun utilisateur "));
    }

   public  void  deconnexion(){
       User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       List<Jwt> jwt = this.jwtRepository.findByUserAndDesactiveAndExpire(user, false, false);
       for(Jwt j : jwt){
           j.setDesactive(true);
           j.setExpire(true);
       }
       jwtRepository.saveAll(jwt);
   }


}
