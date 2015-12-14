package main;

import java.util.*;

/**
 * The BusType class defines bus type objects that hold
 * several characteristics of the type such as make, model,
 * capacities etc.
 */
public class BusType {
	/** the make of this bus type  */
	private final String make;
	/** the model of this bus type  */
	private final String model;
	/** the seated capacity of this bus type  */
	private final int seatedCapacity;
	/** the standing capacity of this bus type  */
	private final int standingCapacity;
	/** a data structure wherin all bus type objects are stored  */
	private static List<BusType> type = new ArrayList<>();

	/**
	 * Creates a bus type and add it to the type list.
	 *
	 * @param make the make of this bus type.
	 * @param model the model of this bus type.
	 * @param seatedcapacity the seated capacity of this bus type.
	 * @param standingcapacity the standing capacity of this bus type.
	 */
	public BusType(String make, String model, int seatedcapacity, int standingcapacity){
		this.make = make;
		this.model = model;
		this.seatedCapacity = seatedcapacity;
		this.standingCapacity = standingcapacity;
		type.add(this);
	}

	/**
	 * Get the make of this bus type.
	 *
	 * @return make the make of the bus type.
	 */
	public String getMake(){
		return make;
	}

	/**
	 * Get the model of this bus type.
	 *
	 * @return model the make of the bus type.
	 */
	public String getModel(){
		return model;
	}

	/**
	 * Get the seated capacity of this bus type.
	 *
	 * @return seatedCapacity the seated capacity of the bus type.
	 */
	public int getSeatedCapacity(){
		return seatedCapacity;
	}

	/**
	 * Get the seated capacity of this bus type.
	 *
	 * @return standingCapacity the standing capacity of the bus type.
	 */
	public int getStandingCapacity(){
		return standingCapacity;
	}

	/**
	 * Get the total capacity of this bus type.
	 *
	 * @return getSeatedCapacity + getStandingCapacity the total capacity of the bus type.
	 */
	public int getTotalCapacity(){
		return this.getSeatedCapacity() + this.getStandingCapacity();
	}
}
