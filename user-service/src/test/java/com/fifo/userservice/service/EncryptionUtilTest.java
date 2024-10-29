package com.fifo.userservice.service;

import com.fifo.userservice.util.EncryptionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EncryptionUtilTest {

    @Test
    void encrypt() throws Exception {
        String plainText = "안녕";
        String encrypted = EncryptionUtil.encrypt(plainText);
        String decrypted = EncryptionUtil.decrypt(encrypted);

        assertThat(plainText).isNotEqualTo(encrypted);
        assertThat(decrypted).isEqualTo(plainText);
    }

    @Test
    void generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // AES 128비트 키 생성
        SecretKey secretKey = keyGenerator.generateKey();
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        assertThat(base64Key).isNotEmpty();
    }
}