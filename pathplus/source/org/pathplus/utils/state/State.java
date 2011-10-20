package org.pathplus.utils.state;

/**
 * 
 * @author levi.lovelock
 *
 * @param <T> to make the state comparable for use in Java Priority Queues.
 */

public interface State<T> extends Comparable<T>{
	
	
	
	
	public double calcHValue();
	
	
	// Getters implemented by BaseState
	//===================================
	
	// Returns the distance from the start state
	public double getGVal();
	
	// Returns the heuristic estimate to a goal state
	public double getHVal();
	
	// Returns the g-value added to the h-value
	public double getFVal();

	// Returns the unique key used 
	public double getKey();
	

	
}
