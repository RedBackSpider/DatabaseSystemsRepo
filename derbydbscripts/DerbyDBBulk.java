import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Properties;
import java.io.File;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.lang.Integer;
import java.io.FileNotFoundException;

public class DerbyDBBulk
{
    private String framework = "embedded";
    private String protocol = "jdbc:derby:";

    public static void main(String[] args)
    {
        new DerbyDBBulk().go(args);
        System.out.println("SimpleApp finished");
    }
    void go(String[] args)
    {
	
        parseArguments(args);
        System.out.println("SimpleApp starting in " + framework + " mode");

        Connection conn = null;
        ArrayList<Statement> statements = new ArrayList<Statement>(); // list of Statements, PreparedStatements
        PreparedStatement psInsert;
        PreparedStatement psUpdate;
        Statement s;
	Statement rts; // Statement to call for Runtimestatistics
	ResultSet rtsrs; // Result set from Runtime Statistics
        ResultSet rs = null;
        try
	    {
		Properties props = new Properties(); // connection properties
		//props.put("user", "user1");
		//props.put("password", "user1");

		String dbName = "derbyDB"; // the name of the database
		conn = DriverManager.getConnection(protocol + dbName
						   + ";create=true", props);

		System.out.println("Connected to and created database " + dbName);

		conn.setAutoCommit(false);

		s = conn.createStatement();
		statements.add(s);
		
		s.execute("create table locale(id int, code varchar(3), name varchar(40))");
		System.out.println("Created table locale");
		//s.execute("call SYSCS_UTIL.SYSCS_SET_RUNTIMESTATISTICS(1)");
		//s.execute("call SYSCS_UTIL.SYSCS_SET_STATISTICS_TIMING(1)");
		psInsert = conn.prepareStatement("insert into locale values (?, ?, ?)");
		statements.add(psInsert);
		String csvFile1 = "/home/ec2-user/test.csv";
		Scanner sc = new Scanner(new File(csvFile1));
		while(sc.hasNextLine())
		{
		    String line = sc.nextLine();
		    StringTokenizer st = new StringTokenizer(line, "\t");
		    int id = 0;
		    ArrayList<String> linetokens = new ArrayList<String>();
		    System.out.println("There");
		    while(st.hasMoreTokens())
		    {
			String token = st.nextToken();
			token = token.replaceAll("^\"|\"$", "");
			linetokens.add(token);
			//System.out.println("Number of Tokens left: " + tokenCount + " Token: " + token);
		    }
		    id = Integer.parseInt(linetokens.get(0));
		    //System.out.println("Number of Tokens:" + linetokens.size());
		    System.out.println(id);
		    psInsert.setInt(1, id);
		    psInsert.setString(2, linetokens.get(1));
		    psInsert.setString(3, linetokens.get(2));
		    psInsert.executeUpdate();
		}
		/*
		psInsert.setInt(1, 1956);
		psInsert.setString(2, "Webster St.");
		psInsert.executeUpdate();
		System.out.println("Inserted 1956 Webster");

		psInsert.setInt(1, 1910);
		psInsert.setString(2, "Union St.");
		psInsert.executeUpdate();
		System.out.println("Inserted 1910 Union");
		*/
		//psUpdate = conn.prepareStatement(
		//					 "update location set num=?, addr=? where num=?");
		//statements.add(psUpdate);

		//psUpdate.setInt(1, 180);
		//psUpdate.setString(2, "Grand Ave.");
		//psUpdate.setInt(3, 1956);
		//psUpdate.executeUpdate();
		//System.out.println("Updated 1956 Webster to 180 Grand");

		//psUpdate.setInt(1, 300);
		//psUpdate.setString(2, "Lakeshore Ave.");
		//psUpdate.setInt(3, 180);
		//psUpdate.executeUpdate();
		//System.out.println("Updated 180 Grand to 300 Lakeshore");

		//rs = s.executeQuery(
		//		    "SELECT num, addr FROM location ORDER BY num");

		/*int number; street number retrieved from the database
		boolean failure = false;
		if (!rs.next())
		    {
			failure = true;
			reportFailure("No rows in ResultSet");
		    }

		if ((number = rs.getInt(1)) != 300)
		    {
			failure = true;
			reportFailure(
				      "Wrong row returned, expected num=300, got " + number);
		    }

		if (!rs.next())
		    {
			failure = true;
			reportFailure("Too few rows");
		    }

		if ((number = rs.getInt(1)) != 1910)
		    {
			failure = true;
			reportFailure(
                        "Wrong row returned, expected num=1910, got " + number);
			}

		if (rs.next())
		    {
		        failure = true;
			reportFailure("Too many rows");
		    }

		if (!failure) 
		{
		    System.out.println("Verified the rows");
		}*/

            // delete the table
            //s.execute("drop table location");
            //System.out.println("Dropped table location");
	    //rs = s.executeQuery("VALUES SYSCS_UTIL.SYSCS_GET_RUNTIMESTATISTICS();");
	    
            conn.commit();
            System.out.println("Committed the transaction");

            if (framework.equals("embedded"))
            {
                try
                {
                    DriverManager.getConnection("jdbc:derby:;shutdown=true");
                }
                catch (SQLException se)
                {
                    if (( (se.getErrorCode() == 50000)
                            && ("XJ015".equals(se.getSQLState()) ))) {
                        System.out.println("Derby shut down normally");
                    } else {
                        System.err.println("Derby did not shut down normally");
                        printSQLException(se);
                    }
                }
            }
        }
        catch (SQLException sqle)
        {
            printSQLException(sqle);
        }
	catch (FileNotFoundException e) 
	{
	    e.printStackTrace();
	}
	finally {
            // release all open resources to avoid unnecessary memory usage

            // ResultSet
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (SQLException sqle) {
                printSQLException(sqle);
            }

            // Statements and PreparedStatements
            int i = 0;
            while (!statements.isEmpty()) {
                Statement st = (Statement)statements.remove(i);
                try {
                    if (st != null) {
                        st.close();
                        st = null;
                    }
                } catch (SQLException sqle) {
                    printSQLException(sqle);
                }
            }

            //Connection
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException sqle) {
                printSQLException(sqle);
            }
        }
    }

    private void reportFailure(String message) {
        System.err.println("\nData verification failed:");
        System.err.println('\t' + message);
    }

    public static void printSQLException(SQLException e)
    {
        // Unwraps the entire exception chain to unveil the real cause of the
        // Exception.
        while (e != null)
        {
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + e.getSQLState());
            System.err.println("  Error Code: " + e.getErrorCode());
            System.err.println("  Message:    " + e.getMessage());
            // for stack traces, refer to derby.log or uncomment this:
            //e.printStackTrace(System.err);
            e = e.getNextException();
        }
    }

    private void parseArguments(String[] args)
    {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("derbyclient"))
		{
		    framework = "derbyclient";
		    protocol = "jdbc:derby://localhost:1527/";
		}
        }
    }
}
