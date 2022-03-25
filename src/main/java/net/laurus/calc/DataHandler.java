package net.laurus.calc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.laurus.calc.gui.MainApp;
import net.laurus.calc.med.Medicine;
import net.laurus.calc.supplier.Supplier;
import net.laurus.calc.util.GsonUtils;
import net.laurus.calc.util.Utils;

public class DataHandler extends Thread {

	private static final File DIR_MEDS;
	private static final File DIR_SUPPLIERS;
	private static short  ID = 0;

	static {
		DIR_MEDS = new File("data/med/");
		DIR_SUPPLIERS = new File("data/supplier");
	}

	public DataHandler() {
		super();
		this.setName("Data-Handler-Thread-"+(ID++));
		this.setDaemon(true);
		this.start();
	}

	@Override
	public synchronized void run() {
		// Update Supplier and Med Lists
		if (Utils.doesDataAleadyExist()) {
			Utils.log("Doing a Rescan.");
			if (DIR_MEDS.exists() || DIR_SUPPLIERS.exists()) {


				Utils.log("Processing Files into memory.");
				if (DIR_MEDS.exists()) {
					ArrayList<File> aMeds = getAllSubFiles(DIR_MEDS);
					MainApp.sMedCache.addAll(loadAllFiles(aMeds, Medicine.class));
				}
				if (DIR_SUPPLIERS.exists()) {
					ArrayList<File> aSuppliers = getAllSubFiles(DIR_SUPPLIERS);
					MainApp.sSupplierCache.addAll(loadAllFiles(aSuppliers, Supplier.class));
				}
				Utils.log("Processed Files into Cache.");
			} else {
				Utils.log("No files found.");
			}
		}
		else {
			Utils.log("No files found.");
		}
	}

	public static ArrayList<File> getAllSubFiles(File aFile) {
		List<String> fileNamesList = new ArrayList<String>();
		ArrayList<File> fileList = new ArrayList<File>();
		try (Stream<Path> walk = Files.walk(Paths.get(aFile.toURI()))) {
			fileNamesList = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());
			for (String p : fileNamesList) {
				File f = new File(p);
				if (f.exists() && f.isFile()) {
					fileList.add(f);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileList;
	}

	public static <T> ArrayList<T> loadAllFiles(ArrayList<File> aData, Class<? extends T> aType) {
		ArrayList<T> aOutput = new ArrayList<T>();
		for (File a : aData) {
			T o = GsonUtils.getObjectFromFile(a.getAbsolutePath(), aType);
			if (o != null) {
				aOutput.add(o);
			}
		}
		return aOutput;
	}

}
