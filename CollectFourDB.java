package Game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class CollectFourDB {
	String arr = null; // We will use this string to execute queries
    String arr2 = null;
	private final String userName = "root"; // enter user name for DB connection
	private final String password = "cancan563"; // enter password for DB connection
	private final String serverName = "localhost";
	private final int portNumber = 3306;
	private final String dbName = "collectfour"; // the name of the database file
	boolean status = false; // check users' login informations


	public Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);
		conn = DriverManager.getConnection("jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName, connectionProps);
		return conn;
	}
	
	// Registration for Users
	public String RegisterData(String clientName, String clientPassword) {
		Connection conn = null;
		Statement s = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", clientName);
		connectionProps.put("password", clientPassword);
		try {
			conn = this.getConnection();
			s = conn.createStatement();

			// DUPLICATE USERNAME CONTROL
			String usernameControl = "SELECT * FROM users where username='" + clientName + "'";
			ResultSet rs = s.executeQuery(usernameControl);

			if (rs.next() == true) {
				System.out.println("It seems that user name is in use");
				return "USER NAME ALREADY EXISTS";
			}
			// IF THE USER NAME IS UNIQUE
			else {
				arr = "INSERT INTO users " + "(username,password) " + "VALUES ('" + clientName + "','" + clientPassword+ "')";
				s.execute(arr);
				return "success";
			}
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
		return null;
	}
	
	public String Login(String clientName, String clientPass) {
		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		}
		try {
			// get login information
			String sql = "SELECT * FROM users WHERE username=? and password=?";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, clientName);
			st.setString(2, clientPass);
			// We write a query to get the data from the table.
			ResultSet rs = st.executeQuery();
			status = rs.next();
			// TO CHECK IF USER ENTERS HIS/HER INFORMATIONS CORRECTLY OR NOT
			if (!status) {
				System.out.println("wrong username and/or password entered. Please try again.");
				return "TRY AGAIN";
			}
			return "success";
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect the table");
			e.printStackTrace();
			return ("failed to get a username from table");
		}

	}
}
