package com.reservation;

import java.util.LinkedList;
/**
 * 
 * @author vinayabhishek
 *
 */
public class PruneOutBlockedSeats implements Runnable {
// This thread will clear the Queue after waiting for the defined -  seatHoldDuration 
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			if(DataLoader.blockedSeatsQueue.size() >0){								
				Thread.sleep(DataLoader.seatHoldDuration);							
				DataLoader.blockedSeatsQueue = new LinkedList<Seat>();			
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


}
