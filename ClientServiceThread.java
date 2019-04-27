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
    
	ClientServiceThread(Socket s, CollectFourDB database, ArrayList<ClientServiceThread> threads,ArrayList<GameLobby> lobbies) {
		socket = s;
		db = database;
		this.threads = threads;
		this.lobbies = lobbies;
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


	public void lobbylist(){
		for(int z=0 ; z<lobbies.size() ; z++){
			if(lobbies.get(z)!=null){
				serverPrintOut.println(lobbies.get(z).getLobbyName());
			}
		}
		serverPrintOut.println("timetostopid598755864081");
	}//end of lobbylist method
	
	
	public void RoomMenuServerside() {

		System.out.println("Welcome! It seems someone has reached to the lobby creation/joining menu");
		try {
			String clientChoice = inFromClient.readLine();
			
			// JOIN ROOM
			if (clientChoice.equals("1")) {
				System.out.println("it seems that joining operation is selected");

				lobbylist();
				
				lobbyname = inFromClient.readLine();
				
				for (int i = 0; i<lobbies.size() ; i++) {
			        if (lobbies.get(i).getLobbyName().equals(lobbyname) ) {
			        	System.out.println("there is a lobby existing with that name! Checking if it has a password...");
			    
			        	if(lobbies.get(i).getLobbyPassword().isEmpty()){
			        		serverPrintOut.println("no password");
			        		System.out.println("it doesn't have a password! Joining to lobby...");
				        	lobbies.get(i).Join(this);
			        	}
			        	else{
			        		serverPrintOut.println("has password");
			        		if(inFromClient.readLine().equals(lobbies.get(i).getLobbyPassword())){
			        			System.out.println("correct password entered! Player can join to the lobby.");
					        	lobbies.get(i).Join(this);
			        		}
			        		else{
			        			System.out.println("wrong password entered by user!");
			        		}
			        	}

			        	blockUntilAllPlayersIn(lobbies.get(i));
			          }
			}//end of loop
				
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
					lobbies.add(new GameLobby(lobbyname,lobbypass,lobbysize));
					lobbies.get(currentLobbySize).Join(this);
					blockUntilAllPlayersIn(lobbies.get(currentLobbySize));
				           
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
			String clientChoice=inFromClient.readLine();
			
			if(clientChoice.equals("1")){
				System.out.println("it seems that login operation is selected");
				String Clientusername=inFromClient.readLine();
				String Clientpassword=inFromClient.readLine();
				String result=db.Login(Clientusername, Clientpassword);
				if(result.equals("success")){
					username=Clientusername;
					serverPrintOut.println("successfully logged in to system!");
					RoomMenuServerside();
					for(GameLobby lobby: lobbies) {
                    	//might be wrong?
					    lobby.play();
                    }

				}
			}
			//REGISTER
			else if(clientChoice.equals("2")) {
				System.out.println("It seems that register operation is selected");
				String Clientusername=inFromClient.readLine();
				String Clientpassword=inFromClient.readLine();
				String result=db.RegisterData(Clientusername,Clientpassword);
				if(result.equals("success")) {
					username=Clientusername;
					serverPrintOut.println("successfully registered!");
					RoomMenuServerside();
                    for(GameLobby lobby: lobbies) {
                    	//might be wrong?
                        lobby.play();
                    }

				}
				else if(result.equals("fail"))
				{
					serverPrintOut.println("failed to register!");	
					Clientusername=inFromClient.readLine();
                    Clientpassword=inFromClient.readLine();
                    result=db.RegisterData(Clientusername,Clientpassword);
                    serverPrintOut.println(result);
                    
                    while(result.equals("fail")) {
                        Clientusername=inFromClient.readLine();
                        Clientpassword=inFromClient.readLine();
                        result=db.RegisterData(Clientusername,Clientpassword);
                        serverPrintOut.println(result);
                    }
				}				
			}
			else
				System.out.println("Invalid choice has been entered.");
			System.exit(0);
			
		}//end of try
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//end of catch
		
	}//end of run
   }//end of inner class