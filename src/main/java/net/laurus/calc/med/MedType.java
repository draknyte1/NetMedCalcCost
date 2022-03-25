package net.laurus.calc.med;

public enum MedType {

	PILL("Pill"),
	CAPSULE("Capsule"),
	INJECTION("Injection");

	private final String type;

	MedType(String aName) {
		this.type = aName;
	}

	public String get() {
		return this.type;
	}
}
