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
	private int angle;
	private Agent theAgent;
	private boolean lidarToggle;
	private boolean agentToggle;
	private boolean destinationToggle;
	private boolean pathToggle;
	private Integer [] tempSense;
	private boolean hasMap;
	private double lastX;
	private double lastY;

	public AppController() {		
		view = new App();
		v = new VLsensor(Orientation.antiClockwise);
		m = new Map();
		pf = new Pathfinder();
		pa = new Path();
		dest = new Waypoint(0,0);
		theAgent = new Agent();
		lidarToggle = false;
		agentToggle = false;
		destinationToggle = false;
		hasMap = false;
	}

	/*
	 * FXML Objects that we wish to manipulate
	 */

	@FXML AnchorPane mapPane;
	@FXML Canvas can;
	@FXML TextField agentSize;


	/*
	 * FXML Event Handlers for Button and click events
	 */
	@FXML protected void handleSetAgentSize(ActionEvent event) {
		Integer size = Integer.parseInt(agentSize.getText());
		if (size > 0) {
			theAgent.setSize(size);
			agentSize.setText("Agent Size set to: " + theAgent.getSize().toString());
			System.out.println(event.getSource());
			pr = new Processor(v , theAgent);
			paint();
		}
		else System.out.printf("Sorry, you selected an invalid agent size! - Perhaps choose one greater than 0?");

	}

	@FXML protected void handleSenseCall(ActionEvent event) {
		if(pr != null) {
			pr.updateMap(m);
			lidarToggle = true;
			paint();
		}
	}

	@FXML protected void canClick(MouseEvent event) {
		System.out.printf("Canvas height: %f, Canvas width: %f\n", mapPane.getHeight(), mapPane.getWidth());
		System.out.printf("Click! X = %f Y = %f \n",event.getX(), event.getY());
		angle = (int)Math.toDegrees(Math.atan2(Math.abs(mapPane.getHeight()/2) - event.getY(), Math.abs((mapPane.getWidth()/2) - event.getX())));
		angle = (angle + theAgent.getPosition().getAngle()) %360;
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
		lastX = event.getX();
		lastY = event.getY();
		paint();
	}

	@FXML protected void handlePathfind(ActionEvent event) {
		if(dest != null && m.getBlockages().size() > 0) {
			System.out.printf(" Dest = %d, %d \n", dest.getAngle(), dest.getDistance());
			pa = pf.pathfind(m, dest);
			hasMap = true;
			paint();
		}
		else {
			System.out.println("Please select a destination by clicking the Map and ensure you have called for data from the LiDAR sensor!");
		}
		System.out.print(event.getSource());

	}

	@FXML public void handleDrive(ActionEvent event) {
		if (dest.getAngle() != 0 && dest.getDistance() != 0) {
			if (tempSense !=  null) {
				if(pa.getPath().size() > 0) {
					Waypoint next = pa.popNextWaypoint();
					theAgent.setPosition(next);
					paint();
				}
				else {
					System.out.printf("Please use pathfind to generate a Path! \n");
				}
			}
			else {
				System.out.printf("Please call for a read from the LiDAR sensor \n");
			}
		}
		else {
			System.out.printf("Please select a Destination! \n");
		}
	}

	@FXML protected void handleCompleteRun(ActionEvent event) {
		//TODO must write ReturnSet Amalgamate
		paint();
		System.out.print(event.getSource());
	}

	@FXML protected void handleAlgorithmChange(ActionEvent event) {
		//TODO Need to write more than 1 or deprecate
		paint();
		System.out.print(event.getSource());
	}

	@FXML protected void handleBasicSense(ActionEvent event) {
		pr.updateMap(m);
		paint();
		System.out.print(event.getSource());
	}


	@FXML protected void handleMediumSense(ActionEvent event) {
		pr.smarterUpdateMap(m);
		paint();
		System.out.print(event.getSource());
	}

	@FXML protected void handleExit(ActionEvent event) {
		System.exit(0);
	}

	@FXML private void handleLiDARToggle(ActionEvent event) {
		lidarToggle = !lidarToggle;
		System.out.println(lidarToggle);
		paint();
	}

	@FXML private void handleAgentToggle() {
		agentToggle = !agentToggle;
		paint();
	}

	@FXML private void handleDestinationToggle() {
		destinationToggle = !destinationToggle;
		paint();
	}

	@FXML private void handlePathToggle() {
		pathToggle = !pathToggle;
		paint();
	}

	/*
	 * Draw methods
	 */
	private void paint() {
		can.getGraphicsContext2D().clearRect(0, 0, can.getWidth(), can.getHeight());
		if (agentToggle) {
			drawAgent();
		}
		if (destinationToggle) {
			drawDest();
		}
		if (pathToggle) {
			drawPath();
		}
		if (lidarToggle) {
			drawSense();
		}
		if(hasMap) {
			drawMap();
			//Must add a toggle for this layer!
		}
		//Paint depending on the values
	}

	private void drawAgent() {
		CartesianPair agentPos = new CartesianPair(theAgent.getPosition());
		can.getGraphicsContext2D().setFill(Color.DARKCYAN);
		can.getGraphicsContext2D().fillRect(agentPos.getX() + (mapPane.getWidth()/2), agentPos.getY() + (mapPane.getHeight()/2), theAgent.getSize(), theAgent.getSize());
	}

	private void drawDest() {
		if (dest != null) {
			can.getGraphicsContext2D().setFill(Color.CHARTREUSE);
			can.getGraphicsContext2D().fillOval(lastX,lastY, 10, 10);
		}
	}

	private void drawPath() {
		can.getGraphicsContext2D().setFill(Color.DARKGOLDENROD);
		can.getGraphicsContext2D().strokeLine(mapPane.getWidth()/2,mapPane.getHeight()/2 , getX(pa.getPath().get(0).getAngle(),pa.getPath().get(0).getDistance()), getY(pa.getPath().get(0).getAngle(),pa.getPath().get(0).getDistance()));
		//TODO refactor to agent position and for multiple waypoints in a path
	}

	private void drawSense() {
			can.getGraphicsContext2D().setFill(Color.CORNFLOWERBLUE);
			int arrPos = 0;
			for(ReturnSet r : m.getBlockages()) {
				for (LReturn l: r.getBlockages()) {
					for(Integer i : l.getBlocks()) {
						can.getGraphicsContext2D().fillOval(getX((arrPos + l.getStart()),i), getY((arrPos + l.getStart()),i),5,5);
						arrPos++;
					}
				}
			}

	}

	private void drawMap() {
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

	/*
	 * To be deprecated if possible
	 */

	private double getY(int angle, int dist) {
		if (v.getOrientation() == Orientation.antiClockwise) {
			return (-dist * (Math.sin(Math.toRadians(angle)))) + mapPane.getHeight()/2;
		}
		else return (dist * (Math.sin(Math.toRadians(angle)))) + mapPane.getHeight()/2;
	}

	private double getX(int angle, int dist) {
		return (dist * (Math.cos(Math.toRadians(angle)))) + mapPane.getWidth()/2;
	}

}
