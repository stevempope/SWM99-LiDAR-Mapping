package lidarMapping;

import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
public class AppController {

	private App view;
	private VLsensor v;
	private Integer a;
	private Map m;
	private Processor pr;
	private Pathfinder pf;
	private Waypoint dest;
	private Path pa;
	private int counter;

	public AppController() {		
		view = new App();
		v = new VLsensor(Orientation.antiClockwise);
		a = 10;
		m = new Map();
		pf = new Pathfinder();
		pa = new Path();
		counter = 0;
	}


	@FXML protected void handleSetAgentSize(ActionEvent event) {
		//TODO add text box to capture size
		System.out.print(event.getSource());
	}

	@FXML protected void handleSenseCall(ActionEvent event) {
		if(counter < 4) {
		System.out.printf("%s \n",Arrays.toString(v.sense(counter)));
		counter++;
		}
		else {
			System.out.printf("Sorry, no more returns in the set...Returning to original \n");
			counter = 0;
		}
		System.out.println(event.getSource());
	}

	@FXML protected void handlePathfind(ActionEvent event) {
		System.out.print(event.getSource());
	}

	@FXML protected void handleCompleteRun(ActionEvent event) {
		System.out.print(event.getSource());
	}

	@FXML protected void handleAlgorithmChange(ActionEvent event) {
		System.out.print(event.getSource());
	}

	@FXML protected void handleBasicSense(ActionEvent event) {
		System.out.print(event.getSource());
	}

	@FXML protected void handleMediumSense(ActionEvent event) {
		System.out.print(event.getSource());
	}

	@FXML protected void handleExit(ActionEvent event) {
		System.exit(0);
	}
}
