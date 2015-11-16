package main;

import java.util.ArrayList;

public class BusType {
	private String make;
	private String model;
	private int seatedCapacity;
	private int standingCapacity;

	
	public BusType(String make, String model, int seatedcapacity, int standingcapacity){
		ArrayList<BusType> type;
		type = new ArrayList<BusType>();
		this.make = make;
		this.model = model;
		this.seatedCapacity = seatedcapacity;
		this.standingCapacity = standingcapacity;
		type.add(this);
		
		/*
		//For Testing whether entries are stored in ArrayList type
		for (int i=0; i<type.size(); i++){
		System.out.print(type.get(i).getMake() + " ");
		System.out.print(type.get(i).getModel() + " ");
		System.out.print(type.get(i).getSeatedCapacity() + " ");
		System.out.print(type.get(i).getStandingCapacity() + " ");
		System.out.println("");
		i++;
		}
		*/
	}
	
	/*
	//Also for testing purposes
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
	*/
}