package com.iek.tcpcomm.stat;

public class BoardRythm {
	private int linearMetersPerTurn;
	private int instantSpeed;
	private int averageSpeed;
	private int deadTimePerTurn;

	public int getLinearMetersPerTurn() {
		return linearMetersPerTurn;
	}

	public void setLinearMetersPerTurn(int linearMetersPerTurn) {
		this.linearMetersPerTurn = linearMetersPerTurn;
	}

	public int getInstantSpeed() {
		return instantSpeed;
	}

	public void setInstantSpeed(int instantSpeed) {
		this.instantSpeed = instantSpeed;
	}

	public int getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(int averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public int getDeadTimePerTurn() {
		return deadTimePerTurn;
	}

	public void setDeadTimePerTurn(int deadTimePerTurn) {
		this.deadTimePerTurn = deadTimePerTurn;
	}

}
