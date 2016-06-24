package com.reservation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

public class DataLoader {

	public static Map<String, LevelProfile> levelProfileMap = new HashMap<String , LevelProfile>();
	public static Queue<Seat> blockedSeatsQueue = new LinkedList<Seat>();

	
	private static String[] alpha = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	public static void buildVenue()
	{
		LevelProfile orchProfile = new LevelProfile();
		orchProfile.setFreeSeats(1250);
		
		TreeMap<String , RowProfile> rowProfileMap = new TreeMap<String , RowProfile>();
		for(int i=0 ; i<25 ; i++)
		{
		String rowName = alpha[i];
		RowProfile rowProfile = new RowProfile();
		rowProfile.setFreeSeats(50);
		rowProfileMap.put(rowName, rowProfile);
		}
		
		orchProfile.setRowProfileMap(rowProfileMap);
		levelProfileMap.put("Orchestra" , orchProfile);
		
		
		
		LevelProfile mainProfile = new LevelProfile();
		mainProfile.setFreeSeats(2000);
	    	rowProfileMap = new TreeMap<String , RowProfile>();
		for (int i = 0; i < 20; i++) {
			String rowName = alpha[i];
			RowProfile rowProfile = new RowProfile();
			rowProfile.setFreeSeats(100);
			rowProfileMap.put(rowName, rowProfile);
		}
		
		mainProfile.setRowProfileMap(rowProfileMap);
		levelProfileMap.put("Main" , mainProfile);
		
		
		LevelProfile bal1Profile = new LevelProfile();
		bal1Profile.setFreeSeats(1500);
		
		rowProfileMap = new TreeMap<String , RowProfile>();
		for (int i = 0; i < 15; i++){
			String rowName = alpha[i];
			RowProfile rowProfile = new RowProfile();
			rowProfile.setFreeSeats(100);
			rowProfileMap.put(rowName, rowProfile);
		}
		
		bal1Profile.setRowProfileMap(rowProfileMap);
		levelProfileMap.put("Balcony 1" , bal1Profile);
		
		
		LevelProfile bal2Profile = new LevelProfile();
		bal2Profile.setFreeSeats(1500);
		
		rowProfileMap = new TreeMap<String , RowProfile>();
		for (int i = 0; i < 15; i++) {
			String rowName = alpha[i];
			RowProfile rowProfile = new RowProfile();
			rowProfile.setFreeSeats(100);
			rowProfileMap.put(rowName, rowProfile);
		}
		
		bal2Profile.setRowProfileMap(rowProfileMap);
		levelProfileMap.put("Balcony 2" , bal2Profile);
		
		
		
	}
	
}
