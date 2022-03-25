package net.laurus.calc.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.laurus.calc.gui.MainApp;

public class GsonUtils {

	public static boolean deleteObject(IDirectory aObject) {
		File aNewFile = new File(aObject.getDirectory()+checkFileType(aObject.getFileName()));
		try {
			Utils.log(""+aNewFile.getCanonicalPath());
			if (aNewFile.exists()) {
				return aNewFile.delete();
			}
			else {
				Utils.log("Target File does not exist.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static <T> T getObjectFromFile(String aFileName, Class<? extends T> aClass){
		try {
			FileReader aReader = new FileReader(checkFileType(aFileName));
			T object = MainApp.GSON.fromJson(aReader, aClass);
			aReader.close();
			return object;
		} catch (JsonSyntaxException | JsonIOException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean writeObjectToFile(IDirectory aObject){
		try {
			if (!Utils.doesDirExist(aObject.getDirectory())) {
				File aNewDir = new File(aObject.getDirectory());
				if (!aNewDir.mkdirs()) {
					Utils.log("Unable to create Data Directories.");
				}
				else {
					Utils.log("Created Data Directories.");
				}
			}
			String aFileName = checkFileType(aObject.getFileName());
			File aNewFile = new File(aObject.getDirectory()+aFileName);
			if (aNewFile.exists()) {
				Utils.log("ERROR: File already exists. "+aNewFile.getPath());
			}
			else {
				FileWriter aWriter = new FileWriter(aNewFile);
				MainApp.GSON.toJson(aObject, aWriter);
				aWriter.close();
				return true;
			}
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String checkFileType(String aFileName) {
		if (!aFileName.toLowerCase().endsWith(".json")) {
			aFileName += ".json";
		}
		return aFileName;
	}

}
