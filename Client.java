package Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements Runnable {
	Scanner input = new Scanner(System.in);

	static Socket clientSocket = null; // The client socket
	static PrintWriter os = null; // The output stream
	static BufferedReader inFromServer = null; // The input stream
	static Scanner inputforIP = new Scanner(System.in); // to get connection IP from user
	private String choice;
	private int option;

	public static void main(String[] args) {

		int portNumber = 3306;
		String host = "localhost";

		System.out.print("Hello! Please enter the ip of the host: ");
		host = inputforIP.nextLine();
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

	@Override
	public void run() {
		CollectFourDatabase cfd = new CollectFourDatabase();

		System.out.println("Press 1 to Login,Press 2 to register");
		option = input.nextInt();

		if (option == 2)
			cfd.RegisterData();

	}

}
