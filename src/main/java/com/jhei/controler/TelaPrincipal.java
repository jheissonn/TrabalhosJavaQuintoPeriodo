package com.jhei.controler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jhei.utils.TelaCaminho;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class TelaPrincipal implements Initializable {
	@FXML
	private ListView<TelaCaminho> listView;
	private ArrayList<TelaCaminho> listaDePaineis = new ArrayList<>();
	private File file;
	@FXML
	private BorderPane borderPrincipal;
	private Label labelFile;
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		criaDadosListView();
		addListView();
		addBtnFileChooser();
		iniciarFileDialog();
	}
	
	private void iniciarFileDialog() {
		Pane pane = carregaFXML("/com/jhei/view/FileDialog.fxml");
		borderPrincipal.setCenter(pane);
	}
	
	private void addListView() {
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TelaCaminho>() {
			@Override
			public void changed(ObservableValue<? extends TelaCaminho> arg0, TelaCaminho arg1, TelaCaminho arg2) {
				Pane pane = carregaFXML(arg2.getFXML());
				borderPrincipal.setCenter(pane);
			}
		});
	}
	
	public void trataMenu(ActionEvent event) {
		String menuClicado = ((MenuItem) event.getSource()).getText();
		for (TelaCaminho painel : listaDePaineis) {
			if (painel.getIdentificacao().equals(menuClicado)) {
				borderPrincipal.setCenter(carregaFXML(painel.getFXML()));
			}
		}
	}
	
	private Pane carregaFXML(String fxml) {
		try {
			return FXMLLoader.load(getClass().getResource(fxml));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	private void criaDadosListView() {
		listaDePaineis.add(new TelaCaminho("FileDialog", "/com/jhei/view/FileDialog.fxml"));

		listView.setItems(FXCollections.observableArrayList(listaDePaineis));
	}
	
	private void addBtnFileChooser() {
		Button btn = new Button();
		labelFile = new Label();
		btn.setText("Open FileChooser");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All files", "*.*");

				FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().add(extFilter);

				// Show open file dialog to select one file. 
				file = fileChooser.showOpenDialog(null);
				if (file != null) {
					labelFile.setText(file.getPath());
				}
			}
		});

		VBox vBox = new VBox();
		vBox.getChildren().addAll(btn);
		borderPrincipal.setCenter(vBox);
	}
}
