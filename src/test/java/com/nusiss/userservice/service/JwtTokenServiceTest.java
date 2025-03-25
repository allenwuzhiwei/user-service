package com.nusiss.userservice.service;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenServiceTest {

    @Test
    void testGenerateToken_ValidInput_ShouldGenerateToken() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";

        // Act
        String token = JwtTokenService.generateToken(username, password);

        // Assert
        assertNotNull(token, "Token should not be null");
        assertTrue(token.startsWith("eyJ"), "Token should start with 'eyJ' (JWT format)");
    }

    @Test
    void testGenerateToken_Expiration_ShouldNotExpireImmediately() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";

        // Act
        String token = JwtTokenService.generateToken(username, password);

        // Assert
        Date expirationDate = Jwts.parserBuilder()
                .setSigningKey(JwtTokenService.SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        // Assert that the expiration date is in the future (within 30 minutes)
        assertTrue(expirationDate.after(new Date()), "Token expiration time should be in the future");
    }

    @Test
    void testGenerateToken_ShouldContainUsernameAndPasswordInClaims() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";

        // Act
        String token = JwtTokenService.generateToken(username, password);

        // Assert
        String extractedUsername = Jwts.parserBuilder()
                .setSigningKey(JwtTokenService.SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class);

        String extractedPassword = Jwts.parserBuilder()
                .setSigningKey(JwtTokenService.SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("password", String.class);

        assertEquals(username, extractedUsername, "Username in token should match the input");
        assertEquals(password, extractedPassword, "Password in token should match the input");
    }


}