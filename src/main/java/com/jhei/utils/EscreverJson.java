package com.jhei.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EscreverJson implements Runnable {
	FileWriter writeFile;
	public EscreverJson(FileWriter wf) {
	  this.writeFile = wf;
	}

	@Override
	public void run() {

		System.out.println("Iniciado escritura" + getData());
		do {

			String task = ContolQueue.getTaskEscrever();

			if (task != null) {
				try {
					writeFile.write(task+ "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			

		} while (ContolQueue.isTerminatedEscrever());
		try {
			writeFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ContolQueue.setFinalizadoTodoProcesso();
		System.out.println("Finalizado escritura" + getData());
	}
	
	private String getData() {
		return new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss.SSS z").format(new Date(System.currentTimeMillis()));
	}
	
}