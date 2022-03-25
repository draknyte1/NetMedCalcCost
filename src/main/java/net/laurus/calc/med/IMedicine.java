package net.laurus.calc.med;

import net.laurus.calc.util.IDirectory;
import net.laurus.calc.util.Measurement;

public interface IMedicine extends IDirectory {

	public float getDose();

	public Measurement getUnit();

	public String getName();

	public MedType getType();

}
