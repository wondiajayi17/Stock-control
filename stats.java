import java.sql.* ;
import java.util.Calendar;
import java.util.Date;	

public class stats
{
	// ----------------------------------------------------------------------
	// Class variables
	// ----------------------------------------------------------------------
	// fields in db-table.
	private Calendar Today = Calendar.getInstance();	
	private int iPresentYear =  Today.get(Calendar.YEAR);
	private int iPresentMonth =  Today.get(Calendar.MONTH) + 1;
	private int iPresentDay =  Today.get(Calendar.DAY_OF_MONTH);
	private int iStatsId;
	private String szShoeName;
	private int iPairs;
	private int iPrice;


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
	public stats()
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
		iStatsId = 0 ;
		szShoeName = null;
		iPairs = 0 ;
		iPrice = 0;

		return ;
	}

	private void clearId()
	{
		this.iStatsId = 0 ;
	}

    //method to add the stats to the database
	public boolean addStats(String szShoe, int iPair, int iInvoice)
	{
		//declare variables
		boolean bRC = false ;
		clearAttributes() ;


		//assign all the shoes that have come in to their global values
		this.szShoeName = szShoe;
		this.iPairs = iPair;
		this.iPrice = iInvoice;
		this.iPresentDay = iPresentDay;
		this.iPresentMonth = iPresentMonth;
		this.iPresentYear = iPresentYear;

		bRC = this.addStats() ;
		return ( bRC ) ;
	}

    //method to add the stats to the database
	public boolean addStats()
	{
		boolean bRC = false ;
		int iRC ;
		try
		{
			sql = "INSERT INTO stats( "
					+ "ShoeName"
					+ ", Pairs"
					+ ", Price"
					+ ", Day"
					+ ", Month"
					+ ", Year"
					+ " ) "
					+ " VALUES( "
					+ "\"" + this.szShoeName  + "\""
					+ ", \"" + this.iPairs + "\""
					+ ", \"" + this.iPrice + "\""
					+ ", \"" + this.iPresentDay + "\""
					+ ", \"" + this.iPresentMonth + "\""
					+ ", \"" + this.iPresentYear + "\""
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

	// ----------------------------------------------------------------------
	// Getters
	// ----------------------------------------------------------------------
    
    //method to check how many shoes were sold today
	public void shoesSoldToday()
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iCountedRows = 0;
		int iAmountOfShoesSold = 0;

		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];

		try
		{            
			//Select the pairs from stats where the date is what is inputted
			sql = "SELECT Pairs FROM stats "
					+ " WHERE "
					+ " Day =  " + iPresentDay
					+ " AND Month =  " + iPresentMonth
					+ " AND Year =  " + iPresentYear + " ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			
			while (resultSet.next() == true)
			{   

				//Array stores the current pairs collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Pairs" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop

			//For loop to loop the pairs and add them to get a total number
			for (i = 0 ; i < (j) ; i ++)
			{

				iAmountOfShoesSold = iAmountOfShoesSold +  iDuplicateArray [ i ] ;
			}//end of the for loop

			//print out how many shoes we they sold today
			System.out.print("\nToday you have sold " + iAmountOfShoesSold + " pairs of shoes\n");


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
    
	// ----------------------------------------------------------------------
    
    //method to check how much money was made today
	public void revenueMadeToday()
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iCountedRows = 0;
		int iAmountOfMoneyMade = 0;



		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];
		int [] iDuplicatePairsArray = new int [iCountedRows + 1];

		try
		{            
			//Select the pairs and price from stats where the date is what is inputted
			sql = "SELECT Pairs,Price FROM stats "
					+ " WHERE "
					+ " Day =  " + iPresentDay
					+ " AND Month =  " + iPresentMonth
					+ " AND Year =  " + iPresentYear + " ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			
			while (resultSet.next() == true)
			{   

				//Array stores the current price and pairs collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Price" ) ;
				iDuplicatePairsArray [ j ] = resultSet.getInt( "Pairs" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop


			//For loop to loop the pairs and price and multiply them to get the total price
			for (i = 0 ; i < (j) ; i ++)
			{

				iAmountOfMoneyMade = iAmountOfMoneyMade +  (iDuplicatePairsArray [ i ] * iDuplicateArray [ i ]) ;
			}//end of the for loop

			//print out how much money made 
			System.out.print("\nToday you have made £" + iAmountOfMoneyMade + ".00\n");


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
    
	// ----------------------------------------------------------------------
	public void shoesSoldThisMonth()
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iAmountOfShoesSold = 0;
		int iCountedRows = 0;

		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];

		try
		{            
			//Select the pairs from stats where the date is what is inputted
			sql = "SELECT Pairs FROM stats "
					+ " WHERE "
					+ " Month =  " + iPresentMonth  
					+ " AND Year = " + iPresentYear + " ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			
			while (resultSet.next() == true)
			{   

				//Array stores the current pairs collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Pairs" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop

            
            //For loop to loop the pairs and add them to get a total number
			for (i = 0 ; i < (j) ; i ++)
			{

				iAmountOfShoesSold = iAmountOfShoesSold +  iDuplicateArray [ i ] ;
			}//end of the for loop


			//print out how many shoes we they sold this month
			System.out.print("\nThis month you have currently sold " + iAmountOfShoesSold + " pairs of shoes\n");

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
    
	// ----------------------------------------------------------------------
	public void revenueMadeThisMonth()
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iAmountOfMoneyMade = 0;
		int iCountedRows = 0;

		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];
		int [] iDuplicatePairsArray = new int [iCountedRows + 1];

		try
		{            
			//Select the pairs and price from stats where the date is what is inputted
			sql = "SELECT Pairs, Price FROM stats "
					+ " WHERE "
					+ " Month =  " + iPresentMonth 
					+ " AND Year = " + iPresentYear + " ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			while (resultSet.next() == true)
			{   

				//Array stores the current price and pairs collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Price" ) ;
				iDuplicatePairsArray [ j ] = resultSet.getInt( "Pairs" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop


			//For loop to loop the pairs and price of the shoes bought and multiply them together to get the total price 
			for (i = 0 ; i < (j) ; i ++)
			{

				iAmountOfMoneyMade = iAmountOfMoneyMade +  (iDuplicatePairsArray [ i ] * iDuplicateArray [ i ]) ;
			}//end of the for loop


			//print out how many shoes we they sold this month
			System.out.print("\nThis month you have currently made £" + iAmountOfMoneyMade + ".00\n");


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
    
	// ----------------------------------------------------------------------
	public void shoesSoldThisYear()
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iAmountOfShoesSold = 0;
		int iCountedRows = 0;

		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];

		try
		{   			
            //Select the pairs from stats where the date is what is inputted
			sql = "SELECT Pairs FROM stats "
					+ " WHERE "
					+ " Year = " + iPresentYear + " ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			
			while (resultSet.next() == true)
			{   

				//Array stores the current pairs collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Pairs" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop


			//For loop to loop the pairs and add them to get a total number
			for (i = 0 ; i < (j) ; i ++)
			{

				iAmountOfShoesSold = iAmountOfShoesSold +  iDuplicateArray [ i ] ;
			}//end of the for loop


			//print out how many shoes we they sold this year
			System.out.print("\nThis year you have currently sold " + iAmountOfShoesSold + " pairs of shoes\n");



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
    
	// ----------------------------------------------------------------------
	public void revenueMadeThisYear()
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iAmountOfMoneyMade = 0;
		int iCountedRows = 0;

		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];
		int [] iDuplicatePairsArray = new int [iCountedRows + 1];

		try
		{            
			//Select the price and pairs from stats where the date is what is inputted
			sql = "SELECT Pairs, Price FROM stats "
					+ " WHERE "
					+ " Year = " + iPresentYear + " ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			
			while (resultSet.next() == true)
			{   

				//Array stores the current price and pairs collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Price" ) ;
				iDuplicatePairsArray [ j ] = resultSet.getInt( "Pairs" ) ;


				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop


			//For loop to loop the pairs and price of the shoes bought and multiply them together to get the total price 
			for (i = 0 ; i < (j) ; i ++)
			{

				iAmountOfMoneyMade = iAmountOfMoneyMade +  (iDuplicatePairsArray [ i ] * iDuplicateArray [ i ]) ;
			}//end of the for loop


			//print out how many shoes we they sold this year
			System.out.print("\nThis year you have currently made £" + iAmountOfMoneyMade + ".00\n");


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
    
	// ----------------------------------------------------------------------
	public int shoesSoldOnThisDay(int iDay, int iMonth, int iYear)
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iCountedRows = 0;
		int iAmountOfShoesSold = 0;



		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];

		try
		{            
            //Select the pairs from stats where the date is what is inputted
			sql = "SELECT Pairs FROM stats "
					+ " WHERE "
					+ " Day =  " + iDay
					+ " AND Month =  " + iMonth 
					+ " AND Year =  " + iYear + " ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			
			while (resultSet.next() == true)
			{   

				//Array stores the current pairs collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Pairs" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop


			//For loop to loop the pairs and add them to get a total number
			for (i = 0 ; i < (j) ; i ++)
			{
				iAmountOfShoesSold = iAmountOfShoesSold +  iDuplicateArray [ i ] ;
			}//end of the for loop


			//switch case to filter the option           
			switch (iMonth) 
			{
			case 1:
				//print out how many shoes we they sold today
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes on January " + iDay + " " + iYear);
				break;
			case 2:
				//print out how many shoes we they sold today
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes on Februrary " + iDay + " " + iYear);
				break;

			case 3:
				//print out how many shoes we they sold today
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes on March " + iDay + " " + iYear);
				break;

			case 4:
				//print out how many shoes we they sold today
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes on April " + iDay + " " + iYear);
				break;

			case 5:
				//print out how many shoes we they sold today
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes on May " + iDay + " " + iYear);

			case 6:
				//print out how many shoes we they sold today
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes on June " + iDay + " " + iYear);
				break;

			case 7:
				//print out how many shoes we they sold today
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes on July " + iDay + " " + iYear);
				break;

			case 8:
				//print out how many shoes we they sold today
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes on August " + iDay + " " + iYear);
				break;

			case 9:
				//print out how many shoes we they sold today
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes on September " + iDay + " " + iYear);
				break;

			case 10:
				//print out how many shoes we they sold today
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes on October " + iDay + " " + iYear);
				break;

			case 11:
				//print out how many shoes we they sold today
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes on November " + iDay + " " + iYear);
				break;

			case 12:
				//print out how many shoes we they sold today
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes on December " + iDay + " " + iYear);
				break;
			}//end the switchcase

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
		return(iDay);
	}
    
	// ----------------------------------------------------------------------
	public int revenueMadeOnThisDay(int iDay, int iMonth, int iYear)
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iCountedRows = 0;
		int iAmountOfMoneyMade= 0;



		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];
		int [] iDuplicatePairsArray = new int [iCountedRows + 1];

		try
		{            
			//Select the pairs and price from stats where the date is what is inputted
			sql = "SELECT Pairs, Price FROM stats "
					+ " WHERE "
					+ " Day =  " + iDay
					+ " AND Month =  " + iMonth 
					+ " AND Year =  " + iYear + " ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			
			while (resultSet.next() == true)
			{   

				//Array stores the current price and pairs collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Price" ) ;
				iDuplicatePairsArray [ j ] = resultSet.getInt( "Pairs" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop



			//For loop to loop the pairs and price of the shoes bought and multiply them together to get the total price 
			for (i = 0 ; i < (j) ; i ++)
			{

				iAmountOfMoneyMade = iAmountOfMoneyMade +  (iDuplicatePairsArray [ i ] * iDuplicateArray [ i ]) ;
			}//end of the for loop

			//switch case to filter the option           
			switch (iMonth) 
			{
			case 1:
				//print out how many shoes we they sold on this day
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 on January " + iDay + " " + iYear);
				break;
			case 2:
				//print out how many shoes we they sold on this day
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 on Februrary " + iDay + " " + iYear);
				break;

			case 3:
				//print out how many shoes we they sold on this day
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 on March " + iDay + " " + iYear);
				break;

			case 4:
				//print out how many shoes we they sold on this day
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 on April " + iDay + " " + iYear);
				break;

			case 5:
				//print out how many shoes we they sold on this day
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 on May " + iDay + " " + iYear);

			case 6:
				//print out how many shoes we they sold on this day
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 on June " + iDay + " " + iYear);
				break;

			case 7:
				//print out how many shoes we they sold on this day
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 on July " + iDay + " " + iYear);
				break;

			case 8:
				//print out how many shoes we they sold on this day
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 on August " + iDay + " " + iYear);
				break;

			case 9:
				//print out how many shoes we they sold on this day
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 on September " + iDay + " " + iYear);
				break;

			case 10:
				//print out how many shoes we they sold on this day
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 on October " + iDay + " " + iYear);
				break;

			case 11:
				//print out how many shoes we they sold on this day
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 on November " + iDay + " " + iYear);
				break;

			case 12:
				//print out how many shoes we they sold on this day
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 on December " + iDay + " " + iYear);
				break;
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

		return(iAmountOfMoneyMade);
	}
    
	// ----------------------------------------------------------------------
	public int shoesSoldInThisMonth(int iMonth, int iYear)
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iCountedRows = 0;
		int iAmountOfShoesSold = 0;

		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];

		try
		{            
			//Select the pairs from stats where the date is what is inputted
			sql = "SELECT Pairs FROM stats "
					+ " WHERE "
					+ " Month =  " + iMonth 
					+ " AND Year =  " + iYear + " ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			 
			while (resultSet.next() == true)
			{   

				//Array stores the current pairs collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Pairs" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop


			//For loop to loop the pairs and add them to get a total number
			for (i = 0 ; i < (j) ; i ++)
			{
				iAmountOfShoesSold = iAmountOfShoesSold +  iDuplicateArray [ i ] ;
			}//end of the for loop

			//switch case to filter the option           
			switch (iMonth) 
			{
			case 1:
				//print out how many shoes we they sold that month
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes in January " + iYear);
				break;
			case 2:
				//print out how many shoes we they sold that month
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes in Februrary " + iYear);
				break;

			case 3:
				//print out how many shoes we they sold that month
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes in March " + iYear);
				break;

			case 4:
				//print out how many shoes we they sold that month
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes in April " + iYear);
				break;

			case 5:
				//print out how many shoes we they sold that month
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes in May " + iYear);

			case 6:
				//print out how many shoes we they sold that month
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes in June " + iYear);
				break;

			case 7:
				//print out how many shoes we they sold that month
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes in July " + iYear);
				break;

			case 8:
				//print out how many shoes we they sold that month
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes in August " + iYear);
				break;

			case 9:
				//print out how many shoes we they sold that month
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes in September " + iYear);
				break;

			case 10:
				//print out how many shoes we they sold that month
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes in October " + iYear);
				break;

			case 11:
				//print out how many shoes we they sold that month
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes in November " + iYear);
				break;

			case 12:
				//print out how many shoes we they sold that month
				System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes in December " + iYear);
				break;
			}//end the switchcase

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
		return(iMonth);
	}
    
	// ----------------------------------------------------------------------
	public int revenueMadeInThisMonth( int iMonth, int iYear)
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iCountedRows = 0;
		int iAmountOfMoneyMade= 0;



		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];
		int [] iDuplicatePairsArray = new int [iCountedRows + 1];

		try
		{            
			//Select the pairs from stats where the date is what is inputted
			sql = "SELECT Pairs, Price FROM stats "
					+ " WHERE "
					+ " Month =  " + iMonth 
					+ " AND Year =  " + iYear + " ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			
			while (resultSet.next() == true)
			{   

				//Array stores the current price and pairs collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Price" ) ;
				iDuplicatePairsArray [ j ] = resultSet.getInt( "Pairs" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop



			//For loop to loop the pairs and price of the shoes bought and multiply them together to get the total price 
			for (i = 0 ; i < (j) ; i ++)
			{

				iAmountOfMoneyMade = iAmountOfMoneyMade +  (iDuplicatePairsArray [ i ] * iDuplicateArray [ i ]) ;
			}//end of the for loop

			//switch case to filter the option           
			switch (iMonth) 
			{
			case 1:
				//print out how many shoes we they sold today
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 in January " + iYear);
				break;
			case 2:
				//print out how many shoes we they sold today
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 in Februrary " + iYear);
				break;

			case 3:
				//print out how many shoes we they sold today
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 in March " + iYear);
				break;

			case 4:
				//print out how many shoes we they sold today
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 in April " + iYear);
				break;

			case 5:
				//print out how many shoes we they sold today
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 in May " + iYear);

			case 6:
				//print out how many shoes we they sold today
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 in June " + iYear);
				break;

			case 7:
				//print out how many shoes we they sold today
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 in July " + iYear);
				break;

			case 8:
				//print out how many shoes we they sold today
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 in August " + iYear);
				break;

			case 9:
				//print out how many shoes we they sold today
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 in September " + iYear);
				break;

			case 10:
				//print out how many shoes we they sold today
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 in October " + iYear);
				break;

			case 11:
				//print out how many shoes we they sold today
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 in November " + iYear);
				break;

			case 12:
				//print out how many shoes we they sold today
				System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 in December " + iYear);
				break;
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

		return(iAmountOfMoneyMade);
	}

    // ----------------------------------------------------------------------
	public int shoesSoldInThisYear(int iYear)
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iCountedRows = 0;
		int iAmountOfShoesSold = 0;

		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];

		try
		{            
			//Select the pairs from stats where the date is what is inputted
			sql = "SELECT Pairs FROM stats "
					+ " WHERE "
					+ " Year =  " + iYear + " ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			/
			while (resultSet.next() == true)
			{   

				//Array stores the current pairs collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Pairs" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop


			//For loop to loop the pairs and add them to get a total number
			for (i = 0 ; i < (j) ; i ++)
			{
				iAmountOfShoesSold = iAmountOfShoesSold +  iDuplicateArray [ i ] ;
			}//end of the for loop

			//print how much shoes thew user sold in the year they choose
			System.out.println("\nYou sold " + iAmountOfShoesSold + " pairs of shoes in " + iYear);

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
		return(iAmountOfShoesSold);
	}
    
    // ----------------------------------------------------------------------
	public int revenueMadeInThisYear(int iYear)
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iCountedRows = 0;
		int iAmountOfMoneyMade= 0;



		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];
		int [] iDuplicatePairsArray = new int [iCountedRows + 1];

		try
		{            
			//Select the pairs from stats where the date is what is inputted
			sql = "SELECT Pairs, Price FROM stats "
					+ " WHERE "
					+ " Year =  " + iYear + " ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			
			while (resultSet.next() == true)
			{   

				//Array stores the current price and pairs collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Price" ) ;
				iDuplicatePairsArray [ j ] = resultSet.getInt( "Pairs" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop


			//For loop to loop the pairs and price of the shoes bought and multiply them together to get the total price 
			for (i = 0 ; i < (j) ; i ++)
			{

				iAmountOfMoneyMade = iAmountOfMoneyMade +  (iDuplicatePairsArray [ i ] * iDuplicateArray [ i ]) ;
			}//end of the for loop

			System.out.println("\nYou made £" + iAmountOfMoneyMade + ".00 in " + iYear);


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

		return(iAmountOfMoneyMade);
	}
    
    // ----------------------------------------------------------------------
	public int bestSellingShoeThisMonth()
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iCountedRows = 0;
		int iAmountOfShoesSold = 0;

		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];
		String [] szDuplicateArrayForShoes = new String [iCountedRows + 1];

		try
		{            
			//Select the shoename and pairs from stats where the date is what is inputted
			sql = "SELECT ShoeName, Pairs FROM stats "
					+ " WHERE Month =  " + iPresentMonth
					+ " AND Year =  " + iPresentYear
					+ " ORDER by Pairs desc ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			
			while (resultSet.next() == true)
			{   

				//Array stores the current pairs and shoename  collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Pairs" ) ;
				szDuplicateArrayForShoes [ j ]  = resultSet.getString( "ShoeName" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop


			//switch case to filter the option           
			switch (iPresentMonth) 
			{
			case 1:
				//print the best selling shoe this month
				System.out.println("\nYour best selling shoe in January " + iPresentYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;
			case 2:
				//print the best selling shoe this month
				System.out.println("\nYour best selling shoe in February " + iPresentYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 3:
				//print the best selling shoe this month
				System.out.println("\nYour best selling shoe in March " + iPresentYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 4:
				//print the best selling shoe this month
				System.out.println("\nYour best selling shoe in April " + iPresentYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 5:
				//print the best selling shoe this month
				System.out.println("\nYour best selling shoe in May " + iPresentYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;
			case 6:
				//print the best selling shoe this month
				System.out.println("\nYour best selling shoe in June " + iPresentYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 7:
				//print the best selling shoe this month
				System.out.println("\nYour best selling shoe in July " + iPresentYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 8:
				//print the best selling shoe this month
				System.out.println("\nYour best selling shoe in August" + iPresentYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 9:
				//print the best selling shoe this month
				System.out.println("\nYour best selling shoe in September " + iPresentYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 10:
				//print the best selling shoe this month
				System.out.println("\nYour best selling shoe in October " + iPresentYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 11:
				//print the best selling shoe this month
				System.out.println("\nYour best selling shoe in November " + iPresentYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 12:
				//print the best selling shoe this month
				System.out.println("\nYour best selling shoe in December " + iPresentYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;
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
		return(iAmountOfShoesSold);
	}

    // ----------------------------------------------------------------------
	public int bestSellingShoeInThisMonth(int iMonth , int iYear)
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iCountedRows = 0;
		int iAmountOfShoesSold = 0;

		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];
		String [] szDuplicateArrayForShoes = new String [iCountedRows + 1];

		try
		{            
			//Select the pairs and shoename from stats where the date is what is inputted
			sql = "SELECT ShoeName, Pairs FROM stats "
					+ " WHERE Month =  " + iMonth
					+ " AND Year =  " + iYear
					+ " ORDER by Pairs desc ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			 
			while (resultSet.next() == true)
			{   

				//Array stores the current shoename and pairs collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Pairs" ) ;
				szDuplicateArrayForShoes [ j ]  = resultSet.getString( "ShoeName" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop



			//switch case to filter the option           
			switch (iMonth) 
			{
			case 1:
				//print the best selling shoe the month of choice
				System.out.println("\nYour best selling shoe in January " + iYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;
			case 2:
				//print the best selling shoe the month of choice
				System.out.println("\nYour best selling shoe in February " + iYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 3:
				//print the best selling shoe the month of choice
				System.out.println("\nYour best selling shoe in March " + iYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 4:
				//print the best selling shoe the month of choice
				System.out.println("\nYour best selling shoe in April " + iYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 5:
				//print the best selling shoe the month of choice
				System.out.println("\nYour best selling shoe in May " + iYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;
			case 6:
				//print the best selling shoe the month of choice
				System.out.println("\nYour best selling shoe in June " + iYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 7:
				//print the best selling shoe the month of choice
				System.out.println("\nYour best selling shoe in July " + iYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 8:
				//print the best selling shoe the month of choice
				System.out.println("\nYour best selling shoe in August" + iYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 9:
				//print the best selling shoe the month of choice
				System.out.println("\nYour best selling shoe in September " + iYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 10:
				//print the best selling shoe the month of choice
				System.out.println("\nYour best selling shoe in October " + iYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 11:
				//print the best selling shoe the month of choice
				System.out.println("\nYour best selling shoe in November " + iYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;

			case 12:
				//print the best selling shoe the month of choice
				System.out.println("\nYour best selling shoe in December " + iYear + " was the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this Month.");
				break;
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
		return(iAmountOfShoesSold);
	}
	// ----------------------------------------------------------------------
	public int bestSellingShoeThisYear()
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iCountedRows = 0;
		int iAmountOfShoesSold = 0;

		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];
		String [] szDuplicateArrayForShoes = new String [iCountedRows + 1];

		try
		{            
			//Select the pairs and shoename from stats where the date is what is inputted
			sql = "SELECT ShoeName, Pairs FROM stats "
					+ " WHERE Year =  " + iPresentYear  
					+ " ORDER by Pairs desc ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			//while the uksize is the same as the next one enter the loop 
			while (resultSet.next() == true)
			{   

				//Array stores the current pairs shoename collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Pairs" ) ;
				szDuplicateArrayForShoes [ j ]  = resultSet.getString( "ShoeName" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop


			//print how much shoes the user sold this year
			System.out.println("\nYour best selling shoe this year is the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this year.");


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
		return(iAmountOfShoesSold);
	}
    
	// ----------------------------------------------------------------------
	public int bestSellingShoeInThisYear(int iYear)
	{
		boolean bRC = false ;
		int i ;
		int j ;
		int iCountedRows = 0;
		int iAmountOfShoesSold = 0;

		// count the amount of rows there are and store them
		iCountedRows = countRows(); 

		//create array
		int [] iDuplicateArray = new int [iCountedRows + 1];
		String [] szDuplicateArrayForShoes = new String [iCountedRows + 1];

		try
		{            
			//Select the pairs and shoename from stats where the date is what is inputted
			sql = "SELECT ShoeName, Pairs FROM stats "
					+ " WHERE Year =  " + iYear  
					+ " ORDER by Pairs desc ;" ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			//set j to be 1 as that is the first database not 0
			j = 1 ;

			
			while (resultSet.next() == true)
			{   

				//Array stores the current pairs and shoename collected from the database
				iDuplicateArray [ j ] = resultSet.getInt( "Pairs" ) ;
				szDuplicateArrayForShoes [ j ]  = resultSet.getString( "ShoeName" ) ;

				//increment j by 1 to update the postion in the array
				j++;

			}// end of while loop



			//print how much shoes the user sold in the year they choose
			System.out.println("\nYour best selling shoe in " + iYear + " is the " + szDuplicateArrayForShoes[1] + "'s.\nYou have sold " + iDuplicateArray [1] +" pairs this year.");

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
		return(iAmountOfShoesSold);
	}

	// ----------------------------------------------------------------------
	

	public void collectRecord( int input )
	{
		try
		{
			sql = "SELECT StatsId"
					+ ", ShoeName"
					+ ", Pairs"
					+ ", Price"
					+ ", Day"
					+ ", Month"
					+ ", Year"
					+ " FROM stats"
					+ " WHERE StatsId = " + input ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;
			resultSet.next() ;

			this.iStatsId = resultSet.getInt( "StatsId" ) ;
			this.szShoeName = resultSet.getString( "ShoeName" ) ;
			this.iPairs = resultSet.getInt( "Pairs" ) ;
			this.iPrice = resultSet.getInt( "Price" ) ;
			this.iPresentDay = resultSet.getInt( "Day" ) ;
			this.iPresentMonth = resultSet.getInt( "Month" ) ;
			this.iPresentYear = resultSet.getInt( "Year" ) ;


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

	// ----------------------------------------------------------------------
	public void viewAllStats(int i)
	{
		try
		{

			countRows();

			sql = "SELECT StatsId"
					+ ", ShoeName"
					+ ", Pairs"
					+ ", Price"
					+ ", Day"
					+ ", Month"
					+ ", Year"
					+ " FROM stats"
					+ " WHERE StatsId = " + i ;

			sqlStatement = dbSession.getConnection().createStatement() ;
			resultSet = sqlStatement.executeQuery( sql ) ;

			resultSet.next() ;

			iStatsId = resultSet.getInt( "StatsId" ) ;
			szShoeName = resultSet.getString( "ShoeName" ) ;
			iPairs = resultSet.getInt( "Pairs" ) ;
			iPrice = resultSet.getInt( "Price" ) ;
			iPresentDay = resultSet.getInt( "Day" ) ;
			iPresentMonth = resultSet.getInt( "Month" ) ;
			iPresentYear= resultSet.getInt( "Year" ) ;


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
	public void display()
	{
		System.out.println("-----------------------------------------------------") ;
		sz = "\n" ;
		sz = sz + "\n\tStatsId := " + iStatsId ;
		sz = sz + "\n\tShoeName := " + szShoeName ;
		sz = sz + "\n\tPairs := " + iPairs ;
		sz = sz + "\n\tPrice := £" + iPrice + ".00";
		sz = sz + "\n\tDay := " + iPresentDay ;
		sz = sz + "\n\tMonth := " + iPresentMonth ;
		sz = sz + "\n\tYear := " + iPresentYear ;

		System.out.println( this.getClass().getName() + sz ) ;


		return ;
	}


	// ----------------------------------------------------------------------
	// test rig
	// ----------------------------------------------------------------------
	public static void main( String [] args )
	{
		stats data = new stats() ;


		data.bestSellingShoeThisMonth();


		System.out.println("\n") ;

		return ;
	}
}
