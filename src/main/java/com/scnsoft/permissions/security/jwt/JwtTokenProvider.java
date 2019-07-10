package com.scnsoft.permissions.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JwtTokenProvider {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_ID = "Token_";
    private static final String AUTHORITIES = "authorities";
    private static final String EMPTY = "";

    @Value("${jwt.token.secret}")
    private String secret;

    private final JwtUserDetailsService userDetailsService;

    public JwtTokenProvider(JwtUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put(AUTHORITIES, getRoleNames(userDetails.getAuthorities()));
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    Authentication getAuthentication(String token) {
        String username = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, EMPTY, userDetails.getAuthorities());
    }

    String resolveToken(HttpServletRequest servletRequest) {
        return Optional.ofNullable(servletRequest.getHeader(AUTHORIZATION_HEADER))
                .filter(token -> token.startsWith(TOKEN_ID))
                .map(token -> token.substring(TOKEN_ID.length() + 1))
                .orElse(EMPTY);
    }

    private List<String> getRoleNames(Collection<? extends GrantedAuthority> authorities) {
        List<String> list = new ArrayList<>();
        authorities.forEach(auth -> list.add(auth.getAuthority()));
        return list;
    }
}