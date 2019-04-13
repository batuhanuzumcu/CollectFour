package Game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;


public class Server extends Thread implements Runnable {
	private static CollectFourDB db;
	static Server newest;
	Socket socket;
	static BufferedReader inFromClient;
	static PrintWriter serverPrintOut;
	static int connectionnumber=0;
	public Server(Socket s) {
		socket = s;
		try {
			inFromClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
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
		
		db = new CollectFourDB(); // Database initialization
	
		ServerSocket ss = new ServerSocket(3333);		

		for (connectionnumber = 0; connectionnumber < 100; connectionnumber++) {
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
		System.out.println("it seems a client has connected! let's wait for him/her to LOGIN or REGISTER!");
		try {
			String clientChoice=inFromClient.readLine();
			
			//LOGIN
			if(clientChoice.equals("1")){ 
				System.out.println("It seems that login operation is selected");
				String Clientusername=inFromClient.readLine();
				String Clientpassword=inFromClient.readLine();
				String result=db.Login(Clientusername, Clientpassword);
				if(result.equals("success")){
					serverPrintOut.println("successfully logged in to system!");
				}
				
			}
			//REGISTER
			else if(clientChoice.equals("2")) {
				System.out.println("It seems that register operation is selected");
				String Clientusername=inFromClient.readLine();
				String Clientpassword=inFromClient.readLine();
				String result=db.RegisterData(Clientusername,Clientpassword);
				if(result.equals("success")) {
					serverPrintOut.println("successfully registered!");
				}
				else if(result.equals("fail"))
				{
					serverPrintOut.println("fail");
				}
			}
			
			else
				serverPrintOut.println("Invalid choice has been entered.");
				
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}//end of run
}//end of class
