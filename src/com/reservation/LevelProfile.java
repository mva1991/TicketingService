package com.reservation;

import java.util.TreeMap;

public class LevelProfile {

	private Integer freeSeats;
	private TreeMap<String, RowProfile> rowProfileMap;
	public Integer getFreeSeats() {
		return freeSeats;
	}
	public void setFreeSeats(Integer freeSeats) {
		this.freeSeats = freeSeats;
	}
	public TreeMap<String, RowProfile> getRowProfileMap() {
		return rowProfileMap;
	}
	public void setRowProfileMap(TreeMap<String, RowProfile> rowProfileMap) {
		this.rowProfileMap = rowProfileMap;
	}
	
}
