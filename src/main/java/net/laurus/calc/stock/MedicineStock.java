package net.laurus.calc.stock;

import net.laurus.calc.med.Medicine;
import net.laurus.calc.packaging.types.BasePackage;
import net.laurus.calc.util.Money;

public class MedicineStock {

	private final Medicine mMed;
	private final Money cost;
	private final BasePackage packaging;

	public MedicineStock(Medicine aMed, BasePackage aQuantity, Money aCost) {
		this.mMed = aMed;
		this.packaging = aQuantity;
		this.cost = aCost;
	}

	public Medicine getMed() {
		return this.mMed;
	}

	public Money getCost() {
		return this.cost;
	}

	public BasePackage getPackaging() {
		return this.packaging;
	}

	@Override
	public String toString() {
		return this.mMed.getName();
	}

}
