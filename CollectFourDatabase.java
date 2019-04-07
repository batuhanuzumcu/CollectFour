package Game;

import java.sql.*;
import java.util.*;;

public class CollectFourDatabase {
	Scanner input = new Scanner(System.in);

	private final String userName = "root"; // enter user name for DB connection
	private final String password = "root"; // enter password for DB connection
	private final String serverName = "localhost";
	private final int portNumber = 3306;
	private final String dbName = "collectfour"; // the name of the database file
	private String username;
	private String userPassword;
	
	//Registration for Users
	public boolean RegisterData() {
		System.out.println("Please enter your user name: ");
		username = input.nextLine();
		System.out.println("Please enter your password: ");
		userPassword = input.nextLine();
		
		
		Boolean status = false;
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);
		Statement s = null;
		
		try {
			
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/collectfour?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Turkey");
			s = conn.createStatement();
			
			//SQL INSERT
			String sql = "INSERT INTO users " + "(username,password) " + "VALUES ('" + username + "','"
					+ userPassword + "')";
			s.execute(sql);
			status = true;
		} catch (SQLException e) {
			

			e.printStackTrace();
		}
		try {
			if (s != null) {
				s.close();
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return status;
		
		
	}
	
	//Login
	public boolean Login() {
		 try{           
		      // Class.forName("com.mysql.jdbc.Driver");  // MySQL database connection
		       Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/collectfour?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Turkey");     
		       PreparedStatement pst = conn.prepareStatement("Select * from users where username=? and password=?");
		       pst.setString(1, userName); 
		       pst.setString(2, userPassword);
		       ResultSet rs = pst.executeQuery();                        
		       if(rs.next())            
		           return true;    
		       else
		           return false;            
		   }
		   catch(Exception e){
		       e.printStackTrace();
		       return false;
		   }       
		} 
		
	

}
