package com.iek.tcpcomm.stat;

import java.util.Calendar;

public class BoardParams {
	private double wheelDiameter;
	private Calendar firstShiftStart;
	private Calendar firstShiftEnd;
	private Calendar secondShiftStart;
	private Calendar secondShiftEnd;
	private Calendar thirdShiftStart;
	private Calendar thirdShiftEnd;

	public double getWheelDiameter() {
		return wheelDiameter;
	}

	public void setWheelDiameter(double wheelDiameter) {
		this.wheelDiameter = wheelDiameter;
	}

	public Calendar getFirstShiftStart() {
		return firstShiftStart;
	}

	public void setFirstShiftStart(Calendar firstShiftStart) {
		this.firstShiftStart = firstShiftStart;
	}

	public Calendar getFirstShiftEnd() {
		return firstShiftEnd;
	}

	public void setFirstShiftEnd(Calendar firstShiftEnd) {
		this.firstShiftEnd = firstShiftEnd;
	}

	public Calendar getSecondShiftStart() {
		return secondShiftStart;
	}

	public void setSecondShiftStart(Calendar secondShiftStart) {
		this.secondShiftStart = secondShiftStart;
	}

	public Calendar getSecondShiftEnd() {
		return secondShiftEnd;
	}

	public void setSecondShiftEnd(Calendar secondShiftEnd) {
		this.secondShiftEnd = secondShiftEnd;
	}

	public Calendar getThirdShiftStart() {
		return thirdShiftStart;
	}

	public void setThirdShiftStart(Calendar thirdShiftStart) {
		this.thirdShiftStart = thirdShiftStart;
	}

	public Calendar getThirdShiftEnd() {
		return thirdShiftEnd;
	}

	public void setThirdShiftEnd(Calendar thirdShiftEnd) {
		this.thirdShiftEnd = thirdShiftEnd;
	}
}
