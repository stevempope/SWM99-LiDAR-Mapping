package lidarMapping;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The view is the visual representation of how my model is displayed to the user.
 *
 * @author Stephen Pope 15836791
 * @version 0.1
 */

public class App extends Application{
	Stage ps;
	Parent pa;
	Scene sc;

	public static void main (String [] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("SWM99 LiDAR Optimisation");
		Parent root = FXMLLoader.load(getClass().getResource("SWM99.FXML"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		ps = primaryStage;
		pa = root;
		sc = scene;
	}
}
