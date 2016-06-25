package com.reservation;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
     //DataLoader.buildVenue();
    TicketServiceApplication tsa = new TicketServiceApplication();
    System.out.println(tsa.getTotalFreeSeatsInTheVenue());
    tsa.findAndHoldSeats(200, 1, 2, "mva1991@gmail.com");
    tsa.findAndHoldSeats(200, 1, 2, "mva1991@gmail.com");
    tsa.findAndHoldSeats(200, 1, 2, "mva1991@gmail.com");
    tsa.findAndHoldSeats(200, 1, 2, "mva1991@gmail.com");
    tsa.findAndHoldSeats(2000, 1, 2, "mva1991@gmail.com");
    System.out.println(tsa.getTotalFreeSeatsInTheVenue());
    System.out.println(DataLoader.blockedSeatsQueue.size());
     
     
		
	}

}
