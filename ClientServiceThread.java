package Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ClientServiceThread extends Thread implements Runnable {

	String username;
	Socket socket;
	BufferedReader inFromClient;
	PrintWriter serverPrintOut;
	String lobbyname;
	String lobbypass;
	CollectFourDB db;
	ArrayList<ClientServiceThread> threads;
	ArrayList<GameLobby> lobbies;
	List<Integer> playerdeck;
	int lobbysize;
	int tobechanged;
	int playerscore;
	boolean receivedscore;
	boolean lobbyexists;
    
	ClientServiceThread(Socket s, CollectFourDB database, ArrayList<ClientServiceThread> threads,ArrayList<GameLobby> lobbies) {
		socket = s;
		db = database;
		this.threads = threads;
		this.lobbies = lobbies;
		playerscore=0;
		lobbyexists=false;
		try {
			inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			serverPrintOut = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	
	public String getplayername(){
		return ("Player: "+username);
	}
	
	public void setplayerdeck(List<Integer> newdeck){
		playerdeck = newdeck;
	}
	public List<Integer> getplayerdeck(){
		return playerdeck;
	}

	public int findnumber(int x){
		for(int g=0; g<playerdeck.size(); g++){
			int nextnumber = playerdeck.get(g);
			if(nextnumber==x){
				tobechanged=g;
			    return nextnumber;
			}
		}
		return 0;
	}
	public void changedeckvalue(int index,int newnumber){
		playerdeck.set(index, newnumber);
	}
	
	public int getnumbertochange(){
		return tobechanged;
	}

	public void addtoscore(int newscore){
		playerscore+=newscore;
	}
	public int getscore(){
		return playerscore;
	}
	public boolean getreceivedScore(){
		return receivedscore;
	}
	public void setreceivedScore(boolean currentstate){
		receivedscore=currentstate;
	}
	

	public void lobbylist(){
		for(int z=0 ; z<lobbies.size() ; z++){
			if(lobbies.get(z)!=null && lobbies.get(z).gamestarted==false){
				serverPrintOut.println(lobbies.get(z).getLobbyName());
			}
		}
		serverPrintOut.println("timetostopid598755864081");
	}//end of lobbylist method
	
	
	public void RoomMenuServerside() {

		serverPrintOut.println("Welcome to the Lobby !");
		serverPrintOut.println("Please type in 1 to JOIN A ROOM or 2 to CREATE A NEW ROOM: ");
		try {
			String clientChoice = inFromClient.readLine();
			
			// JOIN ROOM
			if (clientChoice.equals("1")) {
				//joining operation is selected

				lobbylist();
				
				lobbyname = inFromClient.readLine();
				
				for (int i = 0; i<lobbies.size() ; i++) {
			        if (lobbies.get(i).getLobbyName().equals(lobbyname)) {
			        	lobbyexists=true;
						serverPrintOut.println("The lobby with that name is available! Checking if it has a password...");
			    
			        	if(lobbies.get(i).getLobbyPassword().equals("jeoghj0r4tugjrıgjemja034ı")){
			        		serverPrintOut.println("no password");
			        		//it doesn't have a password! Joining to lobby...
				        	lobbies.get(i).Join(this);
			        	}
			        	else{
			        		serverPrintOut.println("has password");
			        		if(inFromClient.readLine().equals(lobbies.get(i).getLobbyPassword())){
			        			serverPrintOut.println("correct password entered! Joining to the lobby...");
			        			//correct password entered! Player can join to the lobby
					        	lobbies.get(i).Join(this);
			        		}
			        		else{
			        			//wrong password entered by user!"
			        			while(true){
				        			serverPrintOut.println("you entered the wrong password! Please enter again.");
			        				if(inFromClient.readLine().equals(lobbies.get(i).getLobbyPassword())){
			        					serverPrintOut.println("correct password entered! Joining to the lobby...");
					        			//correct password entered! Player can join to the lobby
							        	lobbies.get(i).Join(this);
							        	break;
					        		}
			        			}
			        		}
			        	}

			        	blockUntilAllPlayersIn(lobbies.get(i));
			          }
			}//end of loop
				if(lobbyexists==false){
					serverPrintOut.println("No such lobby exists! Or it already started. Quitting...");
				}
				
			}
			
			// CREATE ROOM
			else if (clientChoice.equals("2")) {
				System.out.println("It seems that creating operation is selected");	
				clientChoice = inFromClient.readLine();
				
				if(clientChoice.equals("1")){
					lobbyname = inFromClient.readLine();
					lobbysize= Integer.parseInt(inFromClient.readLine());
					lobbypass = inFromClient.readLine();
					
					int currentLobbySize = lobbies.size();
					lobbies.add(new GameLobby(lobbyname,lobbypass,lobbysize));
					lobbies.get(currentLobbySize).Join(this);
					blockUntilAllPlayersIn(lobbies.get(currentLobbySize));
					
				}					
				else {
					lobbyname = inFromClient.readLine();
					lobbysize= Integer.parseInt(inFromClient.readLine());
					int currentLobbySize = lobbies.size();
					lobbies.add(new GameLobby(lobbyname,lobbysize));
					lobbies.get(currentLobbySize).Join(this);
					blockUntilAllPlayersIn(lobbies.get(currentLobbySize));
				           
				}
				
			} 
			else
				System.out.println("Invalid choice has been entered by the client. Removing the client now.");

		} // end of try
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // end of catch

	}//end of roommenuserverside method
	
	
	private void blockUntilAllPlayersIn(GameLobby lobby){
        while(true){
            if(lobby.players.size()==lobby.getlobbylimit()){
                break;
            }
        }
    }

	public void run() {
		System.out.println("it seems a client has connected! let's wait for him/her to login or register!");
		try {
			String clientChoice = inFromClient.readLine();

			if (clientChoice.equals("1")) {
				System.out.println("it seems that login operation is selected");
				String Clientusername = inFromClient.readLine();
				String Clientpassword = inFromClient.readLine();
				String result = db.Login(Clientusername, Clientpassword);
				System.out.println(result);
				if (result.equals("success")) {
					username = Clientusername;
					serverPrintOut.println("successfully logged in to system!");
					RoomMenuServerside();
					for(int u=0; u<lobbies.size(); u++){
						if(lobbies.get(u).getLobbyName().equals(lobbyname)){
							lobbies.get(u).play();
						}
					}
					
					
				} else if (result.equals("TRY AGAIN")) {
					serverPrintOut.println("TRY AGAIN");
					Clientusername = inFromClient.readLine();
					Clientpassword = inFromClient.readLine();
					result = db.Login(Clientusername, Clientpassword);
					serverPrintOut.println(result);
					System.out.println(result);

					// while user trying to enter his/her informations
					// incorrectly
					while (result.equals("TRY AGAIN")) {
						Clientusername = inFromClient.readLine();
						Clientpassword = inFromClient.readLine();
						result = db.Login(Clientusername, Clientpassword);
						serverPrintOut.println(result);

					}
					// when username and password is okay
					serverPrintOut.println("Successfully logged in to system!");
					RoomMenuServerside();
					for(int u=0; u<lobbies.size(); u++){
						if(lobbies.get(u).getLobbyName().equals(lobbyname)){
							lobbies.get(u).play();
						}
					}
				}
			}

			// REGISTER
			else if (clientChoice.equals("2")) {
				System.out.println("It seems that register operation is selected");
				String Clientusername = inFromClient.readLine();
				String Clientpassword = inFromClient.readLine();
				String result = db.RegisterData(Clientusername, Clientpassword);
				if (result.equals("success")) {
					username = Clientusername;
					serverPrintOut.println("successfully registered!");
					
					
				} else if (result.equals("USER NAME ALREADY EXISTS")) {
					serverPrintOut.println("Failed to register, please try again!");
					Clientusername = inFromClient.readLine();
					Clientpassword = inFromClient.readLine();
					result = db.RegisterData(Clientusername, Clientpassword);
					serverPrintOut.println(result);
					// while user trying to enter unavailable username
					while (result.equals("USER NAME ALREADY EXISTS")) {
						Clientusername = inFromClient.readLine();
						Clientpassword = inFromClient.readLine();
						result = db.RegisterData(Clientusername, Clientpassword);
						serverPrintOut.println(result);
					}
					// when username is available, user can register
					serverPrintOut.println("Successfully registered!");

				}

			}
			// if user enters invalid choice (except 1 and 2)
			else
				serverPrintOut.println("Invalid choice has been entered. Quitting...");
			

		} // end of try
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // end of catch

	}// end of run
   }//end of inner class