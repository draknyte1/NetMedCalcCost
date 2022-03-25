package net.laurus.calc.med;

import net.laurus.calc.util.Measurement;
import net.laurus.calc.util.Utils;

public class Medicine implements IMedicine {

	private float dose;
	private Measurement unit;
	private String name;
	private MedType type;

	@Override
	public float getDose() {
		return this.dose;
	}

	public void setDose(float dose) {
		this.dose = dose;
	}

	@Override
	public Measurement getUnit() {
		return this.unit;
	}

	public void setUnit(Measurement unit) {
		this.unit = unit;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public MedType getType() {
		return this.type;
	}

	public void setType(MedType type) {
		this.type = type;
	}

	@Override
	public String getDirectory() {
		return "data/med/"+this.type.get()+"/"+(this.name.toLowerCase().replace(" ", "_")+"/");
	}

	@Override
	public String getFileName() {
		return ""+Utils.generateHashCode(new Object[] {getName(), getType().get(), getDose()})+".json";
	}

	@Override
	public String toString() {
		return ""+getName()+": "+getType().get()+", "+getDose()+getUnit().get();
	}
}
