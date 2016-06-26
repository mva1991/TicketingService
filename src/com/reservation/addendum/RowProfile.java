package com.reservation.addendum;

import java.util.Map;
import java.util.TreeMap;

public class RowProfile {

	private Integer freeSeats;
	private Integer lastGrantedSeatNum;
	
	private Map<Integer, Seat> takenSeats = new TreeMap<Integer, Seat>();
	public Integer getFreeSeats() {
		return freeSeats;
	}

	public void setFreeSeats(Integer freeSeats) {
		this.freeSeats = freeSeats;
	}

	public Integer getLastGrantedSeatNum() {
		return lastGrantedSeatNum;
	}

	public void setLastGrantedSeatNum(Integer lastGrantedSeatNum) {
		this.lastGrantedSeatNum = lastGrantedSeatNum;
	}

	public Map<Integer, Seat> getTakenSeats() {
		return takenSeats;
	}

	public void setTakenSeats(Map<Integer, Seat> takenSeats) {
		this.takenSeats = takenSeats;
	}

	

	
}
