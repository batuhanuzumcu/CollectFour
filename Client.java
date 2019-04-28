package Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements Runnable {

	static Socket clientSocket = null; // The client socket
	static PrintWriter os = null; // The output stream
	static BufferedReader inFromServer = null; // The input stream
	static Scanner input = new Scanner(System.in);
	String typebingo;
	String username, password;
	String serverRespond = null;
	int chosennumber;

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

	public void waitforgame() {
		System.out.println("Time to wait for the game to start!");
		try {
			while (true) {
				serverRespond = inFromServer.readLine();
				
				if (serverRespond == null) {
					continue;
				} else if (serverRespond.equals("game starts!")) {
					System.out.println("Everyone is ready and game starts now!");
					System.out.println("here is your starter deck: ");
					// handle server choices for game etc
				} else if (serverRespond.equals("game ended.")) {
					System.out.println("good game!");
					break;
				} else if (serverRespond.equals("Send input")){
					chosennumber=input.nextInt();
					os.println(chosennumber);
					
				} else if (serverRespond.equals("Send Bingo Input:")){
					input.nextLine();
					typebingo=input.nextLine();
					os.println(typebingo);
					
				} else {
					System.out.println(serverRespond);
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void RoomMenuClientside() {
		try {
			System.out.println(inFromServer.readLine());
			System.out.println(inFromServer.readLine());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int choice = input.nextInt();
		input.nextLine();
		os.println(choice);

		if (choice == 1) {
			System.out.println("it seems you chose the join operation");
			System.out.println("Here are the lobbies that are currently open: ");
			System.out.println("  ");
			getlobbylist();
			
			System.out.println("Please enter the name of the lobby you want to join:");
			String joinname = input.nextLine();
			os.println(joinname);
			
			try {
				String lobbynamecontrol=inFromServer.readLine();
				System.out.println(lobbynamecontrol);
				if(lobbynamecontrol.equals("No such lobby exists! Or it already started. Quitting...")){
					System.exit(0);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				String lobbyhaspassword = inFromServer.readLine();
				if (lobbyhaspassword.equals("no password")) {
					System.out.println("it seems that this lobby has no password! Joining to lobby now.");

				} else if (lobbyhaspassword.equals("has password")) {
					System.out.println("it seems that this lobby has a password!");
					System.out.println("Please enter the password to continue: ");
					String joinpass = input.nextLine();
					os.println(joinpass);
					String controllobbypass = inFromServer.readLine();
					System.out.println(controllobbypass);
					while(controllobbypass.equals("you entered the wrong password! Please enter again.")){
						joinpass=input.nextLine();
						os.println(joinpass);
						controllobbypass = inFromServer.readLine();
						System.out.println(controllobbypass);
					}
				}
				
			//to be done after joining to a lobby
				waitforgame();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (choice == 2) {
			System.out.println("Please enter a name for the lobby ");
			String namelobby = input.nextLine();

			System.out.println("Between 4 - 10, set the number of players to play with:");
			int lobbylimit = input.nextInt();
			input.nextLine();

			while (lobbylimit < 4 || lobbylimit > 10) {
				System.out.println("Incorrect number of players entered! Please enter again.");
				lobbylimit = input.nextInt();
				input.nextLine();
			}

			System.out.println("do you want to set a password for the lobby? Input 1 for yes anything else for no.");
			String wantpassword = input.nextLine();
			os.println(wantpassword);
			os.println(namelobby);
			os.println(lobbylimit);

			if (wantpassword.equals("1")) {
				System.out.println("Please type in the password: ");
				String passwordlobby = input.nextLine();
				os.println(passwordlobby);
			}
			System.out.println("everything is prepared!");
			waitforgame();

		}else{
			System.out.println("You entered an invalid choice! Quitting...");
			System.exit(0);
		}
	}

	private void getlobbylist() {
		String lobbyname = "x";

		while (!lobbyname.equals("timetostopid598755864081")) {
			try {
				lobbyname = inFromServer.readLine();

				if (!lobbyname.equals("timetostopid598755864081")) {
					System.out.println("Lobby name: " + lobbyname);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}// end of getlobbylist method

	
	@Override
	public void run() {

		System.out.println("***HELLO***");
		System.out.println("Please type in 1 to LOGIN or 2 to REGISTER: ");
		int choice = input.nextInt();
		input.nextLine();
		os.println(choice);

		// Request for login
		if (choice == 1) {

			System.out.println("please enter your username: ");
			username = input.nextLine();
			System.out.println("please enter your password: ");
			password = input.nextLine();
			os.println(username);
			os.println(password);
			
			try {
				serverRespond = inFromServer.readLine();
				System.out.println(serverRespond);
				
				if (serverRespond.equals("successfully logged in to system!")) {
					RoomMenuClientside();
					
				} else if (serverRespond.equals("TRY AGAIN")) {
					System.out.println("please enter your username: ");
					username = input.nextLine();
					System.out.println("please enter your password: ");
					password = input.nextLine();
					os.println(username);
					os.println(password);
					serverRespond = inFromServer.readLine();
					System.out.println(serverRespond);
					
					while (serverRespond.equals("TRY AGAIN")) {
						System.out.println("please enter your username: ");
						username = input.nextLine();
						System.out.println("please enter your password: ");
						password = input.nextLine();
						os.println(username);
						os.println(password);
						serverRespond = inFromServer.readLine();
					}
					serverRespond = inFromServer.readLine();
					System.out.println(serverRespond);
					RoomMenuClientside();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Request for registration
		else if (choice == 2) {
			System.out.println("Please create a user name: ");
			username = input.nextLine();
			System.out.println("Please create a password: ");
			password = input.nextLine();
			os.println(username);
			os.println(password);
			
			try {
				serverRespond = inFromServer.readLine();
				System.out.println(serverRespond);
				if (serverRespond.equals("successfully registered!")) {
					System.out.println("Now please re-join to login with your account!");
					System.exit(0);
					
				} else if (serverRespond.equals("Failed to register, please try again!")) {

					System.out.println("Please create new user name, again: ");
					username = input.nextLine();
					System.out.println("Please create new password, again: ");
					password = input.nextLine();
					os.println(username);
					os.println(password);
					serverRespond = inFromServer.readLine();
					System.out.println(serverRespond);

					while (serverRespond.equals("USER NAME ALREADY EXISTS")) {
						System.out.println("Please create new user name, again: ");
						username = input.nextLine();
						System.out.println("Please create new password, again: ");
						password = input.nextLine();
						os.println(username);
						os.println(password);
						serverRespond = inFromServer.readLine();
						System.out.println(serverRespond);
					}
					serverRespond = inFromServer.readLine();
					System.out.println(serverRespond);
					System.out.println("Now please re-join to login with your account!");
					System.exit(0);

				} else
					System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				serverRespond = inFromServer.readLine();
				System.out.println(serverRespond);
				System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}// end of run
}// end of class
