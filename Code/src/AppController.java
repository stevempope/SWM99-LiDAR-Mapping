package lidarMapping;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
public class AppController {

	private VLsensor v;
	private Map m;
	private Processor pr;
	private Pathfinder pf;
	private Waypoint dest;
	private Path pa;
	private int angle;
	private Agent theAgent;
	private boolean lidarToggle;
	private boolean destinationToggle;
	private boolean pathToggle;
	private double lastX;
	private double lastY;
	private boolean mapToggle;

	public AppController() {		
		v = new VLsensor();
		m = new Map();
		pf = new Pathfinder();
		pa = new Path();
		dest = new Waypoint(0,0);
		theAgent = new Agent();
		lidarToggle = false;
		destinationToggle = false;
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
		if (event.getX() < can.getWidth()/2) {
			if (event.getY() < can.getHeight()) {
				angle = Math.abs(angle - 180);
			}
			else {
				angle = Math.abs(angle + 180);
			}
		}
		else if (event.getY() > can.getHeight()/2){
			angle = Math.abs(angle + 360);
		}
		dest.setAngle((angle + theAgent.getPosition().getAngle()) % 360);
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
			paint();
		}
		else {
			System.out.println("Please select a destination by clicking the Map and ensure you have called for data from the LiDAR sensor!");
		}
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
		theAgent.setVisibility(!theAgent.isVisible());
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
	
	@FXML private void handleObjectToggle() {
		mapToggle = !mapToggle;
		paint();
	}

	/*
	 * Draw methods
	 */
	private void paint() {
		can.getGraphicsContext2D().clearRect(0, 0, can.getWidth(), can.getHeight());
		if (theAgent.isVisible()) {
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
		if (mapToggle) {
			drawMap();
		}
	}

	private void drawAgent() {
		can.getGraphicsContext2D().setFill(Color.DARKCYAN);
		can.getGraphicsContext2D().fillRect(mapPane.getWidth()/2 - theAgent.getSize()/2, mapPane.getHeight()/2 - theAgent.getSize()/2, theAgent.getSize(), theAgent.getSize());
	}

	private void drawDest() {
		if (dest != null) {
			can.getGraphicsContext2D().setFill(Color.CHARTREUSE);
			can.getGraphicsContext2D().fillOval(lastX,lastY, 10, 10);
		}
	}

	private void drawPath() {
		CartesianPair pathXY = new CartesianPair(pa.getPath().get(0));
		can.getGraphicsContext2D().setFill(Color.DARKGOLDENROD);
		can.getGraphicsContext2D().strokeLine(mapPane.getWidth()/2, mapPane.getHeight()/2, pathXY.getX() + mapPane.getWidth()/2, pathXY.getY() + mapPane.getHeight()/2);
	}

	private void drawSense() {
			can.getGraphicsContext2D().setFill(Color.CORNFLOWERBLUE);
			int arrPos = 0;
			for(ReturnSet r : m.getBlockages()) {
				for (LReturn l: r.getBlockages()) {
					for(Integer i : l.getBlocks()) {
						CartesianPair range = new CartesianPair(new Waypoint(l.getStart()+arrPos,i));
						if (v.getOrientation() == Orientation.antiClockwise) {
							range.setY(-range.getY());
						}
						can.getGraphicsContext2D().fillOval(range.getX() + (mapPane.getWidth()/2), range.getY() + (mapPane.getHeight()/2),5,5);
						arrPos++;
					}
					arrPos = 0;
				}
			}
	}

	private void drawMap() {
		int arrPos = 0;
		for(ReturnSet r : m.getBlockages()) {
			for (LReturn l: r.getBlockages()) {
				System.out.println(l.getBlocks());
				
				can.getGraphicsContext2D().setFill(Color.CRIMSON);
				CartesianPair start = new CartesianPair(new Waypoint(l.getStart(), l.getStartDist()));
				if (v.getOrientation() == Orientation.antiClockwise) {
					start.setY(-start.getY());
				}
				can.getGraphicsContext2D().fillOval(start.getX() + (mapPane.getWidth()/2), start.getY() + (mapPane.getHeight()/2), 10 , 10);
				
				can.getGraphicsContext2D().setFill(Color.BLUEVIOLET);
				CartesianPair end = new CartesianPair(new Waypoint(l.getEnd(), l.getEndDist())); 
				if(v.getOrientation() == Orientation.antiClockwise) {
					end.setY(-end.getY());
				}
				can.getGraphicsContext2D().fillOval(can.getWidth()/2 + end.getX(), can.getHeight()/2 + end.getY(),10 , 10);
				
				can.getGraphicsContext2D().setFill(Color.DARKOLIVEGREEN);
				for(Integer i : l.getBlocks()) {
					CartesianPair block = new CartesianPair(new Waypoint(l.getStart() + arrPos, i));
					if (v.getOrientation() == Orientation.antiClockwise) {
						block.setY(-block.getY());
					}
					can.getGraphicsContext2D().fillOval(can.getWidth()/2 + block.getX(), can.getHeight()/2 + block.getY(),5 , 5);
					arrPos++;
				}
				arrPos = 0;
			}
		}
	}
}
