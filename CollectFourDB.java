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

	private final String userName = "root"; // enter user name for DB connection
	private final String password = "cancan563"; // enter password for DB connection
	private final String serverName = "localhost";
	private final int portNumber = 3306;
	private final String dbName = "collectfour"; // the name of the database file

	// Registration for Users
	public String RegisterData(String clientName, String clientPassword) {
		Connection conn = null;
		Statement s = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", clientName);
		connectionProps.put("password", clientPassword);
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/collectfour?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Turkey");

			s = conn.createStatement();

			// DUPLICATE USERNAME CONTROL
			String usernameControl = "SELECT * FROM users where username='" + clientName + "'";
			ResultSet rs = s.executeQuery(usernameControl);

			if (rs.next() == true) {
				System.out.println("USER NAME ALREADY EXISTS.");
				return "fail";

				// IF THE USER NAME IS UNIQUE
			} else {
				arr = "INSERT INTO users " + "(username,password) " + "VALUES ('" + clientName + "','" + clientPassword
						+ "')";

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

	// LOGIN
	public String Login(String username, String password) {
		// Connect to MySQL
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/collectfour?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Turkey");
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		}
		try {
			PreparedStatement st = conn.prepareStatement("SELECT * " + "FROM users " + "WHERE username LIKE " + "'%"
					+ username + "%'" + " AND password LIKE " + "'%" + password + "%'");
			// We write a query to get the data from the table.
			ResultSet rs = st.executeQuery();

			arr = null;
			while (rs.next()) {
				String x = rs.getString("username");
				arr = x.replace("\n", ",");
			}
			if (!arr.equals(username)) {
				System.out.println("wrong username and/or password entered. Please try again.");
				return "fail";
			}
			return "success";
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect the table");
			e.printStackTrace();
			return ("failed to get a username from table");
		}

	}

}