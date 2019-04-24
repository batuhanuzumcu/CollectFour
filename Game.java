package Game;

import java.util.ArrayList;

public class Game extends Thread implements Runnable{

	ArrayList<ClientServiceThread> players = new ArrayList<ClientServiceThread>();
	
	public Game(ArrayList<ClientServiceThread> lobbyusers){
		players = lobbyusers;
	}


	@Override
	public void run() {

		for(int i=0; i<players.size(); i++){
			players.get(i).serverPrintOut.println("we did it bois");
		}
		
	}

}
