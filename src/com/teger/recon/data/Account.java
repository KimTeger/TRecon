package com.teger.recon.data;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private static List<Account> accounts = new ArrayList<>();
	
	private String ID, PW;
	private String OTP;
	
	public Account(String ID, String PW) {
		this.ID = ID;
		this.PW = PW;
	}
	
	public String getID() {
		return ID;
	}
	
	public String getPW() {
		return PW;
	}
	
	public String getOTP() {
		return OTP;
	}
	
	public void setOTP(String OTP) {
		this.OTP = OTP;
	}
	
	public static Account getAccount(String ID) {
		for(Account acc : accounts)
			if(acc.getID().equalsIgnoreCase(ID))
				return acc;
		return null;
	}
	
	public static void addAcount(Account acc) {
		accounts.add(acc);
	}
	
	public static void removeAcount(String ID) {
		Account acc = getAccount(ID);
		if(acc == null) return;
		accounts.remove(acc);
	}
	
	public static List<Account> getAccounts(){
		return accounts;
	}
}
