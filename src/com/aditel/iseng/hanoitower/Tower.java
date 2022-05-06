package com.aditel.iseng.hanoitower;

import java.util.Arrays;

public class Tower {
	private Integer id;
	private Disk[] disks;

	private Tower(Integer id, Disk[] disks) {
		this.id = id;
		this.disks = disks;
	}

	public Tower(Integer id) {
		this(id, new Disk[0]);
	}

	public Tower(Integer id, Integer diskNumber, Double diskInitSize, Double diskSizeIncreament) {
		this(id, null);
		disks = new Disk[diskNumber];
		for (int i = 0; i < diskNumber; i++) {
			Disk disk = new Disk(i, diskInitSize + (disks.length - i - 1) * diskSizeIncreament, i);
			disks[i] = disk;
		}

		setAboveDisks();
	}

	private void setAboveDisks() {
		for (int i = 0; i < disks.length; i++) {
			disks[i].setAllAboveDisks(Arrays.copyOfRange(disks, i + 1, disks.length));
		}
	}

	public Disk[] getDisks() {
		if (disks == null) {
			disks = new Disk[0];
		}
		return disks;
	}

	public Disk getLowestDisk() {
		try {
			return getDisks()[0];
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public Disk removeDisk(Disk disk) {
		Integer diskIndex = null;

		for (Disk d : disks) {
			if (d == disk) {
				diskIndex = disk.getCurrentPosittion();
			}
		}

		if (diskIndex == null) {
			throw new RuntimeException("Disk not found");
		}

		if (disk.getAllAboveDisks().length > 0) {
			System.out.println(disk.getCurrentPosittion());
			throw new RuntimeException("Can not remove disk wich has another disk(s) on it");
		}

		disks = Arrays.copyOf(disks, disks.length - 1);
		disk.setCurrentPosittion(null);

		setAboveDisks();

		return disk;
	}

	public void putDisk(Disk disk) {
		if (disks.length > 0) {
			if (disk.getDiskSize() > disks[disks.length - 1].getDiskSize()) {
				throw new RuntimeException("You can not put bigger disk on top of smaller disk");
			}
		}
		disks = Arrays.copyOf(disks, disks.length + 1);
		disks[disks.length - 1] = disk;
		disk.setCurrentPosittion(this.getDisks().length);

		setAboveDisks();

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Tower %s".formatted(id));
		sb.append("\r\n");

		for (int i = disks.length - 1; i >= 0; i--) {
			sb.append(disks[i].getCurrentPosittion());
			sb.append("|");
			sb.append(disks[i].getDiskSize());
			sb.append("\r\n");

		}
		sb.append("\r\n");

		return sb.toString();
	}
}
