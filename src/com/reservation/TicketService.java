package com.reservation;

public interface TicketService {

	int numSeatsAvaliable(Integer venueLevel);
	SeatHold findAndHoldSeats(int numSeats , Integer minLevel , Integer maxLevel , String customerEmail);
	String reserveSeats(Seat seat, String customerEmail);
	
}

