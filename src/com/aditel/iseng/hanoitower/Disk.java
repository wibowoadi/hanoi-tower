package com.aditel.iseng.hanoitower;

public class Disk {
	private Integer id;
	private Double diskSize;
	private Integer currentPosittion;
	private Disk[] allAboveDisks;

	public Disk(Integer id, Double diskSize, Integer currentPosittion) {
		super();
		this.id = id;
		this.diskSize = diskSize;
		this.currentPosittion = currentPosittion;
	}

	public Integer getCurrentPosittion() {
		return currentPosittion;
	}

	public void setCurrentPosittion(Integer currentPosittion) {
		this.currentPosittion = currentPosittion;
	}

	public Double getDiskSize() {
		return diskSize;
	}

	public Integer getId() {
		return id;
	}

	public Disk[] getAllAboveDisks() {
		if (allAboveDisks == null) {
			allAboveDisks = new Disk[0];
		}
		return allAboveDisks;
	}

	public Disk getAboveDisk() {
		Disk[] aboveDisks = getAllAboveDisks();
		Disk aboveDisk = null;

		if (aboveDisks.length > 0) {
			aboveDisk = aboveDisks[0];
		}

		return aboveDisk;
	}

	public void setAllAboveDisks(Disk[] allAboveDisks) {
		this.allAboveDisks = allAboveDisks;
	}

}
