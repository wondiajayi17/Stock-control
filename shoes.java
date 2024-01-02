import java.sql.* ;

public class shoes
{
	// ----------------------------------------------------------------------
	// Class variables
	// ----------------------------------------------------------------------
	// fields in db-table.
	private int iShoeId ;
	private String szShoeName ;
	private String szBoxCode ;
	private String szUkSize ;
	private String szEuSize ;
	private String szColour ;
	private int iPrice ;
	private int iStockLevel ;
	private String szOrder;
	private int j;
	private int iSum;



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
	public shoes()
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
		iShoeId = 0 ;
		szShoeName = null ;
		szBoxCode = null;
		szUkSize = null;
		szEuSize = null ;
		szColour = null;
		iPrice = 0;
		iStockLevel = 0 ;
		szOrder = null;

		return ;
	}

	private void clearId()
	{
		this.iShoeId = 0 ;
	}


	//method to add the shoes to the database
	public boolean addShoes( String shoe, String code, String uk, String eu , String colour , int price, int stock, String order)
	{
		//declare variables
		String szTemp = "";
		char chTemp;
		boolean bRC = false ;
		clearAttributes() ;

		//validate the colour so it can be displayed with a capital letter and then lowercase by force
		chTemp = colour.toUpperCase().charAt(0);
		szTemp = szTemp + chTemp;

		for(int i = 1; i < colour.length(); i++)
		{
			chTemp = colour.toLowerCase().charAt(i);
			szTemp = szTemp + chTemp;
		}

		//assign the new string to the variables
		colour = szTemp;

		//assign all the shoes that have come in to thier global values
		this.szShoeName = shoe;
		this.szBoxCode = code;
		this.szUkSize = uk;
		this.szEuSize = eu;
		this.szColour = colour;
		this.iPrice = price;
		this.iStockLevel = stock;
		this.szOrder = order;


		bRC = this.addShoes() ;
		return ( bRC ) ;
	}

	public boolean addShoes()
	{
		boolean bRC = false ;
		int iRC ;
		try
		{
			sql = "INSERT INTO shoes( "
					+ "ShoeName"
					+ ", BoxCode"
					+ ", UkSize"
					+ ", EuSize"
					+ ", Colour "
					+ ", Price"
					+ ", AmountInStock"
					+ ", AvailibleToOrder"
					+ " ) "
					+ " VALUES( "
					+ "\"" + this.szShoeName  + "\""
					+ ", \"" + this.szBoxCode + "\""
					+ ", \"" + this.szUkSize + "\""
					+ ", \"" + this.szEuSize + "\""
					+ ", \"" + this.szColour + "\""
					+ ", \"" + this.iPrice + "\""
					+ ", \"" + this.iStockLevel + "\""
					+ ", \"" + this.szOrder + "\""
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

	//method to update the stocklevels by the amount of shoes added
	public boolean updateStockLevelUp(int iShoeId, int iNum)
	{
		boolean bRC = false ;
		int iRC ;

		//get the shoes and store in in a variable
		try
		{
			sql = "SELECT AmountInStock "
					+ " FROM shoes "
					+ " WHERE "
					+ " ShoeId ="
					+ iShoeId ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;
			bRC = resultSet.next() ;

			if ( bRC == true )
			{
				for (int i = 1 ; i <= iNumRows ; i++ )
				{

					this.iStockLevel = resultSet.getInt( "AmountInStock" ) ;

					bRC = true ;

				}
			}


			resultSet.close() ;

			//update the shoe that is wanted to be updated by how much is wanted
			sql = "UPDATE shoes " 
					+ " SET AmountInStock = "
					+ "\"" + (this.iStockLevel + iNum) + "\""
					+ " WHERE "
					+ " ShoeId ="
					+ iShoeId ;

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
		return ( bRC ) ;
	}


	//subroutine to update stock levels down by the amount of shoes that was sold
	public int updateStockLevelDown(int iShoeId, int iNum)
	{
		boolean bRC = false ;
		int iRC ;
		int iCounter = 0;
		try
		{
			sql = "SELECT AmountInStock "
					+ " FROM shoes "
					+ " WHERE "
					+ " ShoeId ="
					+ iShoeId ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;
			bRC = resultSet.next() ;

			if ( bRC == true )
			{
				for (int i = 1 ; i <= iNumRows ; i++ )
				{

					this.iStockLevel = resultSet.getInt( "AmountInStock" ) ;

					bRC = true ;

				}
			}
			resultSet.close() ;



			// if there is not that many shoes in stock, tell the user they cannot do this.
			if(this.iStockLevel - iNum < 0)
			{
				System.out.println("\nWe dont have " + iNum + " shoe(s) in stock!\nThere is only " + this.iStockLevel +" pair(s) in stock\n") ;

				//Set the counter to 1 so the menu knows to check if the shoe is availible to order;
				iCounter = 1;
			}
			else
			{
				sql = "UPDATE shoes " 
						+ " SET AmountInStock = "
						+ "\"" + (this.iStockLevel - iNum) + "\""
						+ " WHERE "
						+ " ShoeId ="
						+ iShoeId ;

				sqlStatement = dbSession.getConnection().createStatement() ;
				iRC = sqlStatement.executeUpdate( sql ) ;


				// iRC will hold how many records were updated or inserted.  zero is bad in this case.
				if ( iRC == 1 )
				{
					bRC = true ;
				}
			}

		}
		catch( SQLException se )
		{
			System.err.println( this.getClass().getName() + ":: SQL error:: " + se ) ;
			se.printStackTrace() ;
		}
		finally
		{
		}


		return ( iCounter ) ;
	}

	public String orderingShoe(String szShoeId)
	{
		//declare variables
		boolean bRC = false ;
		int iRC ;

		//check if the shoe is availible to order, if it is then contiue to the receipt process         
		try
		{
			sql = "SELECT AvailibleToOrder "
					+ " FROM shoes "
					+ " WHERE "
					+ " ShoeId ="
					+ szShoeId ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;
			bRC = resultSet.next() ;

			if ( bRC == true )
			{
				for (int i = 1 ; i <= iNumRows ; i++ )
				{
					this.szOrder = resultSet.getString( "AvailibleToOrder" ) ;
					bRC = true ;
				}
			}

			resultSet.close() ;
		}
		catch( SQLException se )
		{
			System.err.println( this.getClass().getName() + ":: SQL error:: " + se ) ;
			se.printStackTrace() ;
		}
		finally
		{
		}

		return (this.szOrder) ;
	}
	//subroutine to check if the stock levels are low
	public boolean checkStockLevels(int iShoeId)
	{
		boolean bRC = false ;
		int iRC ;
		try
		{
			sql = "SELECT AmountInStock "
					+ ", ShoeName "
					+ " FROM shoes "
					+ " WHERE "
					+ " ShoeId ="
					+ iShoeId ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;
			bRC = resultSet.next() ;

			if ( bRC == true )
			{
				for (int i = 1 ; i <= iNumRows ; i++ )
				{
					this.szShoeName = resultSet.getString("ShoeName");
					this.iStockLevel = resultSet.getInt( "AmountInStock" ) ;

					bRC = true ;
				}
			}
			resultSet.close() ;

			//if the stock levels are less than or equal to 1 then tell the user this so more can be ordered
			if(this.iStockLevel <= 1)
			{
				System.out.println("\nThe Stocklevels for the \'" + szShoeName + "\' is low, more will be delivered.\n");
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
		return ( bRC ) ;
	}

	//method to delete a shoes from the database
	// must already have found the record before trying to delete it.
	public boolean deleteShoes(int iShoeId)
	{
		boolean bRC = false ;
		int iRC ;
		try
		{
			sql = "DELETE FROM shoes " 
					+ " WHERE"
					+ " ShoeId  ="
					+ iShoeId ;

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

	//method to find a record by the shoe name only
	public boolean getByShoeName( String szShoeName )
	{

		boolean bRC = false ;
		int i;
		int iCount;
		int iCountedRows = 0;


		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];

		try
		{            
			sql = "SELECT ShoeId "
					+ " FROM shoes "
					+ " WHERE ShoeName like \'%" + szShoeName + "%\';" ; 


			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;


			//set j to be 1 as that is the first database not 0
			this.j = 1 ;

			//while the uksize is the same as the next one enter the loop 
			while (resultSet.next() == true)
			{   

				//Array stores the current shoeId collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "ShoeId" ) ;

				//increment j by 1 to update the postion in the array
				this.j++;

			}// end of while loop


			//For loop to loop the shoeid's collected in the array into a method that collects and displays the record 
			for (i = 0 ; i < (j - 1) ; i ++)
			{
				collectRecord(iDuplicateArray[i + 1]);
			}//end of the for loop


			resultSet.close() ;
		}//end of the try statement
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

	//method to collect the shoe by the boxcode
	public boolean getByBoxCode( String szBoxCode)
	{
		boolean bRC = false ;
		int i ;
		try
		{
			sql = "SELECT ShoeId "
					+ ", BoxCode"
					+ " FROM shoes ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;
			bRC = resultSet.next() ;


			if ( bRC == true )
			{
				for ( i = 1 ; i <= iNumRows ; i++ )
				{
					if ( resultSet.getString( "BoxCode" ).compareToIgnoreCase( szBoxCode ) == 0 ) 
					{
						this.iShoeId = resultSet.getInt( "ShoeId" ) ;
						this.szBoxCode = resultSet.getString( "BoxCode" ) ;
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

	// method to find a shoe by the uk size
	public boolean getByUkSize(String szUkSize)
	{
		boolean bRC = false ;
		int i ;
		int iSum;
		int iCountedRows = 0;


		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];

		try
		{            
			//Select the shoeId from shoes where the Uksize is what is inputted
			sql = "SELECT ShoeId FROM shoes "
					+ " WHERE uksize "
					+ " = " + szUkSize + " ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;


			//set j to be 1 as that is the first database not 0
			this.j = 1 ;

			//while the uksize is the same as the next one enter the loop 
			while (resultSet.next() == true )
			{   

				//Array stores the current shoeId collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "ShoeId" ) ;

				//increment j by 1 to update the postion in the array
				this.j++;

			}// end of while loop

			//For loop to loop the shoeid's collected in the array into a method that collects and displays the record 
			for (i = 0 ; i < (j -1)  ; i ++)
			{
				collectRecord(iDuplicateArray[i + 1]);
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

	//method to get a shoe by EU size only
	public boolean getByEuSize( String szEuSize)
	{
		boolean bRC = false ;
		int i ;
		int iSum;
		int iCountedRows = 0;


		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];
		try
		{            
			//Select the shoeId from shoes where the EUsize is what is inputted
			sql = "SELECT ShoeId FROM shoes "
					+ " WHERE eusize "
					+ " = " + szEuSize + " ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			this.j = 1 ;

			//while the uksize is the same as the next one enter the loop 
			while (resultSet.next() == true )
			{   

				//Array stores the current shoeId collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "ShoeId" ) ;

				//increment j by 1 to update the postion in the array
				this.j++;

			}// end of while loop

			//For loop to loop the shoeid's collected in the array into a method that collects and displays the record 
			for (i = 0 ; i < j - 1 ; i ++)
			{
				collectRecord(iDuplicateArray[i + 1]);
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

	//method to get the record by the colour
	public boolean getByColour( String szColour)
	{
		boolean bRC = false ;
		int i ;
		int iSum;
		int iCountedRows = 0;


		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];
		try
		{            
			//Select the shoeId from shoes where the Uksize is what is inputted
			sql = "SELECT ShoeId FROM shoes "
					+ " WHERE Colour "
					+ " =  \'" + szColour + "\' ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;


			//set j to be 1 as that is the first database not 0
			this.j = 1 ;

			//while the uksize is the same as the next one enter the loop 
			while (resultSet.next() == true )
			{   

				//Array stores the current shoeId collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "ShoeId" ) ;

				//increment j by 1 to update the postion in the array
				this.j++;

			}// end of while loop


			//For loop to loop the shoeid's collected in the array into a method that collects and displays the record 
			for (i = 0 ; i < j - 1 ; i ++)
			{
				collectRecord(iDuplicateArray[i + 1]);
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

	public int getShoeId()
	{
		return ( this.iShoeId ) ;
	}

	//method that collects the records
	public void collectRecord( int input )
	{
		try
		{
			sql = "SELECT ShoeId"
					+ ", ShoeName"
					+ ", BoxCode"
					+ ", UkSize"
					+ ", EuSize"
					+ ", Colour"
					+ ", Price"
					+ ", AmountInStock"
					+ ", AvailibleToOrder"
					+ " FROM shoes"
					+ " WHERE ShoeId = " + input ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;
			resultSet.next() ;

			this.iShoeId = resultSet.getInt( "ShoeId" ) ;
			this.szShoeName = resultSet.getString( "ShoeName" ) ;
			this.szBoxCode = resultSet.getString( "BoxCode" ) ;
			this.szUkSize = resultSet.getString( "UkSize" ) ;
			this.szEuSize = resultSet.getString( "EuSize" ) ;
			this.szColour = resultSet.getString( "Colour" ) ;
			this.iPrice = resultSet.getInt( "Price" ) ;
			this.iStockLevel = resultSet.getInt( "AmountInStock" ) ;
			this.szOrder = resultSet.getString( "AvailibleToOrder" ) ;


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

		display();
		return ;
	}


	// ----------------------------------------------------------------------
	// to use the surname to find the other details,
	// assuming they are unique at present..!
	public void collectRecordByShoeId(int iShoeId)
	{
		clearId() ;

		if ( this.iShoeId == 0 )
		{ 
			collectRecord( iShoeId ) ;
		}

		return ;
	}

	public void collectRecordByShoeName(String szShoeName)
	{

		if ( szShoeName.length() > 0 )
		{
			clearId() ;
			if ( this.iShoeId == 0 )
			{
				clearId() ;

				if ( getByShoeName( szShoeName ) == true )
				{

					collectRecord( this.iShoeId ) ;
				}
			}
		}
		return ;
	}

	public void collectRecordByBoxCode(String szBoxCode)
	{
		if ( szBoxCode.length() > 0 )
		{
			clearId() ;
			if ( this.iShoeId == 0 )
			{
				clearId() ;
				if ( getByBoxCode( szBoxCode ) == true )
				{
					collectRecord( this.iShoeId ) ;
				}
			}
		}
		return ;
	}

	public void collectRecordByUkSize(String szUkSize)
	{
		if ( szUkSize.length() > 0 )
		{
			clearId() ;
			if ( this.iShoeId == 0 )
			{
				clearId() ;
				if ( getByUkSize( szUkSize ) == true )
				{
					collectRecord( this.iShoeId ) ;
				}
			}
		}
		return ;
	}


	public void collectRecordByEuSize(String szEuSize)
	{
		if ( szEuSize.length() > 0 )
		{
			clearId() ;
			if ( this.iShoeId == 0 )
			{
				clearId() ;
				if ( getByEuSize( szEuSize ) == true )
				{
					collectRecord( this.iShoeId ) ;
				}
			}
		}
		return ;
	}

	public void collectRecordByColour(String szColour)
	{
		if ( szColour.length() > 0 )
		{
			clearId() ;
			if ( this.iShoeId == 0 )
			{
				clearId() ;
				if ( getByColour( szColour ) == true )
				{
					collectRecord( this.iShoeId ) ;
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
				sql = "SELECT count(*) AS rowCount FROM shoes;" ;

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



	public void viewAllShoes(int i)
	{
		try
		{

			countRows();

			sql = "SELECT ShoeId"
					+ ", ShoeName"
					+ ", BoxCode"
					+ ", UkSize"
					+ ", EuSize"
					+ ", Colour"
					+ ", Price"
					+ ", AmountInStock"
					+ ", AvailibleToOrder"
					+ " FROM shoes"
					+ " WHERE ShoeId = " + i ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			resultSet.next() ;

			iShoeId = resultSet.getInt( "ShoeId" ) ;
			szShoeName = resultSet.getString( "ShoeName" ) ;
			szBoxCode = resultSet.getString( "BoxCode" ) ;
			szUkSize = resultSet.getString( "UkSize" ) ;
			szEuSize = resultSet.getString( "EuSize" ) ;
			szColour = resultSet.getString( "Colour" ) ;
			iPrice = resultSet.getInt( "Price" ) ;
			iStockLevel = resultSet.getInt( "AmountInStock" ) ;
			szOrder = resultSet.getString( "AvailibleToOrder" ) ;

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

		display();

		return ;
	}




	public void display()
	{
		System.out.println("-----------------------------------------------------") ;
		sz = "\n" ;
		sz = sz + "\n\tShoeId := " + iShoeId ;
		sz = sz + "\n\tShoeName := " + szShoeName ;
		sz = sz + "\n\tBoxCode := " + szBoxCode ;
		sz = sz + "\n\tUkSize := " + szUkSize ;
		sz = sz + "\n\tEuSize := " + szEuSize ;
		sz = sz + "\n\tColour := " + szColour ;
		sz = sz + "\n\tPrice := £" + iPrice + ".00";
		sz = sz + "\n\tAmountInStock := " + iStockLevel ;
		sz = sz + "\n\tAvailibleToOrder := " + szOrder ;

		System.out.println( this.getClass().getName() + sz ) ;


		return ;
	}



	// ----------------------------------------------------------------------
	// test rig
	// ----------------------------------------------------------------------
	public static void main( String [] args )
	{
		shoes trainer = new shoes() ;

		//Testing to see if i can file the database details for ...
		trainer.clearId() ;
		trainer.getByShoeName("ffbfbrr");
		System.out.println("\n") ;

		return ;
	}
}
