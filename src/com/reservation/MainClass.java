package com.reservation;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
     //DataLoader.buildVenue();
    TicketServiceApplication tsa = new TicketServiceApplication();
    
    tsa.findAndHoldSeats(2, 1, 2, "mva1991@gmail.com");
    
     
     
		
	}

}
