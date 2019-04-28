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
	boolean passedrequiredscore;
	boolean gamestarted;
	int requiredScore;
	int scoretobeadded;
	int bingocontrolled;
	int waitforbingoinput;
	List<Integer> discarded = new ArrayList<Integer>();
	List<List<Integer>> potentialwinconditions;
    
    public GameLobby(String name,String password, int lobbylimit){
        lobbyname=name;
        lobbypassword=password;
        playercounter=0;
        numberofplayers=lobbylimit;
        TurnEnded=0;
        gamestarted=false;
        
    }

    public GameLobby(String name, int lobbylimit){
    	lobbyname=name;
    	//secret input for no password lobby.
    	lobbypassword="jeoghhgdfheshr24te32451dfs";
    	playercounter=0;
        numberofplayers=lobbylimit;
        TurnEnded=0;
        gamestarted=false;
    }
    
    public int getlobbylimit(){
    	return numberofplayers;
    }
    public void Join(ClientServiceThread a){
    	if(gamestarted==false){
   	    	players.add(playercounter, a);
   	        		playercounter++;	
    	}
    }
    
    public void CurrentPlayers(){
    	sendMessageToAllPlayers("the current users competing inside are: ");

    	for(int a = 0 ; a<players.size(); a++){
    		sendMessageToAllPlayers(players.get(a).getplayername());
    	}
    	
    }
    
    //to give a fresh deck for next potential round.
    public void setnewplayerdecks(){
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
    }
    
    public void play() {
    	if (players.size() == this.numberofplayers) {
    		// lobby is at limit start the game
    		while(true) {
    			sendMessageToAllPlayers("game starts!");
    			gamestarted=true;
    			CurrentPlayers();
    			
    			//set initial bingo condition to false and set score condition
    			FlagForBingo = false;
    			passedrequiredscore=false;
    			bingocontrolled = 0;
    			requiredScore = numberofplayers*numberofplayers;
    			scoretobeadded=numberofplayers;
    			
    			//prepare win conditions for round
    			List<Integer> winConditionInitial = new ArrayList<>();
    			for (int i=1 ; i <= players.size(); i++) {
    				winConditionInitial.addAll(Collections.nCopies(4, i));
    			}
    			//partition the win conditions
    			potentialwinconditions = partitionedCollectFour(winConditionInitial);
    			
    			//prepare player decks and show them
    			setnewplayerdecks();
    			
    			//game will be played using rounds:
    			while(passedrequiredscore==false){
        			GameRounds();
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
    
    private void sendMessageToAllPlayersExceptBingo(String message){
    	for (ClientServiceThread player : players){
			if(player.getreceivedScore()==false){
	    		player.serverPrintOut.println(message);
			}
    	}
    }
    
    private List<List<Integer>> partitionedCollectFour(List<Integer> originalList) {
    	final int chunkSize = 4;
    	List<List<Integer>> partitions = new ArrayList<List<Integer>>();
    	for (int i = 0; i < originalList.size(); i += chunkSize) {
    	    partitions.add(originalList.subList(i,
    	            Math.min(i + chunkSize, originalList.size())));
    	}
    	return partitions;
    }
    
    private void SetDecksToPlayers(List<List<Integer>> partitioned){
    	for (int z=0; z < players.size(); z++) {
			// set each player their decks
			players.get(z).setplayerdeck(partitioned.get(z));
		}
    }
    
   
    private void GameRounds(){
    	//initialize stuff
    	TurnEnded=0;
    	scoretobeadded=numberofplayers;
    	
    	if(discarded.size()!=0){
			for(int t=0 ; t<numberofplayers ; t++){
				discarded.remove(0);			
			}	
		}
    	//initialize receiving scores:
    	for(int v=0; v<players.size(); v++){
    		players.get(v).setreceivedScore(false);
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
    			if(TurnEnded==numberofplayers){
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

    	//the operations to check if someone is at bingo state or not is in this method
    		checkbingostate();   
    	
    	   }//end of gamerounds method
    
    
    //method to check if someone is ready for bingo
    public void checkbingostate(){
    	
    	//initialize bingo checkers.
    	bingocontrolled=0;
    	waitforbingoinput=0;
    	FlagForBingo=false;
    	
    	for(int j=0 ; j<players.size() ; j++){	
    		for(int l=0 ; l<numberofplayers ; l++){
    			//Check if someone reached bingo state!
    			if(players.get(j).getplayerdeck().equals(potentialwinconditions.get(l))){
    				players.get(j).serverPrintOut.println("You can say bingo now! :");
    				players.get(j).serverPrintOut.println("Send Bingo Input:");
    				try {
						clientinput=players.get(j).inFromClient.readLine();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    				//if a players deck is at winning condition
    				while(true){//start of while loop
        				 if(clientinput.equals("bingo")){
        					players.get(j).addtoscore(scoretobeadded);
 		    				scoretobeadded--;
 		    				players.get(j).setreceivedScore(true);
 		    				players.get(j).serverPrintOut.println("Congrats! Now please wait for other players to say bingo.");
 		    				FlagForBingo=true;
 		    				waitforbingoinput++;
 		    	        	sendMessageToAllPlayers("Bingo was typed! Be quick.");
        					break;       				
        				} else {
        					players.get(j).serverPrintOut.println("Incorrect input! Send again please:");
        					players.get(j).serverPrintOut.println("Send Bingo Input:");
        					try {
        						clientinput = players.get(j).inFromClient.readLine();
        					} catch (IOException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					}
        									
        				}							
        			}// end of while loop   
    					
        		break;}
    		}
    		bingocontrolled++;
    	}
    	
    	//block other threads so they continue from this point at same time
    	while(true){
    		if(bingocontrolled==numberofplayers){
    			break;
    		}
    	}
    	
    	  	if(FlagForBingo==true){
    	  		sendMessageToAllPlayersExceptBingo("it seems someone reached bingo! type 'bingo' fast to get most points: ");
				sendMessageToAllPlayersExceptBingo("Send Bingo Input:");		
			for(int o=0; o<players.size(); o++){				
				if(players.get(o).getreceivedScore()==false){
    				try {
						clientinput=players.get(o).inFromClient.readLine();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					while(true){//start of while loop	
        				 if(clientinput.equals("bingo")){
        					players.get(o).addtoscore(scoretobeadded);
 		    				scoretobeadded--;
 		    				players.get(o).setreceivedScore(true);
 		    				players.get(o).serverPrintOut.println("Congrats! Now please wait for other players to say bingo.");
 		    				waitforbingoinput++;
        					break;       				
        				} else {
        					players.get(o).serverPrintOut.println("Incorrect input! Send again please:");
        					players.get(o).serverPrintOut.println("Send Bingo Input:");
        					try {
        						clientinput = players.get(o).inFromClient.readLine();
        					} catch (IOException e) {
        						e.printStackTrace();
        					}				
        				}							
        			}// end of while loop  					
					
				}
			
			}
			
			//point to wait for other players
			while(true){
				if(waitforbingoinput==numberofplayers){
					break;
				}
			}
			//after everyone types in bingos
			sendMessageToAllPlayers("here are the scores at the end of this round: ");
			for(int y = 0 ; y<players.size(); y++){
	    		sendMessageToAllPlayers(players.get(y).getplayername()+"'s score is "+players.get(y).getscore());
	    		if(players.get(y).getscore()>=requiredScore){
	    			passedrequiredscore=true;
	    		}
	    	}
			//to set a new deck to continue playing if they didn't pass the required score.
			if(passedrequiredscore!=true){
				setnewplayerdecks();
			}
			
		} 
    	
    }//end of checkbingostate method
    
}
