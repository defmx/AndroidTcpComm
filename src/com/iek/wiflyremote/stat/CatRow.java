package com.iek.wiflyremote.stat;

public class CatRow {
	private int id;
	private String name;
	private String value;

	public CatRow(int id, String name, String value) {
		setId(id);
		setName(name);
		setValue(value);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
