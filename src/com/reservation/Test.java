package com.reservation;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

		Map <Integer, Integer> map = new TreeMap<Integer, Integer>();
		map.put(1,1);
		map.put(2, 2);
		Integer[] arr =  map.keySet().toArray(new Integer[0]);
		System.out.println(arr.length);
		

	}
	

	
	public static ArrayList<Integer> getContiguousSeats(ArrayList<Integer> seatNumberList, int numSeats, int rowSize){
		ArrayList<Integer> range = new ArrayList<Integer>();
		/**
		 * This check does not take care if the seats are there to the very beginning or very end of the row 
		 * Handler for numSeats = 1 need not do all this right? code some thing for that
		 */
		int size = seatNumberList.size();
		for(int i = 0; i< size; i++){
			// if num seats are available in between seats
			
			if(i < size -1){
				int neighborDifference = (seatNumberList.get(i+1) - seatNumberList.get(i))-1;
				//System.out.println(neighborDifference+" diff");
				if(neighborDifference >= numSeats){
					int j = 0;
					while(j <numSeats){
						int seatNum = seatNumberList.get(i)+j+1;
						range.add(seatNum);
						j++;
					}
					
						break;
				}
			}

		}
		/**
		 * If range is not found out try to find the seats at the beginning/end of the row.
		 */
		if(range.size() == 0){
			//System.out.println("came");
			int firstSeatNum = seatNumberList.get(0);
			int lastSeatNum = seatNumberList.get(size-1);
			//checking at the beginning of the array
			if((firstSeatNum - 1) >= numSeats){
				//Seats are available at the beginning of the row.
				int minSeatNum = 1;
				int maxSeatNum = numSeats;
				for(int i = minSeatNum; i<=maxSeatNum; i++){
					range.add(i);
				}
			}else{
				// Seats are at the very end of the row.
				
				if(rowSize - lastSeatNum >= numSeats){
					int minSeatNum = lastSeatNum + 1;
					int maxSeatNum = minSeatNum + (numSeats-1);
					
					for(int i = minSeatNum; i<=maxSeatNum; i++){
						range.add(i);
					}
				}
			}
		}
		
		if(range.size() == 0){
			for(int i = 1; i <= rowSize;  i++){
				if(range.size() != numSeats){					
					if(!seatNumberList.contains(i)){						
						range.add(i);
					}
				}else break;
			}
		}
		
		return range;
	}
	

}


