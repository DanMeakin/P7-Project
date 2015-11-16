package main;

import java.util.ArrayList;

public class BusType {
	private String make;
	private String model;
	private int seatedCapacity;
	private int standingCapacity;
	ArrayList<BusType> type = new ArrayList<BusType>();
	
	public BusType(String make, String model, int seatedcapacity, int standingcapacity){
		this.make = make;
		this.model = model;
		this.seatedCapacity = seatedcapacity;
		this.standingCapacity = standingcapacity;
		type.add(this);
		
		//For Testing whether entries are stored in ArrayList type
		for (int i=0; i<type.size(); i++){
			System.out.print(type.get(i).getMake() + " ");
			System.out.print(type.get(i).getModel() + " ");
			System.out.print(type.get(i).getSeatedCapacity() + " ");
			System.out.print(type.get(i).getStandingCapacity() + " ");
			System.out.println("");
			}
		System.out.println("End of list");
		
		if (type.size() > 2) {
			System.out.println("Arraylist has greater than two items stored in it!");
		//end test
		}
	}

	public String getMake(){
		return make;
	}
	
	public String getModel(){
		return model;
	}
	
	public int getSeatedCapacity(){
		return seatedCapacity;
	}
	
	public int getStandingCapacity(){
		return standingCapacity;
	}
}