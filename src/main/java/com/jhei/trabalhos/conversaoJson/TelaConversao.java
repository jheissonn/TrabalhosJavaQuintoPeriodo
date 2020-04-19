package com.jhei.trabalhos.conversaoJson;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFileChooser;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class TelaConversao implements Initializable {
	
	@FXML
	private TextField txCaminho, txCaminhoJson;
	@FXML
	private ProgressBar progressBarLeitura, progressBarConversao, progressBarEscreve;
	@FXML
	private TextArea txLogLeitura, txLogConversao, txLogEscrita;
	private ControlerDeConversao controlConversao;
	
	private ExecutorService pool; 
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		progressBarLeitura.setProgress(0);		
		progressBarConversao.setProgress(0);
		progressBarEscreve.setProgress(0);
		pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}
	
	@FXML
	private void abreFolderDialog(){
		JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("choosertitle");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);
	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	    	txCaminhoJson.setText(chooser.getCurrentDirectory().toString());
	    } 
	}
	@FXML
	private void abreFileDialog() {
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivos csv", "*.csv*");

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog to select one file. 
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			txCaminho.setText(file.getPath());
		}
	}
	
	@FXML
	private void processaArquivo()  {
		
		File file = new File(txCaminho.getText());		
		if(file.exists() && !txCaminhoJson.getText().equals("")) {
			controlConversao = new ControlerDeConversao(file, new File(txCaminhoJson.getText() + "\\ArquivoConvertido.json"));
			iniciarProcesso();
		}
		else {
			  Alert alert = new Alert(AlertType.INFORMATION);
              alert.setTitle("Campos obrigatórios.");
              alert.setHeaderText("Por favor, preencha os campos obrigatórios");
              alert.setContentText("");
              alert.showAndWait();
		}			
	}
	
	private void iniciarProcesso() {
		controlConversao.iniciarProcesso();
		pool.execute(getTaskLeitura());
		pool.execute(getTaskConversao());
		pool.execute(getTaskEscrita());
	}
	
	private Task<Void> getTaskLeitura() {
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				updateMessage("Iniciado leitura para: " + controlConversao.getQtdeRegistros() + " registros.");
				do {
					updateProgress(controlConversao.getQdreRegistrosLidos(), controlConversao.getQtdeRegistros());
				} while (controlConversao.isContinuaLeituraCsv());
				updateProgress(controlConversao.getQdreRegistrosLidos(), controlConversao.getQtdeRegistros());
				updateMessage("Finalizado leitura de: " + controlConversao.getQtdeRegistros() + " registros.");
				
				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				updateMessage("Terminado leitrua!");
			}

			@Override
			protected void cancelled() {
				super.cancelled();
				updateMessage("Cancelado leitura!");
			}

			@Override
			protected void failed() {
				super.failed();
				updateMessage("Falhou a leitura do arquivo!");
			}
		};

		task.messageProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obs, String oldMessage, String newMessage) {
				txLogLeitura.setText(txLogLeitura.getText() + "\n" +task.getMessage());
			}
		});

		progressBarLeitura.progressProperty().bind(task.progressProperty());
		return task;
	}
	
	private Task<Void> getTaskConversao() {
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				updateMessage("Iniciado conversão para: " + controlConversao.getQtdeRegistros() + " registros.");

				do {
					updateProgress(controlConversao.getQdreRegistrosConvertidos(), controlConversao.getQtdeRegistros());
				} while (controlConversao.isTerminatedConvert());
				updateProgress(controlConversao.getQdreRegistrosConvertidos(), controlConversao.getQtdeRegistros());
				updateMessage("Finalizado conversão de: " + controlConversao.getQtdeRegistros() + " registros.");
				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				updateMessage("Arquivo convertido com sucesso!");
			}

			@Override
			protected void cancelled() {
				super.cancelled();
				updateMessage("Conversão de arquivo cancelado!");
			}

			@Override
			protected void failed() {
				super.failed();
				updateMessage("Falha ao converter arquivo!");
			}
		};

		task.messageProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obs, String oldMessage, String newMessage) {
				txLogConversao.setText(txLogConversao.getText() + "\n" + task.getMessage());
			}
		});

		progressBarConversao.progressProperty().bind(task.progressProperty());
		return task;
	}
	
	private Task<Void> getTaskEscrita() {
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				updateMessage("Iniciado escrita para: " + controlConversao.getQtdeRegistros() + " registros.");

				do {
					updateProgress(controlConversao.getQtdeEscritos(), controlConversao.getQtdeRegistros());
				} while (controlConversao.isTerminatedEscrever());
				updateProgress(controlConversao.getQtdeEscritos(), controlConversao.getQtdeRegistros());
				updateMessage("Finalizado de escrever: " + controlConversao.getQtdeRegistros() + " registros.");
				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				updateMessage("Terminado de escrever arquivo!");
			}

			@Override
			protected void cancelled() {
				super.cancelled();
				updateMessage("Cancelado escrita do arquivo!");
			}

			@Override
			protected void failed() {
				super.failed();
				updateMessage("Falha ao escrever arquivo!");
			}
		};

		task.messageProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obs, String oldMessage, String newMessage) {
				txLogEscrita.setText(txLogEscrita.getText() +"\n" +  task.getMessage());
			}
		});

		progressBarEscreve.progressProperty().bind(task.progressProperty());
		return task;
	}
}
