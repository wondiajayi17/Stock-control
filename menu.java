//import scanner
import java.util.Scanner;

public class menu
{

	//Declaring objects for the other classes
	private static menu mu = new menu();
	private static shoes trainer = new shoes();
	private static customer custom = new customer();
	private static receipt rece = new receipt();
	private static stats data = new stats() ;

	//open the scanners
	private Scanner szKeyboard = new Scanner (System.in);
	private Scanner iKeyboard  = new Scanner (System.in);

	//Public variables
	private static String szOption;
	private static int iOption;


	//Method to call all the methods
	public int menu()
	{
		System.out.println("\n");
		System.out.println("=====================================================");
		System.out.println("\tWelcome to the kiosk ");
		System.out.println("=====================================================\n");

		//get the user to select a choice 
		System.out.println("Menu:\n");
		System.out.println("1.) Customer options \n2.) Shoe options \n3.) Sold Shoe \n4.) Statistics \n5.) Quit");
		System.out.print("Please select a choice: ");
		szOption = szKeyboard.nextLine();

		//check that the input is valid before continuing
		while(!szOption.matches("[1-5]+"))
		{
			System.out.print("This input is not valid! Please select a choice: ");
			szOption = szKeyboard.nextLine();
		}

		//convert the String back into an int
		iOption =  Integer.valueOf(szOption);

		return iOption;
	}// close the menu

	// A menu filter to guide the user to their place
	public void menuFilter(int iOption)
	{
		menu mu = new menu();
		//massive switchcase that just filters the user to the correct place
		switch (iOption) 
		{
		case 1:
			//call the customer menu
			mu.CustomerMenu();

			break;
		case 2:
			//call the Shoe menu
			mu.ShoeMenu();

			break;
		case 3:
			//call the selling shoe menu 
			mu.SoldMenu();

			break;
		case 4:
			//call the stats menu 
			mu.StatsMenu();

			break;
		case 5:
			//close the kiosk
			System.out.println("=====================================================");
			System.out.println("\tClosing Kiosk");
			System.out.println("=====================================================\n");
			System.exit(0);			
			break;

		}// end switchcase
	}// end menuFilter

	//===================================================================================
	public void CustomerMenu()
	{
		//ask the user what they want to do
		System.out.println("\n");
		System.out.println("=====================================================");
		System.out.println("What would you like to do?");
		System.out.println("1.) View all previous customers \n2.) Search for a customer \n3.) Quit");
		System.out.print("Please select a choice: ");

		//allow the user to enter the input
		szOption = szKeyboard.nextLine();

		//check that the input is valid before continuing
		while(!szOption.matches("[1-3]+"))
		{
			System.out.print("This input is not valid! Please select a choice: ");
			szOption = szKeyboard.nextLine();
		}

		//convert the String back into an int
		iOption =  Integer.valueOf(szOption);


		//switch case to filter the option chosen
		switch (iOption) 
		{
		case 1:
			//call the method to view the table of all the customers ever
			mu.viewCustomers();

			break;
		case 2:
			//call the method to search for a customer
			mu.searchCustomer();
			break;

		case 3:
			//close the kiosk
			System.out.println("=====================================================");
			System.out.println("\tClosing Kiosk");
			System.out.println("=====================================================\n");
			System.exit(0);
			break;
		}
	}//end CustomerMenu

	//=========================================================================================
	public void viewCustomers()
	{
		//declare local variables
		int iNumOfRows = 0 ;

		//count the number of rows that are in the table to be able to loop it that many times
		iNumOfRows = custom.countRows();

		//filer
		System.out.println("-----------------------------------------------------") ;

		//loop the customers table so they all print out the get all the records
		for(int i = 1; i <= iNumOfRows; i ++)
		{
			//call the method from customers to view all the customers in the database
			custom.viewAllCustomers(i);
		}//close for loop

	}//close viewcustomers

