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
	String clientinput;
	int clientparseInt;
	int TurnEnded;
	List<Integer> discarded = new ArrayList<Integer>();
    
    public GameLobby(String name,String password, int lobbylimit){
        lobbyname=name;
        lobbypassword=password;
        playercounter=0;
        numberofplayers=lobbylimit;
        TurnEnded=0;
        
    }

    public GameLobby(String name, int lobbylimit){
    	lobbyname=name;
    	playercounter=0;
        numberofplayers=lobbylimit;
        TurnEnded=0;
    }
    
    public int getlobbylimit(){
    	return numberofplayers;
    }
    
    public void Join(ClientServiceThread a){
        		
    	players.add(playercounter, a);
        		playercounter++;
    		
    }
    
    public void CurrentPlayers(){
    	sendMessageToAllPlayers("the current users competing inside are: ");

    	for(int a = 0 ; a<players.size(); a++){
    		sendMessageToAllPlayers(players.get(a).getplayername());
    	}
    	
    }
    
    public void play() {
    	if (players.size() == this.numberofplayers) {
    		// lobby is at limit start the game
    		while(true) {
    			sendMessageToAllPlayers("game starts!");
    			CurrentPlayers();
    			
    			// If there are four players, the list is { 1,1,1,1,2,2,2,2,3,3,3,3,4,4,4,4 }
    			List<Integer> collectFourInitial = new ArrayList<>();
    			for (int i=1 ; i <= players.size(); i++) {
    				collectFourInitial.addAll(Collections.nCopies(4, i));
    			}
    			
    			// shuffle the whole list
    			Collections.shuffle(collectFourInitial);
    			// partition the list
    			List<List<Integer>> partitionedCollectFour = partitionedCollectFour(collectFourInitial);
    			
    			//Show the initial deck to players
    			for (int z=0; z < players.size(); z++) {
    				// send each player their numbers
    				players.get(z).serverPrintOut.println(partitionedCollectFour.get(z));
    			}
    			//Set initial decks to players
    			SetDecksToPlayers(partitionedCollectFour);
    			
    			sendMessageToAllPlayers("Now that everyone has seen their decks, time to begin! ");
    			
    			//One round of discarding to be commenced below
    			
    			//first we clean discarded number table for the next round
    			//might have to make discarded checked with if to see current size.
    			if(discarded.size()!=0){
        			for(int t=0 ; t<discarded.size() ; t++){
        				discarded.remove(0);			
        			}	
    			}
    			sendMessageToAllPlayers("Please choose a number to discard from your deck: ");
    			sendMessageToAllPlayers("Send input");
    			for(int a=0 ; a<players.size() ; a++){	
    				try {
						clientinput = players.get(a).inFromClient.readLine();		
					} catch (IOException e) {
						e.printStackTrace();
					}		  		
    				while(true){//start of while loop	
        				clientparseInt = Integer.parseInt(clientinput);
        				players.get(a).setIntToParse(clientparseInt);
        				players.get(a).setParseResult(players.get(a).removenumber(players.get(a).getIntToParse()));    				
    					 if(players.get(a).getParseResult()!=0){
        					discarded.add(clientparseInt);
        					TurnEnded++;
        					break;       				
    					} else if(players.get(a).getParseResult()==0){
        					players.get(a).serverPrintOut.println("Incorrect input! Send again please:");
        					players.get(a).serverPrintOut.println("Send input");
        					try {
								clientinput = players.get(a).inFromClient.readLine();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}				
        				}							
    				}// end of while loop    				
    				
    			}//end of for loop
    			
    			//after everyone correctly enters their input for a round:
    			//will be changed with shuffle the discarded ones and distribute them to players!
    			while(true){
    				if(TurnEnded==4){
    	    			sendMessageToAllPlayers("Here is the list of discarded ones for test");
    	    			sendMessageToAllPlayers(discarded.toString());
    					
    					break;
    				}
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
    
    private void sendMessageToAllPlayers(String message) {
    	for (ClientServiceThread player: players) {
			player.serverPrintOut.println(message);
		}
    }
    
    private List<List<Integer>> partitionedCollectFour(List<Integer> originalList) {
    	//prob this method is giving potential size issues for more than 4 players
    	int partitionSize = this.numberofplayers;
    	List<List<Integer>> partitions = new ArrayList<List<Integer>>();
    	for (int i = 0; i < originalList.size(); i += partitionSize) {
    	    partitions.add(originalList.subList(i,
    	            Math.min(i + partitionSize, originalList.size())));
    	}
    	return partitions;
    }
    
    private void SetDecksToPlayers(List<List<Integer>> partitioned){
    	for (int z=0; z < players.size(); z++) {
			// set each player their decks
			players.get(z).setplayerdeck(partitioned.get(z));
		}
    }
    
    
}
