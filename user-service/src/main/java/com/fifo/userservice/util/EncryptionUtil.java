package com.fifo.userservice.util;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtil {
    private static final String ALGORITHM = "AES";

    private static SecretKey secretKey;

    // application.properties에서 암호화 키를 불러옴
    @Value("${encryption.key}")
    public void setKey(String base64Key) {
        secretKey = new SecretKeySpec(Base64.getDecoder().decode(base64Key), ALGORITHM);
    }

    // 문자열을 AES로 암호화
    @SneakyThrows
    public static String encrypt(String plainText) {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 암호화된 문자열을 AES로 복호화
    @SneakyThrows
    public static String decrypt(String encryptedText) {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}
