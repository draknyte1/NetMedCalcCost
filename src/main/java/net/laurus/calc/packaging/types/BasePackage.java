package net.laurus.calc.packaging.types;

import net.laurus.calc.packaging.IMedPackaging;
import net.laurus.calc.packaging.PackageType;

public abstract class BasePackage implements IMedPackaging {

	private final int mAmountPerPackage;
	private final PackageType mPackaging;

	public BasePackage(int aQuantity, PackageType aType) {
		this.mAmountPerPackage = aQuantity;
		this.mPackaging = aType;
	}

	@Override
	public PackageType getPackageType() {
		return this.mPackaging;
	}

	@Override
	public int getAmountPerPackage() {
		return this.mAmountPerPackage;
	}

	@Override
	public String toString() {
		return this.mPackaging.name()+" of "+this.mAmountPerPackage;
	}

}
