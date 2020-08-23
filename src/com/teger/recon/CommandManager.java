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
			cs.sendMessage(ChatColor.RED + "[TRecon] /rc help 를 통해 도움말을 확인하세요.");
			return true;
		} else {
			if(a[0].equalsIgnoreCase("create")) {
				try {
					if(a.length != 3) {
						cs.sendMessage(ChatColor.RED + "[TRecon] /rc help 를 통해 도움말을 확인하세요.");
						return true;
					}
					Account acc = Account.getAccount(Encryptor.getEncrpytMD5(a[1]));
					if(acc != null) {
						cs.sendMessage(ChatColor.RED + "[TRecon] 이미 존재하는 아이디입니다.");
						return true;
					}
					acc = new Account(Encryptor.getEncrpytMD5(a[1]), Encryptor.getEncrpytMD5(a[2]));
					Account.addAcount(acc);
					cs.sendMessage(ChatColor.GREEN + "[TRecon] 계정을 생성했습니다.");
					return true;
				} catch (NoSuchAlgorithmException e) {
					cs.sendMessage(ChatColor.RED + "[TRecon] 계정 생성 중 오류가 발생했습니다.");
					return true;
				}
			} else if(a[0].equalsIgnoreCase("delete")) {
				
			} else if(a[0].equalsIgnoreCase("otp")) {
				try {
					if(a.length != 3) {
						cs.sendMessage(ChatColor.RED + "[TRecon] /rc help 를 통해 도움말을 확인하세요.");
						return true;
					}
					Account acc = Account.getAccount(Encryptor.getEncrpytMD5(a[1]));
					if(acc == null) {
						cs.sendMessage(ChatColor.RED + "[TRecon] 존재하지 않는 아이디입니다.");
						return true;
					}
					if(!Authenticator.isMatchedAccount(a[1], a[2])) {
						cs.sendMessage(ChatColor.RED + "[TRecon] 비밀번호가 일치하지 않습니다.");
						return true;
					}
					if(acc.getOTP() == null) {
						cs.sendMessage("[TRecon] OTP 키를 생성하는 중입니다...");
						Authenticator.generateOTPKey(acc.getID());
						cs.sendMessage("[TRecon] OTP 키를 생성했습니다. [ " + acc.getOTP() + " ]");	
					} else {
						cs.sendMessage("[TRecon] 사용중인 OTP 키 [ " + acc.getOTP() + " ]");	
					}
					return true;
				} catch (NoSuchAlgorithmException e) {
					cs.sendMessage(ChatColor.RED + "[TRecon] 계정 생성 중 오류가 발생했습니다.");
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
