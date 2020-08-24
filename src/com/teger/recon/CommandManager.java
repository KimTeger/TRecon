package com.teger.recon;

import java.security.NoSuchAlgorithmException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.teger.recon.data.Account;
import com.teger.recon.data.Encryptor;
import com.teger.recon.socket.Authenticator;

public class CommandManager implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] a) {
		if(a.length < 1) {
			cs.sendMessage(ChatColor.RED + "[TRecon] Type [/rc help] for help. ");
			return true;
		} else {
			if(a[0].equalsIgnoreCase("create")) {
				try {
					if(a.length != 3) {
						cs.sendMessage(ChatColor.RED + "[TRecon] Type [/rc help] for help.");
						return true;
					}
					Account acc = Account.getAccount(Encryptor.getEncrpytMD5(a[1]));
					if(acc != null) {
						cs.sendMessage(ChatColor.RED + "[TRecon] Already exists account name.");
						return true;
					}
					acc = new Account(Encryptor.getEncrpytMD5(a[1]), Encryptor.getEncrpytMD5(a[2]));
					Account.addAcount(acc);
					cs.sendMessage(ChatColor.GREEN + "[TRecon] Successfully created account.");
					return true;
				} catch (NoSuchAlgorithmException e) {
					cs.sendMessage(ChatColor.RED + "[TRecon] An error occured while creating account.");
					return true;
				}
			} else if(a[0].equalsIgnoreCase("delete")) {
				
			} else if(a[0].equalsIgnoreCase("otp")) {
				try {
					if(a.length != 3) {
						cs.sendMessage(ChatColor.RED + "[TRecon] Type [/rc help] for help.");
						return true;
					}
					Account acc = Account.getAccount(Encryptor.getEncrpytMD5(a[1]));
					if(acc == null) {
						cs.sendMessage(ChatColor.RED + "[TRecon] Account does not exist.");
						return true;
					}
					if(!Authenticator.isMatchedAccount(a[1], a[2])) {
						cs.sendMessage(ChatColor.RED + "[TRecon] Password not correct.");
						return true;
					}
					if(acc.getOTP() == null) {
						cs.sendMessage("[TRecon] Creating OTP Key ...");
						Authenticator.generateOTPKey(acc.getID());
						cs.sendMessage("[TRecon] Successfully created OTP Key. [ " + acc.getOTP() + " ]");	
					} else {
						cs.sendMessage("[TRecon] OTP Key [ " + acc.getOTP() + " ]");	
					}
					return true;
				} catch (NoSuchAlgorithmException e) {
					cs.sendMessage(ChatColor.RED + "[TRecon] An error occured while creating account.");
					return true;
				}
			} else if(a[0].equalsIgnoreCase("rotp")) {
				
			} else if(a[0].equalsIgnoreCase("permission")) {
				
			} else if(a[0].equalsIgnoreCase("connect")) {
				
			} else if(a[0].equalsIgnoreCase("help")){
				
			} else if(a[0].equalsIgnoreCase("testlogin")) {
				if(Authenticator.isMatchedAccount(a[1], a[2])) {
					cs.sendMessage("Login Success.");
				} else {
					cs.sendMessage("Login Fail.");
				}
			} else if(a[0].equalsIgnoreCase("testotp")) {
				if(Authenticator.isMatchedOTP(a[1],a[2])) {
					cs.sendMessage("OTP Success.");
				} else {
					cs.sendMessage("OTP Fail.");
				}
			}
		}
		return false;
	}

	
}
