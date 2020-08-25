package com.teger.recon.socket;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class ReconSender implements RemoteConsoleCommandSender{

	private ConsoleCommandSender cs;
	private List<String> responses = new ArrayList<>();
	
	public ReconSender(ConsoleCommandSender cs) {
		this.cs = cs;
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin arg0) {
		return cs.addAttachment(arg0);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0, int arg1) {
		return cs.addAttachment(arg0, arg1);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2) {
		return cs.addAttachment(arg0, arg1, arg2);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2, int arg3) {
		return cs.addAttachment(arg0, arg1, arg2, arg3);
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return cs.getEffectivePermissions();
	}

	@Override
	public boolean hasPermission(String arg0) {
		return cs.hasPermission(arg0);
	}

	@Override
	public boolean hasPermission(Permission arg0) {
		return cs.hasPermission(arg0);
	}

	@Override
	public boolean isPermissionSet(String arg0) {
		return cs.isPermissionSet(arg0);
	}

	@Override
	public boolean isPermissionSet(Permission arg0) {
		return cs.isPermissionSet(arg0);
	}

	@Override
	public void recalculatePermissions() {
		cs.recalculatePermissions();
	}

	@Override
	public void removeAttachment(PermissionAttachment arg0) {
		cs.removeAttachment(arg0);
	}

	@Override
	public boolean isOp() {
		return cs.isOp();
	}

	@Override
	public void setOp(boolean arg0) {
		cs.setOp(arg0);
	}

	@Override
	public String getName() {
		return cs.getName();
	}

	@Override
	public Server getServer() {
		return cs.getServer();
	}

	@Override
	public void sendMessage(String arg0) {
		responses.add(arg0);
		cs.sendMessage(arg0);
	}

	@Override
	public void sendMessage(String[] arg0) {
		for(String l : arg0) responses.add(l);
		cs.sendMessage(arg0);
	}

	@Override
	public Spigot spigot() {
		return Bukkit.getConsoleSender().spigot();
	}

	public List<String> getResponses(){
		List<String> res = new ArrayList<>();
		for(String l : responses) res.add(l);
		responses.clear();
		return res;
	}
}
