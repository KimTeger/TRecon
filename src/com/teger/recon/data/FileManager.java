package com.teger.recon.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.teger.recon.socket.Blocker;

public class FileManager {

	private static HashMap<String, String> configvalue = new HashMap<>();
	
	private final static File datafolder = new File("plugins/TRecon"),
			config = new File("plugins/TRecon/config.yml"),
			account = new File("plugins/TRecon/account.yml"),
			blocklist = new File("plugins/TRecon/block.yml");
	
	public static void ReadFileConfiguration() {
		checkFile();
		FileConfiguration fconfig = YamlConfiguration.loadConfiguration(config);
		FileConfiguration faccount = YamlConfiguration.loadConfiguration(account);
		FileConfiguration fblocklist = YamlConfiguration.loadConfiguration(blocklist);
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
		Blocker.setBlockList(fblocklist.getStringList("block-list"));
	}
	
	public static void SaveFileConfiguration() {
		if(account.exists()) account.delete();
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
			if(!config.exists()) {
				config.createNewFile();
				FileConfiguration fconfig = YamlConfiguration.loadConfiguration(config);
				fconfig.set("port", 7654);
				fconfig.set("connect-key", "trecon");
			}
			if(!account.exists()) config.createNewFile();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
