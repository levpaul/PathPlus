package org.pathplus.utils.state;

/**
 * 
 * @author levi.lovelock
 *
 */

public abstract class BaseState implements State<BaseState>{

	// Local Variables.
	
	private double fValue, gValue, hValue;
	private BaseState parent;
	
	// Constructors.
	
	public BaseState(){
		gValue = 0;
		hValue = calcHValue();
		fValue = gValue + hValue;
		parent = null;
	}
	
	/**
	 * 
	 * @param parent
	 */
	public BaseState(BaseState parent){
		gValue = parent.gValue + 1;
		hValue = calcHValue();
		fValue = gValue + hValue;
	}
	
	
	// Getters and Setters.
	
	public BaseState getParent(){
		return parent;
	}
	
	public void setParent(BaseState parent){
		this.parent = parent;
	}
	
	public double getGVal(){
		return gValue;
	}
	
	public double getHVal(){
		return hValue;
	}
	
	public double getFVal(){
		return fValue;
	}
	
	public int compareTo(BaseState s){
		return (int) (this.fValue - s.fValue);
	}
	
	public BaseState[] getNeighbours(){
		return null;
	}
	
}
