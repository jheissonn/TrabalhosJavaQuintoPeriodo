package com.jhei.controler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import com.google.gson.Gson;
import com.jhei.utils.JsonBrazil;

import javafx.concurrent.Task;
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
	private Integer totalLines;
	private Task<Object> tarefa;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		txCaminho.setText("C:\\Users\\USER\\Desktop\\5º Periodo\\Java\\brasil\\brasil\\brasil.csv");
		txCaminhoJson.setText("C:\\Users\\USER\\Desktop\\5º Periodo\\Java\\brasil\\brasil");
		progressBar.setProgress(0);		
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
			tarefa = criarTarefa();
			progressBar.progressProperty().unbind();
            progressBar.progressProperty().bind(tarefa.progressProperty());
			lbProgress.textProperty().bind(tarefa.messageProperty());
            new Thread(tarefa).start();
		}
		else {
			  Alert alert = new Alert(AlertType.INFORMATION);
              alert.setTitle("Campos obrigatórios.");
              alert.setHeaderText("Por favor, preencha os campos obrigatórios");
              alert.setContentText("");
        alert.showAndWait();
		}
			 
			
	}
	
	public Task<Object> criarTarefa() {
        return new Task<Object>() {
            @Override
            protected Object call() throws Exception {
            	String arquivoCSV = txCaminho.getText();
				
        		try {
        			totalLines = countLines(arquivoCSV);
        		} catch (IOException e1) {
        			e1.printStackTrace();
        		}
        		List<JsonBrazil> objtosEmJson = new ArrayList<JsonBrazil>();
        	    BufferedReader br = null;
        	    String linha = "";
        	    String csvDivisor = ",";
        	    try {
        	    	System.out.println("Iniciando leitura: " + getData());
        			Integer cont = 0;        			
        			br = new BufferedReader(new FileReader(arquivoCSV));
        			while ((linha = br.readLine()) != null) {
        				if (cont != 0) {
        					
        					String[] campos = linha.split(csvDivisor);
        					JsonBrazil newObj = new JsonBrazil();
        					setCamposJson(campos, newObj);
        					objtosEmJson.add(newObj);
        				}		cont++;

        				updateMessage("Processado:" + cont.toString()  + "de" + totalLines.toString());
        				updateProgress(cont, totalLines);
        			}

        	    } catch (IOException e) {
        	        e.printStackTrace();
        	    } finally {
        	        if (br != null) {
        	            try {
        	                br.close();
        	            } catch (IOException e) {
        	                e.printStackTrace();
        	            }
        	        }
        	    }
        	    System.out.println("Leitura finalizada: " + getData());
        	    updateMessage("Leitura finalizada.");
        	    convertObjToJson(objtosEmJson);
                return true;
            }
        };
    }
	
	private String getData() {
		return new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss.SSS z").format(new Date(System.currentTimeMillis()));
	}
	
	private void convertObjToJson(List<JsonBrazil> objtosEmJson) {
		System.out.println("Finalizando conversão: " + getData());
		Gson convert = new Gson();
		String teste = convert.toJson(objtosEmJson);
		System.out.println("Finalizando conversão: " + getData());
		System.out.println("Iniciando escrita: " + getData());
		FileWriter writeFile = null;
        
         
        try{
            writeFile = new FileWriter(txCaminhoJson.getText() + "\\saida.json");
            //Escreve no arquivo conteudo do Objeto JSON
            writeFile.write(teste);
            writeFile.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Finalizando escrita: " + getData());
	}
	
	

	public Integer countLines(String filename) throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(filename));
		Integer cnt = 0;
		@SuppressWarnings("unused")
		String lineRead = "";
		while ((lineRead = reader.readLine()) != null) {
		}

		cnt = reader.getLineNumber();
		reader.close();
		return cnt;
	}

	private void setCamposJson(String[] campos, JsonBrazil obj) {
		obj.setNumber(Long.parseLong(campos[0])); 
		obj.setGender(campos[1]);
		obj.setNameSet(campos[2]);
		obj.setTitle(campos[3]);
		obj.setGivenName(campos[4]);
		obj.setSurname(campos[5]);
		obj.setStreetAddress(campos[6]);
		obj.setCity(campos[7]);
		obj.setState(campos[8]);
		obj.setZipCode(campos[9]);
		obj.setCountryFull(campos[10]);
		obj.setEmailAddress(campos[11]);
		obj.setUsername(campos[12]);
		obj.setPassword(campos[13]);
		obj.setTelephoneNumber(campos[14]);
		obj.setBirthday(campos[15]);
		obj.setCCType(campos[16]);
		obj.setCCNumber(Long.parseLong(campos[17]));
		obj.setCVV2(Integer.parseInt(campos[18]));
		obj.setCCExpires(campos[19]);
		obj.setNationalID(campos[20]);
		obj.setColor(campos[21]);
		obj.setKilograms(Double.parseDouble(campos[22]));
		obj.setCentimeters(Integer.parseInt(campos[23]));
		obj.setGUID(campos[24]);
	}
	
}
