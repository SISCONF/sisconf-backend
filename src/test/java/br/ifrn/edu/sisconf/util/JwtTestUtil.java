package br.ifrn.edu.sisconf.util;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.oauth2.jwt.Jwt;

import br.ifrn.edu.sisconf.domain.Person;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;

public class JwtTestUtil {
    public static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
        "MySuperSecretTestKeyForJWT1234567890123456".getBytes()
    );

    public static String getToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                .signWith(SECRET_KEY, SIG.HS256)
                .compact();
    }

    public static Jwt getJwt(String token, Person person) {
        if (token == null) {
            token = getToken(person.getEmail());
        }

        return new Jwt(
            getToken(person.getEmail()), Instant.now(), Instant.now().plusSeconds(3600),
            Map.of("alg", "HS256"),
            Map.of(
                "preferred_username", person.getEmail(), 
                "email", person.getEmail(),
                "sub", person.getKeycloakId()
            )
        );
    }
}
