package org.pathplus.utils.Internal;


public interface IState<T> extends Comparable<T>{
	
	
	// Getters
	//===========================
	
	// Returns the distance from the start state
	public double getGVal();
	
	// Returns the heuristic estimate to a goal state
	public double getHVal();
	
	// Returns the g-value added to the h-value
	public double getFVal();

	// Returns the unique key used 
	public double getKey();
	

	
}
