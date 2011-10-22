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
	private BaseState goalState;
	
	// Constructors.
	
	public BaseState(BaseState goalState){
		this.goalState = goalState;
		gValue = 0;
		hValue = calcHValue();
		fValue = gValue + hValue;
		parent = null;
	}
	
	
	// Getters and Setters.
	
	public BaseState getGoalState(){
		return goalState;
	}
	
	public void setGoalState(BaseState goalState){
		this.goalState = goalState;
		hValue = calcHValue();
	}
	
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
