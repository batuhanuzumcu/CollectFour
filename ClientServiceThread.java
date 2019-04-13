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
				System.out.println("Invalid choice has been entered.");
			
		}//end of try
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//end of catch
		
	}//end of run
   }//end of inner class