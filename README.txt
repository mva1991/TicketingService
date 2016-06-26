# TicketingService

Instructions to execute the application:

	mvn compile exec:java -Dexec.mainClass=com.reservation.App

Sample output: 
	
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