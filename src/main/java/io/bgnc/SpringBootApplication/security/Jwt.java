package io.bgnc.SpringBootApplication.security;

import io.bgnc.SpringBootApplication.exceptions.SpringBootApplicationException;
import io.bgnc.SpringBootApplication.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.time.Instant;

import static io.jsonwebtoken.Jwts.parser;

@Service
@RequiredArgsConstructor
public class Jwt {


    private KeyStore keyStore;
    @Value(value = "${jwt.expiration.time}")
    private Long jwtExpirationInMillis;


    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream inputStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(inputStream,"secret".toCharArray());
        }
        catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e){
            throw new SpringBootApplicationException("exception handling");
        }

    }

    public String generateToken(Authentication authentication){

        User principalOfUser = (User) authentication.getPrincipal();

        return Jwts.builder().
                setSubject(principalOfUser.getUsername())
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();

    }

    public String generateTokenWithUsername(String username){


        return Jwts.builder().
                setSubject(username)
                .setIssuedAt(java.util.Date. from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();

    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("springblog","secret".toCharArray());
        }
        catch(KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e){
            throw new SpringBootApplicationException("Exception thrown");
        }

    }

    public boolean validateToken(String jwt){
        parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(jwt);
        return true;
    }

    /**
     * Copy of private key
     * @return
     */
    private PublicKey getPublicKey(){
        try {
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringBootApplicationException("Exception occured while retrieved public key ");
        }
    }

    public String getUsernameFromJwt(String token){

        Claims claims = parser().
                setSigningKey(getPublicKey()).
                parseClaimsJws(token).
                getBody();

        return claims.getSubject();
    }
    public Long getJwtExpirationInMillis(){
        return this.jwtExpirationInMillis;
    }
}
