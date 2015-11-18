package main;

public class BusStop {
	private final int id;
	private final String name;
	private final String location;
	
	public BusStop(int id, String name, String location) {
		this.id = id;
		this.name = name;
		this.location = location;
	}

	public int getID(){
		return id;
	}
	
	public String getName(){
		return name;
	}

	public String getLocation(){
		return location;
	}

}