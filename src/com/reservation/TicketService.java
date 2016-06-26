package com.reservation;

import java.util.ArrayList;

public interface TicketService {
/**
 * 
 * @param venueLevel
 * @return number of seats available
 */
	int numSeatsAvaliable(Integer venueLevel);
	
/**
 * 
 * @param numSeats
 * @param minLevel
 * @param maxLevel
 * @param customerEmail
 * @return ArrayList<Seat> that are holded on behalf of the user
 * @throws Exception
 */
	ArrayList<Seat> findAndHoldSeats(int numSeats , Integer minLevel , Integer maxLevel , String customerEmail) throws Exception;

/**
 * 
 * @param numSeats
 * @param customerEmail
 * @return true implies success and false implies reservation was unsuccessful
 */
	boolean reserveSeats(int numSeats, String customerEmail);
	
}

