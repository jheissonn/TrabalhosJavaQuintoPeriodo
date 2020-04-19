package com.jhei.trabalhos.conversaoJson;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class TrataCsv implements Runnable {
	
	private InterfaceCsv control;
	private File file;
	
	public TrataCsv(InterfaceCsv controler, File filePath) {
		this.control = controler;
		this.file = filePath;		
	}

	@Override
	public void run() {
		try (Reader reader = new FileReader(file)) {
			CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT);
			control.setContinuaLeituraCsv(true);
			for (CSVRecord csvRecord  : parser) {
				if (parser.getCurrentLineNumber() == 1) {
					continue;
				}
				control.addRegistro(csvRecord);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		control.setContinuaLeituraCsv(false);		
	}
}
