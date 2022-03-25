package net.laurus.calc.util;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.Random;

import net.laurus.calc.packaging.PackageType;
import net.laurus.calc.packaging.types.BasePackage;
import net.laurus.calc.packaging.types.Box;
import net.laurus.calc.packaging.types.Strip;
import net.laurus.calc.packaging.types.Vial;
import net.laurus.calc.supplier.Supplier;

public class Utils {

	public static BasePackage getBasePackageFromType(PackageType aType, int aQuantity) {
		if (aType == PackageType.BOX) {
			return new Box(aQuantity);
		}
		else if (aType == PackageType.STRIP) {
			return new Strip(aQuantity);
		}
		else if (aType == PackageType.VIAL) {
			return new Vial(aQuantity);
		}
		else {
			return new Strip(aQuantity);
		}
	}

	public static int generateHashCode(Object[] aData) {
		return Objects.hash(aData);
	}

	public static Money createMoneyObject(float aValue) {
		return new Money(new BigDecimal(aValue).setScale(2, RoundingMode.HALF_EVEN));
	}

	public static boolean doesDataAleadyExist() {
		File aDir = new File("data");
		log(aDir.getAbsolutePath());
		if (aDir.exists() && aDir.isDirectory()) {
			return true;
		}
		return false;
	}

	public static void createDir() {

	}

	public static boolean doesDirExist(String aPath) {
		File aCheck = new File(aPath);
		if (aCheck.exists() && aCheck.isDirectory()) {
			return true;
		}
		return false;
	}

	public static void writeSupplierToFile(Supplier aSupplier) {

	}

	public static void log(String s) {
		System.out.println(s);
	}

	final static Random rand = new Random();

	public static float decimalRounding(float number, int scale) {
		int pow = 10;
		for (int i = 1; i < scale; i++)
			pow *= 10;
		float tmp = number * pow;
		return ( (float) ( (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) ) ) / pow;
	}

	/** Formats a number with group separator and at most 2 fraction digits. */
	public static final NumberFormat sNumberFormat = NumberFormat.getInstance();

	static {
		sNumberFormat.setMaximumFractionDigits(2);
	}

	public static int randInt(final int min, final int max) {
		return rand.nextInt((max - min) + 1) + min;
	}

	public static float randFloat(final float min, final float max) {
		return nextFloat(rand,(max - min) + 1) + min;
	}

	private static float nextFloat(final Random rng, final float n) {
		float bits, val;
		do {
			bits = (rng.nextLong() << 1) >>> 1;
			val = bits % n;
		} while (((bits-val)+(n-1)) < 0L);
		return val;
	}


	public static String formatNumbers(long aNumber) {
		return sNumberFormat.format(aNumber);
	}

	public static String formatNumbers(double aNumber) {
		return sNumberFormat.format(aNumber);
	}

}
