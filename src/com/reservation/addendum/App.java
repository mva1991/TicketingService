package com.reservation.addendum;

import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

	public static TicketServiceApplication tsa = new TicketServiceApplication();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		start();
	}
	
	public static void start(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("-----------------------------------------------------------------");
		System.out.println("Welcome to the Ticket Reservation System.");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("\nTotal Seats Available: "+tsa.getTotalFreeSeatsInTheVenue());
		System.out.println("\nCurrent occupancy status is as follows:\n");
		int i = 1;
		for(Entry<Integer, String> entry : TicketServiceApplication.levelNameMap.entrySet()){
			String levelName = entry.getValue();
			int levelNumber = entry.getKey();
			int freeSeatsInLevel = tsa.numSeatsAvaliable(levelNumber);
			LevelProfile lp = DataLoader.levelProfileMap.get(levelName);
			System.out.println("Level-Number: "+i+", Level-Name: "+levelName+", Seats-Available: "+freeSeatsInLevel+", Ticket-Cost $"+lp.getPricePerSeat()+".00");
			i++;
		}
		
		System.out.println("-----------------------------------------------------------------");
		System.out.println("\nEnter the following details ");
		
		System.out.println("\tEmail Address: ");
		String userEmail = scanner.next();
		
		System.out.println("\tNumber of seats you want to reserve: ");
		Integer numSeats = scanner.nextInt();
		
		System.out.println("\tHighest level number of preference: ");
		int maxLevel = scanner.nextInt();
		
		System.out.println("\tLowest level number of preference: ");
		int minLevel = scanner.nextInt();
		
		System.out.println("-----------------------------------------------------------------");
		
		System.out.println("\nThe following seats have been blocked on your behalf.");
		System.out.println("Please note that the block on these seats will be revoked in "+DataLoader.ticketHoldTimeOut+" seconds.\n");
		ArrayList<Seat> blockedSeatsArray = tsa.findAndHoldSeats(numSeats, minLevel, maxLevel, userEmail);
		showBlockedSeats(blockedSeatsArray);
		System.out.println("\n-----------------------------------------------------------------");
		System.out.println("\nDo you want to reserve them?");
		String decision = scanner.next();
		if(decision.equals("yes")){
			boolean flag = tsa.reserveSeats(numSeats, userEmail);
			if(flag)
				System.out.println("\nReservation Successful. Thank you for booking with us.");
			else
				System.out.println("\nReservation Unsuccessful. Reason: The seats that were blocked have been revoked as the blocking time has elapsed.  Please try again.");
			System.out.println("-----------------------------------------------------------------");
			start();
			
		}else if (decision.equals("no")){
			for(Seat seat : blockedSeatsArray){
				DataLoader.blockedSeatsQueue.poll();
				TicketServiceApplication.unblockSeat(seat);
			}
			start();
				
		}
		scanner.close();
	}
	
	public static void showBlockedSeats(ArrayList<Seat> blockedSeatsArray){
		for(Seat seat : blockedSeatsArray){
			System.out.println("Level : "+seat.getLevel()+", Row : "+seat.getRow()+", Seat-Number : "+seat.getSeatNumber()+", Block Elapse Time : "+seat.getEndTime().getTime());
		}

	}

}
