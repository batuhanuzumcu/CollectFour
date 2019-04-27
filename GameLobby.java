package Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameLobby extends Thread{

	private String lobbyname,lobbypassword=null;
	ArrayList<ClientServiceThread> players = new ArrayList<ClientServiceThread>();
	int playercounter;
	int numberofplayers;
    
    public GameLobby(String name,String password, int lobbylimit){
        lobbyname=name;
        lobbypassword=password;
        playercounter=0;
        numberofplayers=lobbylimit;
    }

    public GameLobby(String name, int lobbylimit){
    	lobbyname=name;
    	playercounter=0;
        numberofplayers=lobbylimit;
    }
    
    public int getlobbylimit(){
    	return numberofplayers;
    }
   
    
    public void Join(ClientServiceThread a){
        		
    	players.add(playercounter, a);
        		playercounter++;
    		
    }
    
    public void CurrentPlayers(){
		System.out.println("the current users inside are: ");

    	for(int a = 0 ; a<players.size(); a++){
    		players.get(a).playername();
    	}
    	
    }
    
    public void play() {
    	if (players.size() == this.numberofplayers) {
    		// lobby is at limit start the game
    		while(true) {
    			sendMessageToAllPlayers("game starts!");
    			
    			// If there are four players, the list is { 1,1,1,1,2,2,2,2,3,3,3,3,4,4,4,4 }
    			List<Integer> collectFourInitial = new ArrayList<>();
    			for (int i=1 ; i <= players.size(); i++) {
    				collectFourInitial.addAll(Collections.nCopies(4, i));
    			}
    			// shuffle the whole list
    			Collections.shuffle(collectFourInitial);
    			// partition the list
    			List<List<Integer>> partitionedConnectFour = partitionedConnectFour(collectFourInitial);
    			
    			for (int z=0; z < players.size(); z++) {
    				// send each player their numbers
    				players.get(z).serverPrintOut.println(partitionedConnectFour.get(z));
    			}
    			//finish the game
    			sendMessageToAllPlayers("game ended.");
    			break;
    		}
    	}
    }
    
    public String getLobbyName(){
    	return lobbyname;
    }
    public String getLobbyPassword(){
    	return lobbypassword;
    }
    
    public void isserverstillup(){
    	System.out.println("server is still alive!");
    }
    
    private void sendMessageToAllPlayers(String message) {
    	for (ClientServiceThread player: players) {
			player.serverPrintOut.println(message);
		}
    }
    
    private List<List<Integer>> partitionedConnectFour(List<Integer> originalList) {
    	int partitionSize = this.numberofplayers;
    	List<List<Integer>> partitions = new ArrayList<List<Integer>>();
    	for (int i = 0; i < originalList.size(); i += partitionSize) {
    	    partitions.add(originalList.subList(i,
    	            Math.min(i + partitionSize, originalList.size())));
    	}
    	return partitions;
    }
    
    
}
