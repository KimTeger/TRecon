package com.teger.recon.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager {

	private static HashMap<String, String> configvalue = new HashMap<>();
	
	private final static File datafolder = new File("plugins/TRecon"),
			config = new File("plugins/TRecon/config.yml"),
			account = new File("plugins/TRecon/account.yml");
	
	public static void ReadFileConfiguration() {
		checkFile();
		FileConfiguration fconfig = YamlConfiguration.loadConfiguration(config);
		FileConfiguration faccount = YamlConfiguration.loadConfiguration(account);
		for(String id : faccount.getKeys(false)) {
			Account acc = new Account(id, faccount.getString(id + ".PW"));
			String otp = faccount.getString(id + ".OTP");
			if(otp != null) acc.setOTP(otp);
			Account.addAcount(acc);
			
			List<String> logs = faccount.getStringList(id + ".Logs");
			Logger.logs.put(id, logs);
		}
		for(String key : fconfig.getKeys(true))
			configvalue.put(key, fconfig.getString(key));
	}
	
	public static void SaveFileConfiguration() {
		if(config.exists()) config.delete();
		checkFile();
		FileConfiguration faccount = YamlConfiguration.loadConfiguration(account);
		for(Account acc : Account.getAccounts()) {
			faccount.set(acc.getID() + ".PW", acc.getPW());
			faccount.set(acc.getID() + ".OTP", acc.getOTP());
			faccount.set(acc.getID() + ".Logs", Logger.logs.get(acc.getID()));
		}
		try {
			faccount.save(account);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String get(String key) {
		return configvalue.get(key);
	}
	
	private static void checkFile() {
		try {
			if(!datafolder.exists()) datafolder.mkdirs();
			if(!config.exists()) config.createNewFile();
			if(!account.exists()) config.createNewFile();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
