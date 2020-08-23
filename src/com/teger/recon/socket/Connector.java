package com.teger.recon.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

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
	private String key = "7654";
	
	@Override
	public void run() {
		try {
			boot();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void boot() throws Exception {
		while(true) {
			try {
				serverSocket = new ServerSocket(Integer.parseInt(FileManager.get("port")));
				System.out.println("[TRecon] " + serverSocket.getLocalPort() + " 에서 원격 서버가 개방되었습니다.");
				socket = serverSocket.accept();
				System.out.println("[TRecon] " + socket.getInetAddress().getHostAddress() + " 에서 로그인을 시도 중입니다.");
				
				//Sign in
				
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

				dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.LGN_ID_REQ.toString(), key));
				String[] id_pw = Encryptor.getDecrpytAES(dis.readUTF(), key).split("===");
				Account acc = Account.getAccount(Encryptor.getEncrpytMD5(id_pw[0]));
				String recv = null;
				if(Authenticator.isMatchedAccount(id_pw[0], id_pw[1])) {
					System.out.println("[TRecon] " + socket.getInetAddress().getHostAddress() + " 로그인 성공");
					dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.LGN_SCS.toString(), key));
				} else {
					System.out.println("[TRecon] " + socket.getInetAddress().getHostAddress() + " 로그인 실패");
					dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.LGN_FIL.toString(), key));
					socket.close();
					serverSocket.close();
					continue;
				}
				if(acc.getOTP() != null) {
					dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.LGN_OTP_REQ.toString(), key));
					recv = dis.readUTF();
					if(Authenticator.isMatchedOTP(id_pw[0], Encryptor.getDecrpytAES(recv, key))) {
						System.out.println("[TRecon] " + socket.getInetAddress().getHostAddress() + " OTP 인증 성공");
						dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.LGN_OTP_SCS.toString(), key));
					} else {
						System.out.println("[TRecon] " + socket.getInetAddress().getHostAddress() + " OTP 인증 실패");
						dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.LGN_OTP_FIL.toString(), key));
					}
				}
				System.out.println("[TRecon] " + socket.getInetAddress().getHostAddress() + " 접속 성공");
				
				//Command Sender
				while(true) {
					String[] recvl = Encryptor.getDecrpytAES(dis.readUTF(), key).split("===");
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
				                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
				            }
				        }.runTask(ReconPlugin.plugin);
					}
					dos.writeUTF(Encryptor.getEncrpytAES(Comvalue.RECV.toString(), key));
				}
				
				socket.close();
				serverSocket.close();
				continue;
			} catch (NumberFormatException | IOException e) {
				socket.close();
				serverSocket.close();
				continue;
			}
		}
	}
	
}