package Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

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
    int currentplayersize;
    
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
	public void playername(){
		System.out.println("The name of this player is: "+username);
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
			    
			        	if(lobbies.get(i).getLobbyPassword().equals(null)){
			        		
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
			        	//time to do after joining lobby part:
				        
			            currentplayersize = lobbies.get(i).players.size();
			        	while(true){
			        		
			        		if(currentplayersize!=lobbies.get(i).players.size()){
			        			  if(lobbies.get(i).players.size()==10){
			        	    	    	
			            	    	    	serverPrintOut.println("max number of players joined!");
			            	    	    	serverPrintOut.println("starting the game now.");
			        	    	    		serverPrintOut.println("start");	    	
			        	    	    	
			        	    	        break;
			        	    	    }
			        	    	    else if(3<lobbies.get(i).players.size() && lobbies.get(i).players.size()<10){
			        	    	    	serverPrintOut.println("it seems enough players have joined to this lobby!");
										serverPrintOut.println("Waiting for admin to start the game:");		        	    	    					        	    	    				
										
			    	    	    		if(lobbies.get(i).getAdminChoice().equals("start")){
			    	    	    			serverPrintOut.println("start");
											break;
			    	    	    		}
			        	    	    		
			        	    	    }//end of else if
			        			  
			        			  currentplayersize=lobbies.get(i).players.size();
			        			  
			        			  
			        		}//end of main if
			        		
			        	}//end of while loop
			        	
			            break;
			          }//end of outer if
			}//end of loop
				
			}
			
			// CREATE ROOM
			else if (clientChoice.equals("2")) {
				System.out.println("It seems that creating operation is selected");	
				clientChoice = inFromClient.readLine();
				
				if(clientChoice.equals("1")){
					lobbyname = inFromClient.readLine();
					lobbypass = inFromClient.readLine();
					for (int i = 0; ; i++) {
				        if (lobbies.get(i) == null) {
				            lobbies.add(i, new GameLobby(lobbyname,lobbypass));
				            lobbies.get(i).Join(this);
				            //dangerous waters.jpg
				            //lobbies.get(i).start();
				    		
				            //after this point using preparation inside here
				          
				            currentplayersize = lobbies.get(i).players.size();
				    		
				            while(true){
				        		
				        		if(currentplayersize!=lobbies.get(i).players.size()){
				        			
				        			  if(lobbies.get(i).players.size()==10){
				        								        	    	    		
				        	    	    		serverPrintOut.println("max number of players joined!");
				        	    	    		serverPrintOut.println("starting the game now.");
				        	    	    		serverPrintOut.println("start");				      	
				        	    	    	Game currentgame = new Game(lobbies.get(i).players);
				        	    	        currentgame.start();
				        	    	        break;
				        	    	    }
				        			  
				        	    	    else if(3<lobbies.get(i).players.size() && lobbies.get(i).players.size()<10){
				        	    	    	try {			    	    	    			
				    	    	    			serverPrintOut.println("it seems enough players have joined to this lobby!");
				    	    	    			serverPrintOut.println("Waiting for admin to start the game:");
				        	    	    	
				        	    	    		while(!lobbies.get(i).getAdminChoice().equals("start")){
				        	    	    			String newchoice = inFromClient.readLine();
				        	    	    			lobbies.get(i).setAdminChoice(newchoice);
				        	    	    		
				        	    	    			if(lobbies.get(i).getAdminChoice().equals("start")){
				        	    	    				serverPrintOut.println("start");					        	    	    		    			        	    	    				
				        	    	    				Game currentgame = new Game(lobbies.get(i).players);
				        	    	    			    currentgame.start();
				        	    	    			    break;

				        	    	    			}//end of if
				        	    	    		}
				        	    				
				        	    			} catch (IOException e) {
				        	    				// TODO Auto-generated catch block
				        	    				e.printStackTrace();
				        	    			}
				        	    	    		
				        	    	    }				        			  
				        			  currentplayersize=lobbies.get(i).players.size();
				        			  	        			  
				        		}//end of main if       						       
				        	}//end of while loop
				            break;
				          }
					}
					//System.out.println("Successfully created a lobby with password!");
					//owner of lobby yapılacaklar
					//will be done from gamelobby
					
				}	
				
				else {
					lobbyname = inFromClient.readLine();
					for (int i = 0; ; i++) {
				        if (lobbies.get(i) == null) {
				            lobbies.add(i, new GameLobby(lobbyname));
				            lobbies.get(i).Join(this);
				            //dangerous waters.jpg
				            //lobbies.get(i).start();
				           
				          //  currentplayersize = lobbies.get(i).players.size();
				    		
				            while(true){
				        		
//				        		if(currentplayersize!=lobbies.get(i).players.size()){
				        			
				        			  if(lobbies.get(i).players.size()==10){			        								        	    	    		
				        	    	    		serverPrintOut.println("max number of players joined!");
				        	    	    		serverPrintOut.println("starting the game now.");
				        	    	    		serverPrintOut.println("start");				      	
				        	    	    	Game currentgame = new Game(lobbies.get(i).players);
				        	    	        currentgame.start();
				        	    	        break;
				        	    	    }
				        			  
				        	    	    else if(3<lobbies.get(i).players.size() && lobbies.get(i).players.size()<10){
				        	    	    	try {			    	    	    			
				    	    	    			serverPrintOut.println("it seems enough players have joined to this lobby!");
				    	    	    			serverPrintOut.println("Waiting for admin to start the game:");
				        	    	    	//potansiyel sıkıntı=?
				    	    	    			while(!lobbies.get(i).getAdminChoice().equals("start")){
				        	    	    			lobbies.get(i).setAdminChoice(inFromClient.readLine());
				        	    	    		
				        	    	    			if(lobbies.get(i).getAdminChoice().equals("start")){
				        	    	    				serverPrintOut.println("start");					        	    	    		    			        	    	    				
				        	    	    				Game currentgame = new Game(lobbies.get(i).players);
				        	    	    			    currentgame.start();
				        	    	    			    break;

				        	    	    			}//end of if
				        	    	    		}
        	    				
				        	    			} catch (IOException e) {
				        	    				// TODO Auto-generated catch block
				        	    				e.printStackTrace();
				        	    			}
				        	    	    		
				        	    	    }
				        			  
				        			 // currentplayersize=lobbies.get(i).players.size();
				        			 
				        			  
//				        		}//end of main if
				        	}//end of while loop			            
				            break;
				          }
				}
					System.out.println("Successfully created a lobby without password!");
					//owner of lobby yapılacaklar
					//will be done from gamelobby				
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
					username=Clientusername;
					serverPrintOut.println("successfully logged in to system!");
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
					username=Clientusername;
					serverPrintOut.println("successfully registered!");
					RoomMenuServerside();

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