package com.reservation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TicketServiceApplication implements TicketService {

	public static Map<Integer , String> levelNameMap = new HashMap<Integer , String>(); // used to map each level to a unique integer
	
	/*
	 * This static block builds the venue based on the #rows and #seats for every level.
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
		/*
		 * As the name of the method goes - it returns the number of seats available in a given level.
		 * if venueLevel is null, the method return the count of free seats in the whole venue.
		 */
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
			Integer maxLevel, String customerEmail) throws Exception {
		// TODO Auto-generated method stub
		

		Integer totalFree = getTotalFreeSeatsInTheVenue();
		ArrayList<Seat> blockedSeatsArray = new ArrayList<Seat>();
		if(totalFree < numSeats){
			throw new Exception("Cannot reserve "+numSeats+" seat(s) as it exceeds the number of available seats("+totalFree+"). Please try again");
			
		}
		else{
			
			validateUserData(customerEmail, numSeats, minLevel, maxLevel); // validate the user input.
			
			Integer temp;
			if(minLevel > maxLevel){
				temp = minLevel;
				minLevel = maxLevel;
				maxLevel = temp;
			}
			
			/*
			 * Find the best available seats starting from the chosen level that is closest to the stage.
			 * If all the seats cannot not be allocated in the same level - Sparsely allocate the seats in different levels starting from min level to max level.
			 * Allocate all available seats in a given level and move to the next level till the total number of seats required are blocked.
			 */
			int tempNumSeats = numSeats;
			if(numSeats != blockedSeatsArray.size()){
				checkFreeSeatsInGivenRange(minLevel, maxLevel,numSeats);
				for(int i = minLevel; i <=maxLevel; i++){
					if(numSeats == blockedSeatsArray.size()) break;
					String levelName = levelNameMap.get(i);
					LevelProfile levelProfile = venueSiteMap.get(levelName);
					int seatsInLevel = levelProfile.getFreeSeats();
					SeatAllocationService sas = new SeatAllocationService();
					ArrayList<Seat> tempreservableSeats = new ArrayList<Seat>();
					if(seatsInLevel < tempNumSeats){
						tempreservableSeats = sas.seatAllocator(levelProfile, seatsInLevel, customerEmail, levelName);
						tempNumSeats = tempNumSeats - seatsInLevel;
					}else tempreservableSeats = sas.seatAllocator(levelProfile, tempNumSeats, customerEmail, levelName);
					
					blockedSeatsArray.addAll(tempreservableSeats);}
			}
			
			/*
			 *The prunerThread reinitializes the DataLoader.blockedSeatsQueue once the seatHoldDuration is elapsed to revoke the hold on the blocked seats. 
			 */
			Thread prunerThread = new Thread(new PruneOutBlockedSeats());
			prunerThread.start();
			

		}
		
		return blockedSeatsArray;
	}

	public static Integer getTotalFreeSeatsInTheVenue(){
		Integer totalSeats = venueSiteMap.get("Orchestra").getFreeSeats()+
		 venueSiteMap.get("Main").getFreeSeats()+
		 venueSiteMap.get("Balcony 1").getFreeSeats()+
		 venueSiteMap.get("Balcony 2").getFreeSeats();
		return totalSeats;
		
	}
	
	@Override
	public  boolean reserveSeats(int numSeats, String customerEmail) {
		// TODO Auto-generated method stub

		boolean flag = false;
		if(DataLoader.blockedSeatsQueue.size() == numSeats){
			for(int i = 1; i<= numSeats; i++){
				/*
				 *  Once the seats are opted for reservation, They can be removed from the blockedSeatsQueue.
				 */
				 Seat reservableSeat = DataLoader.blockedSeatsQueue.poll();	
					/*
					 *  Decrementing the free seat count for the corresponding level by 1
					 */
					String seatLevel = reservableSeat.getLevel();
					LevelProfile currentLevelProfile = TicketServiceApplication.venueSiteMap.get(seatLevel);
					int updatedFreeSeatCountInLevel = currentLevelProfile.getFreeSeats() - 1;
					currentLevelProfile.setFreeSeats(updatedFreeSeatCountInLevel);
					
					/*
					 * Decrementing the free seat count for the corresponding row by 1
					 */
					TreeMap<String,RowProfile> currentRowProfileMap = currentLevelProfile.getRowProfileMap();
					String seatRow = reservableSeat.getRow();
					RowProfile currentRowProfile = currentRowProfileMap.get(seatRow);
					int updatedFreeCountInRow = currentRowProfile.getFreeSeats() - 1;
					currentRowProfile.setFreeSeats(updatedFreeCountInRow);
					
					/*
					 * Adding the Seat seat to the takenMap<Integer, Seat> of that row.
					 */
					int currentSeatNumber = reservableSeat.getSeatNumber();
					Map<Integer,Seat> takenSeatsMap = currentRowProfile.getTakenSeats();
					takenSeatsMap.put(currentSeatNumber,reservableSeat);
					currentRowProfile.setTakenSeats(takenSeatsMap);
			}
			flag = true; // onSuccess return true;
		}
		return flag;

		
	}
	
	/**
	 * 
	 * @param customerEmail
	 * @param numSeats
	 * @param minLevel
	 * @param maxLevel
	 * @throws Exception
	 */
	private void validateUserData(String customerEmail, int numSeats, int minLevel, int maxLevel) throws Exception{
		/*
		 * Validate all the input parameters and throw Exceptions for erroneous data.
		 */
		Pattern validationPattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"); // RegExp for Email				
		Matcher validationMatcher = validationPattern.matcher(customerEmail);
		if(!validationMatcher.matches()){
		
			throw new Exception("Please enter a valid email address");
		}		
		if(numSeats < 1 || numSeats > TicketServiceApplication.getTotalFreeSeatsInTheVenue()){
		
			throw new Exception("Number of seats to be reserved is not valid");
		}

		if(maxLevel <1 || maxLevel >4){			
			throw new Exception("Please re-enter a valid level number. Min:1 Max:4");
		}			
		if(minLevel <1 || minLevel >4){
	
			throw new Exception("Please re-enter a valid level number. Min:1 Max:4");
		}	
}
	/**
	 * 
	 * @param minLevel
	 * @param maxLevel
	 * @param numSeats
	 * @throws Exception
	 */
	private void checkFreeSeatsInGivenRange(int minLevel,int maxLevel, int numSeats) throws Exception{
		/*
		 *For the given minLevel, maxLevel and numSeats required - This method checks if the numSeats can be accommodated in the given range of levels or not. 
		 */
		int freeSeatCount = 0;
		for(int i = minLevel; i <=maxLevel; i++){
			
			String levelName = levelNameMap.get(i);
			LevelProfile levelProfile = venueSiteMap.get(levelName);
			int seatsInLevel = levelProfile.getFreeSeats();
			freeSeatCount += seatsInLevel;
		}
		
		if(freeSeatCount < numSeats){
			throw new Exception("Cannot find "+numSeats+" free seats in the preferred levels. Please change your min-max levels of preference.");
		}
		
	}
	

	
	

}
