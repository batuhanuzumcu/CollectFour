package Game;

import java.io.IOException;
import java.util.ArrayList;

public class GameLobby extends Thread{

	private String lobbyname,lobbypassword=null;
	ArrayList<ClientServiceThread> players = new ArrayList<ClientServiceThread>();
	ClientServiceThread admin;
	String adminchoice;
	int playercounter;
    
    public GameLobby(String name,String password){
        lobbyname=name;
        lobbypassword=password;
        adminchoice="x";
        playercounter=0;
    }

    public GameLobby(String name){
    	lobbyname=name;
    	adminchoice="x";
    	playercounter=0;
    }
    
    public void Join(ClientServiceThread a){
        		
    	players.add(playercounter, a);
        		playercounter++;
    		
    }
    public String getAdminChoice(){
    	return adminchoice;
    }
    public void setAdminChoice(String newChoice){
    	adminchoice=newChoice;
    }
    
    public void CurrentPlayers(){
		System.out.println("the current users inside are: ");

    	for(int a = 0 ; a<players.size(); a++){
    		players.get(a).playername();
    	}
    	
    }
    
    public String getLobbyName(){
    	return lobbyname;
    }
    public String getLobbyPassword(){
    	return lobbypassword;
    }
    
//    public void Preparation(){
//		int currentplayersize = players.size();
//		
//    	while(true){
//    		
//    		if(currentplayersize!=players.size()){
//    			  if(players.size()==10){
//    	    	    	for(int i=0 ; i<players.size() ; i++){
//        	    	    	players.get(i).serverPrintOut.println("max number of players joined!");
//        	    	    	players.get(i).serverPrintOut.println("starting the game now.");
//    	    	    		players.get(i).serverPrintOut.println("start");
//    	    	    	}
//    	    	    	Game currentgame = new Game(players);
//    	    	        currentgame.start();
//    	    	        break;
//    	    	    }
//    	    	    else if(3<players.size() && players.size()<10){
//    	    	    	try {
//	    	    			for(int i=0 ; i<players.size() ; i++){
//        	    	    	players.get(i).serverPrintOut.println("it seems enough players have joined to this lobby!");
//        	    	    	players.get(i).serverPrintOut.println("Waiting for admin to start the game:");
//    	    	    	}
//    	    	    		String adminchoice="x";
//    	    	    		while(!adminchoice.equals("start")){
//    	    	    			
//    	    	    			adminchoice = players.get(0).inFromClient.readLine();
//    	    	    		
//    	    	    			if(adminchoice.equals("start")){
//    	    	    				
//    	    	    				for(int i=0 ; i<players.size() ; i++){
//    	    	    		    		players.get(i).serverPrintOut.println("start");	
//    	    	    		    	}//end of for
//    	    	    				
//    	    	    				Game currentgame = new Game(players);
//    	    	    			    currentgame.start();
//    	    	    			    break;
//
//    	    	    			}//end of if
//    	    	    		}
//
//    	    				
//    	    			} catch (IOException e) {
//    	    				// TODO Auto-generated catch block
//    	    				e.printStackTrace();
//    	    			}
//    	    	    		
//    	    	    }
//    			  
//    			  currentplayersize=players.size();
//    			  try {
//					this.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//    			  
//    		}//end of main if
//    		
//    	}//end of while loop
//        
//    }// end of preparation method

    
    
}
