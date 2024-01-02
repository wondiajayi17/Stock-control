import java.sql.* ;
import java.util.Calendar;
import java.util.Date;	

public class receipt
{
	// ----------------------------------------------------------------------
	// Class variables
	// ----------------------------------------------------------------------
	// fields in db-table.
	static int iReceiptId ;
	private int iShoeId;
	private int iCustomerId ;
	private String szSurname;
	private String szShoeName;
	private String szEmail;
	private String szTown;
	private String szFirstName;
	private String szPostCode;
	private int iPrice;
	private int iInvoice;
	private int iNum;
	private int j;
	private int iNumRows ;




	//initate the link to be able to use classes from the customer and stats classes
	public static customer custom = new customer();
	public static stats data = new stats() ;

	// DB materials.
	private String sql ;
	private String sz ;

	private Statement sqlStatement = null ;
	private ResultSet resultSet = null ;

	private Session dbSession ;

	// ----------------------------------------------------------------------
	// Constructor
	// ----------------------------------------------------------------------
	public receipt()
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

		iNumRows = -1 ; // to spot the difference between empty table & no methods run.
		sz = null ;
		sql = null ;
		sqlStatement = null ;
		resultSet = null ;
		return ;
	}

	//clearing and reseting all the atttributes
	protected void clearAttributes()
	{
		iReceiptId = 0 ;
		iShoeId = 0;
		iCustomerId = 0 ;
		iPrice = 0;

		return ;
	}

	//clear the receiptId
	private void clearId()
	{
		this.iReceiptId = 0 ;
	}

	//Method to generate a receipt into the database with the following variables
	public boolean genReceipt(int iShoeId, String szLast, int iNum)
	{
		boolean bRC = false ;
		clearAttributes() ;

		//making sure the global variables hold the same value as parsed in values.
		this.iInvoice = iNum;
		this.iShoeId = iShoeId;
		this.szSurname = szLast;

        //declare variables
		int i;
		int iSum;
		String szChoice;
		int iCountedRows = 0;
		String szCounter = "2";



		//Get the surname from customers
		try
		{
			//call the customer subroutine that collects surnames.
			szChoice = custom.getBySurname(szLast,szCounter);

			//convert the String back into an int
			this.iCustomerId =  Integer.valueOf(szChoice);

			//collect the details for the customer using thier id
			collectRecord(iCustomerId);


			resultSet.close() ;
		}
		catch ( SQLException se )
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

		//Get the Shoename and the price from shoes database
		try
		{
			//perform a statement to get the shoeName and Price of the shoe selected (by shoeId)
			sql = "SELECT ShoeName "
					+ ", Price"
					+ " FROM myWork.shoes"
					+ " WHERE ShoeId = \'" + iShoeId  + "\'" ;

			//something to make sure the sql statement is somewhat translated
			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;
			resultSet.next() ;

			//assign the got sql statement to the correct variable
			this.szShoeName = resultSet.getString( "ShoeName" ) ;
			this.iPrice = resultSet.getInt( "Price" ) ;
            
            //calculate the invoice by multiplying the price by the initial invoice
			this.iInvoice = iPrice * iInvoice;

			resultSet.close() ;
		}
		catch ( SQLException se )
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

		//send the shoename, pairs of shoes and the amount of the shoes to the database
		data.addStats(szShoeName, iNum, iPrice);

		//method to add the receipt ot the database
		add();
		return ( bRC ) ;
	}

	public boolean add()
	{
		boolean bRC = false ;
		int iRC ;

		try
		{
			//sql statement to insert these fields into the bookings table to create a new receipt
			sql = "INSERT INTO receipts( "
					+ " ShoeId"
					+ ", CustomerId"
					+ ", Surname"
					+ ", Email"
					+ ", ShoeName"
					+ ", Price"
					+ " ) "
					+ " VALUES( "
					+ "\"" + this.iShoeId  + "\""
					+ ", \"" + this.iCustomerId + "\""
					+ ", \"" + this.szSurname + "\""
					+ ", \"" + this.szEmail + "\""
					+ ", \"" + this.szShoeName + "\""
					+ ", \"" + this.iInvoice + "\""
					+ " ) ; " ;


			//something to make sure the sql statement is somewhat translated
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

		//tell the user the receipt has been added
		System.out.print("\n~ Receipt Added ~\n");
		countRows() ; // update how many records we have in the database
		return ( bRC ) ;
	}

	// must already have found the record before trying to delete it.
	public boolean deleteReceipt(int iReceiptId)
	{
		boolean bRC = false ;
		int iRC ;
		try
		{
			sql = "DELETE FROM receipts "
					+ " WHERE"
					+ " ReceiptId  ="
					+ iReceiptId ;

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

		System.out.print("\n~ Receipt Deleted ~\n") ;
		countRows() ;
		return ( bRC ) ;
	}


	// ----------------------------------------------------------------------
	// Getters
	// ----------------------------------------------------------------------
	// ----------------------------------------------------------------------
    
    //method to collect the records
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
			this.iCustomerId = resultSet.getInt( "CustomerId" ) ;
			this.szFirstName = resultSet.getString( "FirstName" ) ;
			this.szSurname = resultSet.getString( "Surname" ) ;
			this.szTown = resultSet.getString( "Town" ) ;
			this.szPostCode = resultSet.getString( "PostCode" ) ;
			this.szEmail = resultSet.getString( "Email" ) ;


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
		return ;
	}

	//method to collect the duplicate records
	public void collectRecordDuplicates( int input )
	{
		try
		{
			sql = "SELECT ReceiptId"
					+ ", ShoeId"
					+ ", CustomerId"
					+ ", Surname"
					+ ", Email"
					+ ", ShoeName"
					+ ", Price"
					+ " FROM receipts"
					+ " WHERE ReceiptId = " + input ;

			//something to make sure the sql statement is somewhat translated
			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;
			resultSet.next() ;

			this.iReceiptId = resultSet.getInt( "ReceiptId" ) ;
			this.iShoeId = resultSet.getInt( "ShoeId" ) ;
			this.iCustomerId = resultSet.getInt( "CustomerId" ) ;
			this.szSurname = resultSet.getString( "Surname" ) ;
			this.szEmail = resultSet.getString( "Email" ) ;
			this.szShoeName = resultSet.getString( "ShoeName" ) ;
			this.iPrice = resultSet.getInt( "Price" ) ;


			resultSet.close();
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
		display();
		return ;
	}

	// Allows the record to be collected based upon receiptId
	public void findReceipt( int iNum )
	{
		int i ;
		int j ;
		int iCountedRows = 0 ;

		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array to store the length of the records found
		int [] iDuplicateArray = new int [iCountedRows + 1];

		try
		{
			sql = "SELECT ReceiptId"
					+ " FROM receipts"
					+ " WHERE CustomerId = " + iNum ;

			//something to make sure the sql statement is somewhat translated
			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;



			//set j to be 1 as that is the first database not 0
			j = 1 ;

			//while the ReceieptId is the same as the next one found enter the loop 
			while (resultSet.next() == true )
			{   

				//Array stores the current ReceieptId collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "ReceiptId" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop



			//For loop to loop the ReceieptId's collected in the array into a method that collects and displays the record 
			for (i = 0 ; i < (j - 1) ; i ++)
			{  
				collectRecordDuplicates(iDuplicateArray[i + 1]); 
			}


			resultSet.close();
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

		return ;
	}
	// ----------------------------------------------------------------------
	// utilities
	// ----------------------------------------------------------------------

	// to determine how many rows exists in this table.
	// this will allow for fixed-loop iterations instead in other methods
	public int countRows()
	{
		if ( iNumRows < 0 )
		{
			try
			{
				sql = "SELECT count(*) AS rowCount FROM receipts;" ;

				//something to make sure the sql statement is somewhat translated
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

	//displays all the receipts that are logged
	public void displayAllRece()
	{
		try
		{
			sql = "SELECT * FROM receipts;" ;

			//something to make sure the sql statement is somewhat translated
			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;
			resultSet.next() ;
			System.out.println( this.getClass().getName() + ":: Found " + iNumRows + " rows in the DB-table." ) ;

			resultSet.close() ;

			//loop that will show all the receipts until the number of rows is exhasted
			for(int i = 1; i <= iNumRows; i++)
			{

				try
				{
					sql = "SELECT ReceiptId"
							+ ", ShoeId"
							+ ", CustomerId"
							+ ", Surname"
							+ ", Email"
							+ ", ShoeName"
							+ ", Price"
							+ " FROM receipts"
							+ " WHERE ReceiptId = " + i ;

					//something to make sure the sql statement is somewhat translated
					sqlStatement = dbSession.getConnection().createStatement() ;
					resultSet = sqlStatement.executeQuery( sql ) ;
					resultSet.next() ;

					iReceiptId = resultSet.getInt( "ReceiptId" ) ;
					iShoeId = resultSet.getInt( "ShoeId" ) ;
					iCustomerId = resultSet.getInt( "CustomerId" ) ;
					szSurname = resultSet.getString( "Surname" ) ;
					this.szEmail = resultSet.getString( "Email" ) ;
					szShoeName = resultSet.getString( "ShoeName" ) ;
					iPrice = resultSet.getInt( "Price" ) ;


					resultSet.close();

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

				display();
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

		return ;
	}

	//display the details to the user
	public void display()
	{
		System.out.println("-----------------------------------------------------") ;
		sz = "\n" ;
		sz = sz + "\n\tReceiptId := " + iReceiptId ;
		sz = sz + "\n\tShoeId := " + iShoeId ;
		sz = sz + "\n\tCustomerId := " + iCustomerId ;
		sz = sz + "\n\tSurname := " + szSurname ;
		sz = sz + "\n\tEmail := " + szEmail ;
		sz = sz + "\n\tShoeName := " + szShoeName ;
		sz = sz + "\n\tInvoice := £" + iPrice ;


		System.out.println( this.getClass().getName() + sz ) ;



		return ;
	}

	// ----------------------------------------------------------------------
	// test rig
	// ----------------------------------------------------------------------
	public static void main( String [] args )
	{
		receipt rece = new receipt() ;


		System.out.println("\n") ;

		return ;
	}
}
