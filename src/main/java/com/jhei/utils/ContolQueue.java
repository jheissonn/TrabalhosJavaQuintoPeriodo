package com.jhei.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

public class ContolQueue implements Runnable {

	private static List<String[]> TaskQueueConverter;
	private static List<String> TaskQueueEscrever;
	private List<String[]> arquivo;
	private static boolean terminouLeitura = true;
	private static boolean terminouConversao = true;
	@FXML
	private static ProgressBar progressBar;
	
	
	
	public static void setFinalizadoTodoProcesso() {
		Platform.runLater(new Runnable() {
            @Override public void run() {
            	progressBar.setProgress(100);	
            }
        });
			
	}
	
	public static void setTerminouConversao(boolean teConversao) {
		terminouConversao = teConversao;
	}
	
	public static boolean getTerminouConversao() {
		return terminouConversao;
	}

	public ContolQueue(List<String[]> ar, ProgressBar pgBar) {
		this.arquivo = ar;
		ContolQueue.progressBar = pgBar;
		TaskQueueConverter = new Vector<String[]>();
		TaskQueueEscrever = new Vector<String>();
	}

	public static synchronized String[] getTask() {
		if (TaskQueueConverter.size() > 0)
			return TaskQueueConverter.remove(0);
		return null;
	}
	
	public static synchronized String getTaskEscrever() {
		if (TaskQueueEscrever.size() > 0)
			return TaskQueueEscrever.remove(0);
		return null;
	}
	
	public static void addTaskEscrever(String task) {
		TaskQueueEscrever.add(task);
	}
	
	private void addTask(String[] task) {
		TaskQueueConverter.add(task);
	}
	
    public static boolean isTerminatedEscrever() {
    		if(terminouLeitura) {
    			return true;
    		} 
    		if(terminouConversao) {
    			return true;
    		} 
    	return  !TaskQueueConverter.isEmpty();		
	}
	
	
	public static boolean isTerminated() {
		if(terminouLeitura) {
			return true;
		}
		if(!TaskQueueConverter.isEmpty()) {
			return true;
		}
		else {
			return !TaskQueueEscrever.isEmpty();
		}
		
	}
	
	@SuppressWarnings("static-access")
	private void receivesData() {
		System.out.println("Iniciado leitura" + getData());
		int qtdeTotalRegistro = arquivo.size();
		int qtdeRegistros = 0;
		do {
			addTask(arquivo.get(qtdeRegistros));
			qtdeRegistros++;
		}while(qtdeRegistros < qtdeTotalRegistro );
		terminouLeitura = false;
		System.out.println("Finalizado leitura" + getData());
	}

	@Override
	public void run() {
		receivesData();
		
	}
	
	private String getData() {
		return new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss.SSS z").format(new Date(System.currentTimeMillis()));
	}

}