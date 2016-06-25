package com.reservation;

import java.util.ArrayList;

public interface TicketService {

	int numSeatsAvaliable(Integer venueLevel);
	ArrayList<Seat> findAndHoldSeats(int numSeats , Integer minLevel , Integer maxLevel , String customerEmail);
	void reserveSeats(int numSeats, String customerEmail);
	
}

