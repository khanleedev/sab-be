package org.project.social_account_business.utils;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

@Slf4j
public class AESUtils {
    private static final String SECRET_KEY = "MRTDeathZone1011";
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";

    public static String encrypt(String input, boolean zipEnable) {
        return encrypt(SECRET_KEY, input, zipEnable);
    }

    public static String decrypt(String input, boolean zipEnable) {
        return decrypt(SECRET_KEY, input, zipEnable);
    }

    private static String encrypt(String encodeKey, String inputStr, boolean zipEnable) {
        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            SecretKeySpec secretKeySpec = new SecretKeySpec(getValidKey(encodeKey), "AES");

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] inputBytes = inputStr.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedBytes = cipher.doFinal(inputBytes);

            log.info("Input bytes length: {}", inputBytes.length);
            log.info("Encrypted bytes length: {}", encryptedBytes.length);

            if (zipEnable) {
                return compressAndEncode(encryptedBytes);
            } else {
                return Base64.getEncoder().encodeToString(encryptedBytes);
            }
        } catch (Exception ex) {
            log.error("Encryption error: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to encrypt data.", ex);
        }
    }

    private static String decrypt(String encodeKey, String encryptedStr, boolean zipEnable) {
        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            SecretKeySpec secretKeySpec = new SecretKeySpec(getValidKey(encodeKey), "AES");

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedStr);
            byte[] decryptedBytes;

            if (zipEnable) {
                decryptedBytes = decompressAndDecrypt(decodedBytes, cipher);
            } else {
                decryptedBytes = cipher.doFinal(decodedBytes);
            }

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            log.error("Decryption error: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to decrypt data.", ex);
        }
    }

    private static byte[] getValidKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] validKey = new byte[16]; // AES requires 128-bit keys (16 bytes)

        System.arraycopy(keyBytes, 0, validKey, 0, Math.min(keyBytes.length, validKey.length));
        return validKey;
    }

    private static String compressAndEncode(byte[] data) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(outputStream)) {
            deflaterOutputStream.write(data);
        }
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    private static byte[] decompressAndDecrypt(byte[] data, Cipher cipher) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        InflaterInputStream inflaterInputStream = new InflaterInputStream(inputStream);

        byte[] decompressedBytes = inflaterInputStream.readAllBytes();
        return cipher.doFinal(decompressedBytes);
    }
}
