package Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server{
	private static CollectFourDB db;
	private static boolean closed = false;
	static ArrayList<ClientServiceThread> threads = new ArrayList<ClientServiceThread>();
	static ArrayList<GameLobby> lobbies = new ArrayList<GameLobby>();


	public static void main(String args[]) throws Exception {
		db = new CollectFourDB(); // Database initialization
		ServerSocket ss = new ServerSocket(3333);		
			Socket s = null;
			
			try {
				 while(!closed) {
						s = ss.accept();
						System.out.println("A new client is connected : " + s);
						ClientServiceThread clientThread = new ClientServiceThread(s,db,threads,lobbies);
						threads.add(clientThread);
						clientThread.start();
 
				 }
				
			} catch (IOException e) {
				throw new RuntimeException("Error accepting client connection", e);
			}
			
	}// end of main
}//end of class
