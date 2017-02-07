package org.openfs.crypto;

import javax.crypto.Cipher;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * A simple utility class for easily encrypting and decrypting data using the RSA ALGORITHM.
 * 
 *  @author Chad Adams
 */
public class RSAUtils {

	private static final String ALGORITHM = "RSA";

	private RSAUtils() {
		
	}

	public static boolean generateKey(String publicKeyOutput, String privateKeyOutput) {
		try {
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
			keyGen.initialize(2048);

			final KeyPair key = keyGen.generateKeyPair();

			try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(publicKeyOutput)))) {
				dos.write(key.getPublic().getEncoded());
			}

			try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(privateKeyOutput)))) {
				dos.write(key.getPrivate().getEncoded());
			}
			
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public static byte[] encrypt(PublicKey key, byte[] data) {		
		try {

			final Cipher cipher = Cipher.getInstance(ALGORITHM);

			cipher.init(Cipher.ENCRYPT_MODE, key);

			return cipher.doFinal(data);

		} catch (Exception ex) {

		}

		return null;

	}

	public static byte[] decrypt(PrivateKey key, byte[] encryptedData) {		

		try {

			final Cipher cipher = Cipher.getInstance(ALGORITHM);

			cipher.init(Cipher.DECRYPT_MODE, key);

			return cipher.doFinal(encryptedData);

		} catch (Exception ex) {

		}

		return null;

	}

	public static PublicKey getPublicKey(String publicKeyPath) throws Exception {
		return KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(Files.readAllBytes(Paths.get(publicKeyPath))));
	}

	public static PrivateKey getPrivateKey(String privateKeyPath) throws Exception {
		return KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(Files.readAllBytes(Paths.get(privateKeyPath))));
	}

	public static PublicKey getPublicKey(byte[] encryptedPublicKey) throws Exception {
		return KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(encryptedPublicKey));
	}

	public static PrivateKey getPrivateKey(byte[] encryptedPrivateKey) throws Exception {
		return KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(encryptedPrivateKey));
	}

}
