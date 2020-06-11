package de.hska.eshopauth;

import org.springframework.core.io.ClassPathResource;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

class KeyConfig {
    private static final String KEY_STORE_FILE = ".keystore-oauth2-demo";
    private static final String KEY_STORE_PASSWORD = "admin1234";
    private static final String KEY_ALIAS = "oauth2-demo-key";
    @SuppressWarnings("deprecation")
    private static KeyStoreKeyFactory KEY_STORE_KEY_FACTORY = new KeyStoreKeyFactory(
            new ClassPathResource(KEY_STORE_FILE), KEY_STORE_PASSWORD.toCharArray());
    @SuppressWarnings("deprecation")
    static final String VERIFIER_KEY_ID = new String(Base64.encode(KeyGenerators.secureRandom(32).generateKey()));

    static RSAPublicKey getVerifierKey() {
        return (RSAPublicKey) getKeyPair().getPublic();
    }

    static RSAPrivateKey getSignerKey() {
        return (RSAPrivateKey) getKeyPair().getPrivate();
    }

    @SuppressWarnings("deprecation")
    private static KeyPair getKeyPair() {
        return KEY_STORE_KEY_FACTORY.getKeyPair(KEY_ALIAS);
    }
}