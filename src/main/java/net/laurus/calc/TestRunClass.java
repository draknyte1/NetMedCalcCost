package net.laurus.calc;

import java.util.ArrayList;

import net.laurus.calc.gui.MainApp;
import net.laurus.calc.med.Medicine;
import net.laurus.calc.stock.MedicineStock;
import net.laurus.calc.supplier.Supplier;
import net.laurus.calc.util.GsonUtils;
import net.laurus.calc.util.TestUtils;
import net.laurus.calc.util.Utils;

public class TestRunClass {

	static boolean generate = true;

	public static void main(String[] args) {

		// Test if data exists
		if (generate || !Utils.doesDataAleadyExist()) {
			Utils.log("Generating Test Data.");
			MainApp.configureGson();
			Utils.log("==============================");
			// We have none, generate some.
			ArrayList<Supplier> aTestSuppliers = TestUtils.generateRandomSuppliers(50);
			ArrayList<Medicine> aTestMeds = TestUtils.generateRandomMeds(100);
			for (Medicine aMed : aTestMeds) {
				for (Supplier aSupplier : aTestSuppliers) {
					if (Utils.randInt(0, 100) >= 90) {
						MedicineStock aStack = new MedicineStock(aMed, TestUtils.generatePackaging(aMed), Utils.createMoneyObject(Utils.randFloat(0.01f, 100f)));
						aSupplier.addStock(aStack);

					}
				}
			}

			Utils.log("==============================");
			for (Supplier aSupplier : aTestSuppliers) {
				GsonUtils.writeObjectToFile(aSupplier);
				Utils.log(""+aSupplier.getName()+": ");
				for (MedicineStock aMed : aSupplier.getStock()) {
					Utils.log(""+aMed.getMed().toString());
					GsonUtils.writeObjectToFile(aMed.getMed());
				}
				Utils.log("==============================");
			}

			Utils.log("Done.");

		}
		else {
			Utils.log("Found Data.");
		}

		// Load Data

		// find cheapest 2 suppliers for every arg

		// print

	}

}
