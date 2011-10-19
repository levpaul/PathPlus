package org.pathplus.utils.state;



// TODO Must sort out the interface issue.
public abstract class State implements IState<State>{

	private double fValue, gValue, hValue;
	
	public State(State parent){
		gValue = parent.gValue + 1;
		calcHValue();
		fValue = gValue + hValue;
	}
	
	private void calcHValue(){
		// TODO this must be implemented
	}
	
	public double getGValue(){
		return gValue;
	}
	
	public double getHValue(){
		return hValue;
	}
	
	public double getFValue(){
		return fValue;
	}
	
	public int compareTo(State s){
		return (int) (this.fValue - s.fValue);
	}
	
}
