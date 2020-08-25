package com.teger.recon;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.teger.recon.data.FileManager;
import com.teger.recon.socket.Connector;
import com.teger.recon.socket.ReconSender;

public class ReconPlugin extends JavaPlugin{

	Connector conthread;
	public static ReconPlugin plugin;
	public static ReconSender sender;
	
	@Override
	public void onEnable() {
		plugin = this;
		sender = new ReconSender(Bukkit.getConsoleSender());
		getCommand("rc").setExecutor(new CommandManager());
		
		FileManager.ReadFileConfiguration();
		Connector conthread = new Connector();
		conthread.start();
		
	}
	
	@Override
	public void onDisable() {
		try {
			FileManager.SaveFileConfiguration();
			conthread.socket.close();
			conthread.serverSocket.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
