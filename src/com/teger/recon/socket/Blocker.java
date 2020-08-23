package com.teger.recon.socket;

import java.util.ArrayList;
import java.util.List;

public class Blocker {

	private static List<String> blocklist = new ArrayList<>();
	
	public static void setBlockList(List<String> list) {
		blocklist = list;
	}
	
	public static void blockIP(String ip) {
		if(blocklist.contains(ip))return;
		blocklist.add(ip);
	}
	
	public static void unblockIP(String ip) {
		if(!blocklist.contains(ip)) return;
		blocklist.remove(ip);
	}
	
	public static boolean isBlocked(String ip) {
		return blocklist.contains(ip);
	}
}
