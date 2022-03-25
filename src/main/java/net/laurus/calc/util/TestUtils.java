package net.laurus.calc.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import net.laurus.calc.med.MedType;
import net.laurus.calc.med.Medicine;
import net.laurus.calc.packaging.types.BasePackage;
import net.laurus.calc.packaging.types.Box;
import net.laurus.calc.packaging.types.Strip;
import net.laurus.calc.packaging.types.Vial;
import net.laurus.calc.supplier.Supplier;

public class TestUtils {

	private static File sMockDir;
	private static File sCities;
	private static File sFirst;
	private static File sLast;
	private static File sMeds;

	public static List<String> sCityNames;
	public static List<String> sFirstNames;
	public static List<String> sLastNames;
	public static List<String> sMedNames;

	static {
		sMockDir = new File("mock");
		if (canTest()) {
			sCities = new File(sMockDir, "Cities.txt");
			sFirst = new File(sMockDir, "First.txt");
			sLast = new File(sMockDir, "Last.txt");
			sMeds = new File(sMockDir, "Med.txt");
			try {
				sCityNames = Files.readLines(sCities, Charsets.UTF_8);
				sFirstNames = Files.readLines(sFirst, Charsets.UTF_8);
				sLastNames = Files.readLines(sLast, Charsets.UTF_8);
				sMedNames = Files.readLines(sMeds, Charsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		else {
			Utils.log("Unable to test, canTest is false;");
			System.exit(0);
		}
	}

	public static boolean canTest() {
		if (sMockDir.exists() && sMockDir.isDirectory()) {
			return true;
		}
		return false;
	}

	public static <T> T getRandomEntryFromEnum(Class<T> e) {
		int aSize = e.getEnumConstants().length;
		return e.getEnumConstants()[Utils.randInt(0, aSize - 1)];
	}

	public static ArrayList<Supplier> generateRandomSuppliers(int aAmount){
		ArrayList<Supplier> aDat = new ArrayList<Supplier>();
		for (int i=0; i < aAmount; i++) {
			Supplier aSup = new Supplier();
			aSup.setRegion(generateCityName());
			aSup.setName(generateFullName());
			aDat.add(aSup);
		}
		return aDat;
	}

	public static String generateFullName(){
		return generateFirstName()+" "+generateLastName();
	}

	public static String generateFirstName(){
		return sFirstNames.get(Utils.randInt(0, sFirstNames.size()-1));
	}

	public static String generateLastName(){
		return sLastNames.get(Utils.randInt(0, sLastNames.size()-1));
	}

	public static ArrayList<Medicine> generateRandomMeds(int aAmount){
		ArrayList<Medicine> aDat = new ArrayList<Medicine>();
		for (int i=0; i < aAmount; i++) {
			Medicine aSup = new Medicine();
			aSup.setName(generateFullMedName());
			aSup.setDose(Utils.decimalRounding(Utils.randFloat(0.01f, 100f), 2));
			aSup.setType(getRandomEntryFromEnum(MedType.class));
			aSup.setUnit(getRandomEntryFromEnum(Measurement.class));
			aDat.add(aSup);
		}
		return aDat;
	}

	public static BasePackage generatePackaging(Medicine aSup) {
		int aMedAmount = Utils.randInt(1, 100);
		return aSup.getType() == MedType.INJECTION ? new Vial(aMedAmount) : Utils.randInt(1, 2) == 1 ? new Box(aMedAmount) : new Strip(aMedAmount);
	}

	public static String generateFullMedName(){
		int aRandomSuffixes = Utils.randInt(0, 2);
		StringBuilder aName = new StringBuilder().append(generateMedName());
		if (aRandomSuffixes > 0) {
			for (int i = 0; i < aRandomSuffixes; i++) {
				aName.append(" ").append(generateMedName());
			}
		}
		return aName.toString();
	}

	public static String generateMedName(){
		return sMedNames.get(Utils.randInt(0, sMedNames.size()-1));
	}

	public static String generateCityName(){
		return sCityNames.get(Utils.randInt(0, sCityNames.size()-1));
	}

}
