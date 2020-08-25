package com.teger.recon.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import com.teger.recon.ReconPlugin;
import com.teger.recon.data.Account;
import com.teger.recon.data.Encryptor;
import com.teger.recon.data.FileManager;

public class Connector extends Thread{

	public ServerSocket serverSocket;
	public Socket socket;
	private String key;
	
	@Override
	public void run() {
		key = FileManager.get("connect-key");
		try { boot(); } catch (Exception e) { e.printStackTrace(); }
	}
	
	public void boot() throws Exception {
		
		while(true) {
			try {
				serverSocket = new ServerSocket(Integer.parseInt(FileManager.get("port")));
				System.out.println("[TRecon] TRecon Server was open at " + serverSocket.getLocalPort() + " port.");
				socket = serverSocket.accept();
				System.out.println("[TRecon] " + socket.getInetAddress().getHostAddress() + " try sign in.");
				
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				
				//Block
				
				if(Blocker.isBlocked(socket.getInetAddress().getHostAddress())) {
					System.out.println("[TRecon] " + socket.getInetAddress().getHostAddress() + " blocked.");
					dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.BLOCK.toString(), key));
					socket.close();
					serverSocket.close();
				}
				
				//Sign in

				dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.LGN_ID_REQ.toString(), key));
				String[] id_pw = Encryptor.getDecrpytAES(dis.readUTF(), key).split("===");
				Account acc = Account.getAccount(Encryptor.getEncrpytMD5(id_pw[0]));
				String recv = null;
				if(Authenticator.isMatchedAccount(id_pw[0], id_pw[1])) {
					Blocker.resetFailCount(socket.getInetAddress().getHostAddress());
					System.out.println("[TRecon] " + socket.getInetAddress().getHostAddress() + " sign in success");
					dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.LGN_SCS.toString(), key));
				} else {
					Blocker.increaseFailCount(socket.getInetAddress().getHostAddress());
					System.out.println("[TRecon] " + socket.getInetAddress().getHostAddress() + " sign in fail");
					if(Blocker.isBlocked(socket.getInetAddress().getHostAddress()))
						System.out.println("[TRecon] " + socket.getInetAddress().getHostAddress() + " blocked.");
					dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.LGN_FIL.toString(), key));
					socket.close();
					serverSocket.close();
					continue;
				}
				if(acc.getOTP() != null) {
					dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.LGN_OTP_REQ.toString(), key));
					recv = dis.readUTF();
					if(Authenticator.isMatchedOTP(id_pw[0], Encryptor.getDecrpytAES(recv, key))) {
						System.out.println("[TRecon] " + socket.getInetAddress().getHostAddress() + " OTP authentication success");
						dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.LGN_OTP_SCS.toString(), key));
					} else {
						System.out.println("[TRecon] " + socket.getInetAddress().getHostAddress() + " OTP authentication fail");
						dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.LGN_OTP_FIL.toString(), key));
					}
				}
				System.out.println("[TRecon] " + socket.getInetAddress().getHostAddress() + " connect success");
				
				//Command Sender
				while(true) {
					System.out.println("Waiting...");
					String[] recvl = Encryptor.getDecrpytAES(dis.readUTF(), key).split("===");
					System.out.println("Received !");
					if(Comvalue.valueOf(recvl[0]).equals(Comvalue.DISCON)) break;
					Comvalue cv = Comvalue.valueOf(recvl[0]);
					if(cv.equals(Comvalue.CMD_VAL)) {
						String command = recvl[1];
						for(int i = 2; i < recvl.length; i ++) 
							command += (" "+recvl[i]);
						final String cmd = command;
						new BukkitRunnable() {
				            @Override
				            public void run() {
				                Bukkit.dispatchCommand(ReconPlugin.sender, cmd);
				            }
				        }.runTask(ReconPlugin.plugin);
				        sleep(500);
						List<String> res = ReconPlugin.sender.getResponses();
						dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.CMD_RST_START.toString(), key));
						for(String l : res)
							dos.writeUTF(Encryptor.getEncrpytAES(l, key));
						dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.CMD_RST_END.toString(), key));
					}
				}
				
				socket.close();
				serverSocket.close();
				continue;
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
				socket.close();
				serverSocket.close();
				continue;
			}
		}
	}
}