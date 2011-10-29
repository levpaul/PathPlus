package org.pathplus.utils.path;

import java.util.ArrayList;

import org.pathplus.utils.state.State;

public class PathResult<T extends State<T>> implements Result<T> {

	private T startState,  goalState;
	private ArrayList<T> pathArrayList;
	
	public PathResult(T goalState){
		generatePathArrayList(goalState);
		startState = pathArrayList.get(pathArrayList.size() - 1);
		this.goalState = pathArrayList.get(0);
	}

	// This method creates a new path from a given goalState.
	/**
	* @params goalState The final state which will link to the start state
	* through its parents.
	*/
	private void generatePathArrayList(T goalState) {
		pathArrayList = new ArrayList<T>();
		T current = goalState;
		pathArrayList.add(current);
		
		while(current.getParent() != null){
			pathArrayList.add((T) current.getParent());
			current = (T) current.getParent();
		}
	}

	public T[] toArray() {
		@SuppressWarnings("unchecked") //TODO Fix it.
		T[] tempArray = (T[]) pathArrayList.toArray();
		return tempArray;
	}

	public double getLength() {
		return pathArrayList.size() - 1;
	}

	public T getGoalState() {
		return goalState;
	}

	public void setGoalState(T goalState) {
		this.goalState = goalState;
	}

	public T getStartState() {
		return startState;
	}

	public void setStartState(T startState) {
		this.startState = startState;
	}

	

}