	//====================================================================================
	public void searchCustomer()
	{
		//declare local variables
		int iNum;
		int iAnswer;
		String szAnswer;
		String szCounter = "1";

		//ask the user if they want to search by surname or id or email
		System.out.print("\nWould you like to search by (1) Surname, (2) CustomerId or (3) Email?: ");
		iAnswer = iKeyboard.nextInt();

		//switchcase to filter option
		switch (iAnswer) 
		{
		case 1:
			//ask user to enter the customers surname
			System.out.print("Please enter the Surname of the customer you are looking for: ");
			szAnswer = szKeyboard.next();

			//validation to make sure the user cannot enter the wrong input
			//call subroutine to validate the value
			szAnswer = validationForString(szAnswer);

			//method to collect the record by surname
			custom.getBySurname(szAnswer,szCounter) ;
			break;

		case 2:
			//allow the user to search for a customer by their customer id
			System.out.print("Please enter the CustomerId of the customer you are looking for: ");
			szAnswer = szKeyboard.next();

			//validation to make sure the user cannot enter the wrong input
			szAnswer = validationForInt(szAnswer);

			//convert the String back into an int
			iNum =  Integer.valueOf(szAnswer);

			//method to collect the record of the customer by their id.
			custom.collectRecord(iNum) ;

			break;

		case 3:
			//allow the user to search for a customer by their email
			System.out.print("Please enter the Email of the customer you are looking for: ");
			szAnswer = szKeyboard.next();

			//validation to make sure the user cannot enter the wrong input
			while(!szAnswer.matches("[a-zA-Z0-9@.]+"))
			{
				System.out.print("This input is not valid! Emails can only contain letters, numbers '@' and '.': ");
				szAnswer = szKeyboard.next();
			}

			//method to collect record of customer by their email
			custom.collectRecordByEmail(szAnswer) ;

			break;
		}
	}

	//=================================================================================================
	public void ShoeMenu()
	{
		//declare variables 
		int iNum;

		System.out.println("\n");
		System.out.println("=====================================================");
		System.out.println("What would you like to do?");
		System.out.println("1.) Add a new shoe \n2.) Update the StockLevels of a shoe \n3.) Delete a shoe (no longer sell this shoe, ONLY) "
				+ "\n4.) Find shoe \n5.) View all shoes \n6.) Quit");

		System.out.print("Please select a choice: ");

		//ask the user to enter their option
		szOption = szKeyboard.nextLine();

		//check that the input is valid before continuing
		while(!szOption.matches("[1-6]+"))
		{
			System.out.print("This input is not valid! Please select a choice: ");
			szOption = szKeyboard.nextLine();
		}

		//convert the String back into an int
		iOption =  Integer.valueOf(szOption);


		//switch case to filter the option           
		switch (iOption) 
		{
		case 1:
			//call the method to add a new shoe to the database, including all the details (if an shooe is added that we already have, just update stock levels by 1)
			mu.addShoe();
			break;
		case 2:
			//call the method to update their stock levels up to a new number ( if you get in 2 new shoes of the type already on the system then add 2)
			mu.updateStockLevels();

			break;

		case 3:
			//call the method to delete a shoe from stock levels, only do this if the shoe is no longer going to be sold
			mu.deleteShoes();

			break;

		case 4:
			//call the method to find a shoe by name or by uksize or by colour or by eusize or by boxcode or by shoe name
			mu.findShoe();

			break;

		case 5:
			//call the subroutine to just view all the shoes in the database
			mu.viewAllShoes();

		case 6:
			//close the kiosk
			System.out.println("=====================================================");
			System.out.println("\tClosing Kiosk");
			System.out.println("=====================================================\n");
			System.exit(0);
			break;
		}
	}//end ShoeMenu

