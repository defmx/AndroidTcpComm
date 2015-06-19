package com.iek.wiflyremote.stat;

import java.util.Calendar;

public class BReg {
	private Calendar dateTime;
	private float velocity;
	private float dtime;
	private float avgvelocity;
	private float distance;

	public Calendar getTime() {
		return dateTime;
	}

	public void setTime(Calendar time) {
		this.dateTime = time;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public float getDtime() {
		return dtime;
	}

	public void setDtime(float dtime) {
		this.dtime = dtime;
	}

	public float getAvgvelocity() {
		return avgvelocity;
	}

	public void setAvgvelocity(float avgvelocity) {
		this.avgvelocity = avgvelocity;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}
}
