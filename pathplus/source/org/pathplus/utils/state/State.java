package org.pathplus.utils.state;

/**
 * 
 * @author levi.lovelock
 *
 * @param <State> to make the state comparable for use in Java Priority Queues.
 */

public interface State<T extends State<T>> extends Comparable<T>{
	
	
	
	// Calculates and returns the Heuristic Value for a given State.
	public double calcHValue();
	
	// Generates and sets the key used to store State in a Hashtable.
	public void generateKey();
	
	// Returns the set of States which are reachable from this State.
	public T[] getNeighbours();
	
	
	// Getters implemented by BaseState
	//===================================
	
	// Returns the distance from the start state
	public double getGVal();
	
	// Returns the heuristic estimate to a goal state
	public double getHVal();
	
	// Returns the g-value added to the h-value
	public double getFVal();

	// Returns the unique key used to store State in a Hashtable.
	public int getKey();
	
	public void setGoalState(T goalState);
	
	public T getGoalState();
	
	public void setParent(T parent);
	
	public T getParent();
	
}
