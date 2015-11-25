package main;

import java.util.ArrayList;

public class BusType {
	private final String make;
	private final String model;
	private final int seatedCapacity;
	private final int standingCapacity;
	private static ArrayList<BusType> type = new ArrayList<BusType>();

	public BusType(String make, String model, int seatedcapacity, int standingcapacity){
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
			}
		System.out.println("End of list");
		System.out.println("number of entries in ArrayList type: " + type.size());
		System.out.println("");
		//end test
		}
		*/
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