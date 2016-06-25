package com.reservation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TicketServiceApplication implements TicketService {

	private static Map<Integer , String> levelNameMap = new HashMap<Integer , String>();
	
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
		blockedSeatPruner();
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
	public  void reserveSeats(int numSeats, String customerEmail) {
		// TODO Auto-generated method stub
		/*
		 *  Once the seats are opted for reservation, They can be removed from the 
		 *  blockedSeatsQueue
		 */
		for(int i = 1; i<= numSeats; i++){
			 DataLoader.blockedSeatsQueue.poll();			
		}
		
	}
		 
	private static void blockedSeatPruner(){
		/*
		 * The runnable thread in the blockedSeatPruner performs the following tasks if the 
		 * blockedSeatsQueue is not empty.
		 * 1. Peak the Queue and get the first seat in the queue
		 * 2. If that seat's end time equals the current time or if its greater than the current time
		 * 3. Remove the seat from the queue
		 */		
		final Runnable runnableThread = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(DataLoader.blockedSeatsQueue.size() >0){
					Seat seat = DataLoader.blockedSeatsQueue.peek();
					Calendar currentCal = Calendar.getInstance();
					Calendar seatCal = seat.getEndTime();
					if(seatCal.equals(currentCal) || seatCal.after(currentCal)){
						DataLoader.blockedSeatsQueue.poll();
						unblockSeat(seat);
					}					
				}
			
			}
			
		};
		
	ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
	sched.scheduleAtFixedRate(runnableThread, 0, 5, TimeUnit.SECONDS);
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
		freeSeatsInTheLevel += 1;
		lvlProf.setFreeSeats(freeSeatsInTheLevel);
		Map<String,RowProfile> rowProfileMap = lvlProf.getRowProfileMap();
		String rowName = seat.getRow();
		RowProfile rowProfile = rowProfileMap.get(rowName);
		int freeSeatsInTheRow = rowProfile.getFreeSeats();
		freeSeatsInTheRow += 1;
		rowProfile.setFreeSeats(freeSeatsInTheRow);
		Map<Integer, Seat> takenSeats = rowProfile.getTakenSeats();
		int unblockedSeatNumber = seat.getSeatNumber();
		takenSeats.remove(unblockedSeatNumber);
	}
	
	

}
