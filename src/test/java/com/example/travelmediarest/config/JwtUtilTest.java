package com.example.travelmediarest.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Base64;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class JwtUtilTest {

    @Autowired
    JwtUtil jwtUtil;

    private String token;

    @Test
//    @Disabled
    void generateToken() {
        JwtUtil jwtUtil = new JwtUtil();
        String response = jwtUtil.generateToken("himon");
        token = response;
        assertThat(response).isNotEmpty();
//        assertEquals(response, response);
    }
    @Test
//    @Disabled
    void validateTokenAndRetrieveSubject() {
        JwtUtil jwtUtil = new JwtUtil();
        String token = jwtUtil.generateToken("himon");
        String response = jwtUtil.validateTokenAndRetrieveSubject(token);
        assertEquals("himon", response);
    }

    @Test
    void validateTokenAndRetrieveObjectTest(){
        JwtUtil jwtUtil = new JwtUtil();
        String token = jwtUtil.generateToken("himon");

        DecodedJWT jwt = JWT.decode(token);
        assertThat(jwt).isNotNull();
        assertThat(jwt.getClaim("username").asString()).isEqualTo("himon");
        assertThat(jwt.getSubject()).isEqualTo("travel media");
        assertThat(jwt.getIssuer()).isEqualTo("travel media agency");
        assertThat(jwt.getHeader()).isNotNull();
        assertThat(jwt.getExpiresAt()).isNotNull();
        assertThat(jwt.getIssuedAt()).isNotNull();

//        assertThat(jwt, is(notNullValue()));
//        assertThat(jwt.getClaims(), is(notNullValue()));
//        assertThat(jwt.getClaims(), is(instanceOf(Map.class)));
//        assertThat(jwt.getClaims().get("exp"), is(notNullValue()));
//        assertThat(jwt.getClaims().get("iat"), is(notNullValue()));
//        assertThat(jwt.getClaims().get("nbf"), is(notNullValue()));
//        assertThat(jwt.getClaims().get("jti"), is(notNullValue()));
//        assertThat(jwt.getClaims().get("aud"), is(notNullValue()));
//        assertThat(jwt.getClaims().get("sub"), is(notNullValue()));
//        assertThat(jwt.getClaims().get("iss"), is(notNullValue()));
//        assertThat(jwt.getClaims().get("extraClaim"), is(notNullValue()));
//        assertThat(jwt.getClaim("username")).isEqualTo("himon");

    }

}