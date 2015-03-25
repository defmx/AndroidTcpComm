package com.iek.tcpcomm.stat;

public class BoardFacts {
	private String speedUnits;
	private int speed;
	private int avgSpeed;
	private int deadTime;
	private int linearMeters;
	private int lastStop;
	private int elapsedTime;

	public String getSpeedUnits() {
		return speedUnits;
	}

	public void setSpeedUnits(String speedUnits) {
		this.speedUnits = speedUnits;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(int avgSpeed) {
		this.avgSpeed = avgSpeed;
	}

	public int getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(int deadTime) {
		this.deadTime = deadTime;
	}

	public int getLinearMeters() {
		return linearMeters;
	}

	public void setLinearMeters(int linearMeters) {
		this.linearMeters = linearMeters;
	}

	public int getLastStop() {
		return lastStop;
	}

	public void setLastStop(int lastStop) {
		this.lastStop = lastStop;
	}

	public int getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(int elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

}
