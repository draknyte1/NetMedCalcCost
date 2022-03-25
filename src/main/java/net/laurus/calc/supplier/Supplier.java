package net.laurus.calc.supplier;

import java.util.Vector;

import net.laurus.calc.stock.MedicineStock;

public class Supplier implements ISupplier {

	private String name;
	private String region;
	private String email;
	private Vector<MedicineStock> stock = new Vector<MedicineStock>();

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public Vector<MedicineStock> getStock() {
		return this.stock;
	}

	public void setStock(Vector<MedicineStock> stock) {
		this.stock = stock;
	}

	public boolean addStock(MedicineStock med) {
		return this.stock.add(med);
	}

	@Override
	public String getDirectory() {
		return "data/supplier/"+this.region+"/"+(this.name.toLowerCase().replace(" ", "_")+"/");
	}

	@Override
	public String getFileName() {
		return "data.json";
	}

	@Override
	public String toString() {
		return "("+getRegion()+") "+getName();
	}

}
