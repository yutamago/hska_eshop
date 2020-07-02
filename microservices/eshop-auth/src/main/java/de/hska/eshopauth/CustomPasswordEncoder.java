package de.hska.eshopauth;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CustomPasswordEncoder implements PasswordEncoder {
    private String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword.toString().equals("userNotFoundPassword")) {
            return rawPassword.toString();
        }

        try {
            return bytesToHex(MessageDigest.getInstance("SHA-256").digest(rawPassword.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            return rawPassword.toString();
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword.startsWith("{noop}")) {
            return rawPassword.toString().equals(encodedPassword.substring(6));
        }
        if (encodedPassword.startsWith("{custom}")) {
            return encode(rawPassword.toString()).equals(encodedPassword.substring("{custom}".length()));
        }
        if (encodedPassword.startsWith("{sha256}")) {
            return encode(rawPassword.toString()).equals(encodedPassword.substring("{sha256}".length()));
        }

        return rawPassword.toString().equals(encodedPassword);
    }
}
