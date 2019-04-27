package Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GameLobby extends Thread{

	private String lobbyname,lobbypassword=null;
	ArrayList<ClientServiceThread> players = new ArrayList<ClientServiceThread>();
	int playercounter;
	int numberofplayers;
	String clientinput;
	int clientparseInt;
	int TurnEnded;
	boolean FlagForBingo;
	int requiredScore;
	List<Integer> discarded = new ArrayList<Integer>();
	List<List<Integer>> potentialwinconditions;
    
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
    			
    			//set initial bingo condition to false and set score condition
    			FlagForBingo = false;
    			requiredScore = numberofplayers*numberofplayers;
    			
    			//prepare win conditions for round
    			List<Integer> winConditionInitial = new ArrayList<>();
    			for (int i=1 ; i <= players.size(); i++) {
    				winConditionInitial.addAll(Collections.nCopies(4, i));
    			}
    			//partition the win conditions
    			potentialwinconditions = partitionedCollectFour(winConditionInitial);
    			
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
    			//game will be played using rounds:
    			while(true){
        			GameRound();
        			//might do break after score checking every time gameround ends.
        			break;
    			}

    			//finish the game
    			sendMessageToAllPlayers("it seems that a player has passed the required points for victory.");
    			sendMessageToAllPlayers("Here are the final scores: ");
    	    	for(int v = 0 ; v<players.size(); v++){
    	    		sendMessageToAllPlayers(players.get(v).getplayername()+" had a score of "+players.get(v).getscore());
    	    	}
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
    
   
    private void GameRound(){
    	//initialize stuff
    	TurnEnded=0;
    	if(discarded.size()!=0){
			for(int t=0 ; t<discarded.size() ; t++){
				discarded.remove(0);			
			}	
		}
    	//Check if someone reached bingo state!
    	for(int j=0 ; j<players.size() ; j++){	
    		for(int l=0 ; l<numberofplayers ; l++){
    			if(players.get(j).getplayerdeck()==potentialwinconditions.get(l)){
    				FlagForBingo=true;
    				//yarın bunu dogru yere kurmakla ugras puanlar dagıtılsın xd
        		}
    		}
    		
    	}
    	//start the discard operation
    	sendMessageToAllPlayers("Please choose a number to discard from your deck: ");
		sendMessageToAllPlayers("Send input");
		for(int a=0 ; a<players.size() ; a++){	//start of for loop
			try {
				clientinput = players.get(a).inFromClient.readLine();		
			} catch (IOException e) {
				e.printStackTrace();
			}		  		
			while(true){//start of while loop	
				clientparseInt = Integer.parseInt(clientinput);
				 if(players.get(a).findnumber(clientparseInt)!=0){
					discarded.add(clientparseInt);
					TurnEnded++;
					break;       				
				} else if(players.get(a).findnumber(clientparseInt)==0){
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
		while(true){
			if(TurnEnded==4){
    			Collections.shuffle(discarded);
    			sendMessageToAllPlayers("now to get your new numbers: ");	    			
				break;
			}
		}
		//distribute shuffled discard values!
		for(int h=0 ; h<players.size() ; h++){
			players.get(h).changedeckvalue(players.get(h).getnumbertochange(),discarded.get(h));
			players.get(h).serverPrintOut.println(players.get(h).getplayerdeck());
		}
		
		
		
    }//end of one round method
    
}
