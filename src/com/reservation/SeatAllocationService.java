package com.reservation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;


/**
 * 
 * @author vinayabhishek
 *
 */
public class SeatAllocationService {

/**
 * 	
 * @param levelProfile
 * @param numSeats
 * @param userEmail
 * @param levelName
 * @return list of blocked seats
 */
	public ArrayList<Seat> seatAllocator(LevelProfile levelProfile,int numSeats, String userEmail, String levelName){
		ArrayList<Seat> reservableSeats = new ArrayList<Seat>();
		/*
		 * Scan for every row in a given level and return the maximum number of seats ( not more than the required)
		 * that can be reserved on a per row basis.
		 * Aggregate all the seats into a returnable ArrayList- reservableSeats.
		 * If reservableSeats.size() match the numSeats break the loop and return reservableSeats
		 * Once the reservableSeats are obtained - Block them on behalf of the user by adding them to a queue.
		 */
		int tempNumSeats = numSeats;		
		Map<String,RowProfile> rowProfileMap = levelProfile.getRowProfileMap();

		for(Entry<String, RowProfile> entry : rowProfileMap.entrySet()){
			if(reservableSeats.size() == numSeats){
				break;
			}
			String rowName = entry.getKey();
			RowProfile rowProfile = entry.getValue();
			Integer[] arr =  rowProfile.getTakenSeats().keySet().toArray(new Integer[0]);
			ArrayList<Integer> reservedSeatNumbers = new ArrayList<Integer>(Arrays.asList(arr));
			int currentRowSize = rowProfile.getFreeSeats() + rowProfile.getTakenSeats().size();
			int availableSeatCount = entry.getValue().getFreeSeats();
			ArrayList<Seat> freeSeatsArray = new ArrayList<Seat>();
			if(availableSeatCount < tempNumSeats){
				freeSeatsArray = getSeatsInARow(reservedSeatNumbers,availableSeatCount,currentRowSize,rowName,levelName,userEmail);
				tempNumSeats = tempNumSeats - availableSeatCount;
			} else freeSeatsArray = getSeatsInARow(reservedSeatNumbers,tempNumSeats,currentRowSize,rowName,levelName,userEmail);
									
			reservableSeats.addAll(freeSeatsArray);
				
		}
		
		
		for(Seat reserveableSeat : reservableSeats){
			DataLoader.blockedSeatsQueue.add(reserveableSeat);		
			
		}
		
		return reservableSeats;
		
	}
	
	
	
/**
 * 
 * @param seatNumberList
 * @param numSeats
 * @param rowSize
 * @param rowName
 * @param levelName
 * @param userEmail
 * @return list of free seats
 */
	
	public static ArrayList<Seat> getSeatsInARow(ArrayList<Integer> seatNumberList, int numSeats, int rowSize, String rowName,String levelName, String userEmail){
		ArrayList<Integer> seatNumberArray = new ArrayList<Integer>();
		ArrayList<Seat> freeSeatArray = new ArrayList<Seat>();
		/* Intent is to derive all seats in a continuous order. */
		
		int size = seatNumberList.size();
		/*
		 * If the seatNumberList is empty - Start allocating seats from seat no 1
		 */
		if(size == 0){
			for (int i = 1; i <= numSeats; i++){
				Seat seat = encapsulateSeatInformation(userEmail, i, rowName, levelName);
				freeSeatArray.add(seat);
			}			
			return freeSeatArray;
		}else{			
				for(int i = 1; i <= rowSize;  i++){
					if(seatNumberArray.size() != numSeats){					
						if(!seatNumberList.contains(i)){						
							seatNumberArray.add(i);
						}
					}else break;
				}		
		}
		
		/*
		 * Generating an ArrayList<Seat> with all the information - We return this ArrayList from this method
		 */		
		for(Integer seatNumber : seatNumberArray){
			Seat seat = encapsulateSeatInformation(userEmail, seatNumber, rowName, levelName);
			freeSeatArray.add(seat);
		}		
		return freeSeatArray;
	}
	
	/**
	 * 
	 * @param userEmail
	 * @param seatNumber
	 * @param row
	 * @param level
	 * @param endTime
	 * @return Seat
	 */
	public static Seat encapsulateSeatInformation(String userEmail, Integer seatNumber, String row, String level){
		Seat seat = new Seat();
		seat.setUserEmail(userEmail);
		seat.setLevel(level);
		seat.setRow(row);
		seat.setSeatNumber(seatNumber);		
		return seat;
	}

	
	
	

}