	//===============================================================================================
	public void addShoe()
	{
		//declare variables
		String szShoeName ;
		String szBoxCode ;
		String szUkSize ;
		String szEuSize ;
		String szColour ;
		int iPrice ;
		String szPrice ;
		int iStockLevel ;
		String szStockLevel ;
		String szOrder ;
		String szCheck;
		String szTemp;

		//ask the user to enter the following
		System.out.print("\nPlease enter the following:\n");


		//ask the user to enter shoename
		System.out.print("ShoeName: ");
		szKeyboard.useDelimiter("\n");
		szShoeName = szKeyboard.next();


		//ask the user to enter Boxcode
		System.out.print("BoxCode (__ ____ ____): ");

		//call subroutine to validate the value
		szKeyboard.useDelimiter("\n");
		szBoxCode = validationForInt(szKeyboard.next());

		//while the length of the boxcode is less than 10 (what it should be if entered correctly)
		while(szBoxCode.length() < 10 )
		{
			System.out.print("This input is not valid! Please enter the correct input: ");
			szBoxCode = szKeyboard.nextLine();

		}

		//use substring to get the character that should be the space
		szCheck = szBoxCode.substring(2, 3);

		//if the character is not a space then 
		if(!szCheck.contains(" "))
		{	
			//store the rest of the boxcode from the proposed space onwards into a temp string 
			szTemp = szBoxCode.substring(2);

			//add a space where the space should be
			szBoxCode = szBoxCode.substring(0, 2) + " ";

			//connect the second half of the string back with the rest of the string
			szBoxCode = szBoxCode + szTemp ;
		}

		//use substring to get the character that should be the space
		szCheck = szBoxCode.substring(7, 8);


		//if the character is not a space then
		if(!szCheck.contains(" "))
		{

			//store the rest of the boxcode from the proposed space onwards into a temp string 
			szTemp = szBoxCode.substring(7);

			//add a space where the space should be
			szBoxCode = szBoxCode.substring(0, 7) + " ";

			//connect the first half of the string back with the rest of the string
			szBoxCode = szBoxCode + szTemp ;

		}


		//if the string is now less than 12 ( what the string would be when spaces are added)
		if(szBoxCode.length() > 12)
		{
			// replace all the rest of the string with a space to get rid of it visually
			szBoxCode = szBoxCode.replaceAll(szBoxCode.substring(12), " ");

		}


		//ask the user to enter uksize of the shoe
		System.out.print("UkSize: ");
		szUkSize =  szKeyboard.next();

		//call subroutine to validate the input
		szUkSize = validationForInt(szUkSize);

		//ask the user to enter eusize of the shoe
		System.out.print("EuSize: ");
		szEuSize =  szKeyboard.next();

		//call subroutine to validate the input
		szEuSize = validationForInt(szEuSize);

		//ask the user to enter colour of the shoe
		System.out.print("Colour: ");
		szColour =  szKeyboard.next();

		//call subroutine to validate the input
		szColour = validationForString(szColour);

		//ask the user to enter the price of the shoe
		System.out.print("Price: ");
		szPrice =  szKeyboard.next();

		//call subroutine to validate the input
		szPrice = validationForInt(szPrice);

		//convert the String back into an int
		iPrice =  Integer.valueOf(szPrice);

		//ask user to enter the stocklevel of the shoe
		System.out.print("StockLevel: ");
		szStockLevel =  szKeyboard.next();

		//call subroutine to validate the input
		szStockLevel = validationForInt(szStockLevel);

		//convert the String back into an int
		iStockLevel =  Integer.valueOf(szStockLevel);

		//ask the user to enter if the shoe is avalible to order
		System.out.print("AvailibleToOrder (Y = Yes, N = No): ");
		szOrder =  szKeyboard.next();

		while(!szOrder.matches("[YyNn]{1}"))
		{
			System.out.print("This input is not valid! Please enter the correct input: ");
			szOrder =  szKeyboard.next();
		}

		//method to add shoes to the database
		trainer.addShoes(szShoeName, szBoxCode, szUkSize, szEuSize, szColour, iPrice, iStockLevel, szOrder);

		System.out.println("\n~ Shoe added ~\n");

	}
	//==========================================================================================================================================================================    
	public void updateStockLevels()
	{
		//daclare variables
		int iShoeId;
		String szShoeId;
		int iNum;
		String szNum;

		////allow the user to enter the shoeid
		System.out.print("Please enter the shoeId of the shoe you would like update: ");
		szShoeId = szKeyboard.next();

		szShoeId = validationForInt(szShoeId);

		//convert the String back into an int
		iShoeId =  Integer.valueOf(szShoeId);

		//allow the use rto enter the number of shoes theyre gonna add
		System.out.print("Please enter the amount you would like to add: ");
		szNum =  szKeyboard.next();

		//call subroutine to validate the input
		szNum = validationForInt(szNum);

		//convert the String back into an int
		iNum =  Integer.valueOf(szNum);

		//method to update the stock levels
		trainer.updateStockLevelUp(iShoeId, iNum);
		System.out.println("\n~ Stock levels have been updated ~\n");

	}
	//==========================================================================================
	public void deleteShoes()
	{
		//declare variables
		int iShoeId;
		String szShoeId;

		// ask the user to enter the shoeid of the shoe they want to delete
		System.out.print("Please enter the shoeId of the shoe you would like delete: ");
		szShoeId = szKeyboard.next();

		//call subroutine to validate the input
		szShoeId = validationForInt(szShoeId);

		//convert the String back into an int
		iShoeId =  Integer.valueOf(szShoeId);

		//method to delete shoes 
		trainer.deleteShoes(iShoeId);
		System.out.println("\n~ Stock levels have been updated ~\n");

	}
	//===========================================================================================
	public void findShoe()
	{
		//declare local variables
		String szAnswer;
		int iNum;
		String szNum;
		int iAnswer;
		String szTemp;
		String szCheck;

		//ask the user if they want to search by shoeid, shoename, boxcode, uksize, eusize or colour
		System.out.print("\nWould you like to search by (1) ShoeId, (2) ShoeName, (3) BoxCode, (4) Uk Size, (5) Eu Size, (6) Colour : ");
		szAnswer = szKeyboard.next();

		//call subroutine to validate the input
		szAnswer = validationForInt(szAnswer);

		//convert the String back into an int
		iAnswer =  Integer.valueOf(szAnswer);

		//if they search by shoeId then let them search by shoeId
		if(iAnswer == 1)
		{
			System.out.print("Please enter the ShoeId of the shoe you are looking for: ");
			szNum =  szKeyboard.next();

			//call subroutine to validate the input
			szNum = validationForInt(szNum);

			//convert the String back into an int
			iNum =  Integer.valueOf(szNum);

			//call function to collect the record by shoeId
			trainer.collectRecord(iNum) ;

		}


		else if(iAnswer == 2) //if they enter by shoe name then let them search by shoe name
		{

			szKeyboard.useDelimiter("\n"); //Delimiter to make spaces count in a string
			System.out.print("Please enter the name of the shoe you are looking for: ");
			szAnswer = szKeyboard.next();      

			// method to collect a record by the shoe name
			trainer.getByShoeName(szAnswer) ;
		}


		else if(iAnswer == 3) //let them search by boxcode 
		{
			System.out.print("Please enter the boxcode of the shoe you are looking for: ");

			//change the delimiter to another pattern such as linefeed
			szKeyboard.useDelimiter("\n");
			szAnswer = szKeyboard.next();



			//while the length of the boxcode is less than 10 (what it should be if entered correctly)
			while(szAnswer.length() < 10 )
			{
				System.out.print("This input is not valid! Please enter the correct input: ");
				szAnswer = szKeyboard.next();

			}

			//use substring to get the character that should be the space
			szCheck = szAnswer.substring(2, 3);

			//if the character is not a space then 
			if(!szCheck.contains(" "))
			{	
				//store the rest of the boxcode from the proposed space onwards into a temp string 
				szTemp = szAnswer.substring(2);

				//add a space where the space should be
				szAnswer = szAnswer.substring(0, 2) + " ";

				//connect the second half of the string back with the rest of the string
				szAnswer = szAnswer + szTemp ;
			}

			//use substring to get the character that should be the space
			szCheck = szAnswer.substring(7, 8);


			//if the character is not a space then
			if(!szCheck.contains(" "))
			{

				//store the rest of the boxcode from the proposed space onwards into a temp string 
				szTemp = szAnswer.substring(7);

				//add a space where the space should be
				szAnswer = szAnswer.substring(0, 7) + " ";

				//connect the first half of the string back with the rest of the string
				szAnswer = szAnswer + szTemp ;

			}


			//if the string is now less than 12 ( what the string would be when spaces are added)
			if(szAnswer.length() > 12)
			{
				// replace all the rest of the string with a space to get rid of it visually
				szAnswer = szAnswer.replaceAll(szAnswer.substring(12), " ");

			}


			// method to collect a record by the boxcode
			trainer.collectRecordByBoxCode( szAnswer ) ;
		}

		else if(iAnswer == 4) //let them search by  uk size
		{
			//allow the user to search for shoes by uk size
			System.out.print("Please enter the Uk size of the shoe you are looking for: ");

			szAnswer = szKeyboard.next();

			//call subroutine to validate the input
			szAnswer = validationForInt(szAnswer);


			// method to collect a record by the uk size
			trainer.collectRecordByUkSize( szAnswer ) ;

		}
		else if(iAnswer == 5) //let them search by Eu size
		{
			System.out.print("Please enter the Eu size of the shoe you are looking for: ");

			szAnswer = szKeyboard.next();

			//call subroutine to validate the input
			szAnswer = validationForInt(szAnswer);

			// method to collect a record by the EU size
			trainer.collectRecordByEuSize( szAnswer ) ;

		}

		else if(iAnswer == 6) //let them search by colour 
		{
			//let hem search by colour
			System.out.print("Please enter the colour of the shoe you are looking for: ");

			szAnswer = szKeyboard.next();

			//call subroutine to validate the input
			szAnswer = validationForString(szAnswer);

			// method to collect a record by the colour
			trainer.collectRecordByColour(szAnswer) ;
		}
	}
	//=====================================================================================
	public void viewAllShoes()
	{
		//declare local variables
		int iNumOfRows = 0 ;

		//assign the rows to this variable
		iNumOfRows = trainer.countRows();

		//filer
		System.out.println("-----------------------------------------------------") ;

		//loop the records to print them all out
		for(int i = 1; i <= iNumOfRows; i ++)
		{
			trainer.viewAllShoes(i);
		}

	}
	//=======================================================================================
	public void SoldMenu() 
	{

		System.out.println("\n");
		System.out.println("=====================================================");
		System.out.println("What would you like to do?");
		System.out.println("1.) Selling Shoe \n2.) Shoes Returned \n3.) View Receipt \n4.) View all Receipts \n5.) Quit");
		System.out.print("Please select a choice: ");

		szOption = szKeyboard.nextLine();

		//check that the input is valid before continuing
		while(!szOption.matches("[1-5]+"))
		{
			System.out.print("This input is not valid! Please select a choice: ");
			szOption = szKeyboard.nextLine();
		}

		//convert the String back into an int
		iOption =  Integer.valueOf(szOption);


		//switch case to filter the options
		switch (iOption) 
		{
		case 1:
			//call the method to update stock levels
			mu.soldShoe();

			break;
		case 2:
			//call the method to return a shoe
			mu.shoeReturned();
			break;

		case 3:
			//call the method to find the desired receipt
			mu.findReceipt();
			break;

		case 4:
			//call the method to view all receipts
			mu.viewAllReceipts();
			break;

		case 5:
			System.out.println("=====================================================");
			System.out.println("\tClosing Kiosk");
			System.out.println("=====================================================\n");
			System.exit(0);
			break;
		}
	}//end SoldMenu

