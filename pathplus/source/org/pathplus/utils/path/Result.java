package org.pathplus.utils.path;

import org.pathplus.utils.state.State;


/**
 * 
 * @author levi.lovelock
 *
 */
public interface Result <T extends State<T>>{

	public T[] toArray();
	
	public double getLength();
	
	public T getGoalState();
	public void setGoalState(T goalState);
	
	public T getStartState();
	public void setStartState(T startState);
	
	
	
}
