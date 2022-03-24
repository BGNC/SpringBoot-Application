package io.bgnc.SpringBootApplication.security;

import io.bgnc.SpringBootApplication.exceptions.SpringBootApplicationException;
import io.bgnc.SpringBootApplication.model.User;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
@RequiredArgsConstructor
public class Jwt {


    private KeyStore keyStore;

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
}
