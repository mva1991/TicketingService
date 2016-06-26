package com.reservation;

import java.util.LinkedList;

public class PruneOutBlockedSeats implements Runnable {

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
	
	/**
	 * 
	 * @param seat
	 * 
	 */
	/*
	 * 1. Increment the totalFreeSeats at in the corresponding level and row
	 * 2. Remove the seat from takenSeats map of the seat's corresponding row 
	 
	public static void unblockSeat(Seat seat){

		String levelName = seat.getLevel();
		LevelProfile lvlProf = TicketServiceApplication.venueSiteMap.get(levelName);
		int freeSeatsInTheLevel = lvlProf.getFreeSeats();
		Map<String,RowProfile> rowProfileMap = lvlProf.getRowProfileMap();
		String rowName = seat.getRow();
		RowProfile rowProfile = rowProfileMap.get(rowName);
		int freeSeatsInTheRow = rowProfile.getFreeSeats();
		Map<Integer, Seat> takenSeats = rowProfile.getTakenSeats();
		int unblockedSeatNumber = seat.getSeatNumber();		
		takenSeats.remove(unblockedSeatNumber);
		freeSeatsInTheLevel += 1;
		lvlProf.setFreeSeats(freeSeatsInTheLevel);
		freeSeatsInTheRow += 1;
		rowProfile.setFreeSeats(freeSeatsInTheRow);
		
	}*/

}
