package co.com.crediya.api.security;

import co.com.crediya.api.dto.JwtUserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {
    
    private final SecretKey secretKey;
    
    public JwtService(@Value("${app.jwt.secret}") String jwtSecret) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        log.info("JWT Service initialized successfully");
    }
    
    public JwtUserInfo extractUserInfo(String token) {
        Claims claims = extractAllClaims(token);

        String identityDocument = claims.getSubject();
        String email = claims.get("email", String.class);
        Integer roleId = claims.get("roleId", Integer.class);
        String roleName = claims.get("roleName", String.class);

        return new JwtUserInfo(identityDocument, email, roleId, roleName);
    }
    
    public Date extractExpiration(String token) {
        try {
            Date expiration = extractClaim(token, Claims::getExpiration);
            if (expiration == null) {
                throw new RuntimeException("JWT token missing expiration claim");
            }
            return expiration;
        } catch (Exception e) {
            log.debug("Error extracting expiration from JWT: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token - cannot extract expiration", e);
        }
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public Boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);

            // Verificar si tiene claim de expiración
            if (claims.getExpiration() == null) {
                log.warn("JWT token does not have expiration claim");
                return false;
            }

            // Verificar si está expirado
            return !isTokenExpired(token);

        } catch (Exception e) {
            log.debug("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }
    
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.warn("JWT signature validation failed");
            throw new RuntimeException("JWT signature validation failed", e);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.warn("JWT token has expired");
            throw new RuntimeException("JWT token expired", e);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            log.warn("JWT token is malformed");
            throw new RuntimeException("Malformed JWT token", e);
        } catch (Exception e) {
            log.debug("Error parsing JWT token: {}", e.getMessage());
            throw new RuntimeException("Failed to parse JWT token", e);
        }
    }
}