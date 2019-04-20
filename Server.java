package Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server{
	private static CollectFourDB db;
	private static boolean closed = false;
	static ArrayList<ClientServiceThread> threads = new ArrayList<ClientServiceThread>(100);
	static ArrayList<GameLobby> lobbies = new ArrayList<GameLobby>(100);


	public static void main(String args[]) throws Exception {
		for (int initialize = 0; initialize < 100; initialize++) {
			  threads.add(null);
			  lobbies.add(null);
			}
		
		db = new CollectFourDB(); // Database initialization
		ServerSocket ss = new ServerSocket(3333);		
			Socket s = null;
			
			try {
				 while(!closed) {
						s = ss.accept();
						System.out.println("A new client is connected : " + s);
						 for (int i = 0; ; i++) {
				        if (threads.get(i) == null) {
				            threads.add(i, new ClientServiceThread(s,db,threads,lobbies));
				            threads.get(i).run();
				            break;
				          }
				        }
				 }
					 //Thread clientThread = new Thread (new ClientServiceThread(s,db,threads));
					//clientThread.start();
				
			} catch (IOException e) {
				throw new RuntimeException("Error accepting client connection", e);
			}
			
	}// end of main
}//end of class
