package Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
	private static CollectFourDB db;
	private static boolean closed = false;

	public static void main(String args[]) throws Exception {
		db = new CollectFourDB(); // Database initialization
		ServerSocket ss = new ServerSocket(3333);		
			Socket s = null;
			
			try {
				while (!closed) {
					s = ss.accept();
					System.out.println("A new client is connected : " + s);

					Thread clientThread = new Thread (new ClientServiceThread(s,db));
					clientThread.start();
				}
			} catch (IOException e) {
				throw new RuntimeException("Error accepting client connection", e);
			}
			System.out.println("Assigning new thread for this client");
	}// end of main
}//end of class
