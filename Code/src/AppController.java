package lidarMapping;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
public class AppController {

	private App view;
	private VLsensor v;
	private Map m;
	private Processor pr;
	private Pathfinder pf;
	private Waypoint dest;
	private Path pa;
	private int counter;
	private int angle;
	private Agent theAgent;

	public AppController() {		
		view = new App();
		v = new VLsensor(Orientation.antiClockwise);
		m = new Map();
		pf = new Pathfinder();
		pa = new Path();
		dest = new Waypoint();
		counter = 0;
		theAgent = new Agent();
	}
	@FXML AnchorPane mapPane;
	@FXML Canvas can;
	@FXML TextField agentSize;

	@FXML protected void handleSetAgentSize(ActionEvent event) {
		theAgent.setSize(Integer.parseInt(agentSize.getText()));
		agentSize.setText("Agent Size set to: " + theAgent.getSize().toString());
		System.out.println(event.getSource());
		pr = new Processor(v , theAgent.getSize());
		drawAgent();
	}

	private void drawAgent() {
		can.getGraphicsContext2D().setFill(Color.DARKCYAN);
		can.getGraphicsContext2D().fillRect(getX(theAgent.getPosition().getAngle(), theAgent.getPosition().getDistance()), getY(theAgent.getPosition().getAngle(), theAgent.getPosition().getDistance()) , theAgent.getSize(), theAgent.getSize());	
	}

	@FXML protected void handleSenseCall(ActionEvent event) {
		if(counter < v.getDataSetSize()) { 
			can.getGraphicsContext2D().setFill(Color.CORNFLOWERBLUE);
			int arrPos = 0;
			for(int i : v.sense(counter)) {
				if (i > 0) {
					can.getGraphicsContext2D().fillOval(getX(arrPos,i), getY(arrPos,i), 5, 5);
				}
				arrPos++;
			}
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
		drawDest();
	}

	private void drawDest() {
		if (dest != null) {
			can.getGraphicsContext2D().setFill(Color.CHARTREUSE);
			can.getGraphicsContext2D().fillOval(getX(dest.getAngle(), dest.getDistance()), getY(dest.getAngle(), dest.getDistance()), 10, 10);
		}
	}

	@FXML protected void handlePathfind(ActionEvent event) {
		if(dest != null && m.getBlockages().size() > 0) {
			System.out.printf(" Dest = %d, %d \n", dest.getAngle(), dest.getDistance());
			pa = pf.pathfind(m, dest);
			can.getGraphicsContext2D().setFill(Color.DARKGOLDENROD);
			can.getGraphicsContext2D().strokeLine(mapPane.getWidth()/2,mapPane.getHeight()/2 , getX(pa.getPath().get(0).getAngle(),pa.getPath().get(0).getDistance()), getY(pa.getPath().get(0).getAngle(),pa.getPath().get(0).getDistance()));
			//TODO refactor to agent position and for multiple waypoints in a path
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
		//TODO Need to write more than 1 or deprecate
		System.out.print(event.getSource());
	}

	@FXML protected void handleBasicSense(ActionEvent event) {
		pr.updateMap(m);
		drawMap(m);
		System.out.print(event.getSource());
	}

	private void drawMap(Map theMap) {
		can.getGraphicsContext2D().clearRect(0, 0, can.getWidth(), can.getHeight());
		int arrPos = 0;
		for(ReturnSet r : m.getBlockages()) {
			for (LReturn l: r.getBlockages()) {
				System.out.println(l.getBlocks());
				can.getGraphicsContext2D().setFill(Color.CRIMSON);
				can.getGraphicsContext2D().fillOval(getX(l.getStart(), l.getStartDist()), getY(l.getStart(), l.getStartDist()),10 , 10);
				can.getGraphicsContext2D().setFill(Color.BLUEVIOLET);
				can.getGraphicsContext2D().fillOval(getX(l.getEnd(), l.getEndDist()), getY(l.getEnd(), l.getEndDist()),10 , 10);
				can.getGraphicsContext2D().setFill(Color.DARKOLIVEGREEN);
				for(Integer i : l.getBlocks()) {
					can.getGraphicsContext2D().fillOval(getX((arrPos + l.getStart()),i), getY((arrPos + l.getStart()),i),5,5);
					arrPos++;
				}
			}
		}
	}

	@FXML protected void handleMediumSense(ActionEvent event) {
		pr.smarterUpdateMap(m);
		drawMap(m);
		System.out.print(event.getSource());
	}

	@FXML protected void handleExit(ActionEvent event) {
		System.exit(0);
	}
}
