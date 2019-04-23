package Game;

import java.util.ArrayList;

public class GameLobby extends Thread{

	private String lobbyname,lobbypassword=null;
	ArrayList<ClientServiceThread> players = new ArrayList<ClientServiceThread>();
    
    public GameLobby(String name,String password){
        lobbyname=name;
        lobbypassword=password;
    }

    public GameLobby(String name){
    	lobbyname=name;
    }
    
    public void Join(ClientServiceThread a){
    	players.add(a);  	
    }
    
    
    public void CurrentPlayers(){
		System.out.println("the current players inside are: ");

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
    
    
}
