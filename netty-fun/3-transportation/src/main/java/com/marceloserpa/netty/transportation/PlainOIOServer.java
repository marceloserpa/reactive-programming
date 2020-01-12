package com.marceloserpa.netty.transportation;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class PlainOIOServer {

	public static void main(String[] args) throws IOException {
		new PlainOIOServer().serve(9090);
	}
	
	public void serve(int port) throws IOException {
		final ServerSocket socket = new ServerSocket(port);
		try {
			for(;;) {
				final Socket clientSocket = socket.accept();
				System.out.println("Accepted request: " + clientSocket);
				
				// Create a thread to handle the request
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						OutputStream out;
						try {
							out = clientSocket.getOutputStream();
							out.write("Hi!".getBytes(Charset.forName("UTF-8")));
							out.flush();
						} catch (IOException	 e) {
							e.printStackTrace();
						} finally {
							try {
								clientSocket.close();
							} catch(IOException ex) {
								ex.printStackTrace();
							}
						}
						
					}
				}).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
