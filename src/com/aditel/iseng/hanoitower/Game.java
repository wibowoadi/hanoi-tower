package com.aditel.iseng.hanoitower;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private Integer diskNumber;
	private Tower[] towers;
	private List<Step> steps;

	public Game(Integer diskNumber) {
		this.diskNumber = diskNumber;
		init();
	}

	private void init() {
		towers = new Tower[] { new Tower(0, diskNumber, 2d, 0.2), new Tower(1), new Tower(2) };
		steps = new ArrayList<Step>();
	}

	public List<Step> getSteps() {
		return steps;
	}

	public void solve() {
		moveDisk(towers[0].getDisks()[0], 0, 2);
	}

	private void moveDisk(Disk lowestDisk, Integer fromTower, Integer toTower) {

		Disk aboveDisk = lowestDisk.getAboveDisk();

		if (aboveDisk == null) {
			towers[toTower].putDisk(towers[fromTower].removeDisk(lowestDisk));
			steps.add(new Step(lowestDisk.getId(), fromTower, toTower));
		} else {
			Integer reminingTower = getReminingTower(fromTower, toTower);
			moveDisk(aboveDisk, fromTower, reminingTower);
			towers[toTower].putDisk(towers[fromTower].removeDisk(lowestDisk));
			steps.add(new Step(lowestDisk.getId(), fromTower, toTower));
			moveDisk(aboveDisk, reminingTower, toTower);
		}
	}

	private Integer getReminingTower(Integer towerA, Integer towerB) {
		Integer remining = null;
		if (towerA + towerB == 1) {
			remining = 2;
		} else if (towerA + towerB == 2) {
			remining = 1;
		} else {
			remining = 0;
		}

		return remining;
	}
}
