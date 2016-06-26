package com.reservation.addendum;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.TreeMap;
/**
 * 
 * @author vinayabhishek
 *
 */
public class DataLoader {

	public static Map<String, LevelProfile> levelProfileMap = new HashMap<String , LevelProfile>();
	public static Queue<Seat> blockedSeatsQueue = new LinkedList<Seat>();
	public static int ticketHoldTimeOut = 25;
	
	private static String[] alpha = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

	public static void buildVenue()
	{
		/*
		 * The initial configuration (#Rows per level, #Seats per level, #price per seat) is taken from a properties file - venue.properties.
		 * This is to make it easier to change the #Seats,#Rows,Price/Seat with out rebuilding the source code.
		 * buildVenue() will extract the information from the properties file and sets up the following:
		 * 1. LevelProfile per level -> Number of free seats in every level - At the time of initialization free seats = total seats
		 * 2. RowProfile is an enumeration of freeSeats in the row and takenSeats Map.
		 * 3. rowProfileMap for each level -> size of the map = number of rows.
		 */
		try {
			
			Properties venueProperties = new Properties();
			InputStream input = new FileInputStream("venue.properties");
			venueProperties.load(input);
			
			int orchRows = getInt((venueProperties.get("orchestraRows").toString()));
			int orchRowSeats = getInt((venueProperties.get("orchestraRowSeats").toString()));
			int orchSeatPrice = getInt((venueProperties.get("orchestraSeatPrice").toString()));
			int orchSeats = orchRows * orchRowSeats;
			
			LevelProfile orchProfile = new LevelProfile();
			orchProfile.setFreeSeats(orchSeats);			
			TreeMap<String , RowProfile> rowProfileMap = new TreeMap<String , RowProfile>();			
			for(int i=0 ; i<orchRows ; i++)
			{
			String rowName = alpha[i];
			RowProfile rowProfile = new RowProfile();
			rowProfile.setFreeSeats(orchRowSeats);
			rowProfileMap.put(rowName, rowProfile);
			}
			orchProfile.setPricePerSeat(orchSeatPrice);
			orchProfile.setRowProfileMap(rowProfileMap);
			levelProfileMap.put("Orchestra" , orchProfile);
			
			
			int mainRows = getInt((venueProperties.get("mainRows").toString()));
			int mainRowSeats = getInt((venueProperties.get("mainRowSeats").toString()));
			int mainSeatPrice = getInt((venueProperties.get("mainSeatPrice").toString()));
			int mainSeats = mainRows * mainRowSeats;
			
			LevelProfile mainProfile = new LevelProfile();
			mainProfile.setFreeSeats(mainSeats);
			rowProfileMap = new TreeMap<String , RowProfile>();
			for (int i = 0; i < mainRows; i++) {
				String rowName = alpha[i];
				RowProfile rowProfile = new RowProfile();
				rowProfile.setFreeSeats(mainRowSeats);
				rowProfileMap.put(rowName, rowProfile);
			}
			mainProfile.setPricePerSeat(mainSeatPrice);
			mainProfile.setRowProfileMap(rowProfileMap);
			levelProfileMap.put("Main" , mainProfile);
			
			int balcony1Rows = getInt((venueProperties.get("balcony1Rows").toString()));
			int balcony1RowSeats = getInt((venueProperties.get("balcony1RowSeats").toString()));
			int balcony1SeatPrice = getInt((venueProperties.get("balcony1SeatPrice").toString()));
			int balcony1Seats = balcony1Rows * balcony1RowSeats;
			
			LevelProfile bal1Profile = new LevelProfile();
			bal1Profile.setFreeSeats(balcony1Seats);			
			rowProfileMap = new TreeMap<String , RowProfile>();
			for (int i = 0; i < balcony1Rows; i++){
				String rowName = alpha[i];
				RowProfile rowProfile = new RowProfile();
				rowProfile.setFreeSeats(balcony1RowSeats);
				rowProfileMap.put(rowName, rowProfile);
			}
			bal1Profile.setPricePerSeat(balcony1SeatPrice);
			bal1Profile.setRowProfileMap(rowProfileMap);
			levelProfileMap.put("Balcony 1" , bal1Profile);
			
			int balcony2Rows = getInt((venueProperties.get("balcony2Rows").toString()));
			int balcony2RowSeats = getInt((venueProperties.get("balcony2RowSeats").toString()));
			int balcony2SeatPrice = getInt((venueProperties.get("balcony2SeatPrice").toString()));
			int balcony2Seats = balcony2Rows * balcony2RowSeats;

			LevelProfile bal2Profile = new LevelProfile();
			bal2Profile.setFreeSeats(balcony2Seats);
			
			rowProfileMap = new TreeMap<String , RowProfile>();
			for (int i = 0; i < balcony2Rows; i++) {
				String rowName = alpha[i];
				RowProfile rowProfile = new RowProfile();
				rowProfile.setFreeSeats(balcony2RowSeats);
				rowProfileMap.put(rowName, rowProfile);
			}
			bal2Profile.setPricePerSeat(balcony2SeatPrice);
			bal2Profile.setRowProfileMap(rowProfileMap);
			levelProfileMap.put("Balcony 2" , bal2Profile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public static Integer getInt(String s){
		Integer number = null;
		try {
			number = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			System.out.println("Input could not correspond to a number"+e.getMessage());
		}
		return number;
	} 
	
}
