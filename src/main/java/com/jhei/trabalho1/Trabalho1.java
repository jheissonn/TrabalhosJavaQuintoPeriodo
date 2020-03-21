package com.jhei.trabalho1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Trabalho1 extends Application {
	private static Stage primaryStage;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			Trabalho1.primaryStage = primaryStage;
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("/com/jhei/view/TelaPrincipal.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		/*
		primaryStage.setTitle("Trabalho 1");
		final Label labelFile = new Label();

		Button btn = new Button();
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
		vBox.getChildren().addAll(btn, labelFile);

		StackPane root = new StackPane();
		root.getChildren().add(vBox);
		primaryStage.setScene(new Scene(root, 300, 250));
		primaryStage.show();
		*/
	}
}
