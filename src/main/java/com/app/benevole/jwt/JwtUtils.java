package com.app.benevole.jwt;

import com.app.benevole.exception.*;
import com.app.benevole.security.CustomUserDetails;
import com.app.benevole.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    private final UserDetailsServiceImpl userDetailsService;

    public JwtUtils(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Value(value = "${application.security.expireAccessToken}")
    private Long EXPIRE_ACCESS_TOKEN;

    @Value(value = "${application.security.secretKey}")
    private String SECRET_KEY;

    public String generateUserAccessToken(String email) {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);

        StringBuilder roles = new StringBuilder();
        userDetails.getAuthorities().forEach(role -> {
            roles.append(role.getAuthority()).append(" ");
        });
        return Jwts.builder()
                .setSubject(userDetails.getId().toString())
                .setIssuer(roles.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + EXPIRE_ACCESS_TOKEN))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String generateTokenFromEmail(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        StringBuilder roles = new StringBuilder();
        userDetails.getAuthorities().forEach(role -> {
            roles.append(role.getAuthority()).append(" ");
        });
        return Jwts.builder()
                .setSubject(email)
                .setIssuer(roles.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + EXPIRE_ACCESS_TOKEN))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateJwtToken(String authToken) throws JwtMalformedException, JwtExpiredException, JwtUnsupportedException,
            JwtIllegalArgumentException, JwtSignatureException {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            LOGGER.error("JwtUtils | validateJwtToken | Invalid JWT signature: {}", e.getMessage());
            throw new JwtSignatureException(e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("JwtUtils | validateJwtToken | Invalid JWT token: {}", e.getMessage());
            throw new JwtMalformedException(e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JwtUtils | validateJwtToken | JWT token is expired: {}", e.getMessage());
            throw new JwtExpiredException(e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JwtUtils | validateJwtToken | JWT token is unsupported: {}", e.getMessage());
            throw new JwtUnsupportedException(e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JwtUtils | validateJwtToken | JWT claims string is empty: {}", e.getMessage());
            throw new JwtIllegalArgumentException(e.getMessage());
        }
    }

    public String getSubjectFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public String generateJwtToken(String email) {
        return generateUserAccessToken(email);
    }
}
