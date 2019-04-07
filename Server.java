package Game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread implements Runnable {
	static Server newest;
	Socket socket;
	static String clientChoice;
	static BufferedReader inFromClient;
	static PrintWriter serverPrintOut;
	static int connectionnumber=0;
	CollectFourDatabase database = new CollectFourDatabase();
	
	
	public Server(Socket s) {
		socket = s;
		try {
			inFromClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
			clientChoice=inFromClient.toString();
			 System.out.println("Message Received: " + clientChoice);
			 if(clientChoice.equals("1")) {
					database.RegisterData();
				}
				else if(clientChoice.equals("2")) {
					database.Login();
				}
			 
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			serverPrintOut = new PrintWriter(s.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
	}

	public static void main(String args[]) throws Exception {
		ServerSocket ss = new ServerSocket(3333);	
	

		for (connectionnumber = 0; connectionnumber < 1000; connectionnumber++) {
			Socket s = null;
			try {
				s = ss.accept();
				System.out.println("A new client is connected : " + s);
			} catch (IOException e) {
				throw new RuntimeException("Error accepting client connection", e);
			}
			System.out.println("Assigning new thread for this client");

			newest = new Server(s);
			newest.start();

		} // end of connection number for loop

	}// end of main
	
	public void run() {
		System.out.println("connection was a success.");
	}
}
