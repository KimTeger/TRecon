package com.teger.recon.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Logger {
	public static HashMap<String, List<String>> logs = new HashMap<>();
	
	public static List<String> getLogs(String address){
		return logs.get(address);
	}
	
	public static void addLog(String address, String line) {
		List<String> log = getLogs(address);
		String date = new SimpleDateFormat("[yy:MM:dd:HH:mm:ss]").format(new Date());
		log.add(date + line);
		logs.put(address, log);
	}
}
