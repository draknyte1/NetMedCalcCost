package net.laurus.calc.supplier;

import java.util.ArrayList;

import net.laurus.calc.med.Medicine;

public class SupplierStock extends ArrayList<Medicine> {

	private static final long serialVersionUID = -8377906405471410841L;

	@Override
	public boolean add(Medicine aMed) {
		return false;
	}

	public boolean remove(Medicine aMed) {
		return false;
	}

	public boolean put(Medicine aMed) {
		return false;
	}


}
