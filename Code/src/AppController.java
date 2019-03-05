package lidarMapping;

import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
		dest = new Waypoint();
		counter = 0;
	}

	@FXML TextField agentSize;
	@FXML protected void handleSetAgentSize(ActionEvent event) {
		a = Integer.parseInt(agentSize.getText());
		agentSize.setText("Agent Size set to: " + a.toString());
		System.out.println(event.getSource());
	}

	@FXML protected void handleSenseCall(ActionEvent event) {
		if(counter < v.getDataSetSize()) {
			System.out.printf("%s \n",Arrays.toString(v.sense(counter)));
			counter++;
		}
		else {
			System.out.printf("Sorry, no more returns in the set...Returning to original \n");
			counter = 0;
		}
		System.out.println(event.getSource());
	}
	
	@FXML AnchorPane mapPane;
	@FXML Canvas can;
	@FXML protected void canClick(MouseEvent event) {
		System.out.printf("Canvas height: %f, Canvas width: %f\n", mapPane.getHeight(), mapPane.getWidth());
		System.out.printf("Click! X = %f Y = %f \n",event.getX(), event.getY());
		System.out.println("Click!");
		//dest.setAngle((int)Math.toDegrees(Math.atan2(Math.abs(mapPane.getHeight()/2) - event.getY(), Math.abs((mapPane.getWidth()/2) - event.getX()))));
		//System.out.printf("%d\n", dest.getAngle());
		dest.setDistance ((int)(Math.sqrt(Math.abs(((can.getHeight()/2) - event.getY()) * ((can.getHeight()/2) - event.getY())) + Math.abs(((can.getWidth()/2) - event.getX()) * ((can.getWidth()/2) - event.getX())))));
		System.out.println(dest.getDistance().toString());
		//dest = 0;
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
