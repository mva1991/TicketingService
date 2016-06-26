package com.reservation.addendum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TicketServiceApplication implements TicketService {

	public static Map<Integer , String> levelNameMap = new HashMap<Integer , String>();
	
	/*
	 * This static block will perform the following tasks:
	 * 1. Build the venue based on the #rows and #seats for every level.
	 * 2. Invoke blockedSeatPruner thread that executes once in every five seconds to free up the blocked seats
	 */
	static 
	{   		
		DataLoader.buildVenue();
		levelNameMap.put(1, "Orchestra");
		levelNameMap.put(2, "Main");
		levelNameMap.put(3, "Balcony 1");
		levelNameMap.put(4, "Balcony 2");	
		
	}
	
	static Map<String,LevelProfile> venueSiteMap = DataLoader.levelProfileMap;
	
	@Override
	public int numSeatsAvaliable(Integer venueLevel) {
		
		// TODO Auto-generated method stub
		Integer freeSeats = null;
		if(venueLevel !=null){
			String levelName = levelNameMap.get(venueLevel);
            LevelProfile lp = DataLoader.levelProfileMap.get(levelName);
            freeSeats = lp.getFreeSeats();
            
		} else freeSeats = getTotalFreeSeatsInTheVenue();
		return freeSeats;
	}

	@Override
	public ArrayList<Seat> findAndHoldSeats(int numSeats, Integer minLevel,
			Integer maxLevel, String customerEmail) {
		// TODO Auto-generated method stub
		Integer totalFree = getTotalFreeSeatsInTheVenue();
		ArrayList<Seat> blockedSeatsArray = new ArrayList<Seat>();
		if(totalFree < numSeats){
			System.out.println("Cannot reserve "+numSeats+" as the hall is full. Only "+totalFree+" seats are available");
			return null;
		}else{
			Integer temp;
			if(minLevel > maxLevel){
				temp = minLevel;
				minLevel = maxLevel;
				maxLevel = temp;
			}
			
			
			/*
			 * Allocate all the seats in the same level if possible.
			 */
			for (int i = minLevel; i<=maxLevel; i++){				
				if(numSeats == blockedSeatsArray.size()) break;
				String levelName = levelNameMap.get(i);
				if(venueSiteMap.get(levelName).getFreeSeats() > numSeats){
					SeatAllocationService sas = new SeatAllocationService();
					LevelProfile levelProfile = venueSiteMap.get(levelName);
					blockedSeatsArray = sas.seatAllocator(levelProfile, numSeats, customerEmail, levelName);

				}				
			}
			/*
			 * If all the seats could not be allocated in the same level - Sparsely allocate the seats in different levels
			 * Allocate all seats in a given level and move to the next level till the total number of seats required are blocked.
			 */
			if(numSeats != blockedSeatsArray.size()){
				for(int i = minLevel; i <=maxLevel; i++){
					if(numSeats == blockedSeatsArray.size()) break;
					String levelName = levelNameMap.get(i);
					LevelProfile levelProfile = venueSiteMap.get(levelName);
					int seatsInLevel = levelProfile.getFreeSeats();
					SeatAllocationService sas = new SeatAllocationService();
					ArrayList<Seat> tempreservableSeats = sas.seatAllocator(levelProfile, seatsInLevel, customerEmail, levelName);
					blockedSeatsArray.addAll(tempreservableSeats);}
			}

			

		}
		
		return blockedSeatsArray;
	}

	public Integer getTotalFreeSeatsInTheVenue(){
		Integer totalSeats = venueSiteMap.get("Orchestra").getFreeSeats()+
		 venueSiteMap.get("Main").getFreeSeats()+
		 venueSiteMap.get("Balcony 1").getFreeSeats()+
		 venueSiteMap.get("Balcony 2").getFreeSeats();
		return totalSeats;
		
	}
	
	@Override
	public  boolean reserveSeats(int numSeats, String customerEmail) {
		// TODO Auto-generated method stub
		/*
		 *  Once the seats are opted for reservation, They can be removed from the 
		 *  blockedSeatsQueue, Set the status attribute in the seat to Reserved.
		 */
		boolean flag = false;
		if(DataLoader.blockedSeatsQueue.size() >= numSeats){
			for(int i = 1; i<= numSeats; i++){
				 Seat seat = DataLoader.blockedSeatsQueue.poll();	
				 seat.setStatus("Reserved");
				 String levelName = seat.getLevel();
				 LevelProfile lpf = DataLoader.levelProfileMap.get(levelName);
				 Map<String,RowProfile> rowProfileMap = lpf.getRowProfileMap();
				 String rowName = seat.getRow();
				 RowProfile rowProfile = rowProfileMap.get(rowName);
				 int seatNumber = seat.getSeatNumber();
				 Map<Integer, Seat> takenSeats = rowProfile.getTakenSeats();
				 takenSeats.put(seatNumber,seat);
			}
			flag = true;
		}
		return flag;

		
	}
		 

	
	/**
	 * 
	 * @param seat
	 * 
	 */
	public static void unblockSeat(Seat seat){
		/*
		 * 1. Increment the totalFreeSeats at in the corresponding level and row
		 * 2. Remove the seat from takenSeats map of the seat's corresponding row 
		 */
		String levelName = seat.getLevel();
		LevelProfile lvlProf = venueSiteMap.get(levelName);
		int freeSeatsInTheLevel = lvlProf.getFreeSeats();
		Map<String,RowProfile> rowProfileMap = lvlProf.getRowProfileMap();
		String rowName = seat.getRow();
		RowProfile rowProfile = rowProfileMap.get(rowName);
		int freeSeatsInTheRow = rowProfile.getFreeSeats();
		Map<Integer, Seat> takenSeats = rowProfile.getTakenSeats();
		int unblockedSeatNumber = seat.getSeatNumber();
		
		
		Seat ubSeat = takenSeats.get(unblockedSeatNumber);
		if(ubSeat.getStatus().equals("Blocked")){
			takenSeats.remove(unblockedSeatNumber);
			freeSeatsInTheLevel += 1;
			lvlProf.setFreeSeats(freeSeatsInTheLevel);
			freeSeatsInTheRow += 1;
			rowProfile.setFreeSeats(freeSeatsInTheRow);
		}
	}
	
	

}
