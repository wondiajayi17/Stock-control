/** 
** @Filname customer.java
** @author Wonderful Ajayi | Candidate number: 6502
** @Purpose Can add customers, can search for customers by specific requirement

import java.sql.* ;
import java.util.Scanner;

public class customer
{
	// ----------------------------------------------------------------------
	// Class variables
	// ----------------------------------------------------------------------
	// fields in db-table.
	private int iCustomerId ;
	private String szFirstName ;
	private String szSurname ;
	private String szTown ;
	private String szPostCode ;
	private String szEmail;
	private int j;
	private int iSum;

	private Scanner szKeyboard = new Scanner (System.in);

	private int iNumRows ;

	// DB materials.
	private String sql ;
	private String sz ;


	private Statement sqlStatement = null ; 
	private ResultSet resultSet = null ;

	private Session dbSession ;

	// ----------------------------------------------------------------------
	// Constructor
	// ----------------------------------------------------------------------
	public customer()
	{
		reset() ;
		this.dbSession = new Session() ;
		dbSession.connect();
		if ( dbSession.isConnected() == false )
		{
			System.err.println( this.getClass().getName() + ":: failed to connect to DB for table." ) ;
		}
		countRows() ;
		return ;
	}

	// ----------------------------------------------------------------------
	// Setters
	// ----------------------------------------------------------------------
	protected void reset()
	{
		clearAttributes() ;

		iNumRows = -1 ;		// to spot the difference between empty table & no methods run.
		sz = null ;
		sql = null ;
		sqlStatement = null ;
		resultSet = null ;
		return ;			
	}

	protected void clearAttributes()
	{
		iCustomerId = 0 ;
		szFirstName = null ;
		szSurname = null ;
		szTown = null ;
		szPostCode = null ;
		szEmail = null ;
		return ;
	}

	private void clearId()
	{
		this.iCustomerId = 0 ;
	}

	//method to add details to the custome database
	public boolean addDetails( String szFirst, String szLast, String szCity ,String szCode, String szMail)
	{
		//declare variables
		boolean bRC = false ;
		String szTemp = "";
		char chTemp;
		clearAttributes() ;


		//validate the city so it can be displayed with a capital letter and then lowercase
		chTemp = szCity.toUpperCase().charAt(0);
		szTemp = szTemp + chTemp;

		for(int i = 1; i < szCity.length(); i++)
		{
			chTemp = szCity.toLowerCase().charAt(i);
			szTemp = szTemp + chTemp;
		}

		//assign the new string to the variables
		szCity = szTemp;

		//reset the temp
		szTemp = "";

		//validate the firstname so it can be displayed with a capital letter and then lowercase
		chTemp = szFirst.toUpperCase().charAt(0);
		szTemp = szTemp + chTemp;

		for(int i = 1; i < szFirst.length(); i++)
		{
			chTemp = szFirst.toLowerCase().charAt(i);
			szTemp = szTemp + chTemp;
		}

		//assign the new string to the variables
		szFirst = szTemp;

		//reset the temp
		szTemp = "";

		//validate the lastname so it can be displayed with a capital letter and then lowercase
		chTemp = szLast.toUpperCase().charAt(0);
		szTemp = szTemp + chTemp;

		for(int i = 1; i < szLast.length(); i++)
		{
			chTemp = szLast.toLowerCase().charAt(i);
			szTemp = szTemp + chTemp;
		}

		//assign the new string to the variables
		szLast = szTemp;

		this.szFirstName = szFirst ;
		this.szSurname = szLast ;
		this.szTown = szCity ;
		this.szPostCode = szCode;
		this.szEmail = szMail ;

		bRC = this.addDetails() ;
		return ( bRC ) ;
	}

	// add all the details to the database that is entered
	public boolean addDetails()
	{
		boolean bRC = false ;
		int iRC ;
		try
		{
			sql = "INSERT INTO customers( "
					+ "FirstName"
					+ ", Surname"
					+ ", Town"
					+ ", PostCode"
					+ ", Email"
					+ " ) "
					+ " VALUES( "
					+ "\"" + this.szFirstName  + "\""
					+ ", \"" + this.szSurname + "\""
					+ ", \"" + this.szTown + "\""
					+ ", \"" + this.szPostCode + "\""
					+ ", \"" + this.szEmail + "\""
					+ " ) ; " ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			iRC = sqlStatement.executeUpdate( sql ) ;

			// iRC will hold how many records were updated or inserted.  zero is bad in this case.
			if ( iRC == 1 )
			{
				bRC = true ;
			}
		}
		catch( SQLException se )
		{
			System.err.println( this.getClass().getName() + ":: SQL error:: " + se ) ;
			se.printStackTrace() ;
		}
		catch ( Exception e )
		{
			System.err.println( this.getClass().getName() + ":: Error:: " + e ) ;
			e.printStackTrace() ;
		}
		finally
		{
		}
		countRows() ;	// update how many records we have.
		return ( bRC ) ;
	}

	//method to delete a customer for the database
	//must already have found the record before trying to delete it.
	public boolean delete(int iCustomerId)
	{
		boolean bRC = false ;
		int iRC ;
		try
		{
			sql = "DELETE FROM customers " 
					+ " WHERE"
					+ " CustomerId  ="
					+ iCustomerId ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			iRC = sqlStatement.executeUpdate( sql ) ;

			// iRC will hold how many records were updated or inserted.  zero is bad in this case.
			if ( iRC == 1 )
			{
				bRC = true ;
			}
		}
		catch( SQLException se )
		{
			System.err.println( this.getClass().getName() + ":: SQL error:: " + se ) ;
			se.printStackTrace() ;
		}
		catch ( Exception e )
		{
			System.err.println( this.getClass().getName() + ":: Error:: " + e ) ;
			e.printStackTrace() ;
		}
		finally
		{
		}
		countRows() ;
		return ( bRC ) ;
	}

	// ----------------------------------------------------------------------
	// Getters
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------

	//allow the user to get the surname of the user that has been passed in.
	public String getBySurname( String szInput, String szCounter )
	{
		boolean bRC = false ;
		int i ;
		int iSum;
		int iCountedRows = 0;
		String szChoice = "";
		int iChoice;

		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array to store the length of the records found
		int [] iDuplicateArray = new int [iCountedRows + 1];

		try
		{
			//Select the customerId from customers where the surname is what is inputted
			sql = "SELECT CustomerId FROM customers "
					+ " WHERE Surname "
					+ " = \'" + szInput + "\' ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			this.j = 1 ;

			//while the surname is the same as the next one found enter the loop 
			while (resultSet.next() == true )
			{   

				//Array stores the current CustomerId collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "CustomerId" ) ;

				//increment j by 1 to update the postion in the array
				this.j++;

			}// end of while loop

			//subtract the array length from the number of records found
			iSum = iDuplicateArray.length - j;

			//subtract the array length from the previous sum (add 1 to balance the array)
			iSum = iDuplicateArray.length - (iSum + 1);


			if(iSum > 1)
			{
				//For loop to loop the customerid's collected in the array into a method that collects and displays the record 
				for (i = 0 ; i < iSum ; i ++)
				{
					collectRecord(iDuplicateArray[i + 1]);
				} 

				//if the counter is 2 then it is being called from the receipts.java and must enter here if not contiue witht the program
				if(szCounter.equals("2"))
				{

					//subroutine to allow the user to choose the correct customer with the same surname
					szChoice = getDuplicateChoice(szInput);

				}            
			}                       
			else
			{
				//collec the record of the customer using the ID
				collectRecord(iDuplicateArray [ 1 ]);


				//store it in an int
				iChoice = iDuplicateArray [ 1 ];

				//store int in a string to parse back to the receipts.
				szChoice =  Integer.toString(iChoice);

			}

			resultSet.close() ;
		}
		catch( SQLException se )
		{
			System.err.println( this.getClass().getName() + ":: SQL error:: " + se ) ;
			se.printStackTrace() ;
		}
		catch ( Exception e )
		{
			System.err.println( this.getClass().getName() + ":: Error:: " + e ) ;
			e.printStackTrace() ;
		}
		finally
		{
		}
		return (szChoice);
	}

	//method to get the duplicate cutomers 
	public String getDuplicateChoice( String szInput )
	{
		//declare variables
		String szChoice;

		//tell the user that they have muliple customers with the same surname
		System.out.print("\nThere are duplicate customers with the Surname \"" + szInput + "\"."
				+  " Please select which customerId is the customer your're making the receipt for: " );

		//allow the user to enter the choice of the customer theyre looking for
		szChoice = szKeyboard.next();

		return (szChoice);
	}

	//gets the users email as an input and searches for the customerId and email and send it back to collectRecordByEmail 
	public boolean getByEmail( String szInput )
	{
		boolean bRC = false ;
		int i ;
		try
		{
			sql = "SELECT CustomerId "
					+ ", Email"
					+ " FROM customers ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;
			bRC = resultSet.next() ;

			if ( bRC == true )
			{
				for ( i = 1 ; i <= iNumRows ; i++ )
				{
					if ( resultSet.getString( "Email" ).compareToIgnoreCase( szInput ) == 0 ) 
					{
						this.iCustomerId = resultSet.getInt( "CustomerId" ) ;
						this.szSurname = resultSet.getString( "Email" ) ;
						bRC = true ;
					}
					else
					{
						resultSet.next() ;
					}
				}
			}
			else
			{
				System.err.println( this.getClass().getName() + ":: resultSet := " + resultSet ) ;
			}
			resultSet.close() ;
		}
		catch( SQLException se )
		{
			System.err.println( this.getClass().getName() + ":: SQL error:: " + se ) ;
			se.printStackTrace() ;
		}
		catch ( Exception e )
		{
			System.err.println( this.getClass().getName() + ":: Error:: " + e ) ;
			e.printStackTrace() ;
		}
		finally
		{
		}
		return ( bRC ) ;
	}

	// ----------------------------------------------------------------------
	// Allows the entire record to be collected based upon either the CustomerID
	public void collectRecord( int input )
	{
		try
		{
			sql = "SELECT CustomerId"
					+ ", FirstName"
					+ ", Surname"
					+ ", Town"
					+ ", PostCode"
					+ ", Email"
					+ " FROM customers"
					+ " WHERE CustomerId = " + input ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;
			resultSet.next() ;
			iCustomerId = resultSet.getInt( "CustomerId" ) ;
			szFirstName = resultSet.getString( "FirstName" ) ;
			szSurname = resultSet.getString( "Surname" ) ;
			szTown = resultSet.getString( "Town" ) ;
			szPostCode = resultSet.getString( "PostCode" ) ;
			szEmail = resultSet.getString( "Email" ) ;

			resultSet.close() ;
		}
		catch( SQLException se )
		{
			System.err.println( this.getClass().getName() + ":: SQL error:: " + se ) ;
			se.printStackTrace() ;
		}
		catch ( Exception e )
		{
			System.err.println( this.getClass().getName() + ":: Error:: " + e ) ;
			e.printStackTrace() ;
		}
		finally
		{
		}

		//display the record collected
		display();

		return ;
	}

	//allows the user to search for the user by entering their email
	public void collectRecordByEmail(String szEmail)
	{
		if ( szEmail.length() > 0 )
		{
			if ( this.iCustomerId == 0 )
			{
				clearId() ;
				if ( getByEmail( szEmail ) == true )
				{
					collectRecord( this.iCustomerId ) ;
				}
			}
		}
		return ;
	}


	// ----------------------------------------------------------------------
	// utilities
	// ----------------------------------------------------------------------

	// ----------------------------------------------------------------------
	// to determine how many rows exists in this table.
	// this will allow for fixed-loop iterations instead in other methods
	public int countRows()
	{
		if ( iNumRows < 0 )
		{
			try
			{
				sql = "SELECT count(*) AS rowCount FROM customers;" ;

				sqlStatement = dbSession.getConnection().createStatement() ;
				resultSet = sqlStatement.executeQuery( sql ) ;
				resultSet.next() ;

				iNumRows = resultSet.getInt( "rowCount" ) ;
				System.out.println( this.getClass().getName() + ":: Found " + iNumRows + " rows in the DB-table." ) ;

				resultSet.close() ;
			}
			catch( SQLException se )
			{
				System.err.println( this.getClass().getName() + ":: SQL error:: " + se ) ;
				se.printStackTrace() ;
			}
			catch ( Exception e )
			{
				System.err.println( this.getClass().getName() + ":: Error:: " + e ) ;
				e.printStackTrace() ;
			}
			finally
			{
			}
		}
		return ( this.iNumRows ) ;
	}

	//Method to view all of the customers stored in the database
	public void viewAllCustomers(int i)
	{
		try
		{

			countRows();

			sql = "SELECT CustomerId"
					+ ", FirstName"
					+ ", Surname"
					+ ", Town"
					+ ", PostCode"
					+ ", Email"
					+ " FROM customers" 
					+ " WHERE CustomerId = " + i ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			resultSet.next() ;

			iCustomerId = resultSet.getInt( "CustomerId" ) ;
			szFirstName = resultSet.getString( "FirstName" ) ;
			szSurname = resultSet.getString( "Surname" ) ;
			szTown = resultSet.getString( "Town" ) ;
			szPostCode = resultSet.getString( "PostCode" ) ;
			szEmail = resultSet.getString( "Email" ) ;

			resultSet.close() ;
		}       

		catch( SQLException se )
		{
			System.err.println( this.getClass().getName() + ":: SQL error:: " + se ) ;
			se.printStackTrace() ;
		}
		catch ( Exception e )
		{
			System.err.println( this.getClass().getName() + ":: Error:: " + e ) ;
			e.printStackTrace() ;
		}
		finally
		{
		}

		//Method to display the record through the variables
		display();

		return ;
	}


	public void display()
	{
		System.out.println("-----------------------------------------------------") ;
		sz = "\n" ;
		sz = sz + "\n\tCustomerId := " + iCustomerId ;
		sz = sz + "\n\tFirstName := " + szFirstName ;
		sz = sz + "\n\tSurname := " + szSurname ;
		sz = sz + "\n\tTown := " + szTown ;
		sz = sz + "\n\tPostCode := " + szPostCode ;
		sz = sz + "\n\tEmail := " + szEmail ;
		System.out.println( this.getClass().getName() + sz ) ;


		return ;
	}

	// ----------------------------------------------------------------------
	// test rig
	// ----------------------------------------------------------------------
	public static void main( String [] args )
	{
		customer custom = new customer() ;

		//Testing to see if i can file the database details for ...
		custom.clearId() ;
		custom.collectRecordByEmail("Ogudanieldo@outlook.com") ;
		custom.display() ;
		System.out.println("\n") ;

		return ;
	}
}