package lidarMapping;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class App extends Application{
	
	public static void main (String [] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage theStage) throws Exception {
		Button btn0 = new Button ("Set Agent Size");
		Button btn1 = new Button ("Scan");
		Button btn2 = new Button ("Navigate to...");
		Button btn3 = new Button ("Change Algorithm");
		Button btn4 = new Button ("Show Statistics");
		Button btn5 = new Button("Exit");
		btn5.setOnAction( e -> {
			System.out.println("App will now exit");
			System.exit(0);
		});
		
		GridPane theGrid = new GridPane();
		theGrid.setAlignment(Pos.BASELINE_RIGHT);
		theGrid.setHgap(10);
		theGrid.setVgap(10);
		theGrid.setPadding(new Insets(25,25,25,25));
		theGrid.add(btn0, 0,0);
		theGrid.add(btn1, 0,1);
		theGrid.add(btn2, 0,2);
		theGrid.add(btn3, 0,3);
		theGrid.add(btn4, 0,4);
		theGrid.add(btn5, 0,5);
		Scene map = new Scene(theGrid, 500, 700);
		theStage.setTitle("Hello World!");
		theStage.setScene(map);
		theStage.show();
	}
	
}
