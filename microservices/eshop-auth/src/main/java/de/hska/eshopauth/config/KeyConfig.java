package de.hska.eshopauth.config;

import com.nimbusds.jose.util.Base64;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
class KeyConfig {
    private static final String KEY_STORE_FILE = ".eshop-keystore-oauth2";
    private static final String KEY_STORE_PASSWORD = "123456";
    private static final String KEY_ALIAS = "eshop-oauth2-key";
    @SuppressWarnings("deprecation")
    private static KeyStoreKeyFactory KEY_STORE_KEY_FACTORY = new KeyStoreKeyFactory(
            new ClassPathResource(KEY_STORE_FILE), KEY_STORE_PASSWORD.toCharArray());
    @SuppressWarnings("deprecation")
    static final String VERIFIER_KEY_ID = String.valueOf(Base64.encode(KeyGenerators.secureRandom(32).generateKey()));

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