/**
 * @file: Session.java
 * This is to create and handle the SQL connection from within Java.
 * The idea is that it will be permanent.
 * Based upon work that I did in the past using DB2 & C++.
 * Java generates a lot of potential errors.
 * Some of these are handled.  
 * @author: Amritpal
 * @bugs:
 * @link: http://www.tutorialspoint.com/jdbc/jdbc-db-connections.htm
 * @link: https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html
 * @link: https://mariadb.com/kb/en/mariadb/set-transaction/
 * @link: https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-usagenotes-statements.html#connector-j-examples-execute-select
 *
 */
import java.sql.* ;

public class Session
{
	// ----------------------------------------------------------------------
	// Class variables
	// ----------------------------------------------------------------------
	// JDBC driver name and database URL
	// There is a bug/feature with Java11 and MySQL.  Read the following links found in April 2020.
	// https://bugs.mysql.com/bug.php?id=93590
	// https://stackoverflow.com/questions/53131321/spring-boot-jdbc-javax-net-ssl-sslexception-closing-inbound-before-receiving-p/53136942#53136942
	// The workarounds include setting the SSL flag to either zero or disabled.
	// Both lines are included in the code for reference for the DB_URL String.
	final String JDBC_DRIVER = "org.mysql.jdbc.Driver";  
	// final String DB_URL = "jdbc:mysql://localhost:3306/three"; // this worked on c9.io which used Java8 & MySQL5
    final String DB_URL = "jdbc:mysql://localhost:3306/myWork?useSSL=false"; // this works on Java11 & MySQL5
    // final String DB_URL = "jdbc:mysql://localhost:3306/three?sslMODE=DISABLED"; // this one did not catch the error in codio.co.uk

	// Database credentials
	final String USER = "root";			// in c9.io system, userid = singha
    final String PASS = "codio";  // changed to codio as per their install
	Connection conn = null;
	boolean bConnected ;

	// ----------------------------------------------------------------------
	// Constructor
	// ----------------------------------------------------------------------
	public Session()
	{
		reset() ;
		return ;
	}

	// ----------------------------------------------------------------------
	// Setters
	// ----------------------------------------------------------------------
	private void reset()
	{
		this.bConnected = false ;
		return ;
	}

	public void connect() 
	{
		try
		{
			System.err.println( this.getClass().getName() + ":: Connecting to database...");
			DriverManager.getDriver( DB_URL ) ;
			conn = DriverManager.getConnection( DB_URL , USER , PASS ) ;
			if ( conn != null )
			{
				this.bConnected = true ;
			}
			System.err.println( this.getClass().getName() + ":: successful connection to DB." ) ;
		}
		catch ( SQLException se )
		{ 
			System.err.println( this.getClass().getName() + "::SQL error:: " + se ) ;
			se.printStackTrace() ;
		}
		catch ( Exception e )
		{
			System.err.println( this.getClass().getName() + "::Error:: " + e ) ;
			e.printStackTrace() ;
		}
		finally
		{
		}
		return ;
	}

	public void disconnect()
	{
		try
		{
			this.conn.close() ;
			this.bConnected = false ;
			System.err.println( this.getClass().getName() + ":: disconnected." ) ;
		}
		catch ( SQLException se )
		{
			System.err.println( this.getClass().getName() + ":: SQL error " + se ) ;
			se.printStackTrace() ;
		}
		catch ( Exception e )
		{
			System.err.println( this.getClass().getName() + ":: error " + e ) ;
			e.printStackTrace() ;
		}
		finally
		{
		}
		return ;
	}

	public void commit()
	{
		// this will commit the logical unit of work... if mySQL supports it.
		// will need to use the InnoDB when creating the tables
		// as well as the transaction type.
		// as our system is not distributed or running in parallel, it could be skipped.
	}

	public void rollback()
	{
		// If I use a transaction, have to allow for the failing conditions.
	}
	
	// ----------------------------------------------------------------------
	// Getters
	// ----------------------------------------------------------------------
	public boolean isConnected()
	{
		return ( this.bConnected ) ;
	}

	public Connection getConnection()
	{
		return ( this.conn ) ;
	}

	// ----------------------------------------------------------------------
	// test rig
	// ----------------------------------------------------------------------
	public static void main( String [] args )
	{
		Session foo = new Session() ;
		foo.connect() ;
		if ( foo.isConnected() == true )
		{
			System.out.println( "Session.main():: am now talking to DB." ) ;
		}
		else
		{
			System.out.println( "Session.main():: failed to talk to DB." ) ;
		}
		foo.disconnect() ;
		return ;
	}
}
