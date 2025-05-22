package org.test.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadWriteDataFile {

	private static final Logger logger = LogManager.getLogger(ReadWriteDataFile.class);
	private FileInputStream fs = null;
	private XSSFWorkbook xwb = null;
	private XSSFSheet sheet = null;
	private FileOutputStream fos = null;

	public ReadWriteDataFile(String fileName, String sheetName) {
		try {
			this.fs = new FileInputStream(fileName);
			this.xwb = new XSSFWorkbook(fs);
			this.sheet = xwb.getSheet(sheetName);
			this.fos = new FileOutputStream(fileName);
		} catch (Exception ex) {
			logger.error("Exception caught: " + ex.getMessage());
		}
	}

	public String readFromFile(int rowNum, int cellNum) {
		return sheet.getRow(rowNum).getCell(cellNum).getStringCellValue();
	}

	/**
	 * Method to write given data into a sheet at given row and cell number
	 * 
	 * @param rowNum
	 * @param cellNum
	 * @param data    - value to be updated in the given cell
	 */
	public void writeInFile(int rowNum, int cellNum, String data) {
		sheet.getRow(rowNum).createCell(cellNum).setCellValue(data);
		try {
			xwb.write(fos);
		} catch (IOException e) {
			logger.error("Exception caught in writeInFile(): " + e.getMessage());
		}

	}

	/**
	 * Using own cleanup method instead of finalize(). The garbage collector calls
	 * finalize() at an unspecified time, if at all. There's no guarantee that it
	 * will be called promptly or even at all, especially if the JVM terminates
	 * abruptly.
	 */
	public void closeFile() {
		try {
			this.fs.close();
			this.xwb.close();
			this.fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
