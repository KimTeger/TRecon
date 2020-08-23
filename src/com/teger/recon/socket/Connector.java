package com.teger.recon.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.teger.recon.data.FileManager;

public class Connector {

	public ServerSocket serverSocket;
	public Socket socket;
	
	public void start() {
		try {
			serverSocket = new ServerSocket(Integer.parseInt(FileManager.get("port")));
			socket = serverSocket.accept();
			
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
}
class Receiver extends Thread{
	
	private Socket socket;
	private InputStream is;
	private DataInputStream dis;

	
	public Receiver(Connector con) throws IOException {
		socket = con.socket;
		is = socket.getInputStream();
		dis = new DataInputStream(is);
	}
	
	@Override
	public void run() {
		try {
			String recv = dis.readUTF();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
