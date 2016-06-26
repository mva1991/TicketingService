# TicketingService

Instructions to execute the application:

	mvn compile exec:java -Dexec.mainClass=com.reservation.App
	
Principles:

	1. This application uses Map<K,V> as the key data structure to persist the venue/seat availability information.
	2. A hierarchical structure defining the relation between Level -> Row -> Seat  is built at the start up.
	3. This application requires venue.properties file where information about the initial set up is stored.
	4. The parameters in the venue.properties can be changed based on the user preference with caution.
	5. Best-Seat-Picking-Strategy: Seats are allocated beginning from the level and row closest to the stage, based on the current seat availability and range of levels the user chooses at the RunTime.
	 
Errors and Validations:

	The following exceptions are handled by the logic.
	1. The seats that were blocked have been revoked as the blocking time has elapsed.  
	2. Could not complete the reservation. Reason: Incorrect input was entered. Please try again.
	3. Cannot reserve "+numSeats+" seat(s) as it exceeds the number of available seats("+totalFree+"). Please try again.
	4. Please enter a valid email address.
	5. Number of seats to be reserved is not valid.
	6. Please re-enter a valid level number. Min:1 Max:4
	7. Cannot find "+numSeats+" free seats in the preferred levels. Please change your min-max levels of preference.
	8. Input could not correspond to a number

Disadvantages of this approach and mitigation

	1. Since there is no disk based storage - The application state cannot be preserved over restarts.
	2. Concurrent access of multiple users is not supported as the application is a stand alone java program and there is no application server.
	Under these eventualities - An application server and a disk based storage can facilitate better usage of the application's core functionality.
	
Sample success output: 
	
-----------------------------------------------------------------
Welcome to the Ticket Reservation System.
-----------------------------------------------------------------

Total seats available in the venue: 6250

Current occupancy status is as follows:
---------------------------------------------------------------------------------
Level-Number: 1, Level-Name: Orchestra, Seats-Available: 1250, Ticket-Cost $100.00
Level-Number: 2, Level-Name: Main, Seats-Available: 2000, Ticket-Cost $75.00
Level-Number: 3, Level-Name: Balcony 1, Seats-Available: 1500, Ticket-Cost $50.00
Level-Number: 4, Level-Name: Balcony 2, Seats-Available: 1500, Ticket-Cost $40.00
---------------------------------------------------------------------------------
-----------------------------------------------------------------

Enter the following details 
	Email Address: 
mva1991@gmail.com
	Number of seats you want to reserve: 
15
	Highest level number of preference: 
1
	Lowest level number of preference: 
2
-----------------------------------------------------------------

The following seats have been blocked on your behalf.
Please note that the block on these seats will be revoked in 25000 seconds.

Level : Orchestra, Row : A, Seat-Number : 1
Level : Orchestra, Row : A, Seat-Number : 2
Level : Orchestra, Row : A, Seat-Number : 3
Level : Orchestra, Row : A, Seat-Number : 4
Level : Orchestra, Row : A, Seat-Number : 5
Level : Orchestra, Row : A, Seat-Number : 6
Level : Orchestra, Row : A, Seat-Number : 7
Level : Orchestra, Row : A, Seat-Number : 8
Level : Orchestra, Row : A, Seat-Number : 9
Level : Orchestra, Row : A, Seat-Number : 10
Level : Orchestra, Row : A, Seat-Number : 11
Level : Orchestra, Row : A, Seat-Number : 12
Level : Orchestra, Row : A, Seat-Number : 13
Level : Orchestra, Row : A, Seat-Number : 14
Level : Orchestra, Row : A, Seat-Number : 15

-----------------------------------------------------------------

Do you want to reserve them? (yes/no)
yes

Congratulations! Your reservation is successful. Thank you for booking with us.
-----------------------------------------------------------------
-----------------------------------------------------------------
Welcome to the Ticket Reservation System.
-----------------------------------------------------------------

Total seats available in the venue: 6235

Current occupancy status is as follows:
---------------------------------------------------------------------------------
Level-Number: 1, Level-Name: Orchestra, Seats-Available: 1235, Ticket-Cost $100.00
Level-Number: 2, Level-Name: Main, Seats-Available: 2000, Ticket-Cost $75.00
Level-Number: 3, Level-Name: Balcony 1, Seats-Available: 1500, Ticket-Cost $50.00
Level-Number: 4, Level-Name: Balcony 2, Seats-Available: 1500, Ticket-Cost $40.00
---------------------------------------------------------------------------------
-----------------------------------------------------------------



Sample failure output

-----------------------------------------------------------------
Welcome to the Ticket Reservation System.
-----------------------------------------------------------------

Total seats available in the venue: 6238

Current occupancy status is as follows:
---------------------------------------------------------------------------------
Level-Number: 1, Level-Name: Orchestra, Seats-Available: 1238, Ticket-Cost $100.00
Level-Number: 2, Level-Name: Main, Seats-Available: 2000, Ticket-Cost $75.00
Level-Number: 3, Level-Name: Balcony 1, Seats-Available: 1500, Ticket-Cost $50.00
Level-Number: 4, Level-Name: Balcony 2, Seats-Available: 1500, Ticket-Cost $40.00
---------------------------------------------------------------------------------
-----------------------------------------------------------------

Enter the following details 
	Email Address: 
kfdjsgl;kdfjg;kljdsfkljglkjdsflkgjdflkgjdklfjgkldfjglkdfjgkldfj
	Number of seats you want to reserve: 
12
	Highest level number of preference: 
1
	Lowest level number of preference: 
4
-----------------------------------------------------------------
Oops, Could not complete the reservation. Reason: Please enter a valid email address
-----------------------------------------------------------------
