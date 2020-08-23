package com.teger.recon.data;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class Encryptor {

	public static String getEncrpytMD5(String text) throws NoSuchAlgorithmException {
		StringBuffer buf = new StringBuffer();
		MessageDigest mDigest = MessageDigest.getInstance("MD5");
		mDigest.update(text.getBytes());
		byte[] str = mDigest.digest();
		
		for(int i = 0; i < str.length; i ++) {
			String tmp = Integer.toHexString((int)str[i] & 0x00ff);
			buf.append(tmp);
		}
		return buf.toString();
	}
	
	public static String getEncrpytAES(String text, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] bytes = key.getBytes();
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(bytes);
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, sr);
		
		SecretKey skey = kgen.generateKey();
		SecretKeySpec skeySpec = new SecretKeySpec(skey.getEncoded(), "AES");
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.ENCRYPT_MODE, skeySpec);
		
		byte[] encrypted = c.doFinal(text.getBytes());
		return Hex.encodeHexString(encrypted);
	}
	
	public static String getDecrpytAES(String text, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, DecoderException {
		byte[] bytes = key.getBytes();
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(bytes);
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, sr);
		
		SecretKey skey = kgen.generateKey();
		SecretKeySpec skeySpec = new SecretKeySpec(skey.getEncoded(), "AES");
		
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = c.doFinal(Hex.decodeHex(text.toCharArray()));
		return new String(decrypted);

	}
}
