package com.reservation;

import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

	public static TicketServiceApplication tsa = new TicketServiceApplication();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		start();
	}
	static Scanner scanner = new Scanner(System.in);
	public static void start(){
		
		System.out.println("-----------------------------------------------------------------");
		System.out.println("Welcome to the Ticket Reservation System.");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("\nTotal seats available in the venue: "+TicketServiceApplication.getTotalFreeSeatsInTheVenue());
		System.out.println("\nCurrent occupancy status is as follows:");
		System.out.println("---------------------------------------------------------------------------------");
		int i = 1;
		for(Entry<Integer, String> entry : TicketServiceApplication.levelNameMap.entrySet()){
			String levelName = entry.getValue();
			int levelNumber = entry.getKey();
			int freeSeatsInLevel = tsa.numSeatsAvaliable(levelNumber);
			LevelProfile lp = DataLoader.levelProfileMap.get(levelName);
			System.out.println("Level-Number: "+i+", Level-Name: "+levelName+", Seats-Available: "+freeSeatsInLevel+", Ticket-Cost $"+lp.getPricePerSeat()+".00");
			i++;
		}
		System.out.println("---------------------------------------------------------------------------------");		

		try {

			System.out.println("-----------------------------------------------------------------");
			System.out.println("\nEnter the following details ");
			
			System.out.println("\tEmail Address: ");
			String userEmail = scanner.next();
			
			System.out.println("\tNumber of seats you want to reserve: ");
			String data = scanner.next();
			Integer numSeats = Integer.parseInt(data);
			
			System.out.println("\tHighest Level-Number of preference: ");
			data = scanner.next();
			Integer maxLevel = Integer.parseInt(data);
			
			System.out.println("\tLowest Level-Number of preference: ");
			data = scanner.next();
			Integer minLevel = Integer.parseInt(data);
			

			
			System.out.println("-----------------------------------------------------------------");
			ArrayList<Seat> blockedSeatsArray;
			blockedSeatsArray = tsa.findAndHoldSeats(numSeats, minLevel, maxLevel, userEmail);
			System.out.println("\nThe following seats have been blocked on your behalf.");
			System.out.println("Please note that the block on these seats will be revoked in "+DataLoader.seatHoldDuration/1000+" seconds.\n");
			showBlockedSeats(blockedSeatsArray);
			System.out.println("\n-----------------------------------------------------------------");
			System.out.println("\nDo you want to reserve them? (yes/no)");
			String decision = scanner.next();
			if(decision.equals("yes")){
				boolean flag = tsa.reserveSeats(numSeats, userEmail);
				if(flag)
					System.out.println("\nCongratulations! Your reservation is successful. Thank you for booking with us.");
				else
					throw new Exception("\nOops, Could not complete the reservation. Reason: The seats that were blocked have been revoked as the blocking time has elapsed.  Please try again.");
				System.out.println("-----------------------------------------------------------------");
				start();
				
			}else if (decision.equals("no")){				
				DataLoader.blockedSeatsQueue = new LinkedList<Seat>();
				start();
					
			}else throw new Exception ("Decision can only be yes/no");

			scanner.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (e instanceof NumberFormatException){
				System.out.println("Oops, Could not complete the reservation. Reason: Incorrect input was entered. Please try again.");
			}
			else{
				
					System.out.println(e.getMessage());
			} 
			start();
		}


	}
	
	public static void showBlockedSeats(ArrayList<Seat> blockedSeatsArray){
		for(Seat seat : blockedSeatsArray){
			System.out.println("Level : "+seat.getLevel()+", Row : "+seat.getRow()+", Seat-Number : "+seat.getSeatNumber());
		}

	}
	


}
