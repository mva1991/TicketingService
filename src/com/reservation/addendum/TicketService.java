package com.reservation.addendum;

import java.util.ArrayList;

public interface TicketService {

	int numSeatsAvaliable(Integer venueLevel);
	ArrayList<Seat> findAndHoldSeats(int numSeats , Integer minLevel , Integer maxLevel , String customerEmail);
	boolean reserveSeats(int numSeats, String customerEmail);
	
}