	//=========================================================================================================
	public void soldShoe() 
	{
		//declare local variables
		int iShoeId;
		String szShoeId;
		int iNum;
		String szNum;
		int iCounter = 0;
		String szOrder;
		String szAnswer;

		//Check what the shoe id is then remove one off the stock levels
		System.out.print("Please enter the shoeId of the shoe you are selling: ");
		szShoeId = szKeyboard.next();

		//make sure what is entered is the correct input
		szShoeId = validationForInt(szShoeId);

		//convert the String back into an int
		iShoeId =  Integer.valueOf(szShoeId);

		//Ask how many pairs are being sold to know how many to remove off the stock levels
		System.out.print("How many pairs?: ");
		szNum =  szKeyboard.next();

		//call subroutine to validate the input
		szNum = validationForInt(szNum);

		//convert the String back into an int
		iNum =  Integer.valueOf(szNum);

		//Method to actually update the stock levels
		iCounter = trainer.updateStockLevelDown(iShoeId, iNum);

		//if the counter = 1 then check if the shoe is avalilble to order if not then continue
		if(iCounter == 1)
		{
			szOrder = trainer.orderingShoe(szShoeId);

			if(szOrder.equals("Y"))
			{
				System.out.println("This shoe is availible to order to your home.\nWould you like to order this shoe?");

				//ask the user to enter if want to order the shoe
				System.out.print("Y = Yes, N = No: ");
				szAnswer =  szKeyboard.next();

				while(!szAnswer.matches("[YyNn]{1}"))
				{
					System.out.print("This input is not valid! Please enter the correct input: ");
					szAnswer =  szKeyboard.next();
				}

				//if the user wants to order to shoe continue to the receipt
				if(szAnswer.equalsIgnoreCase("Y"))
				{

					//stocklevels will not be altered as the shoes will be delivered from the warehouse

					//method to generate the receipt
					mu.genReceipt(iShoeId , iNum);

					//end the code
					System.out.println("=====================================================");
					System.out.println("\tClosing Kiosk");
					System.out.println("=====================================================\n");
					System.exit(0);
				}
				else
				{
					System.out.println("=====================================================");
					System.out.println("\tClosing Kiosk");
					System.out.println("=====================================================\n");
					System.exit(0);
				}
			}
			else
			{
				System.out.println("=====================================================");
				System.out.println("\tClosing Kiosk");
				System.out.println("=====================================================\n");
				System.exit(0);
			}

		}
		else
		{
			System.out.println("\n~ Stock levels have been updated ~\n");

			//check if the stocklevels are low, if they are order more (precaution)
			trainer.checkStockLevels(iShoeId);

			//open method to generate a receipt using the same id of the shoe the user just sold
			mu.genReceipt(iShoeId , iNum);       
		}
	}// end method  
	//=================================================================================================
	public void genReceipt(int iShoeId, int iNum)
	{
		//declare local variables to enter into the add subroutine
		String szFirst ;
		String szLast ;
		String szCity;
		String szCode;
		String szMail;
		String szAnswer;

		//ask the user if they are a returning customer to then automatically get their details
		System.out.print("Is this a returning customer? (Y = Yes , N = No): ");
		szAnswer = szKeyboard.next();

		while(!szAnswer.matches("[YyNn]{1}"))
		{
			System.out.print("This input is not valid! Please enter the correct input: ");
			szAnswer =  szKeyboard.next();
		}

		//if they're a returning customer then collect their old details
		if(szAnswer.equalsIgnoreCase("Y"))
		{

			// ask them to enter that customers surname
			System.out.print("Please enter the Surname of the customer: ");
			szLast =  szKeyboard.next();

			//call subroutine to validate the input
			szLast = validationForString(szLast);

			generateRece(iShoeId, szLast, iNum);

		}
		else
		{
			//ask the user to enter the following
			System.out.print("\nPlease enter the following:\n");

			System.out.print("FirstName: ");
			szFirst = szKeyboard.next();

			//call subroutine to validate the input
			szFirst = validationForString(szFirst);


			System.out.print("Surname: ");
			szLast =  szKeyboard.next();

			//call subroutine to validate the input
			szLast = validationForString(szLast);

			System.out.print("Town: ");
			szCity =  szKeyboard.next();

			//call subroutine to validate the input
			szCity = validationForString(szCity);

			System.out.print("PostCode: ");
			szCode =  szKeyboard.next();

			//validation to make sure the user cannot enter the wrong input
			while(!szCode.matches("[a-zA-Z0-9]+"))
			{
				System.out.print("This input is not valid! Please enter the correct input: ");
				szCode = szKeyboard.next();

			}

			szKeyboard.nextLine();

			System.out.print("Email: ");
			szMail =  szKeyboard.nextLine();

			//validation to make sure the user cannot enter the wrong input
			while(!szMail.matches("[a-zA-Z0-9@.]+"))
			{
				System.out.print("This input is not valid! Emails can only contain letters, numbers '@' and '.': ");
				szMail = szKeyboard.next();
			}

			//pass them into the function to add a customer to the database
			custom.addDetails(szFirst, szLast, szCity, szCode, szMail);

			//print that the customer has been added
			System.out.print("\n ~ Customer Added ~\n");

			//procedue just to seperate the customer getting added and the making of the receipt
			generateRece(iShoeId, szLast, iNum);
		}//end else

	}
	//====================================================================================================
	public void generateRece(int iShoeId, String szLast, int iNum)
	{
		//use the shoe id and surname to get the name of the shoe and the price of the shoe and add it onto the recipt
		rece.genReceipt(iShoeId, szLast, iNum);


	}
	//===================================================================================================================
	public void shoeReturned() 
	{
		//Declare local variables 
		int iShoeId;
		String szShoeId;
		int iRecepitId;
		String szRecepitId;
		int iNum = 1;

		//Ask user what shoe they sold (by ID)
		System.out.print("Please enter the shoeId of the shoe that is being returned: ");
		szShoeId = szKeyboard.next();

		//make sure what is entered is the correct input
		szShoeId = validationForInt(szShoeId);

		//convert the String back into an int
		iShoeId =  Integer.valueOf(szShoeId);

		//method to update stock levels
		trainer.updateStockLevelUp(iShoeId, iNum);
		System.out.println("\n~ Stock levels have been updated ~\n");

		//Ask user what the receiptid of the customer was so that it can be removed off the system
		System.out.print("Please enter the ReceiptId that the customer has: ");
		szRecepitId = szKeyboard.next();

		//make sure what is entered is the correct input
		szRecepitId = validationForInt(szRecepitId);

		//convert the String back into an int
		iRecepitId =  Integer.valueOf(szRecepitId);

		//method to delete the receipt of the customer off the system as the shoes returned means they were never sold
		rece.deleteReceipt(iRecepitId);

	}// end method shoeReturned
	//===========================================================================================================================
	public void findReceipt()
	{
		//declare variables
		int iNum;
		String szNum;

		// ask the user to enter the bookingid of the customers booking they are looking for
		System.out.print("Please enter the CustomerId of the person of the receipt you are looking for: ");
		szNum =  szKeyboard.next();

		//call subroutine to validate the input
		szNum = validationForInt(szNum);

		//convert the String back into an int
		iNum =  Integer.valueOf(szNum);

		//method to find receipt by id
		rece.findReceipt(iNum) ;
	}
	//==========================================================================================================================
	public void viewAllReceipts()
	{
		rece.displayAllRece();       
	}

