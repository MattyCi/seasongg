package com.seasongg.config.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    @Autowired
    CryptoUtil cryptoUtil;

    @Value("${SGG_JWT_KEY}")
    private String jwtKey;

    private String secretKey;

    private static final long SESSION_TIMEOUT = 1000 * 60 * 60 * 6;

    @PostConstruct
    public void init() {
        secretKey = cryptoUtil.decrypt(jwtKey);
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {

        Date currentDate = new Date(System.currentTimeMillis());
        Date expireDate = new Date(System.currentTimeMillis() + SESSION_TIMEOUT);

        byte[] decodedSecretKey = Decoders.BASE64.decode(secretKey);

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(currentDate).setExpiration(expireDate)
                .signWith(Keys.hmacShaKeyFor(decodedSecretKey), SignatureAlgorithm.HS256).compact();
    }

    public String extractUsername(String token) throws JwtException {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws JwtException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

}
