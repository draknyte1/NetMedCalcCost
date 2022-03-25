package net.laurus.calc.supplier;

import java.util.List;

import net.laurus.calc.stock.MedicineStock;
import net.laurus.calc.util.IDirectory;

public interface ISupplier extends IDirectory {

	public String getName();

	public String getRegion();

	public String getEmail();

	public List<MedicineStock> getStock();

}
