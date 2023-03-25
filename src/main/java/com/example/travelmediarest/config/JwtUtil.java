package com.example.travelmediarest.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    @Value("${jwtSecret}")
    private String secret="secrete";
    @Value("${jwtExp}")
    private Long jwtExpirationInMs=1000L;

    public String generateToken(String username) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject("travel media")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + jwtExpirationInMs))
                .withIssuer("travel media agency")
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("travel media")
                .withIssuer("travel media agency")
                .build();
        DecodedJWT decoderJWT = jwtVerifier.verify(token);
        return decoderJWT.getClaim("username").asString();
    }
}
