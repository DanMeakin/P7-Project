package main;

public class Stop {
	private final int id;
	private final String name;
	private final double latitude;
  private final double longitude;
	
	public Stop(int id, String name, double latitude, double longitude) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
    this.longitude = longitude; 
	}

	public int getID(){
		return id;
	}
	
	public String getName(){
		return name;
	}

	public double[] getLocation(){
		return new double[]{latitude, longitude};
	}

}
