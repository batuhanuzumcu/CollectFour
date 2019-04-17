package Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientServiceThread implements Runnable {

	Socket socket;
	BufferedReader inFromClient;
	PrintWriter serverPrintOut;
	String lobbyname;
	String lobbypass;
	CollectFourDB db;
	ClientServiceThread(Socket s, CollectFourDB database) {
		socket = s;
		db = database;
		try {
			inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			serverPrintOut = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	
	
	public void RoomMenuServerside() {

		System.out.println("Welcome! It seems someone has reached to the lobby creation/joining menu");
		try {
			String clientChoice = inFromClient.readLine();
			
			// JOIN ROOM
			if (clientChoice.equals("1")) {
				System.out.println("it seems that joining operation is selected");
				
			}
			// CREATE ROOM
			else if (clientChoice.equals("2")) {
				System.out.println("It seems that creating operation is selected");
				clientChoice = inFromClient.readLine();
				if(clientChoice.equals("1")){
					lobbyname = inFromClient.readLine();
					lobbypass = inFromClient.readLine();
					GameLobby lol = new GameLobby(lobbyname,lobbypass);
					System.out.println("Successfully created a lobby with password!");
				}
				else if(clientChoice.equals("2")){
					lobbyname = inFromClient.readLine();
					GameLobby lolxd = new GameLobby(lobbyname);
					System.out.println("Successfully created a lobby without password!");

				}
				else{
					System.out.println("you entered an incorrect choice!");
				}

			} 
			else
				System.out.println("Invalid choice has been entered.");

		} // end of try
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // end of catch

	}//end of roommenuserverside method
	
	public void run() {
		System.out.println("it seems a client has connected! let's wait for him/her to login or register!");
		try {
			String clientChoice=inFromClient.readLine();
			
			if(clientChoice.equals("1")){
				System.out.println("it seems that login operation is selected");
				String Clientusername=inFromClient.readLine();
				String Clientpassword=inFromClient.readLine();
				String result=db.Login(Clientusername, Clientpassword);
				if(result.equals("success")){
					System.out.println("successfully logged in to system!");
					RoomMenuServerside();
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
					RoomMenuServerside();

				}
				else if(result.equals("fail"))
				{
					serverPrintOut.println("failed to register!");	
				}				
			}
			else
				System.out.println("Invalid choice has been entered.");
			
		}//end of try
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//end of catch
		
	}//end of run
   }//end of inner class