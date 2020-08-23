package com.teger.recon;

import org.bukkit.plugin.java.JavaPlugin;

import com.teger.recon.data.FileManager;

public class ReconPlugin extends JavaPlugin{

	@Override
	public void onEnable() {
		getCommand("rc").setExecutor(new CommandManager());
		FileManager.ReadFileConfiguration();
	}
	
	@Override
	public void onDisable() {
		FileManager.SaveFileConfiguration();
	}
}