	//==============================================================================================================================
	public void StatsMenu()
	{
		//ask the user what they want to do
		System.out.println("\n");
		System.out.println("=====================================================");
		System.out.println("What would you like to do?");
		System.out.println("1.) Shoe related stats \n2.) Revenue related stats \n3.) Quit");
		System.out.print("Please select a choice: ");

		//allow the user to choose
		szOption = szKeyboard.nextLine();

		//check that the input is valid before continuing
		while(!szOption.matches("[1-3]+"))
		{
			System.out.print("This input is not valid! Please select a choice: ");
			szOption = szKeyboard.nextLine();
		}

		//convert the String back into an int
		iOption =  Integer.valueOf(szOption);


		//switch case to filter the option chosen
		switch (iOption) 
		{
		case 1:
			//call the method to view the stats relating to the shoes sold
			mu.shoeRelatedStats();

			break;
		case 2:
			//call the method to view the stats relating to the money made
			mu.revenueRelatedStats();
			break;

		case 3:
			//close the kiosk
			System.out.println("=====================================================");
			System.out.println("\tClosing Kiosk");
			System.out.println("=====================================================\n");
			System.exit(0);
			break;
		}
	}//end StatsMenu

	//=============================================================================================================
	public void shoeRelatedStats()
	{
		//ask the user what they want to do
		System.out.println("\n");
		System.out.println("=====================================================");
		System.out.println("What would you like to do?");
		System.out.println("1.) Shoes sold today \n2.) Shoes sold this month \n3.) Shoes sold this year \n4.) Shoes sold on a day of choice"
				+ "\n5.) Shoes sold on a month of choice \n6.) Shoes sold on a year of choice \n7.) Best selling shoe this Month"
				+ "\n8.) Best selling shoe this year \n9.) Best selling shoe in month of choice"
				+ "\n10.) Best selling shoe in year of choice \n11.) Quit ");
		System.out.print("Please select a choice: ");

		//allow the user to choose
		szOption = szKeyboard.nextLine();

		//check that the input is valid before continuing
		while(!szOption.matches("[0-9]+"))
		{
			System.out.print("This input is not valid! Please select a choice: ");
			szOption = szKeyboard.nextLine();
		}

		//convert the String back into an int
		iOption =  Integer.valueOf(szOption);

		int iDay;
		String szDay;
		int iMonth;
		String szMonth;
		int iYear;
		String szYear;

		//switch case to filter the option chosen
		switch (iOption) 
		{
		case 1:
			//call the method to view the shoes sold today
			data.shoesSoldToday();

			break;
		case 2:
			//call the method to view the shoes sold this month
			data.shoesSoldThisMonth();
			break;           
		case 3:
			//call the method to view the shoes sold this year
			data.shoesSoldThisYear();
			break;              
		case 4:

			System.out.print("Please select a day: ");

			//allow the user to choose what day
			szDay = szKeyboard.next();

			//validate the input
			szDay = validationForInt(szDay);

			//convert the String back into an int
			iDay =  Integer.valueOf(szDay);

			System.out.print("Please select a month: ");

			//allow the user to choose what month
			szMonth = szKeyboard.next();

			//validate the input
			szMonth = validationForInt(szMonth);

			//convert the String back into an int
			iMonth =  Integer.valueOf(szMonth);

			System.out.print("Please select a year: ");

			//allow the user to choose what year
			szYear = szKeyboard.next();

			//validate the input
			szYear = validationForInt(szYear);

			//convert the String back into an int
			iYear =  Integer.valueOf(szYear);

			//call the method to view the shoes sold on a particular day of choice
			data.shoesSoldOnThisDay(iDay,iMonth,iYear);
			break;                             
		case 5:
			System.out.print("Please select a month: ");

			//allow the user to choose what month
			szMonth = szKeyboard.next();

			//validate the input
			szMonth = validationForInt(szMonth);

			//convert the String back into an int
			iMonth =  Integer.valueOf(szMonth);

			System.out.print("Please select a year: ");

			//allow the user to choose what year
			szYear = szKeyboard.next();

			//validate the input
			szYear = validationForInt(szYear);

			//convert the String back into an int
			iYear =  Integer.valueOf(szYear);

			//call the method to view the shoes sold this in a particular month of choice
			data.shoesSoldInThisMonth(iMonth, iYear);
			break;                                  
		case 6:
			System.out.print("Please select a year: ");

			//allow the user to choose what year
			szYear = szKeyboard.next();

			//validate the input
			szYear = validationForInt(szYear);

			//convert the String back into an int
			iYear =  Integer.valueOf(szYear);

			//call the method to view the shoes sold in a particular year of choice
			data.shoesSoldInThisYear(iYear);
			break;                                                
		case 7:
			//call the method to view the best selling shoe this month
			data.bestSellingShoeThisMonth();
			break;                                                  
		case 8:
			//call the method to view the best selling shoe this year
			data.bestSellingShoeThisYear();
			break;                                                         
		case 9:
			System.out.print("Please select a month: ");

			//allow the user to choose what month
			szMonth = szKeyboard.next();

			//validate the input
			szMonth = validationForInt(szMonth);

			//convert the String back into an int
			iMonth =  Integer.valueOf(szMonth);

			System.out.print("Please select a year: ");

			//allow the user to choose what year
			szYear = szKeyboard.next();

			//validate the input
			szYear = validationForInt(szYear);

			//convert the String back into an int
			iYear =  Integer.valueOf(szYear);

			//call the method to view the best selling shoe in a particular month of choice
			data.bestSellingShoeInThisMonth(iMonth, iYear);
			break;
		case 10:
			System.out.print("Please select a year: ");

			//allow the user to choose what year
			szYear = szKeyboard.next();

			//validate the input
			szYear = validationForInt(szYear);

			//convert the String back into an int
			iYear =  Integer.valueOf(szYear);

			//call the method to view the best selling shoe a particular year of choice
			data.bestSellingShoeInThisYear(iYear);
			break;   
		case 11:
			//close the kiosk
			System.out.println("=====================================================");
			System.out.println("\tClosing Kiosk");
			System.out.println("=====================================================\n");
			System.exit(0);
			break;
		}
	}//end StatsMenu

