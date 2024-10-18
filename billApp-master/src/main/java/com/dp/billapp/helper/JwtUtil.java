package com.dp.billapp.helper;


import com.dp.billapp.model.User;
import com.dp.billapp.model.UserConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 10000 * 60 * 60 * 10;

    private String SECRET_KEY = "tosmart@drupsystem";

    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public String generateToken(User user, UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(UserConstants.contactNo, userDetails.getUsername());
        claims.put(UserConstants.userId,user != null ? user.getId()+"": "0");
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Map<String, String> getJwtTokenDetails(HttpServletRequest request){
        String requestHeader = request.getHeader("Authorization");
        if(requestHeader!=null && requestHeader.startsWith("Bearer ")) {
            String jwtToken = requestHeader.substring(7);
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken).getBody();
            Map<String, String> map = new HashMap<>();
            map.put(UserConstants.contactNo, claims.get(UserConstants.contactNo, String.class));
            map.put(UserConstants.userId, claims.get(UserConstants.userId, String.class));
            return map;
        }
        return null;
    }

    public boolean checkUser(HttpServletRequest request){
        String requestHeader = request.getHeader("Authorization");
        if(requestHeader != null && requestHeader.startsWith("Bearer ")) {
            String jwtToken = requestHeader.substring(7);
            if(!this.isTokenExpired(request.getHeader(jwtToken))){
                return true;
            }
            return false;
        }
        return false;
    }
}
