package lidarMapping;

import java.util.ArrayList;

public class Map {
	private ArrayList<ReturnSet> reads;
	
	public Map() {
		reads = new ArrayList<ReturnSet>();		
	}
	
	public boolean addScan(ReturnSet read) {
		reads.add(read);
		return true;
	}
	
	public boolean clearMap() {
		reads.clear();
		return true;
	}
	
	public ArrayList<ReturnSet> getBlockages(){
		return reads;
	}

}