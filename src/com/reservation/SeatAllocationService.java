package com.reservation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


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
		 */
		Map<String,RowProfile> rowProfileMap = levelProfile.getRowProfileMap();
		for(Entry<String, RowProfile> entry : rowProfileMap.entrySet()){
			String rowName = entry.getKey();
			if(reservableSeats.size() == numSeats){
				break;
			}
			RowProfile rowProfile = entry.getValue();
			Integer[] arr =  rowProfile.getTakenSeats().keySet().toArray(new Integer[0]);
			ArrayList<Integer> reservedSeatNumbers = new ArrayList<Integer>(Arrays.asList(arr));
			int currentRowSize = rowProfile.getFreeSeats() + rowProfile.getTakenSeats().size();
			int availableSeatCount = entry.getValue().getFreeSeats();
			ArrayList<Seat> freeSeatsArray = new ArrayList<Seat>();
			if(availableSeatCount < numSeats)
				freeSeatsArray = getSeatsInARow(reservedSeatNumbers,availableSeatCount,currentRowSize,rowName,levelName,userEmail);
			else
				freeSeatsArray = getSeatsInARow(reservedSeatNumbers,numSeats,currentRowSize,rowName,levelName,userEmail);
			reservableSeats.addAll(freeSeatsArray);
			
		}
		
		/*
		 * Once the seats are obtained - Block them by adding them to a queue and reducing the totalFreeSeats count
		 * for the corresponding row and level.
		 */
		
		for(Seat reserveableSeat : reservableSeats){
			DataLoader.blockedSeatsQueue.add(reserveableSeat);
			/*
			 *  Decrementing the free seat count for the corresponding level by 1
			 */
			String seatLevel = reserveableSeat.getLevel();
			LevelProfile currentLevelProfile = TicketServiceApplication.venueSiteMap.get(seatLevel);
			int updatedFreeSeatCountInLevel = currentLevelProfile.getFreeSeats() - 1;
			currentLevelProfile.setFreeSeats(updatedFreeSeatCountInLevel);
			
			/*
			 * Decrementing the free seat count for the corresponding level by 1
			 */
			TreeMap<String,RowProfile> currentRowProfileMap = currentLevelProfile.getRowProfileMap();
			String seatRow = reserveableSeat.getRow();
			RowProfile currentRowProfile = currentRowProfileMap.get(seatRow);
			int updatedFreeCountInRow = currentRowProfile.getFreeSeats() - 1;
			currentRowProfile.setFreeSeats(updatedFreeCountInRow);
			
			/*
			 * Adding the free seat to the takenMap of that row.
			 */
			int currentSeatNumber = reserveableSeat.getSeatNumber();
			Map<Integer,Seat> takenSeatsMap = currentRowProfile.getTakenSeats();
			takenSeatsMap.put(currentSeatNumber,reserveableSeat);
			currentRowProfile.setTakenSeats(takenSeatsMap);
			
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
		/* Intent is to derive all seats in a continuous order. Case 3 below will not accommodate for this.
		 * This method has 3 conditions where free seats are checked for.
		 * 1. If the free seats are in between the any two reserved seats
		 * 2. If the free seats are at the beginning or ending of the row
		 * 3. If the free seats spawn through out the row in an unordered fashion
		 * The beginning of each condition is clearly commented below */
		
		/*
		 * Condition-1: If all free seats are in between the any two reserved seats
		 */
		int size = seatNumberList.size();
		
		if(size == 0){
			for (int i = 1; i <= numSeats; i++){
				Calendar endTime = Calendar.getInstance();
				endTime.add(Calendar.SECOND, DataLoader.ticketHoldTimeOut);
				Seat seat = encapsulateSeatInformation(userEmail, i, rowName, levelName, endTime);
				freeSeatArray.add(seat);
			}
			
			return freeSeatArray;
		}
		for(int i = 0; i< size; i++){			
			if(i < size -1){
				int neighborDifference = (seatNumberList.get(i+1) - seatNumberList.get(i))-1;
				if(neighborDifference >= numSeats){
					int j = 0;
					while(j <numSeats){
						int seatNum = seatNumberList.get(i)+j+1;
						seatNumberArray.add(seatNum);
						j++;
					}
					
						break;
				}
			}

		}
		/*
		 * Condition-2: If the free seats are at the beginning or ending of the row.
		 */
		if(seatNumberArray.size() == 0){

			int firstSeatNum = seatNumberList.get(0);
			int lastSeatNum = seatNumberList.get(size-1);

			if((firstSeatNum - 1) >= numSeats){
				//Seats are available at the beginning of the row.
				int minSeatNum = 1;
				int maxSeatNum = numSeats;
				for(int i = minSeatNum; i<=maxSeatNum; i++){
					seatNumberArray.add(i);
				}
			}else{
				// Seats are at the very end of the row.
				
				if(rowSize - lastSeatNum >= numSeats){
					int minSeatNum = lastSeatNum + 1;
					int maxSeatNum = minSeatNum + (numSeats-1);
					
					for(int i = minSeatNum; i<=maxSeatNum; i++){
						seatNumberArray.add(i);
					}
				}
			}
		}
		/*
		 * Condition-3 : If the free seats spawn through out the row in an unordered fashion
		 */
		if(seatNumberArray.size() == 0){
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
			Calendar endTime = Calendar.getInstance();
			endTime.add(Calendar.SECOND, DataLoader.ticketHoldTimeOut);
			Seat seat = encapsulateSeatInformation(userEmail, seatNumber, rowName, levelName, endTime);
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
	public static Seat encapsulateSeatInformation(String userEmail, Integer seatNumber, String row, String level, Calendar endTime){
		Seat seat = new Seat();
		seat.setUserEmail(userEmail);
		seat.setEndTime(endTime);
		seat.setLevel(level);
		seat.setRow(row);
		seat.setSeatNumber(seatNumber);
		return seat;
	}

	
	
	

}
