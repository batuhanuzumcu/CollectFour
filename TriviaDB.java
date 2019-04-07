package TriviaQuiz_1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class TriviaDB {
	int numberofquestions = 38; // the range to take the random number from
								// total number of questions
	
	String arr = null; // We will use this string to execute queries
	int checkprevious = 0;// to control not to get same question in a row

	public int getRandomNumber() {// take random question ID to get question data
		
		int randomnumberquestion = (int) (Math.random() * (numberofquestions - 1)) + 1;
		
		//to prevent duplicate entry in a row.
		if (checkprevious == randomnumberquestion) {
			randomnumberquestion = (int) (Math.random() * (numberofquestions - 1)) + 1;
			checkprevious = randomnumberquestion;
		}
		return randomnumberquestion;
	}

	private final String userName = "root"; // enter user name for DB connection
	private final String password = "123"; // enter password for DB connection
	private final String serverName = "localhost";
	private final int portNumber = 3306;
	private final String dbName = "test"; // the name of the database file

	public Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);
		conn = DriverManager.getConnection("jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName, connectionProps);
		return conn;
	}

	// We start to write get methods to take data from database tables using
	// query statements.

	public String getQuestion(int id) {
		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		}

		try {
			PreparedStatement st = conn.prepareStatement("SELECT * " + "FROM questiontable " + "WHERE id=" + id);
			// We write a query to get the data from the table.
			ResultSet rs = st.executeQuery();

			arr = null;
			while (rs.next()) {
				String questn = rs.getString("question");
				arr = questn.replace("\n", ",");
			}
			return arr;
		}

		catch (SQLException e) {
			System.out.println("ERROR: Could not connect the table");
			e.printStackTrace();
			return ("failed to get a question from table");
		}

	}
	// After this point it is just get methods for answers ,score and correct answer.

	
	public String getAnswerA(int id) {
		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		}

		try {
			PreparedStatement st = conn.prepareStatement("SELECT * " + "FROM questiontable " + "WHERE id=" + id);
			ResultSet rs = st.executeQuery();

			arr = null;
			while (rs.next()) {
				String AnswerA = rs.getString("A");
				arr = AnswerA.replace("\n", ",");
			}
			return arr;
		}

		catch (SQLException e) {
			System.out.println("ERROR: Could not connect the table");
			e.printStackTrace();
			return ("failed to get answer A from table");
		}

	}

	public String getAnswerB(int id) {
		// Connect to MYSQL
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		}

		try {
			PreparedStatement st = conn.prepareStatement("SELECT * " + "FROM questiontable " + "WHERE id=" + id);
			ResultSet rs = st.executeQuery();

			arr = null;
			while (rs.next()) {
				String AnswerB = rs.getString("B");
				arr = AnswerB.replace("\n", ",");
			}
			return arr;
		}

		catch (SQLException e) {
			System.out.println("ERROR: Could not connect the table");
			e.printStackTrace();
			return ("failed to get answer B from table");
		}

	}

	public String getAnswerC(int id) {
		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		}

		try {
			PreparedStatement st = conn.prepareStatement("SELECT * " + "FROM questiontable " + "WHERE id=" + id);
			ResultSet rs = st.executeQuery();

			arr = null;
			while (rs.next()) {
				String AnswerC = rs.getString("C");
				arr = AnswerC.replace("\n", ",");
			}
			return arr;
		}

		catch (SQLException e) {
			System.out.println("ERROR: Could not connect the table");
			e.printStackTrace();
			return ("failed to get answer C from table");
		}

	}

	public String getAnswerD(int id) {
		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		}

		try {
			PreparedStatement st = conn.prepareStatement("SELECT * " + "FROM questiontable " + "WHERE id=" + id);
			ResultSet rs = st.executeQuery();

			arr = null;
			while (rs.next()) {
				String AnswerD = rs.getString("D");
				arr = AnswerD.replace("\n", ",");
			}
			return arr;
		}

		catch (SQLException e) {
			System.out.println("ERROR: Could not connect the table");
			e.printStackTrace();
			return ("failed to get answer D from table");
		}

	}

	public String getcorrectAnswer(int id) {
		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		}

		try {
			PreparedStatement st = conn.prepareStatement("SELECT * " + "FROM questiontable " + "WHERE id=" + id);
			ResultSet rs = st.executeQuery();

			arr = null;
			while (rs.next()) {
				String CorrectAnswer = rs.getString("answer");
				arr = CorrectAnswer.replace("\n", ",");
			}
			return arr;
		}

		catch (SQLException e) {
			System.out.println("ERROR: Could not connect the table");
			e.printStackTrace();
			return ("failed to get the correct answer from table");
		}

	}

	public String getQuestionScore(int id) {
		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		}

		try {
			PreparedStatement st = conn.prepareStatement("SELECT * " + "FROM questiontable " + "WHERE id=" + id);
			ResultSet rs = st.executeQuery();

			arr = null;
			while (rs.next()) {
				String Score = rs.getString("score");
				arr = Score.replace("\n", ",");
			}
			return arr;
		}

		catch (SQLException e) {
			System.out.println("ERROR: Could not connect the table");
			e.printStackTrace();
			return ("failed to get the question score from table");
		}

	}

}
