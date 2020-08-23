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
			cs.sendMessage(ChatColor.RED + "[TRecon] /rc help �� ���� ������ Ȯ���ϼ���.");
			return true;
		} else {
			if(a[0].equalsIgnoreCase("create")) {
				try {
					if(a.length != 3) {
						cs.sendMessage(ChatColor.RED + "[TRecon] /rc help �� ���� ������ Ȯ���ϼ���.");
						return true;
					}
					Account acc = Account.getAccount(Encryptor.getEncrpytMD5(a[1]));
					if(acc != null) {
						cs.sendMessage(ChatColor.RED + "[TRecon] �̹� �����ϴ� ���̵��Դϴ�.");
						return true;
					}
					acc = new Account(Encryptor.getEncrpytMD5(a[1]), Encryptor.getEncrpytMD5(a[2]));
					Account.addAcount(acc);
					cs.sendMessage(ChatColor.GREEN + "[TRecon] ������ �����߽��ϴ�.");
					return true;
				} catch (NoSuchAlgorithmException e) {
					cs.sendMessage(ChatColor.RED + "[TRecon] ���� ���� �� ������ �߻��߽��ϴ�.");
					return true;
				}
			} else if(a[0].equalsIgnoreCase("delete")) {
				
			} else if(a[0].equalsIgnoreCase("otp")) {
				try {
					if(a.length != 3) {
						cs.sendMessage(ChatColor.RED + "[TRecon] /rc help �� ���� ������ Ȯ���ϼ���.");
						return true;
					}
					Account acc = Account.getAccount(Encryptor.getEncrpytMD5(a[1]));
					if(acc == null) {
						cs.sendMessage(ChatColor.RED + "[TRecon] �������� �ʴ� ���̵��Դϴ�.");
						return true;
					}
					if(!Authenticator.isMatchedAccount(a[1], a[2])) {
						cs.sendMessage(ChatColor.RED + "[TRecon] ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
						return true;
					}
					if(acc.getOTP() == null) {
						cs.sendMessage("[TRecon] OTP Ű�� �����ϴ� ���Դϴ�...");
						Authenticator.generateOTPKey(acc.getID());
						cs.sendMessage("[TRecon] OTP Ű�� �����߽��ϴ�. [ " + acc.getOTP() + " ]");	
					} else {
						cs.sendMessage("[TRecon] ������� OTP Ű [ " + acc.getOTP() + " ]");	
					}
					return true;
				} catch (NoSuchAlgorithmException e) {
					cs.sendMessage(ChatColor.RED + "[TRecon] ���� ���� �� ������ �߻��߽��ϴ�.");
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
