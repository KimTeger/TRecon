package com.teger.recon;

import org.bukkit.plugin.java.JavaPlugin;

import com.teger.recon.data.FileManager;
import com.teger.recon.socket.Connector;

public class ReconPlugin extends JavaPlugin{

	Connector conthread;
	public static ReconPlugin plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		getCommand("rc").setExecutor(new CommandManager());
		FileManager.ReadFileConfiguration();
		System.out.println(FileManager.get("connect-key"));
		Connector conthread = new Connector();
		conthread.start();
	}
	
	@Override
	public void onDisable() {
		FileManager.SaveFileConfiguration();
		try {
			conthread.socket.close();
			conthread.serverSocket.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
