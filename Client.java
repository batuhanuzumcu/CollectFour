package Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements Runnable  {

	static Socket clientSocket = null; // The client socket
	static PrintWriter os = null; // The output stream
	static BufferedReader inFromServer = null; // The input stream
	static Scanner input = new Scanner(System.in); // to get connection IP from user
	String username, password;
	String serverRespond = null;
	static String serverConfirm=null; 
	public static void main(String[] args) {

		int portNumber = 3333;
		String host = "localhost";

		System.out.print("Hello! Please enter the ip of the host: ");
		host = input.nextLine();
		System.out.println("");

		// Open a socket on a given host and port. Open input and output streams
		try {
			clientSocket = new Socket(host, portNumber);
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			os = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + host);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to the host " + host);
		}
		if (clientSocket != null && os != null && inFromServer != null) {
			// time to start the thread
			new Thread(new Client()).start();
		}

	}// end of main	

	public void RoomMenuClientside() {
		System.out.println("Welcome to the Lobby !");
		System.out.println("Please type in 1 to JOIN A ROOM or 2 to CREATE A NEW ROOM: ");
		int choice=input.nextInt();
		input.nextLine();
		os.println(choice);
		
		if(choice==1) {
			System.out.println("it seems you chose the join operation");
			//System.out.println("Here are the lobbies that are currently open: ");
			System.out.println("Please enter the name of the lobby you want to join:");
			String joinname = input.nextLine();
			os.println(joinname);

			try {
				String lobbyhaspassword = inFromServer.readLine();
				if(lobbyhaspassword.equals("no password")){
		    		System.out.println("it seems that this lobby has no password! Joining to lobby now.");

				}
				else if(lobbyhaspassword.equals("has password")){
		    		System.out.println("it seems that this lobby has a password!");
		    		System.out.println( "Please enter the password to continue: ");
		    		String joinpass = input.nextLine();
		    		os.println(joinpass);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		else if(choice==2) {
			System.out.println("Please enter a name for the lobby ");
			String namelobby = input.nextLine();
			System.out.println("do you want to set a password for the lobby? Input 1 for yes anything else for no.");
			String wantpassword = input.nextLine();
			os.println(wantpassword);

			if(wantpassword.equals("1")){
				System.out.println("Please type in the password: ");
				String passwordlobby = input.nextLine();
				os.println(namelobby);
				os.println(passwordlobby);
			}
			else{
				os.println(namelobby);
			}
		}
	}
	
	@Override
	public void run() {

		System.out.println("***HELLO***");
		System.out.println("Please type in 1 to LOGIN or 2 to REGISTER: ");
		int choice=input.nextInt();
		input.nextLine();
		os.println(choice);

		//Request for login
		if(choice==1){

			System.out.println("please enter your username: ");
			username=input.nextLine();
			System.out.println("please enter your password: ");
			password=input.nextLine();
			os.println(username);
			os.println(password);
			
			try{
				serverRespond = inFromServer.readLine();
				System.out.println(serverRespond);
			
				if(serverRespond.equals("successfully logged in to system!")) {
					RoomMenuClientside();
					//stuff to do after logging in to see menu
					//buraya yapýlýcak :D
				}
				else if(serverRespond.equals("TRY AGAIN")) {	
					System.out.println("please enter your username: ");
					username=input.nextLine();
					System.out.println("please enter your password: ");
					password=input.nextLine();
					os.println(username);
					os.println(password);
					serverRespond=inFromServer.readLine();
					System.out.println(serverRespond);
					while(serverRespond.equals("TRY AGAIN")) {
						System.out.println("please enter your username: ");
						username=input.nextLine();
						System.out.println("please enter your password: ");
						password=input.nextLine();
						os.println(username);
						os.println(password);
						serverRespond=inFromServer.readLine();
						
					}
					serverRespond=inFromServer.readLine();
					System.out.println(serverRespond);
					RoomMenuClientside();
				}
				
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		//Request for registration
		else if (choice == 2) 
		{
			System.out.println("Please create a user name: ");
			username = input.nextLine();
			System.out.println("Please create a password: ");
			password = input.nextLine();
			os.println(username);
			os.println(password);
			
			try{	
				serverRespond = inFromServer.readLine();
				System.out.println(serverRespond);
				
				if(serverRespond.equals("successfully registered!")){
					RoomMenuClientside();
					//stuff to do after registering to see menu
					//buraya yapýlacak :D
				}
				else if(serverRespond.equals("Failed to register, please try again!")) {

                    System.out.println("Please create new user name, again: ");
                    username = input.nextLine();
                   
                    System.out.println("Please create new password, again: ");
                    password = input.nextLine();
                    os.println(username);
                    os.println(password);
                    serverRespond = inFromServer.readLine();
                    System.out.println(serverRespond);

                    while(serverRespond.equals("USER NAME ALREADY EXISTS")) {
                        System.out.println("Please create new user name, again: ");
                        username = input.nextLine();
                        System.out.println("Please create new password, again: ");
                        password = input.nextLine();
                        os.println(username);
                        os.println(password);
                        serverRespond=inFromServer.readLine();
                        System.out.println(serverRespond);
                    }
                    serverRespond=inFromServer.readLine();
					System.out.println(serverRespond);
					RoomMenuClientside();
					
                    

                }
				else 
					System.exit(0);
				
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		else
		{
			try{
				serverRespond = inFromServer.readLine();
				System.out.println(serverRespond);
				System.exit(0);
				
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
	}//end of run
}//end of class