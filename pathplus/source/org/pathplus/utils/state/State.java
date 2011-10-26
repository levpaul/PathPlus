package org.pathplus.utils.state;

/**
 * 
 * @author levi.lovelock
 *
 * @param <State> to make the state comparable for use in Java Priority Queues.
 */

public interface State<State> extends Comparable<State>{
	
	
	
	// Calculates and returns the Heuristic Value for a given State.
	public double calcHValue();
	
	// Generates and sets the key used to store State in a Hashtable.
	public void generateKey();
	
	// Returns the set of States which are reachable from this State.
	public <T extends State> T[] getNeighbours();
	
	
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
	
	public void setGoalState(State goalState);
	
	public State getGoalState();
	
	public void setParent(State parent);
	
	public State getParent();
	
}
