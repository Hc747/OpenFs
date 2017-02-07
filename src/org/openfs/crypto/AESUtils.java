package org.openfs.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * A simple utility class for easily encrypting and decrypting data using the AES ALGORITHM.
 * 
 *  @author Chad Adams
 */
public class AESUtils {

	private static final String ALGORITHM = "AES";

	private AESUtils() {

	}

	public static SecretKey generateKey() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
			keyGenerator.init(128);
			return keyGenerator.generateKey();
		} catch (Exception ex) {

		}
		return null;
	}

	public static SecretKey createKey(String password) {
		try {
			byte[] key = password.getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit

			return new SecretKeySpec(key, ALGORITHM);
		} catch (Exception ex) {

		}

		return null;
	}

	public static SecretKey createKey(byte[] salt, String password) {
		try {
			byte[] key = (salt + password).getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit

			return new SecretKeySpec(key, ALGORITHM);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static void writeKey(SecretKey key, File file) throws IOException {
		try (FileOutputStream fis = new FileOutputStream(file)) {
			fis.write(key.getEncoded());
		}
	}

	public static SecretKey getSecretKey(File file) throws IOException {
		return new SecretKeySpec(Files.readAllBytes(file.toPath()), ALGORITHM);
	}

	public static byte[] encrypt(SecretKey secretKey, byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return cipher.doFinal(data);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;

	}

	public static byte[] decrypt(String password, byte[] encrypted) {		
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, AESUtils.createKey(password));
			return cipher.doFinal(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static byte[] decrypt(SecretKey secretKey, byte[] encrypted) {		
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return cipher.doFinal(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
