package com.jhei.controler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFileChooser;

import com.jhei.utils.ContolQueue;
import com.jhei.utils.EscreverJson;
import com.jhei.utils.ParseJson;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class FileDialog implements Initializable {
	
	@FXML
	private TextField txCaminho, txCaminhoJson;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private Label lbProgress;
	private static final int quantidadeCpu = Runtime.getRuntime().availableProcessors();
	private ExecutorService pool; 
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		progressBar.setProgress(0);		
		pool = Executors.newFixedThreadPool(quantidadeCpu);
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
		Reader reader;
		try {
			reader = Files.newBufferedReader(Paths.get(txCaminho.getText()));
		
	    CSVReader csvReader = new CSVReaderBuilder(reader)
	                            .withSkipLines(1)//para o caso do CSV ter cabeçalho.
	                            .build();
	    List<String[]> linhas = csvReader.readAll();
	    progressBar.setProgress(-1);
	    pool.execute(new ContolQueue(linhas, progressBar));
	    pool.execute(new ParseJson());
	    pool.execute(new EscreverJson(new FileWriter(txCaminhoJson.getText() + "\\saida.json")));
	    
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
