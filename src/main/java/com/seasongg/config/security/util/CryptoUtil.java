package com.seasongg.config.security.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

@Service
public class CryptoUtil implements ApplicationContextAware {

    @Autowired
    private ApplicationArguments applicationArguments;

    private static final String ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5PADDING";

    private static final Logger LOG = LoggerFactory.getLogger(CryptoUtil.class);
	private static final String SGG_KEY_MISSING = "Application decryption key missing. Shutting down.";
    private static final String UNEXPECTED_ERROR = "Unexpected error occurred during encryption/decryption.";

	private ApplicationContext applicationContext;

	public String encrypt(String plaintext) {

        try {

            byte[] iv = generateIv();

            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, iv);

            byte[] byteCipherText = cipher.doFinal(plaintext.getBytes());

            String encodedCipherText = Base64.getEncoder().withoutPadding().encodeToString(byteCipherText);
            String encodedIv = Base64.getEncoder().encodeToString(iv);

            return encodedCipherText + "." + encodedIv;

        } catch (Exception e) {
            LOG.error(UNEXPECTED_ERROR, e);
        }

        return null;
    }

    public String decrypt(String ciphertext) {

        try {

            getSecretKey();

            String[] cipherTextWithIv = ciphertext.split("\\.");

            byte[] decodedCiphertext = Base64.getDecoder().decode(cipherTextWithIv[0]);
            byte[] decodedIv = Base64.getDecoder().decode(cipherTextWithIv[1]);

            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, decodedIv);

            byte[] byteDecryptedText = cipher.doFinal(decodedCiphertext);

            return new String(byteDecryptedText);

        } catch (Exception e) {
            LOG.error(UNEXPECTED_ERROR, e);
        }

        return null;
    }

    private Cipher getCipher(int mode, byte[] iv) throws GeneralSecurityException {

        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(mode, getSecretKey(), new IvParameterSpec(iv));

        return cipher;

    }

    private SecretKey getSecretKey() {

        byte[] decodedKey = Base64.getDecoder().decode(getSggKey());
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);

    }

    private String getSggKey() {

		String[] applicationArgs = applicationArguments.getSourceArgs();
		String sggKey = null;

		if (applicationArgs != null && applicationArgs.length > 0) {

			sggKey = applicationArgs[0];

		} else {

			LOG.error(SGG_KEY_MISSING);
			int exitCode = SpringApplication.exit(applicationContext, () -> 13);
			System.exit(exitCode);

		}

		return sggKey;

    }

    private static byte[] generateIv() {

        final int AES_KEY_LENGTH = 256;
        byte[] iv = new byte[AES_KEY_LENGTH / 16];
        SecureRandom prng = new SecureRandom();
        prng.nextBytes(iv);

        return iv;
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
