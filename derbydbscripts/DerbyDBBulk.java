import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Properties;
import java.io.File;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.lang.Integer;
import java.lang.Long;
import java.util.Arrays;
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
        Statement s;
	ResultSet rtsrs = null; // Result set from Runtime Statistics
        ResultSetMetaData rtsrsmd = null;
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
		
		s.execute("create table asicnames(id int, bntype varchar(14), bnname varchar(200), bnstatus varchar(12), bnreg varchar(10), bncancel varchar(10), bnrenew varchar(10), bnstatenum varchar(10), bnstatereg varchar(3), bnabn bigint)");
		System.out.println("Created table asicnames");
		s.execute("call SYSCS_UTIL.SYSCS_SET_RUNTIMESTATISTICS(1)");
		s.execute("call SYSCS_UTIL.SYSCS_SET_STATISTICS_TIMING(1)");
		psInsert = conn.prepareStatement("insert into asicnames values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		statements.add(psInsert);
		int idcount = 0;
		String csvFile1 = "/home/ec2-user/BUSINESS_NAMES_201803.csv";
		Scanner sc = new Scanner(new File(csvFile1));
		sc.nextLine();
		while(sc.hasNextLine())
		{
		    String line = sc.nextLine();
		    int id = 0;
		    ArrayList<String> linetokens = new ArrayList<String>(Arrays.asList(line.split("\t", -1)));
		    idcount++;
		    long abn = 0;
		    if(linetokens.get(8).length() != 0)
		    {
			abn = Long.parseLong(linetokens.get(8));
		    }
		    psInsert.setInt(1, idcount);
		    psInsert.setString(2, linetokens.get(0));
		    psInsert.setString(3, linetokens.get(1));
		    psInsert.setString(4, linetokens.get(2));
		    psInsert.setString(5, linetokens.get(3));
		    psInsert.setString(6, linetokens.get(4));
		    psInsert.setString(7, linetokens.get(5));
		    psInsert.setString(8, linetokens.get(6));
		    psInsert.setString(9, linetokens.get(7));
		    if(abn != 0)
		    {
			psInsert.setLong(10, abn);
		    }
		    psInsert.executeUpdate();
		}
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
		
	    rtsrs = s.executeQuery("VALUES SYSCS_UTIL.SYSCS_GET_RUNTIMESTATISTICS()");
	    rtsrsmd = rtsrs.getMetaData();
	    int columnsNumber = rtsrsmd.getColumnCount();
	    while (rtsrs.next()) {
		for (int i = 1; i <= columnsNumber; i++) {
		    if (i > 1) System.out.print(",  ");
		    String columnValue = rtsrs.getString(i);
		    System.out.print(columnValue + " " + rtsrsmd.getColumnName(i));
		}
		System.out.println("");
	    }
            s.execute("call SYSCS_UTIL.SYSCS_SET_RUNTIMESTATISTICS(0)");
	    s.execute("call SYSCS_UTIL.SYSCS_SET_STATISTICS_TIMING(0)");	
	    
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
		if (rtsrs != null)
		{
		    rtsrs.close();
		    rtsrs = null;
		}
		if (rtsrsmd != null)
		{
		    rtsrsmd = null;
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
