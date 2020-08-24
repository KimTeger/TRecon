package com.teger.recon.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Blocker {

	private static HashMap<String, Integer> fail = new HashMap<>();
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
	
	public static Integer getFailCount(String ip) {
		return fail.get(ip);
	}
	
	public static void increaseFailCount(String ip) {
		Integer f = getFailCount(ip);
		if(f == null) f = 0;
		f ++;
		if(f >= 5) {
			resetFailCount(ip);
			blockIP(ip);
		} else {
			fail.put(ip, f);
		}
	}
	
	public static void resetFailCount(String ip) {
		fail.remove(ip);
	}
	
	public static Set<String> getIps() {
		return fail.keySet();
	}
	
	public static List<String> getBlocks(){
		return blocklist;
	}
}
