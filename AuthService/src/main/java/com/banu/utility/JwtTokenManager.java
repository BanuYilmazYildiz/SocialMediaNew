package com.banu.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.banu.exception.AuthServiceException;
import com.banu.exception.ErrorType;
import com.banu.utility.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenManager {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    private Long expireDate = System.currentTimeMillis() + (1000 * 3000);

    public Optional<String> createToken(Long authId) {
        String token = "";
        try {
            token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("authId", authId)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(expireDate))
                    .sign(Algorithm.HMAC512(secretKey));
            return Optional.of(token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<String> createToken(Long authId, Role role) {
        String token = "";
        try {
            token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("authId", authId)
                    .withClaim("role", role.toString())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(expireDate))
                    .sign(Algorithm.HMAC512(secretKey));
            return Optional.of(token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Boolean validationToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            if (decodedJWT == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new AuthServiceException(ErrorType.INVALID_TOKEN);
        }
    }

    public Optional<Long> getIdFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            if (decodedJWT == null) {
                throw new AuthServiceException(ErrorType.INVALID_TOKEN);
            }
            Long authId = decodedJWT.getClaim("authId").asLong();
            return Optional.of(authId);
        } catch (Exception e) {
            throw new AuthServiceException(ErrorType.INVALID_TOKEN);
        }
    }

    public Optional<String > getRoleFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            if (decodedJWT == null) {
                throw new AuthServiceException(ErrorType.INVALID_TOKEN);
            }
            String role = decodedJWT.getClaim("role").asString();
            return Optional.of(role);
        } catch (Exception e) {
            throw new AuthServiceException(ErrorType.INVALID_TOKEN);
        }
    }


}
