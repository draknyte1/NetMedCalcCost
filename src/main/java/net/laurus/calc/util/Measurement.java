package net.laurus.calc.util;

public enum Measurement {

	SINGLE("x1"),
	MICROGRAM("ug"),
	MILLIGRAM("mg"),
	GRAM("g"),
	OTHER(" (other)");

	private final String measurement;

	Measurement(String aName) {
		this.measurement = aName;
	}

	public String get() {
		return this.measurement;
	}

}
