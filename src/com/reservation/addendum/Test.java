package com.reservation.addendum;

import java.util.ArrayList;


public class Test {

	public static void main(String[] args) {
		  try {
			TicketServiceApplication tsa = new TicketServiceApplication();
				ArrayList<Seat> blockedSeatsArray = tsa.findAndHoldSeats(5, 1,
						2, "mva1991@gmail.com");
				System.out.println("ArrSize "+blockedSeatsArray.size());
				System.out.println("Q size "+DataLoader.blockedSeatsQueue.size());
				System.out.println("Seats in Level"+tsa.numSeatsAvaliable(1));
				Thread.sleep(5000);
				System.out.println("Q size "+DataLoader.blockedSeatsQueue.size());
				System.out.println("Seats in Level"+tsa.numSeatsAvaliable(1));
				Thread.sleep(5000);
				System.out.println("Q size "+DataLoader.blockedSeatsQueue.size());
				System.out.println("Seats in Level"+tsa.numSeatsAvaliable(1));
				Thread.sleep(5000);
				System.out.println("Q size "+DataLoader.blockedSeatsQueue.size());
				System.out.println("Seats in Level"+tsa.numSeatsAvaliable(1));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}


