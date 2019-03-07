package lidarMapping;

import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
	private int angle;

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
	@FXML AnchorPane mapPane;
	@FXML Canvas can;
	@FXML TextField agentSize;

	@FXML protected void handleSetAgentSize(ActionEvent event) {
		a = Integer.parseInt(agentSize.getText());
		agentSize.setText("Agent Size set to: " + a.toString());
		System.out.println(event.getSource());
		GraphicsContext gc = can.getGraphicsContext2D();
		gc.setFill(Color.DARKCYAN);
		gc.fillRect(mapPane.getWidth()/2 - a/2, mapPane.getHeight()/2 - a/2, a, a);
	}

	@FXML protected void handleSenseCall(ActionEvent event) {
		if(counter < v.getDataSetSize()) {
			GraphicsContext gc =  can.getGraphicsContext2D();
			gc.setFill(Color.CORNFLOWERBLUE);
			int arrPos = 0;
			for(int i : v.sense(counter)) {
				if (i > 0) {
					gc.fillOval(getX(arrPos,i), getY(arrPos,i), 5, 5);
				}
				arrPos++;
			}
			//System.out.printf("%s \n",Arrays.toString(v.sense(counter)));
			counter++;
		}
		else {
			System.out.printf("Sorry, no more returns in the set...Returning to original \n");
			counter = 0;
		}
		System.out.println(event.getSource());
		//TODO printing to the canvas
	}

	private double getY(int angle, int dist) {
		if (v.getOrientation() == Orientation.antiClockwise) {
			return (-dist * (Math.sin(Math.toRadians(angle)))) + mapPane.getHeight()/2;
		}
		else return (dist * (Math.sin(Math.toRadians(angle)))) + mapPane.getHeight()/2;
	}

	private double getX(int angle, int dist) {
		return (dist * (Math.cos(Math.toRadians(angle)))) + mapPane.getWidth()/2;
	}

	@FXML protected void canClick(MouseEvent event) {
		System.out.printf("Canvas height: %f, Canvas width: %f\n", mapPane.getHeight(), mapPane.getWidth());
		System.out.printf("Click! X = %f Y = %f \n",event.getX(), event.getY());
		angle = (int)Math.toDegrees(Math.atan2(Math.abs(mapPane.getHeight()/2) - event.getY(), Math.abs((mapPane.getWidth()/2) - event.getX())));
		if (event.getX() < can.getWidth()/2) {
			if (event.getY() < can.getHeight()) {
				dest.setAngle(Math.abs(angle - 180));
			}
			else {
				dest.setAngle(Math.abs(angle + 180));
			}
		}
		else if (event.getY() > can.getHeight()/2){
			dest.setAngle(Math.abs(angle + 360));
		}
		else {
			dest.setAngle(angle);
		}
		System.out.printf("%d\n", dest.getAngle());
		dest.setDistance ((int)(Math.sqrt(Math.abs(((can.getHeight()/2) - event.getY()) * ((can.getHeight()/2) - event.getY())) + Math.abs(((can.getWidth()/2) - event.getX()) * ((can.getWidth()/2) - event.getX())))));
		System.out.println(dest.getDistance().toString());
	}

	@FXML protected void handlePathfind(ActionEvent event) {
		if(dest != null && m.getBlockages().size() > 0) {
			System.out.printf(" Dest = %d, %d \n", dest.getAngle(), dest.getDistance());
			pf.pathfind(m, dest);
		}
		else {
			System.out.println("Please select a destination by clicking the Map and ensure you have called for data from the LiDAR sensor!");
		}
		System.out.print(event.getSource());
	}

	@FXML protected void handleCompleteRun(ActionEvent event) {
		//TODO must write ReturnSet Amalgamate
		System.out.print(event.getSource());
	}

	@FXML protected void handleAlgorithmChange(ActionEvent event) {
		//TODO Need to write more than 1
		System.out.print(event.getSource());
	}

	@FXML protected void handleBasicSense(ActionEvent event) {
		pr = new Processor(v, a);
		pr.updateMap(m);
		//TODO print result to canvas
		System.out.print(event.getSource());
	}

	@FXML protected void handleMediumSense(ActionEvent event) {
		pr = new Processor(v, a);
		pr.smarterUpdateMap(m);
		//TODO May need to alter this so calling multiple times move through data set
		System.out.print(event.getSource());
	}

	@FXML protected void handleExit(ActionEvent event) {
		System.exit(0);
	}
}
