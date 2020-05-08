package com.jhei.trabalhos.conversaoJson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFileChooser;

import org.apache.commons.collections4.SetUtils.SetView;

import com.jhei.trabalhoSocket1.PathProcesso;

import javafx.application.Platform;
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
	private ProgressBar progressBarAconverter, progressBarAescrever;
	@FXML
	private TextArea txLogLeitura, txLogConversao, txLogEscrita;
	private ControlerDeConversao controlConversao;
	
	private ExecutorService pool; 
	private ViewProcesso viewProcessoGlobal;
	private Socket client;
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		progressBarLeitura.setProgress(0);		
		progressBarConversao.setProgress(0);
		progressBarEscreve.setProgress(0);
		progressBarAconverter.setProgress(0);
		progressBarAescrever.setProgress(0);
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
			iniciarSocket();
		}
		else {
			  Alert alert = new Alert(AlertType.INFORMATION);
              alert.setTitle("Campos obrigat�rios.");
              alert.setHeaderText("Por favor, preencha os campos obrigat�rios");
              alert.setContentText("");
              alert.showAndWait();
		}			
	}
	
	private void iniciarSocket() {
		pool.execute(getTaskSocket());
	}
	
	private void iniciarProcessoSocket() {
		pool.execute(getTaskLeitura());
		pool.execute(getTaskConversao());
		pool.execute(getTaskEscrita());
	}
	private Task<Void> getTaskSocket(){
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				ObjectOutputStream EnviandoServidor = null;
				ViewProcesso viewProcesso = null;
				try {
					PathProcesso pathProcesso = new PathProcesso();
					pathProcesso.setPathCsv(txCaminho.getText());
					pathProcesso.setPathJson(txCaminhoJson.getText() + "\\ArquivoConvertido.json");
					pathProcesso.setStatus("Iniciado");
					client = new Socket("127.0.0.1", 12345);
					InputStream inputStream = client.getInputStream();
					EnviandoServidor = new ObjectOutputStream(client.getOutputStream());
					EnviandoServidor.writeObject(pathProcesso);
					EnviandoServidor.flush();
					ObjectInputStream objectOutputStream = new ObjectInputStream(inputStream);
					viewProcesso = (ViewProcesso) objectOutputStream.readObject();
					setViewProcesso(viewProcesso);
					iniciarProcessoSocket();
					pathProcesso = new PathProcesso();
					pathProcesso.setStatus("Andamento");
					
					EnviandoServidor.writeObject(pathProcesso);
					EnviandoServidor.flush();
					do {	
						viewProcesso = (ViewProcesso) objectOutputStream.readObject();
						setViewProcesso(viewProcesso);
						atualizaProgress(viewProcesso);
					}
					while(viewProcesso.isTerminatedEscrever());
					
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
	} 
	
	private void atualizaProgress(ViewProcesso view) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				progressBarAconverter.setProgress(view.getAConverter());
				progressBarAescrever.setProgress(view.getAEscrever());
			}
		});
		
	}
	
	private Task<Void> getTaskLeitura() {
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				updateMessage("Iniciado leitura para: " + getViewProcesso().getQtdeRegistros() + " registros: " + new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z").format(new Date(System.currentTimeMillis())));
				do {
					updateProgress(getViewProcesso().getResgistrosLidos(), getViewProcesso().getQtdeRegistros());
				} while (getViewProcesso().isContinuaLeituraCsv());
				updateProgress(getViewProcesso().getResgistrosLidos(), getViewProcesso().getQtdeRegistros());
				updateMessage("Finalizado leitura de: " + getViewProcesso().getQtdeRegistros() + " registros: " + new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z").format(new Date(System.currentTimeMillis())));
				updateMessage("Ao chegar aqui ele finalizou a thread de leitura.");
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
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						txLogLeitura.setText(txLogLeitura.getText() + "\n" + task.getMessage());
					}
				});
			}
		});
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				progressBarLeitura.progressProperty().bind(task.progressProperty());
			}
		});
		return task;
	}
	
	private Task<Void> getTaskConversao() {
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				updateMessage("Iniciado convers�o para: " + getViewProcesso().getQtdeRegistros() + " registros: " + new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z").format(new Date(System.currentTimeMillis())));
				
				do {
					updateProgress(getViewProcesso().getQdeRegistrosConvertidos(), getViewProcesso().getQtdeRegistros());
				} while (getViewProcesso().isTerminatedConvert());
				updateProgress(getViewProcesso().getQdeRegistrosConvertidos(), getViewProcesso().getQtdeRegistros());
				updateMessage("Finalizado convers�o de: " + getViewProcesso().getQtdeRegistros() + " registros: " + new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z").format(new Date(System.currentTimeMillis())));
				
				updateMessage("Ao chegar aqui ele finalizou a thread de convers�o.");
				
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
				updateMessage("Convers�o de arquivo cancelado!");
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
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						txLogConversao.setText(txLogConversao.getText() + "\n" + task.getMessage());
					}
				});
			}
		});
		Platform.runLater(new Runnable() {
            @Override public void run() {
        		progressBarConversao.progressProperty().bind(task.progressProperty());
            }
        });
		return task;
	}
	
	private Task<Void> getTaskEscrita() {
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				updateMessage("Iniciado escrita para: " + getViewProcesso().getQtdeRegistros() + " registros: " + new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z").format(new Date(System.currentTimeMillis())));

				do {
					updateProgress(getViewProcesso().getQdeRegistrosEscritos(), getViewProcesso().getQtdeRegistros());
				} while (getViewProcesso().isTerminatedEscrever());
				updateProgress(getViewProcesso().getQdeRegistrosEscritos(), getViewProcesso().getQtdeRegistros());
				updateMessage("Finalizado de escrever: " + getViewProcesso().getQtdeRegistros() + " registros: " + new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z").format(new Date(System.currentTimeMillis())));
				updateMessage("Ao chegar aqui ele finalizou a thread de escrita.");
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
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						txLogEscrita.setText(txLogEscrita.getText() + "\n" + task.getMessage());
					}
				});
			}
		});
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				progressBarEscreve.progressProperty().bind(task.progressProperty());
			}
		});
		return task;
	}
	
	private synchronized  void setViewProcesso(ViewProcesso vw){
		this.viewProcessoGlobal = vw; 
	}
	
	private synchronized ViewProcesso getViewProcesso() {
		return this.viewProcessoGlobal;
	}
}
