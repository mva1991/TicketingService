package com.reservation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class TicketServiceApplication implements TicketService {

	private static Map<Integer , String> levelNameMap = new HashMap<Integer , String>();
	
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
            
		}
		return freeSeats;
	}

	@Override
	public SeatHold findAndHoldSeats(int numSeats, Integer minLevel,
			Integer maxLevel, String customerEmail) {
		// TODO Auto-generated method stub
		Integer totalFree = getTotalFreeSeatsInTheVenue();
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
			
			ArrayList<Seat> blockedSeatsArray = new ArrayList<Seat>();
			/**
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
			/**
			 * If all the seats could not be allocated in the same level - Sparsely allocate the seats in different levels
			 * Allocate all seats in a given level and move to the next level till the total number of seats required are full.
			 */
			
			for(int i = minLevel; i <=maxLevel; i++){
				if(numSeats == blockedSeatsArray.size()) break;
				String levelName = levelNameMap.get(i);
				LevelProfile levelProfile = venueSiteMap.get(levelName);
				int seatsInLevel = levelProfile.getFreeSeats();
				SeatAllocationService sas = new SeatAllocationService();
				ArrayList<Seat> tempreservableSeats = sas.seatAllocator(levelProfile, seatsInLevel, customerEmail, levelName);
				blockedSeatsArray.addAll(tempreservableSeats);

				
			}
			
			/**
			 * 
			 */
		}
		
		return null;
	}

	@Override
	public String reserveSeats(Seat seat, String customerEmail) {
		// TODO Auto-generated method stub
		/**
		 *  This part only for testing
		 */
		Seat mySeat = new Seat();
		mySeat.setSeatNumber(1);
		mySeat.setRow("A");
		mySeat.setLevel("Orchestra");
		mySeat.setUserEmail("mva1991@gmail.com");
		mySeat.setEndTime(new Date());
		
		Map<String,RowProfile> rowProfileMap = new TreeMap<String, RowProfile>();
		LevelProfile lp =  venueSiteMap.get("Orchestra");
		rowProfileMap = lp.getRowProfileMap();
		RowProfile rp = rowProfileMap.get("A");
		Integer freeSeats = rp.getFreeSeats();
		freeSeats = freeSeats - 1; 
		Map<Integer,Seat> seatMap = rp.getTakenSeats();
		seatMap.put(1,mySeat);
		rp.setFreeSeats(freeSeats);
		lp.setFreeSeats(lp.getFreeSeats() - 1);		
		return "Seat Reserved";
	}
	
	public Integer getTotalFreeSeatsInTheVenue(){
		Integer totalSeats = venueSiteMap.get("Orchestra").getFreeSeats()+
		 venueSiteMap.get("Main").getFreeSeats()+
		 venueSiteMap.get("Balcony 1").getFreeSeats()+
		 venueSiteMap.get("Balcony 2").getFreeSeats();
		return totalSeats;
		
	}


}
