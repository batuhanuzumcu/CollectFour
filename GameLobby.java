package Game;


public class GameLobby extends Thread{

	private String lobbyname,lobbypassword=null;
    
    public GameLobby(String name,String password){
        lobbyname=name;
        lobbypassword=password;
    }

    public GameLobby(String name){
    	lobbyname=name;
    }
}
