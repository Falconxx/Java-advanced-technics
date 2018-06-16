package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainView extends Application {

	public Stage primaryStage;
	private AnchorPane rootLayout;
	


	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Titelek");
		
		initRootLayout();
		
		//showPersonOverview();
	}
	public static void main(String[] args) {
		launch(args);
	}
	
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainView.class.getResource("MainView.fxml"));
			rootLayout = (AnchorPane) loader.load();
			AnchorPane.setTopAnchor(rootLayout,0.0);
			AnchorPane.setBottomAnchor(rootLayout,0.0);
			AnchorPane.setLeftAnchor(rootLayout,0.0);
			AnchorPane.setRightAnchor(rootLayout,0.0);
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			rootLayout.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
			scene.getStylesheets().add("application/mainViewStyle.css");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
