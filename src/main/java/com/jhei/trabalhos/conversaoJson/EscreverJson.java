package com.jhei.trabalhos.conversaoJson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class EscreverJson implements Runnable {
	private FileWriter writeFile;
	private InterfaceWriterFile controler;
	
	public EscreverJson(File json, InterfaceWriterFile control) {
	  try {
		this.writeFile = new FileWriter(json);
	} catch (IOException e) {
		e.printStackTrace();
	}
	  this.controler = control;
	}

	@Override
	public void run() {
		do {
			String task = controler.getObjToConvert();
			if (task != null) {
				try {
					writeFile.write(task+ "\n");
					controler.addEscrito();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			

		} while (controler.isTerminatedEscrever());
		try {
			writeFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}