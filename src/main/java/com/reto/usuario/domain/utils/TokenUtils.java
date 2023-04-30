package com.reto.usuario.domain.utils;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.reto.usuario.domain.exceptions.AuthenticationFailedException;
import com.reto.usuario.domain.exceptions.TokenInvalidException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TokenUtils {

    private static final Long ACCESS_TOKEN_VALIDITY_SECONDS = 2_592_000L;
    private static final String ACCESS_TOKEN_SECRET = "8y/B?E(G+KbPeShVmYq3t6w9z$C&F)J@";

    private TokenUtils() {
    }

    public static String createToken(String email, List<String> rol, String name, String lastName) {
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);
        Map<String, Object> extra = new HashMap<>();
        extra.put("name", name);
        extra.put("lastName", lastName);
        extra.put("rol", rol);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .addClaims(extra)
                .signWith(SignatureAlgorithm.HS256, ACCESS_TOKEN_SECRET.getBytes())
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        validateToken(token);
        try {
            JWT jwt = JWTParser.parse(token);
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            String email = claims.getSubject();
            List<String> rol = claims.getStringListClaim("rol");
            return new UsernamePasswordAuthenticationToken(email, null,
                    rol.stream().map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()));
        } catch (ParseException e) {
            throw new AuthenticationFailedException("Error token could not be read");
        }
    }

    public static void validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(ACCESS_TOKEN_SECRET.getBytes()).parseClaimsJws(token);
        } catch (MalformedJwtException e) {
            throw new TokenInvalidException("malformed token");
        } catch (UnsupportedJwtException e) {
            throw new TokenInvalidException("token not supported");
        } catch (ExpiredJwtException e) {
            throw new TokenInvalidException("expired token");
        } catch (IllegalArgumentException e) {
            throw new TokenInvalidException("token empty");
        } catch (SignatureException e) {
            throw new TokenInvalidException("fail in signing");
        }
    }
}