	//=============================================================================================================
	public void revenueRelatedStats()
	{
		//ask the suer what they want to do
		System.out.println("\n");
		System.out.println("=====================================================");
		System.out.println("What would you like to do?");
		System.out.println("1.) Revenue made today \n2.) Revenue made this month \n3.) Revenue made this year \n4.) Revenue made on a day of choice"
				+ "\n5.) Revenue made on a month of choice \n6.) Revenue made on a year of choice \n7.) Quit ");

		System.out.print("Please select a choice: ");

		//allow the uset to choose
		szOption = szKeyboard.nextLine();

		//check that the input is valid before continuing
		while(!szOption.matches("[1-9]+"))
		{
			System.out.print("This input is not valid! Please select a choice: ");
			szOption = szKeyboard.nextLine();
		}

		//convert the String back into an int
		iOption =  Integer.valueOf(szOption);

		int iDay;
		String szDay;
		int iMonth;
		String szMonth;
		int iYear;
		String szYear;

		//switch case to filter the option chosen
		switch (iOption) 
		{
		case 1:
			//call the method to view the amount of money made today
			data.revenueMadeToday();

			break;
		case 2:
			//call the method to view the amount of money made this month
			data.revenueMadeThisMonth();
			break;           
		case 3:
			//call the method to view the amount of money made this year
			data.revenueMadeThisYear();
			break;              
		case 4:

			System.out.print("Please select a day: ");

			//allow the user to choose what day
			szDay = szKeyboard.next();

			//validate the input
			szDay = validationForInt(szDay);

			//convert the String back into an int
			iDay =  Integer.valueOf(szDay);

			System.out.print("Please select a month: ");

			//allow the user to choose what month
			szMonth = szKeyboard.next();

			//validate the input
			szMonth = validationForInt(szMonth);

			//convert the String back into an int
			iMonth =  Integer.valueOf(szMonth);

			System.out.print("Please select a year: ");

			//allow the user to choose what year
			szYear = szKeyboard.next();

			//validate the input
			szYear = validationForInt(szYear);

			//convert the String back into an int
			iYear =  Integer.valueOf(szYear);

			//call the method to view the amount of money made on a particular day of choice
			data.revenueMadeOnThisDay(iDay,iMonth,iYear);
			break;                             
		case 5:
			System.out.print("Please select a month: ");

			//allow the user to choose what month
			szMonth = szKeyboard.next();

			//validate the input
			szMonth = validationForInt(szMonth);

			//convert the String back into an int
			iMonth =  Integer.valueOf(szMonth);

			System.out.print("Please select a year: ");

			//allow the user to choose what year
			szYear = szKeyboard.next();

			//validate the input
			szYear = validationForInt(szYear);

			//convert the String back into an int
			iYear =  Integer.valueOf(szYear);

			//call the method to view the amount of money made in a particular month of choice
			data.revenueMadeInThisMonth(iMonth, iYear);
			break;                                  
		case 6:
			System.out.print("Please select a year: ");

			//allow the user to choose what year
			szYear = szKeyboard.next();

			//validate the input
			szYear = validationForInt(szYear);

			//convert the String back into an int
			iYear =  Integer.valueOf(szYear);

			//call the method to view the amount of money made in a particular Year of choice
			data.revenueMadeInThisYear(iYear);
			break;                                                 
		case 7:
			//close the kiosk
			System.out.println("=====================================================");
			System.out.println("\tClosing Kiosk");
			System.out.println("=====================================================\n");
			System.exit(0);
			break;
		}
	}//end StatsMenu


	//=========================================================================================
	public String validationForString(String szAnswer)
	{
		//validation to make sure the user cannot enter the wrong input
		while(!szAnswer.matches("[a-zA-Z\n]+"))
		{
			System.out.print("This input is not valid! Please enter the correct input: ");
			szAnswer = szKeyboard.next();
		}

		//to clear the scanner
		szKeyboard.nextLine();
		return szAnswer;
	}

	public String validationForInt(String szAnswer)
	{
		//validation to make sure the user cannot enter the wrong input
		while(!szAnswer.matches("[0-9]+"))
		{
			System.out.print("This input is not valid! Please enter the correct input: ");
			szAnswer = szKeyboard.next();
		}

		//to clear the scanner
		szKeyboard.nextLine();

		return szAnswer;
	}
	//==================================================================================================
	public static void main(String[] args) 
	{
		//show the menu display
		mu.menu();

		//open the menu sorter and direct the user to the right place pass in the variable of iOption so that it can use the input
		mu.menuFilter(iOption);


	}//end main
}//end class