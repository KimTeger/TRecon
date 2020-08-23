package com.teger.recon.socket;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;

import com.teger.recon.data.Account;
import com.teger.recon.data.Encryptor;

public class Authenticator {

	public static boolean isMatchedAccount(String id, String pw) {
		Account acc = null;
		try {
			acc = Account.getAccount(Encryptor.getEncrpytMD5(id));
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(acc == null) return false;
		try {
			return acc.getPW().equals(Encryptor.getEncrpytMD5(pw));
		} catch (NoSuchAlgorithmException e) {
			return false;
		}
	}
	
	public static void createAccount(String id, String pw) {
		try {
			Account acc = Account.getAccount(Encryptor.getEncrpytMD5(id));
			if(acc != null) return; //Exception
			acc = new Account(Encryptor.getEncrpytMD5(id), Encryptor.getEncrpytMD5(pw));
			Account.addAcount(acc);
		} catch (NoSuchAlgorithmException e) {
			//Exception
		}
	}
	
	public static void deleteAccount(String id) {
		Account.removeAcount(id);
	}
	
	public static void generateOTPKey(String id) {
		Account acc = Account.getAccount(id);
		if(acc == null) return;
		if(acc.getOTP() != null) return;
		String key = generate(id, "rcon");
		acc.setOTP(key);
	}
	
	public static boolean isMatchedOTP(String id, String otp) {
		Account acc = null;
		try {
			acc = Account.getAccount(Encryptor.getEncrpytMD5(id));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(acc == null) return false;
		return checkCode(otp, acc.getOTP());
	}
	
	//Google Authenticator Source Code
	
	private static String generate(String userName, String hostName) {
	    byte[] buffer = new byte[5 + 5 * 5];
	    new Random().nextBytes(buffer);
	    Base32 codec = new Base32();
	    byte[] secretKey = Arrays.copyOf(buffer, 10);
	    byte[] bEncodedKey = codec.encode(secretKey);
	    return new String(bEncodedKey);
	}
	
	private static boolean checkCode(String userCode, String otpkey) {
	    long otpnum = Integer.parseInt(userCode);
	    long wave = new Date().getTime() / 30000;
	    boolean result = false;
	    try {
	        Base32 codec = new Base32();
	        byte[] decodedKey = codec.decode(otpkey);
	        int window = 3;
	        for (int i = -window; i <= window; ++i) {
	            long hash = verify_code(decodedKey, wave + i);
	            if (hash == otpnum) result = true;
	        }
	    } catch (InvalidKeyException | NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return result;
	}

	private static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
	    byte[] data = new byte[8];
	    long value = t;
	    for (int i = 8; i-- > 0; value >>>= 8) {
	        data[i] = (byte) value;
	    }
	    SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
	    Mac mac = Mac.getInstance("HmacSHA1");
	    mac.init(signKey);
	    byte[] hash = mac.doFinal(data);
	    int offset = hash[20 - 1] & 0xF;
	    long truncatedHash = 0;
	    for (int i = 0; i < 4; ++i) {
	        truncatedHash <<= 8;
	        truncatedHash |= (hash[offset + i] & 0xFF);
	    }
	    truncatedHash &= 0x7FFFFFFF;
	    truncatedHash %= 1000000;
	    return (int) truncatedHash;
	}
}
